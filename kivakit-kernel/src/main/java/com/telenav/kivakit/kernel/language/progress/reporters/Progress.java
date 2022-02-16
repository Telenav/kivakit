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

package com.telenav.kivakit.kernel.language.progress.reporters;

import com.telenav.kivakit.kernel.language.progress.ProgressListener;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.kernel.language.strings.AsciiArt;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.time.Rate;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.language.values.level.Percent;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.broadcasters.Multicaster;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageProgress;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

/**
 * A progress reporter that sends progress messages to a {@link Listener} as an operation proceeds. Progress reporting
 * on an operation is started with {@link #start(String)}. At each step, {@link #next()} or {@link #next(long)} should
 * be called to report progress. When the operation is over {@link #end(String, Object...)} should be called.
 * <p>
 * <b>Example - Broadcasting Progress of an Operation</b>
 * <pre>
 * var progress = Progress.create(LOGGER, "bytes");
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
 * {@link Progress} is not thread-safe. To report progress in a multi-threaded operations, use {@link
 * ConcurrentProgress}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageProgress.class)
public class Progress extends Multicaster implements ProgressReporter
{
    private static final Duration REPORT_SLOWEST = Duration.seconds(4);

    private static final Duration REPORT_FASTEST = Duration.seconds(0.25);

    public static Progress create()
    {
        return Progress.create(Listener.none());
    }

    public static Progress create(Listener listener)
    {
        return create(listener, "items");
    }

    public static Progress create(Listener listener, String itemName)
    {
        return create(listener, itemName, null);
    }

    /**
     * @param listener The message listener
     * @param itemName The item that is being processed, like "bytes"
     * @param steps The number of steps in the operation
     * @return A new progress object
     */
    public static Progress create(Listener listener, String itemName, Count steps)
    {
        return listener.listenTo(new Progress()
                .withItemName(itemName)
                .withSteps(steps));
    }

    public static Progress createConcurrent(Listener listener)
    {
        return createConcurrent(listener, "items");
    }

    public static Progress createConcurrent(Listener listener, String itemName)
    {
        return createConcurrent(listener, itemName, null);
    }

    public static Progress createConcurrent(Listener listener, String itemName, Count steps)
    {
        return listener.listenTo(new ConcurrentProgress()
                .withItemName(itemName)
                .withSteps(steps));
    }

    private long at;

    private long steps = -1;

    private long every = 10;

    private long start = Time.now().asMilliseconds();

    private Time lastReportedAt = Time.now();

    private String phase;

    private String itemName;

    private boolean ended;

    private boolean started;

    private ProgressListener listener;

    private int lastPercent;

    protected Progress(Progress that)
    {
        super(that);

        at = that.at;
        every = that.every;
        phase = that.phase;
        steps = that.steps;
        itemName = that.itemName;
        listener = that.listener;
        start = that.start;
        lastReportedAt = that.lastReportedAt;
        lastPercent = that.lastPercent;
        ended = that.ended;
        started = that.started;
    }

    protected Progress()
    {
    }

    public long at()
    {
        return at;
    }

    @Override
    public synchronized void end(String message, Object... arguments)
    {
        if (!ended)
        {
            ended = true;
            report(at());
            var formatted = Message.format(message, arguments);
            feedback(AsciiArt.bottomLine("$ $ in $", formatted, itemName, Time.milliseconds(start).elapsedSince()));
        }
    }

    public void feedback(String message)
    {
        information(phase == null ? message : phase + message);
    }

    @Override
    public Progress listener(ProgressListener listener)
    {
        this.listener = listener;
        return this;
    }

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
                at(at);
            }
        }
    }

    @Override
    public Progress phase(String phase)
    {
        this.phase = phase;
        reset();
        return this;
    }

    @Override
    public void reset()
    {
        started = false;
        ended = false;
        start = Time.now().asMilliseconds();
        every = 10;
        at(0);
    }

    @Override
    public Progress start(String label)
    {
        if (!started)
        {
            started = true;
            at(0);
            feedback(AsciiArt.topLine(label + " " + itemName));
            start = Time.now().asMilliseconds();
            if (listener != null)
            {
                listener.at(Percent._0);
            }
        }
        return this;
    }

    @Override
    public Progress steps(Count steps)
    {
        if (steps != null)
        {
            this.steps = steps.get();
        }
        return this;
    }

    @Override
    public Count steps()
    {
        return steps < 0 ? null : Count.count(steps);
    }

    @Override
    public String toString()
    {
        return toString(Count.count(at()));
    }

    public Progress withItemName(String itemName)
    {
        var progress = newInstance();
        progress.itemName = itemName;
        return progress;
    }

    public Progress withPhase(String phase)
    {
        var progress = newInstance();
        progress.phase = phase;
        return progress;
    }

    public Progress withSteps(Count steps)
    {
        if (steps != null)
        {
            var progress = newInstance();
            progress.steps = steps.get();
            return progress;
        }
        else
        {
            return this;
        }
    }

    protected void at(long at)
    {
        this.at = at;
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

    @NotNull
    protected Progress newInstance()
    {
        return new Progress(this);
    }

    private Percent percentComplete()
    {
        if (steps().isNonZero())
        {
            return Percent.of(100.0 * at() / steps().asLong());
        }
        return null;
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

        if (steps < 0)
        {
            feedback(toString(Count.count(at)) + " ");
        }
        else
        {
            feedback(toString(Count.count(at)));
        }

        var elapsed = lastReportedAt.elapsedSince().maximum(Duration.milliseconds(1));

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
            elapsed = elapsed.divide(10);
        }

        every = Math.min(every, 1_000_000);

        lastReportedAt = Time.now();
    }

    private String toString(Count count)
    {
        var builder = new StringBuilder();
        var commaSeparatedCount = count.asCommaSeparatedString();
        builder.append("  ").append(commaSeparatedCount);
        builder.append(" of ");
        if (steps > 0)
        {
            builder.append(Count.count(steps));
            builder.append(" (");
            builder.append(Percent.of(100.0 * count.get() / steps).asInt());
            builder.append("%)");
        }
        else
        {
            builder.append("?");
        }
        var elapsed = Time.milliseconds(start).elapsedSince();
        if (itemName != null)
        {
            builder.append(" ");
            builder.append(itemName);
        }
        builder.append(" in ");
        builder.append(elapsed);
        var rate = Rate.perSecond(count.get() / elapsed.asSeconds());
        builder.append(" (");
        builder.append(rate);
        builder.append(")");
        return builder.toString();
    }
}
