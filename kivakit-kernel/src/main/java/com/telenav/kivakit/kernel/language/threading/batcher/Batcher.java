////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.kernel.language.threading.batcher;

import com.telenav.kivakit.kernel.interfaces.code.Unchecked;
import com.telenav.kivakit.kernel.interfaces.collection.Addable;
import com.telenav.kivakit.kernel.language.threading.Threads;
import com.telenav.kivakit.kernel.language.threading.conditions.StateMachine;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramBatchProcessing;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;

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
 * private static final Maximum QUEUE_SIZE = Maximum._128;
 * private static final Count BATCH_SIZE = Count._16384;
 * private static final Count WORKER_THREADS = Count._8;
 *
 *   [...]
 *
 * var batcher = Batcher.<T>create()
 *     .withName(qualifiedName())
 *     .withQueueSize(QUEUE_SIZE)
 *     .withBatchSize(BATCH_SIZE)
 *     .withConsumer(batch -> batch.forEach(this::add));
 *
 *   [...]
 *
 * batcher.start(WORKER_THREADS);
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
public class Batcher<Element> extends BaseRepeater
{
    /**
     * @return Creates a new batcher
     */
    public static <Element> Batcher<Element> create()
    {
        return new Batcher<>();
    }

    /** The execution state of this batcher */
    private enum State
    {
        READY,
        RUNNING,
        STOPPING,
        STOPPED,
    }

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

            assert !outer.state.is(State.STOPPING);
            assert !outer.state.is(State.STOPPED);

            // add the item to the batch
            batch.add(item);

            // and if the batch is full,
            if (batch.isFull())
            {
                // add the batch to the queue
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
    @LexakaiJavadoc(complete = true)
    public class Batch extends ArrayList<Element>
    {
        boolean isFull()
        {
            return size() >= Batcher.this.batchSize || batchFullPredicate.test(this);
        }

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
    private String name = "Batcher";

    /** Size of batches */
    private int batchSize = 4096;

    /** Size of batch queue */
    private int queueSize = 8;

    /** The blocking queue of batches to process */
    private ArrayBlockingQueue<Batch> queue;

    /** The worker threads to process batches */
    private ExecutorService executor;

    /** The code to process batches */
    private Consumer<Batch> consumer;

    /** State machine to track the execution phases of this batcher */
    private StateMachine<State> state = new StateMachine<>(State.READY);

    /** Set of batch adders for clients */
    private final Set<BatchAdder> adders = new HashSet<>();

    /** Predicate to determine if a batch is full (in addition to the batch size) */
    private Predicate<Batch> batchFullPredicate = batch -> false;

    protected Batcher()
    {
    }

    protected Batcher(final Batcher<Element> that)
    {
        this.name = that.name;
        this.batchSize = that.batchSize;
        this.queueSize = that.queueSize;
        this.queue = that.queue;
        this.consumer = that.consumer;
        this.executor = that.executor;
        this.state = that.state;
        this.batchFullPredicate = that.batchFullPredicate;
    }

    /**
     * @return An object that adds to an un-synchronized batch and enqueues it when full
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
        ensure(!state.is(State.STOPPED), "Cannot restart a batcher, create a new one instead");

        // If we aren't already running
        if (state.transition(State.READY, State.RUNNING))
        {
            // create a blocking queue,
            queue = new ArrayBlockingQueue<>(queueSize, true);

            // then create an executor,
            executor = Threads.threadPool(name + "-Batcher", workers);

            // start a job for each worker,
            final var outer = this;
            workers.loop(() -> executor.submit(() ->
            {
                // and loop until we are asked to stop,
                trace("$: Processing batches", outer.name);
                while (!state.is(State.STOPPING))
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
        if (state.transition(State.RUNNING, State.STOPPING))
        {
            // shut down the executor, interrupting waiting threads and waiting for them to exit,
            trace("$: Stopping", name);
            final var pending = executor.shutdownNow();
            Threads.await(executor);
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

            state.transitionTo(State.STOPPED);
        }
    }

    public Batcher<Element> withBatchFullPredicate(final Predicate<Batch> predicate)
    {
        final var copy = copy();
        copy.batchFullPredicate = predicate;
        return copy;
    }

    public Batcher<Element> withBatchSize(final Count size)
    {
        final var copy = copy();
        copy.batchSize = size.asInt();
        return copy;
    }

    public Batcher<Element> withConsumer(final Consumer<Batch> consumer)
    {
        final var copy = copy();
        copy.consumer = consumer;
        return copy;
    }

    public Batcher<Element> withName(final String name)
    {
        final var copy = copy();
        copy.name = name;
        return copy;
    }

    public Batcher<Element> withQueueSize(final Count size)
    {
        final var copy = copy();
        copy.queueSize = size.asInt();
        return copy;
    }

    protected Batcher<Element> copy()
    {
        return new Batcher<>(this);
    }

    /**
     * @param batch The batch for the subclass to process
     */
    protected void onBatch(final Batch batch)
    {
        consumer.accept(batch);
    }

    /**
     * @return The next batch from the queue, or an empty batch if interrupted
     */
    private Batch nextBatch()
    {
        return Unchecked.of(() -> queue.take()).or(Batch::new);
    }
}
