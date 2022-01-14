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

package com.telenav.kivakit.kernel.language.time;

import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageTime;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

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
@UmlClassDiagram(diagram = DiagramLanguageTime.class)
@LexakaiJavadoc(complete = true)
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

    public static final Frequency CONTINUOUSLY = every(Duration.NONE);

    public static Frequency cyclesPerDay(int times)
    {
        return every(Duration.ONE_DAY.divide(times));
    }

    public static Frequency cyclesPerHour(int times)
    {
        return every(Duration.ONE_HOUR.divide(times));
    }

    public static Frequency cyclesPerMinute(int times)
    {
        return every(Duration.ONE_MINUTE.divide(times));
    }

    public static Frequency cyclesPerSecond(int times)
    {
        return every(Duration.ONE_SECOND.divide(times));
    }

    public static Frequency every(Duration duration)
    {
        return new Frequency(duration);
    }

    /**
     * Converts to and from a frequency, expressed as a duration between cycles.
     *
     * @author jonathanl (shibo)
     * @see Duration
     */
    @LexakaiJavadoc(complete = true)
    public static class Converter extends BaseStringConverter<Frequency>
    {
        public Converter(Listener listener)
        {
            super(listener);
        }

        @Override
        protected Frequency onToValue(String value)
        {
            var duration = Duration.parse(this, value);
            return duration == null ? null : Frequency.every(duration);
        }
    }

    /**
     * The start time of a cycle
     */
    @LexakaiJavadoc(complete = true)
    public class Cycle
    {
        private final Time start;

        public Cycle(Time start)
        {
            this.start = start;
        }

        public Time next()
        {
            return Time.now().plus(waitTimeBeforeNextCycle());
        }

        public Duration waitTimeBeforeNextCycle()
        {
            if (cycleLength.isNone())
            {
                return Duration.NONE;
            }
            
            // The time since this frequency started
            var sinceStart = start.elapsedSince();

            // The elapsed time modulus the duration is how far we are into the
            // current cycle
            var intoCurrentCycle = Duration
                    .milliseconds(sinceStart.asMilliseconds() % cycleLength.asMilliseconds());

            // Find out how long is left until the next cycle
            return cycleLength.minus(intoCurrentCycle);
        }
    }

    private final Duration cycleLength;

    protected Frequency(Duration cycleLength)
    {
        if (cycleLength == null)
        {
            Ensure.fail("Duration may not be null");
        }
        this.cycleLength = cycleLength;
    }

    /**
     * @return The duration of one cycle
     */
    public Duration cycleLength()
    {
        return cycleLength;
    }

    /**
     * @param start The time this frequency object started
     * @return A {@link Cycle} object which provides the wait time until the next cycle
     */
    public Cycle start(Time start)
    {
        return new Cycle(start);
    }

    /**
     * @return A {@link Cycle} object which provides the wait time until the next cycle
     */
    public Cycle start()
    {
        return start(Time.now());
    }

    @Override
    public String toString()
    {
        return "every " + cycleLength;
    }
}
