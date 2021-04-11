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

package com.telenav.kivakit.core.kernel.language.time;

import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageTime;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A simple frequency domain object. This object is not thread-safe and cannot be shared.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageTime.class)
public class Frequency
{
    public static final Frequency ONCE = every(Duration.MAXIMUM);

    public static final Frequency EVERY_SECOND = timesPerSecond(1);

    public static final Frequency EVERY_HALF_SECOND = timesPerSecond(2);

    public static final Frequency EVERY_5_SECONDS = every(Duration.seconds(5));

    public static final Frequency EVERY_10_SECONDS = every(Duration.seconds(10));

    public static final Frequency EVERY_15_SECONDS = every(Duration.seconds(15));

    public static final Frequency EVERY_30_SECONDS = every(Duration.seconds(30));

    public static final Frequency EVERY_MINUTE = timesPerMinute(1);

    public static final Frequency ONCE_A_DAY = timesPerDay(1);

    public static final Frequency FOUR_TIMES_A_DAY = timesPerDay(4);

    public static final Frequency CONTINUOUSLY = every(Duration.NONE);

    public static Frequency every(final Duration duration)
    {
        return new Frequency(duration);
    }

    public static Frequency timesPerDay(final int times)
    {
        return every(Duration.ONE_DAY.divide(times));
    }

    public static Frequency timesPerHour(final int times)
    {
        return every(Duration.ONE_HOUR.divide(times));
    }

    public static Frequency timesPerMinute(final int times)
    {
        return every(Duration.ONE_MINUTE.divide(times));
    }

    public static Frequency timesPerSecond(final int times)
    {
        return every(Duration.ONE_SECOND.divide(times));
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
        public Converter(final Listener listener)
        {
            super(listener);
        }

        @Override
        protected Frequency onConvertToObject(final String value)
        {
            final var duration = Duration.parse(value);
            return duration == null ? null : Frequency.every(duration);
        }
    }

    public class Cycle
    {
        private final Time start;

        public Cycle(final Time start)
        {
            this.start = start;
        }

        public Time next()
        {
            return Time.now().add(untilNext());
        }

        public Duration untilNext()
        {
            if (duration.isNone())
            {
                return Duration.NONE;
            }
            // The time since this frequency started
            final var sinceStart = start.elapsedSince();

            // The elapsed time modulus the duration is how far we are into the
            // current cycle
            final var intoCurrentCycle = Duration
                    .milliseconds(sinceStart.asMilliseconds() % duration.asMilliseconds());

            // Find out how long is left until the next cycle
            return duration.subtract(intoCurrentCycle);
        }
    }

    private final Duration duration;

    protected Frequency(final Duration duration)
    {
        if (duration == null)
        {
            Ensure.fail("Duration may not be null");
        }
        this.duration = duration;
    }

    public Duration duration()
    {
        return duration;
    }

    public Cycle start(final Time start)
    {
        return new Cycle(start);
    }

    @Override
    public String toString()
    {
        return "every " + duration;
    }
}
