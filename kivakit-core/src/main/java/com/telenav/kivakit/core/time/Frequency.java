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

import com.telenav.kivakit.core.lexakai.DiagramTime;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.string.Strip;
import com.telenav.kivakit.core.test.NoTestRequired;
import com.telenav.kivakit.core.test.Tested;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Objects;

import static com.telenav.kivakit.core.time.Duration.ZERO_DURATION;
import static com.telenav.kivakit.core.time.Duration.parseDuration;

/**
 * A simple frequency domain object. Frequency is modeled as a {@link Duration} per cycle. Static factory methods allow
 * construction of frequencies in terms of cycles per time unit. The {@link #start()} method begins tracking cycles by
 * returning a {@link Cycle} object that can be used to determine how long the caller should wait before the next cycle
 * begins (by calling {@link Cycle#waitTimeBeforeNextCycle()}.
 *
 * <p><b>Note</b></p>
 *
 * <p>
 * <i>This object is not thread-safe and cannot be shared.</i>
 * </p>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramTime.class)
@LexakaiJavadoc(complete = true)
@Tested
public class Frequency
{
    public static final Frequency ONCE = every(Duration.MAXIMUM);

    public static final Frequency EVERY_SECOND = cyclesPerSecond(1);

    public static final Frequency EVERY_HALF_SECOND = cyclesPerSecond(2);

    public static final Frequency EVERY_5_SECONDS = every(Duration.seconds(5));

    public static final Frequency EVERY_10_SECONDS = every(Duration.seconds(10));

    public static final Frequency EVERY_15_SECONDS = every(Duration.seconds(15));

    public static final Frequency EVERY_30_SECONDS = every(Duration.seconds(30));

    public static final Frequency EVERY_MINUTE = cyclesPerMinute(1);

    public static final Frequency ONCE_A_DAY = cyclesPerDay(1);

    public static final Frequency FOUR_TIMES_A_DAY = cyclesPerDay(4);

    public static final Frequency CONTINUOUSLY = every(ZERO_DURATION);

    @Tested
    public static Frequency cyclesPerDay(int times)
    {
        return every(Duration.ONE_DAY.dividedBy(times));
    }

    @Tested
    public static Frequency cyclesPerHour(int times)
    {
        return every(Duration.ONE_HOUR.dividedBy(times));
    }

    @Tested
    public static Frequency cyclesPerMinute(int times)
    {
        return every(Duration.ONE_MINUTE.dividedBy(times));
    }

    @Tested
    public static Frequency cyclesPerSecond(int times)
    {
        return every(Duration.ONE_SECOND.dividedBy(times));
    }

    @Tested
    public static Frequency every(Duration duration)
    {
        return new Frequency(duration);
    }

    @Tested
    public static Frequency parseFrequency(String value)
    {
        return parseFrequency(Listener.throwing(), value);
    }

    @Tested
    public static Frequency parseFrequency(Listener listener, String value)
    {
        value = Strip.leading(value, "every").strip();

        if (value.length() > 0)
        {
            if (!Character.isDigit(value.charAt(0)))
            {
                value = "1 " + value;
            }

            var duration = parseDuration(listener, value);
            if (duration != null)
            {
                return every(duration);
            }
        }

        listener.problem("Invalid frequency: $", value);
        return null;
    }

    /**
     * The start time of a cycle
     */
    @LexakaiJavadoc(complete = true)
    @Tested
    public class Cycle
    {
        private final Time start;

        @NoTestRequired
        public Cycle(Time start)
        {
            this.start = start;
        }

        @Tested
        public Time next()
        {
            return Time.now().plus(waitTimeBeforeNextCycle());
        }

        @Tested
        public Duration waitTimeBeforeNextCycle()
        {
            // If there is no cycle lenth,
            if (cycleLength.isNone())
            {
                // then it's always time for the next cycle.
                return ZERO_DURATION;
            }

            // The duration to wait before the next cycle is the cycle length minus
            // the elapsed time (modulus the cycle length to handle skipped cycles)
            return cycleLength.minus(start.elapsedSince().modulus(cycleLength));
        }
    }

    /** The cycle length of this frequency */
    private final Duration cycleLength;

    @NoTestRequired
    protected Frequency(Duration cycleLength)
    {
        this.cycleLength = Objects.requireNonNull(cycleLength);
    }

    /**
     * @return The duration of one cycle
     */
    @NoTestRequired
    public Duration cycleLength()
    {
        return cycleLength;
    }

    @Override
    @Tested
    public boolean equals(final Object object)
    {
        if (object instanceof Frequency)
        {
            Frequency that = (Frequency) object;
            return this.cycleLength.equals(that.cycleLength);
        }
        return false;
    }

    @Override
    @Tested
    public int hashCode()
    {
        return Objects.hash(cycleLength);
    }

    /**
     * @param start The time this frequency object started
     * @return A {@link Cycle} object which provides the wait time until the next cycle
     */
    @Tested
    public Cycle start(Time start)
    {
        return new Cycle(start);
    }

    /**
     * @return A {@link Cycle} object which provides the wait time until the next cycle
     */
    @NoTestRequired
    public Cycle start()
    {
        return start(Time.now());
    }

    @Override
    @Tested
    public String toString()
    {
        return "every " + cycleLength;
    }
}
