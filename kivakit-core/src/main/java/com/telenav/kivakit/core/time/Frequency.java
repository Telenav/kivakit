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

package com.telenav.kivakit.core.time;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramTime;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.string.Strip.stripLeading;
import static com.telenav.kivakit.core.time.Duration.FOREVER;
import static com.telenav.kivakit.core.time.Duration.ONE_DAY;
import static com.telenav.kivakit.core.time.Duration.ONE_HOUR;
import static com.telenav.kivakit.core.time.Duration.ONE_MINUTE;
import static com.telenav.kivakit.core.time.Duration.ONE_SECOND;
import static com.telenav.kivakit.core.time.Duration.ZERO_DURATION;
import static com.telenav.kivakit.core.time.Duration.parseDuration;
import static com.telenav.kivakit.core.time.Duration.seconds;
import static com.telenav.kivakit.core.time.Time.now;
import static java.lang.Character.isDigit;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

/**
 * A simple frequency domain object. Frequency is modeled as a {@link Duration} per cycle. Static factory methods allow
 * construction of frequencies in terms of cycles per time unit. The {@link #startingNow()} method begins tracking
 * cycles by returning a {@link Cycle} object that can be used to determine how long the caller should wait before the
 * next cycle begins (by calling {@link Cycle#waitTimeBeforeNextCycle()}.
 *
 * <p><b>Example</b></p>
 *
 * <p>
 * This code prints "hi" every 5 minutes starting at the next minute that is evenly divisible by 5, for example, 11:30,
 * 11:35, 11:40, etc.
 * </p>
 *
 * <pre>every(minutes(5)).startingAt(now().roundUp(minutes(5)).run(() -> println("hi"))</pre>
 *
 * <p><b>Note</b></p>
 *
 * <p>
 * <i>This object is not thread-safe and cannot be shared.</i>
 * </p>
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #cyclesPerDay(int)}</li>
 *     <li>{@link #cyclesPerHour(int)}</li>
 *     <li>{@link #cyclesPerMinute(int)}</li>
 *     <li>{@link #cyclesPerSecond(int)}</li>
 *     <li>{@link #every(Duration)}</li>
 *     <li>{@link #parseFrequency(Listener, String)}</li>
 * </ul>
 *
 * <p><b>Cycling</b></p>
 *
 * <ul>
 *     <li>{@link #cycleLength()}</li>
 *     <li>{@link #startingNow()}</li>
 *     <li>{@link #startingAt(Time)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramTime.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class Frequency
{
    public static final Frequency ONCE = every(FOREVER);

    public static final Frequency EVERY_SECOND = cyclesPerSecond(1);

    public static final Frequency EVERY_HALF_SECOND = cyclesPerSecond(2);

    public static final Frequency EVERY_5_SECONDS = every(seconds(5));

    public static final Frequency EVERY_10_SECONDS = every(seconds(10));

    public static final Frequency EVERY_15_SECONDS = every(seconds(15));

    public static final Frequency EVERY_30_SECONDS = every(seconds(30));

    public static final Frequency EVERY_MINUTE = cyclesPerMinute(1);

    public static final Frequency ONCE_A_DAY = cyclesPerDay(1);

    public static final Frequency FOUR_TIMES_A_DAY = cyclesPerDay(4);

    public static final Frequency CONTINUOUSLY = every(ZERO_DURATION);

    public static Frequency cyclesPerDay(int times)
    {
        return every(ONE_DAY.dividedBy(times));
    }

    public static Frequency cyclesPerHour(int times)
    {
        return every(ONE_HOUR.dividedBy(times));
    }

    public static Frequency cyclesPerMinute(int times)
    {
        return every(ONE_MINUTE.dividedBy(times));
    }

    public static Frequency cyclesPerSecond(int times)
    {
        return every(ONE_SECOND.dividedBy(times));
    }

    /**
     * A frequency that repeats every given duration
     */
    public static Frequency every(Duration duration)
    {
        return new Frequency(duration);
    }

    /**
     * Parses the given text as a frequency, such as "every 6 days" or "every 1.5 seconds"
     *
     * @param listener The listener to notify of any problems
     * @param text The text to parse
     * @return The frequency
     */
    public static Frequency parseFrequency(Listener listener, String text)
    {
        text = stripLeading(text, "every").strip();

        if (text.length() > 0)
        {
            if (!isDigit(text.charAt(0)))
            {
                text = "1 " + text;
            }

            var duration = parseDuration(listener, text);
            if (duration != null)
            {
                return every(duration);
            }
        }

        listener.problem("Invalid frequency: $", text);
        return null;
    }

    /**
     * The start time of a cycle. The {@link #next()} method returns the time at which this cycle will repeat. The
     * amount of time before this time is returned by {@link #waitTimeBeforeNextCycle()}
     */
    @TypeQuality(stability = STABLE_EXTENSIBLE,
                 testing = UNTESTED,
                 documentation = DOCUMENTED)
    public class Cycle
    {
        private final Time start;

        public Cycle(Time start)
        {
            this.start = start;
        }

        /**
         * Returns next time this cycle will repeat
         */
        public Time next()
        {
            return now().plus(waitTimeBeforeNextCycle());
        }

        /**
         * Runs the given code using this frequency cycle
         *
         * @param runnable The code
         */
        @SuppressWarnings("InfiniteLoopStatement")
        public void run(Runnable runnable)
        {
            while (true)
            {
                waitTimeBeforeNextCycle().sleep();
                runnable.run();
            }
        }

        /**
         * Returns the amount of time to wait before this cycle repeats
         */
        public Duration waitTimeBeforeNextCycle()
        {
            // If there is no cycle length,
            if (cycleLength.isZero())
            {
                // then it's always time for the next cycle.
                return ZERO_DURATION;
            }

            // The duration to wait before the next cycle is the cycle length minus
            // the elapsed time (modulus the cycle length to handle skipped cycles)
            return cycleLength.minus(start.elapsedSince().modulo(cycleLength));
        }
    }

    /** The cycle length of this frequency */
    private final Duration cycleLength;

    protected Frequency(Duration cycleLength)
    {
        this.cycleLength = requireNonNull(cycleLength);
    }

    /**
     * Returns the duration of one cycle
     */
    public Duration cycleLength()
    {
        return cycleLength;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Frequency that)
        {
            return this.cycleLength.equals(that.cycleLength);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return hash(cycleLength);
    }

    /**
     * @param start The time this frequency object started
     * @return A {@link Cycle} object which provides the wait time until the next cycle
     */
    public Cycle startingAt(Time start)
    {
        return new Cycle(start);
    }

    /**
     * Returns a {@link Cycle} object which provides the wait time until the next cycle
     */
    public Cycle startingNow()
    {
        return startingAt(now());
    }

    @Override
    public String toString()
    {
        return "every " + cycleLength;
    }
}
