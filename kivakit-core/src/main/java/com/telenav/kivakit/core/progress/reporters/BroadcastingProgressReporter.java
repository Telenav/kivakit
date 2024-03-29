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

package com.telenav.kivakit.core.progress.reporters;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramProgress;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.broadcasters.Multicaster;
import com.telenav.kivakit.core.progress.ProgressListener;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.Rate;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.BaseCount;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.level.Percent;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.messaging.Listener.nullListener;
import static com.telenav.kivakit.core.string.AsciiArt.bottomLine;
import static com.telenav.kivakit.core.string.AsciiArt.topLine;
import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.core.time.Duration.seconds;
import static com.telenav.kivakit.core.time.Time.epochMilliseconds;
import static com.telenav.kivakit.core.time.Time.now;
import static com.telenav.kivakit.core.value.count.Count.count;

/**
 * A progress reporter that sends progress messages to a {@link Listener} as an operation proceeds.
 * BroadcastingProgressReporter reporting on an operation is started with {@link #start(String, Object...)}. At each
 * step, {@link #next()} or {@link #next(long)} should be called to report progress. When the operation is over
 * {@link #end(String, Object...)} should be called.
 * <p>
 * <b>Example - Broadcasting Progress of an Operation</b>
 * <pre>
 * var progress = progressReporter(this, "bytes");
 * progress.start("Downloading")
 * while ([...])
 * {
 *     [...]
 *
 *     progress.next();
 * }
 * progress.end("Downloaded");
 * </pre>
 * <p>
 * <b>Example - Listening to the Progress of an Operation</b>
 * <pre>
 * progress.listener(percent -&gt; System.out.println("$ complete", percent);
 * </pre>
 * {@link BroadcastingProgressReporter} is not thread-safe. To report progress in a multithreaded operations, use
 * {@link ConcurrentBroadcastingProgressReporter}.
 *
 * <p><b>Progress</b></p>
 *
 * <ul>
 *     <li>{@link #at()} - The current point in the operation</li>
 *     <li>{@link #at(Count)} - Sets the current point in the operation</li>
 *     <li>{@link #end(String, Object...)} - Ends the operation</li>
 *     <li>{@link #next()} - Moves to the next step</li>
 *     <li>{@link #next(Count)} - Moves ahead by the given number of steps</li>
 *     <li>{@link #next(int)} - Moves ahead by the given number of steps</li>
 *     <li>{@link #next(long)} - Moves ahead by the given number of steps</li>
 *     <li>{@link #phase(String)} - Sets the phase of the operation</li>
 *     <li>{@link #reset()} - Resets progress to step 0</li>
 *     <li>{@link #start()} - Starts the operation</li>
 *     <li>{@link #start(String, Object...)} - Starts the operation, transmitting the given label</li>
 *     <li>{@link #steps()} - Returns the number of steps in the operation</li>
 *     <li>{@link #steps(BaseCount)} - Sets the number of steps in the operation</li>
 *     <li>{@link #withPhase(String)} - This reporter with the given phase</li>
 *     <li>{@link #withSteps(BaseCount)} - This reporter with the given number of steps</li>
 *     <li>{@link #withUnits(String)} - This reporter with the given item name, specifying the units</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramProgress.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class BroadcastingProgressReporter extends Multicaster implements ProgressReporter
{
    /** The slowest we should report progress */
    private static final Duration REPORT_SLOWEST = seconds(4);

    /** The fastest rate at which we should report progress */
    private static final Duration REPORT_FASTEST = seconds(0.25);

    public static BroadcastingProgressReporter concurrentProgressReporter(Listener listener)
    {
        return concurrentProgressReporter(listener, "items");
    }

    public static BroadcastingProgressReporter concurrentProgressReporter(Listener listener, String itemName)
    {
        return concurrentProgressReporter(listener, itemName, null);
    }

    public static BroadcastingProgressReporter concurrentProgressReporter(Listener listener,
                                                                          String itemName,
                                                                          BaseCount<?> steps)
    {
        return listener.listenTo(new ConcurrentBroadcastingProgressReporter()
            .withUnits(itemName)
            .withSteps(steps));
    }

    /**
     * @param listener The message listener
     * @param itemName The item that is being processed, like "bytes"
     * @param steps The number of steps in the operation
     */
    public static BroadcastingProgressReporter progressReporter(Listener listener, String itemName,
                                                                BaseCount<?> steps)
    {
        return listener.listenTo(new BroadcastingProgressReporter()
            .withUnits(itemName)
            .withSteps(steps));
    }

    public static BroadcastingProgressReporter progressReporter()
    {
        return progressReporter(nullListener());
    }

    public static BroadcastingProgressReporter progressReporter(Listener listener)
    {
        return progressReporter(listener, "items");
    }

    public static BroadcastingProgressReporter progressReporter(Listener listener, String itemName)
    {
        return progressReporter(listener, itemName, null);
    }

    /** The current step */
    private long at;

    /** True if the operation has ended */
    private boolean ended;

    /** The number of steps before reporting */
    private long every = 10;

    /** True if the user wants every to be fixed */
    private boolean fixedEvery = false;

    /** The name of each step as a unit */
    private String unitName;

    /** The last percentage we reported, from 0 to 100 */
    private int lastPercent;

    /** The last time we reported progress */
    private Time lastReportedAt = now();

    /** The progress listener to update */
    private ProgressListener listener;

    /** The current phase of the operation */
    private String phase;

    /** The time that progress reporting began */
    private long start = now().milliseconds();

    /** True if this reporter has started reporting */
    private boolean started;

    /** The number of steps in this operation */
    private long steps;

    /** The number of problems that have been flagged */
    private long problems;

    protected BroadcastingProgressReporter(BroadcastingProgressReporter that)
    {
        super(that);

        at = that.at;
        every = that.every;
        fixedEvery = that.fixedEvery;
        phase = that.phase;
        steps = that.steps;
        unitName = that.unitName;
        listener = that.listener;
        start = that.start;
        lastReportedAt = that.lastReportedAt;
        lastPercent = that.lastPercent;
        ended = that.ended;
        started = that.started;
        problems = that.problems;
    }

    protected BroadcastingProgressReporter()
    {
    }

    /**
     * Returns the step we are at in the operation
     */
    @Override
    public Count at()
    {
        return count(at);
    }

    /**
     * Sets the step we are at
     */
    public void at(Count at)
    {
        this.at = at.asLong();
    }

    /**
     * Ends this operation with the given formatted message
     */
    @Override
    public synchronized void end(String message, Object... arguments)
    {
        if (!ended)
        {
            ended = true;
            report(at);
            var formatted = format(message, arguments);
            feedback(bottomLine(70, "$ $ in $ ($ problems)", formatted, unitName,
                epochMilliseconds(start).elapsedSince(), problems()));
        }
    }

    /**
     * Provides a feedback message
     */
    public void feedback(String message)
    {
        information(phase == null ? message : phase + message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void next()
    {
        // HOTSPOT: This method has been determined to be a hotspot by YourKit profiling
        // (which is hardly surprising)

        // Increase the count;
        var at = increment();

        // If the count is evenly divided by every,
        if (at % every == 0)
        {
            // report the count
            report(at);
        }
        // otherwise, if it's at the maximum,
        else if (at == steps)
        {
            // we're done
            end();
        }
    }

    /**
     * Moves ahead by the given number of steps
     */
    public synchronized void next(long increase)
    {
        var count = increase(increase);
        if (steps > 0 && count >= steps)
        {
            end();
        }
        else
        {
            var start = count - increase;
            var next = ((start / every) + 1) * every;
            for (var at = next; at <= count; at += every)
            {
                report(at);
                this.at = at;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BroadcastingProgressReporter phase(String phase)
    {
        this.phase = phase;
        reset();
        return this;
    }

    @Override
    public void problems(long problems)
    {
        this.problems += problems;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Count problems()
    {
        return count(problems);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BroadcastingProgressReporter progressReporter(ProgressListener listener)
    {
        this.listener = listener;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset()
    {
        started = false;
        ended = false;
        start = now().milliseconds();
        if (!fixedEvery)
        {
            every = 10;
        }
        this.at = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BroadcastingProgressReporter start(String message, Object... arguments)
    {
        if (!started)
        {
            started = true;
            this.at = 0;
            var formatted = format(message, arguments);
            feedback(topLine(70, formatted + " " + unitName));
            start = now().milliseconds();
            if (listener != null)
            {
                listener.at(Percent._0);
            }
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BroadcastingProgressReporter steps(BaseCount<?> steps)
    {
        if (steps != null)
        {
            this.steps = steps.get();
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Count steps()
    {
        return steps <= 0 ? null : count(steps);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return progressMessage(at());
    }

    public BroadcastingProgressReporter withEvery(Count every)
    {
        var copy = copy();
        copy.every = every.asInt();
        copy.fixedEvery = true;
        return copy;
    }

    /**
     * Returns a copy of this reporter with the given phase
     */
    public BroadcastingProgressReporter withPhase(String phase)
    {
        var copy = copy();
        copy.phase = phase;
        return copy;
    }

    /**
     * Returns a copy of this reporter with the given number of steps
     */
    public BroadcastingProgressReporter withSteps(BaseCount<?> steps)
    {
        if (steps != null)
        {
            var copy = copy();
            copy.steps = steps.asInt();
            return copy;
        }
        else
        {
            return this;
        }
    }

    /**
     * Returns a copy of this reporter with the given unit name
     */
    public BroadcastingProgressReporter withUnits(String unitName)
    {
        var copy = copy();
        copy.unitName = unitName;
        return copy;
    }

    @NotNull
    protected BroadcastingProgressReporter copy()
    {
        return new BroadcastingProgressReporter(this);
    }

    protected long increase(long increase)
    {
        at += increase;
        return at;
    }

    protected long increment()
    {
        return ++at;
    }

    private Percent percentComplete()
    {
        if (steps() != null && steps().isNonZero())
        {
            return Percent.percent(100.0 * at / steps().asLong());
        }
        return null;
    }

    private String progressMessage(Count count)
    {
        var builder = new StringBuilder();
        var commaSeparatedCount = count.asCommaSeparatedString();
        builder.append("  ").append(commaSeparatedCount);
        builder.append(" of ");
        if (steps > 0)
        {
            builder.append(count(steps));
            builder.append(" (");
            builder.append(Percent.percent(100.0 * count.get() / steps).asInt());
            builder.append("%)");
        }
        else
        {
            builder.append("?");
        }
        var elapsed = epochMilliseconds(start).elapsedSince();
        if (unitName != null)
        {
            builder.append(" ");
            builder.append(unitName);
        }
        builder.append(" in ");
        builder.append(elapsed);
        var rate = Rate.perSecond(count.get() / elapsed.asSeconds());
        builder.append(" (");
        builder.append(rate);
        builder.append(", ");
        builder.append(problems);
        builder.append(" problems)");
        return builder.toString();
    }

    private synchronized void report(long at)
    {
        if (!isIndefinite())
        {
            var percent = percentComplete();
            if (percent != null && lastPercent != percent.asInt())
            {
                if (listener != null)
                {
                    listener.at(percent);
                }
                lastPercent = percent.asInt();
            }
        }

        if (steps <= 0)
        {
            feedback(progressMessage(count(at)) + " ");
        }
        else
        {
            feedback(progressMessage(count(at)));
        }

        var elapsed = lastReportedAt.elapsedSince().maximum(Duration.milliseconds(1));

        if (!fixedEvery)
        {
            // While we're going too fast
            while (elapsed.isLessThan(REPORT_FASTEST))
            {
                // we increase the reporting interval (slowing down reporting)
                // and the estimated elapsed time by 10
                every *= 10;
                elapsed = elapsed.times(10);
            }

            // While we're going too slow (and we prefer not to!),
            while (elapsed.isGreaterThan(REPORT_SLOWEST))
            {
                // we decrease the reporting interval (speeding up reporting)
                // and the elapsed time by 10
                every = Math.max(10, every / 10);
                elapsed = elapsed.dividedBy(10);
            }

            every = Math.min(every, 1_000_000);
        }

        lastReportedAt = now();
    }
}
