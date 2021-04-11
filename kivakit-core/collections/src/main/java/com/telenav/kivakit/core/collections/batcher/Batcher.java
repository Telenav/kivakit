////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.batcher;

import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramBatchProcessing;
import com.telenav.kivakit.core.kernel.interfaces.code.CheckedCode;
import com.telenav.kivakit.core.kernel.interfaces.collection.Addable;
import com.telenav.kivakit.core.kernel.language.threading.Threads;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;

/**
 * A {@link Batcher} has a queue of element batches which are processed by one or more worker threads.
 *
 * <p><b>Adding Elements</b></p>
 *
 * <p>
 * New elements are added via a {@link BatchAdder} object that is <i>not</i> thread-safe and should be kept only in a
 * local variable or on a per-thread basis using {@link ThreadLocal}. A batch adder is created by a call to {@link
 * #adder()} and objects are added to the adder with {@link BatchAdder#add(Object)}. When the batch adder's batch is
 * full, the batch is added to the queue and a new batch is started.
 * </p>
 *
 * <p><b>Processing Elements</b></p>
 *
 * <p>
 * A batcher has a set of worker threads that are started with {@link #start(Count)}, passing in the desired number of
 * threads. Worker threads pull batches out of the queue and call {@link Batch#process()} which calls {@link
 * #onBatch(Batch)} to let the subclass process the batch of elements. When the thread(s) that are adding elements to
 * the batcher are done, they must call {@link #stop()}, to shut down the batcher and wait until the processing of all
 * batches is complete.
 * </p>
 *
 * <p><b>Example</b></p>
 *
 * <pre>
 * Batcher&lt;Record&gt; batcher = new Batcher&lt;&gt;("RecordBatcher", Maximum._8, Count.16_384)
 * {
 *     protected void onBatch(final Batch batch)
 *     {
 *         for (var record : batch)
 *         {
 *             [...]
 *         }
 *     }
 * };
 *
 *     [...]
 *
 * batcher.start(Count._8);
 *
 *    [...]
 *
 * batcher.adder().add(record);
 *
 *    [...]
 *
 * batcher.close();
 *
 * </pre>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramBatchProcessing.class)
public abstract class Batcher<Element> extends BaseRepeater
{
    /**
     * Adds elements to a batch and enqueues the batch when it is full before starting a new one. Note that this design
     * is better than adding to a single batch in the {@link Batcher} because that batch data structure would have to be
     * shared among threads and therefore it would have to be synchronized. Having each thread add elements to its own
     * thread-local batch adder reduces lock contention.
     */
    public class BatchAdder implements Addable<Element>
    {
        /** The batch to fill with elements */
        private Batch batch = new Batch();

        /**
         * Adds the given item to a batch and enqueues the batch if it is full
         */
        @Override
        public boolean add(final Element item)
        {
            final var outer = Batcher.this;

            assert !outer.stopping;
            assert !outer.stopped;

            // add the item to the batch
            batch.add(item);

            // and if the batch is full,
            if (batch.size() >= outer.batchSize)
            {
                // add it to the queue
                enqueue();
            }
            return true;
        }

        /**
         * Enqueues the current batch and starts a new one
         */
        synchronized void enqueue()
        {
            // If we have a non-empty batch,
            if (batch != null && !batch.isEmpty())
            {
                // put it in the queue
                final var outer = Batcher.this;
                trace("$: Enqueueing batch of $ items", name, batch.size());
                try
                {
                    outer.queue.put(batch);
                }
                catch (final InterruptedException ignored)
                {
                }

                // and start a new batch
                batch = new Batch();
            }
        }
    }

    /**
     * A batch of elements for processing
     */
    public class Batch extends ArrayList<Element>
    {
        /**
         * Processes this batch of elements
         */
        void process()
        {
            final var outer = Batcher.this;
            if (!isEmpty())
            {
                try
                {
                    final var start = Time.now();
                    trace("$: Processing $ element batch ${hex}", outer.name, size(), hashCode());
                    onBatch(this);
                    trace("$: Processed $ items in $", outer.name, size(), start.elapsedSince());
                }
                catch (final Exception e)
                {
                    problem(e, "$: Unable to process batch", outer.name);
                }
            }
        }
    }

    /** Name of this batcher */
    private final String name;

    /** Size of batches */
    private final int batchSize;

    /** The blocking queue of batches to process */
    private ArrayBlockingQueue<Batch> queue;

    /** The worker threads to process batches */
    private ExecutorService executor;

    /** True if we're trying to stop */
    private volatile boolean stopping;

    /** True if this batcher is running */
    private boolean running;

    /** True if the batcher was running and now it has stopped */
    private volatile boolean stopped;

    /** Set of batch adders for clients */
    private final Set<BatchAdder> adders = new HashSet<>();

    protected Batcher(final String name, final Maximum queueSize, final Count batchSize)
    {
        this.name = name;
        this.batchSize = batchSize.asInt();
        queue = new ArrayBlockingQueue<>(queueSize.asInt(), true);
    }

    /**
     * @return An object that adds to an unsynchronized batch and enqueues it when full
     */
    public synchronized BatchAdder adder()
    {
        final var adder = new BatchAdder();
        adders.add(adder);
        return adder;
    }

    /**
     * Starts this batcher with the given number of worker threads
     */
    public synchronized void start(final Count workers)
    {
        ensure(!stopped, "Cannot restart a batcher, create a new one instead");

        // If we aren't already running
        if (!running)
        {
            // then create an executor,
            running = true;
            executor = Threads.threadPool(name + "-Batcher", workers);

            // start a job for each worker,
            final var outer = this;
            workers.loop(() -> executor.submit(() ->
            {
                // and loop until we are asked to stop,
                trace("$: Processing batches", outer.name);
                while (!stopping)
                {
                    // processing batches.
                    nextBatch().process();
                }
                trace("$: Processor is done", outer.name);
            }));
        }
    }

    /**
     * Stops background thread once current batch is finished processing, but without flushing the queue.
     */
    public synchronized void stop()
    {
        // If we are running and we aren't already trying to stop
        if (running && !stopping)
        {
            // shut down the executor, interrupting waiting threads and waiting for them to exit,
            trace("$: Stopping", name);
            stopping = true;
            final var pending = executor.shutdownNow();
            Threads.await(executor);
            running = false;
            trace("$: Stopped", name);

            // then run any tasks that never started executing,
            for (final var task : pending)
            {
                task.run();
            }

            // add any remaining items to the queue,
            for (final var adder : adders)
            {
                adder.enqueue();
            }

            // and process any remaining batches
            final var remaining = new ArrayList<Batch>();
            queue.drainTo(remaining);
            assert queue.isEmpty();

            // (set the queue to null to catch any mistaken attempts to add after threads have stopped)
            queue = null;

            trace("$: Processing $ remaining batches", name, remaining.size());
            for (final var batch : remaining)
            {
                batch.process();
            }

            stopped = true;
        }
    }

    /**
     * @param batch The batch for the subclass to process
     */
    protected abstract void onBatch(Batch batch);

    /**
     * @return The next batch from the queue, or an empty batch if interrupted
     */
    private Batch nextBatch()
    {
        return CheckedCode.of(() -> queue.take()).or(new Batch());
    }
}
