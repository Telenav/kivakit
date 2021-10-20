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

import com.telenav.kivakit.kernel.data.conversion.string.primitive.FormattedDoubleConverter;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

public class PreciseDuration
{
    private static final double WEEKS_PER_YEAR = 52.177457;

    private static final ThreadMXBean cpu = ManagementFactory.getThreadMXBean();

    private static final Logger LOGGER = LoggerFactory.newLogger();

    public static PreciseDuration cpuTime()
    {
        if (!cpu.isThreadCpuTimeSupported() || !cpu.isThreadCpuTimeEnabled())
        {
            unsupported();
        }
        return nanoseconds(cpu.getCurrentThreadCpuTime());
    }

    public static PreciseDuration microseconds(final double microseconds)
    {
        return nanoseconds((long) (microseconds * 1_000));
    }

    public static PreciseDuration milliseconds(final double milliseconds)
    {
        return microseconds(milliseconds * 1_000);
    }

    public static PreciseDuration nanoseconds(final long nanoseconds)
    {
        return new PreciseDuration(nanoseconds);
    }

    public static PreciseDuration seconds(final double seconds)
    {
        return milliseconds(seconds * 1_000);
    }

    private final long nanoseconds;

    private PreciseDuration(final long nanoseconds)
    {
        this.nanoseconds = nanoseconds;
    }

    /**
     * Retrieves the number of days of the current <code>Duration</code>.
     *
     * @return Number of days of the current <code>Duration</code>
     */
    public final double asDays()
    {
        return asHours() / 24.0;
    }

    public Duration asDuration()
    {
        return Duration.milliseconds(asMilliseconds());
    }

    /**
     * Retrieves the number of hours of the current <code>Duration</code>.
     *
     * @return number of hours of the current <code>Duration</code>
     */
    public final double asHours()
    {
        return asMinutes() / 60.0;
    }

    public double asMicroseconds()
    {
        return nanoseconds / 1000.0;
    }

    public double asMilliseconds()
    {
        return nanoseconds / 1000000.0;
    }

    /**
     * Retrieves the number of minutes of the current <code>Duration</code>.
     *
     * @return number of minutes of the current <code>Duration</code>
     */
    public final double asMinutes()
    {
        return asSeconds() / 60.0;
    }

    public long asNanoseconds()
    {
        return nanoseconds;
    }

    /**
     * Retrieves the number of seconds of the current <code>Duration</code>.
     *
     * @return number of seconds of the current <code>Duration</code>
     */
    public final double asSeconds()
    {
        return asMilliseconds() / 1000.0;
    }

    /**
     * Retrieves the number of weeks of the current <code>Duration</code>.
     *
     * @return Number of weeks of the current <code>Duration</code>
     */
    public final double asWeeks()
    {
        return asDays() / 7;
    }

    /**
     * Retrieves the number of years of the current <code>Duration</code>.
     *
     * @return Number of years of the current <code>Duration</code>
     */
    public final double asYears()
    {
        return asWeeks() / WEEKS_PER_YEAR;
    }

    public boolean isGreaterThan(final PreciseDuration that)
    {
        return nanoseconds > that.nanoseconds;
    }

    public boolean isLessThan(final PreciseDuration that)
    {
        return nanoseconds < that.nanoseconds;
    }

    public PreciseDuration minus(final PreciseDuration that)
    {
        return new PreciseDuration(nanoseconds - that.nanoseconds);
    }

    public PreciseDuration plus(final PreciseDuration that)
    {
        return new PreciseDuration(nanoseconds + that.nanoseconds);
    }

    @Override
    public String toString()
    {
        if (asMilliseconds() >= 0)
        {
            if (asYears() >= 1.0)
            {
                return unitString(asYears(), "year");
            }
            if (asWeeks() >= 1.0)
            {
                return unitString(asWeeks(), "week");
            }
            if (asDays() >= 1.0)
            {
                return unitString(asDays(), "day");
            }
            if (asHours() >= 1.0)
            {
                return unitString(asHours(), "hour");
            }
            if (asMinutes() >= 1.0)
            {
                return unitString(asMinutes(), "minute");
            }
            if (asSeconds() >= 1.0)
            {
                return unitString(asSeconds(), "second");
            }
            if (asMilliseconds() >= 1.0)
            {
                return unitString(asMilliseconds(), "millisecond");
            }
            if (asMicroseconds() >= 1.0)
            {
                return unitString(asMicroseconds(), "microsecond");
            }
            return unitString(asNanoseconds(), "nanoseconds");
        }
        else
        {
            return "N/A";
        }
    }

    /**
     * Converts a value to a unit-suffixed value, taking care of English singular/plural suffix.
     *
     * @param value a <code>double</code> value to format
     * @param units the units to apply singular or plural suffix to
     * @return a <code>String</code> representation
     */
    private String unitString(final double value, final String units)
    {
        return new FormattedDoubleConverter(LOGGER).unconvert(value) + " " + units + (value > 1.0 ? "s" : "");
    }
}
