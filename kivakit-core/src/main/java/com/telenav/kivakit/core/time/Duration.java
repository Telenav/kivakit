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
import com.telenav.kivakit.core.test.NoTestRequired;
import com.telenav.kivakit.core.test.Tested;
import com.telenav.kivakit.core.value.level.Percent;
import com.telenav.kivakit.interfaces.time.LengthOfTime;
import com.telenav.kivakit.interfaces.time.Nanoseconds;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.math.BigDecimal;
import java.util.regex.Pattern;

import static com.telenav.kivakit.core.string.Strings.isOneOf;
import static com.telenav.kivakit.core.time.Duration.Restriction.FORCE_POSITIVE;
import static com.telenav.kivakit.core.time.Duration.Restriction.THROW_IF_NEGATIVE;
import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * A {@link Duration} is an immutable length of time stored as a number of nanoseconds using {@link BigDecimal}.
 *
 * <p><b>Important Note on Precision</b></p>
 *
 * <p>
 * Some convenience methods take or produce <i>double values</i>, which are only precise up to 2^53 nanoseconds, or
 * approximately 0.285 years (at this time). If it is desirable to avoid imprecision for values larger than this, avoid
 * using these methods, and the {@link #nanoseconds()} method will provide the fully-accurate underlying {@link
 * BigDecimal} value.
 * </p>
 *
 * <p><b>Creation</b></p>
 *
 * <p>
 * These static factory methods allow easy construction of value objects:
 * </p>
 *
 * <ul>
 *     <li>{@link #nanoseconds(double)} </li>
 *     <li>{@link #nanoseconds(double, Restriction)} </li>
 *     <li>{@link #microseconds(double)}</li>
 *     <li>{@link #microseconds(double, Restriction)}</li>
 *     <li>{@link #milliseconds(double)}</li>
 *     <li>{@link #milliseconds(double, Restriction)}</li>
 *     <li>{@link #seconds(double)}</li>
 *     <li>{@link #seconds(double, Restriction)}</li>
 *     <li>{@link #minutes(double)}</li>
 *     <li>{@link #minutes(double, Restriction)}</li>
 *     <li>{@link #hours(double)}</li>
 *     <li>{@link #hours(double, Restriction)}</li>
 *     <li>{@link #days(double)}</li>
 *     <li>{@link #days(double, Restriction)}</li>
 *     <li>{@link #weeks(double)}</li>
 *     <li>{@link #weeks(double, Restriction)}</li>
 *     <li>{@link #years(double)}</li>
 *     <li>{@link #years(double, Restriction)}</li>
 * </ul>
 *
 * <p><b>Conversion</b></p>
 *
 * <p>
 * The precise number of nanoseconds represented by a {@link Duration} object can be retrieved
 * by calling the {@link #nanoseconds()} method. The value of a duration object
 * in a given unit like days or hours can be retrieved by calling one of the following unit methods,
 * each of which returns a double-precision floating point number:
 * <ul>
 *     <li>{@link #asMicroseconds()}</li>
 *     <li>{@link #asMilliseconds()}</li>
 *     <li>{@link #asSeconds()}</li>
 *     <li>{@link #asMinutes()}</li>
 *     <li>{@link #asHours()}</li>
 *     <li>{@link #asDays()}</li>
 *     <li>{@link #asWeeks()}</li>
 *     <li>{@link #asYears()}</li>
 * </ul>
 *
 * @author Jonathan Locke
 * @see Time
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramTime.class)
@Tested
public class Duration implements LengthOfTime<Duration>
{
    /** Constant for maximum duration. */
    public static final Duration MAXIMUM = nanoseconds(Long.MAX_VALUE);

    /** Constant for no duration. */
    public static final Duration ZERO_DURATION = nanoseconds(0);

    /** Constant for one day. */
    public static final Duration ONE_DAY = days(1);

    /** Constant for one half hour. */
    public static final Duration ONE_HALF_HOUR = hours(0.5);

    /** Constant for 15 minutes. */
    public static final Duration ONE_QUARTER_HOUR = hours(.25);

    /** Constant for one hour. */
    public static final Duration ONE_HOUR = hours(1);

    /** Constant for on minute. */
    public static final Duration ONE_MINUTE = minutes(1);

    /** Constant for one second. */
    public static final Duration ONE_SECOND = seconds(1);

    /** Constant for one week. */
    public static final Duration ONE_WEEK = days(7);

    /** Constant for one year. */
    public static final Duration ONE_YEAR = years(1);

    /** Pattern to match strings */
    private static final Pattern PATTERN = Pattern.compile(
            "(?x) (?<quantity> [0-9]+ ([.,] [0-9]+)?) "
                    + "(\\s+ | - | _)?"
                    + "(?<units> d | h | m | s | ms | us | ns | ((nanosecond | microsecond | millisecond | second | minute | hour | day | week | year) s?))", CASE_INSENSITIVE);

    private static final ThreadMXBean cpu = ManagementFactory.getThreadMXBean();

    public static Duration cpuTime()
    {
        if (!cpu.isThreadCpuTimeSupported() || !cpu.isThreadCpuTimeEnabled())
        {
            throw new UnsupportedOperationException();
        }
        return nanoseconds(cpu.getCurrentThreadCpuTime());
    }

    @Tested
    public static Duration days(double days)
    {
        return days(days, THROW_IF_NEGATIVE);
    }

    @Tested
    public static Duration days(double days, Restriction restriction)
    {
        return hours(days * 24, restriction);
    }

    @Tested
    public static Duration hours(double hours)
    {
        return hours(hours, THROW_IF_NEGATIVE);
    }

    @Tested
    public static Duration hours(double hours, Restriction restriction)
    {
        return minutes(hours * 60, restriction);
    }

    @Tested
    public static Duration microseconds(double microseconds, Restriction restriction)
    {
        return nanoseconds(microseconds * 1E3, restriction);
    }

    @Tested
    public static Duration microseconds(double microseconds)
    {
        return microseconds(microseconds, THROW_IF_NEGATIVE);
    }

    @Tested
    public static Duration milliseconds(double milliseconds, Restriction restriction)
    {
        return microseconds(milliseconds * 1E3, restriction);
    }

    @Tested
    public static Duration milliseconds(double milliseconds)
    {
        return milliseconds(milliseconds, THROW_IF_NEGATIVE);
    }

    @Tested
    public static Duration minutes(double minutes)
    {
        return minutes(minutes, THROW_IF_NEGATIVE);
    }

    @Tested
    public static Duration minutes(double minutes, Restriction restriction)
    {
        return seconds(minutes * 60, restriction);
    }

    @Tested
    public static Duration nanoseconds(double nanoseconds, Restriction restriction)
    {
        return nanoseconds(Nanoseconds.nanoseconds(nanoseconds), restriction);
    }

    @Tested
    public static Duration nanoseconds(Nanoseconds nanoseconds, Restriction restriction)
    {
        return new Duration(nanoseconds, restriction);
    }

    @Tested
    public static Duration nanoseconds(Nanoseconds nanoseconds)
    {
        return nanoseconds(nanoseconds, THROW_IF_NEGATIVE);
    }

    @Tested
    public static Duration nanoseconds(double nanoseconds)
    {
        return nanoseconds(nanoseconds, THROW_IF_NEGATIVE);
    }

    @Tested
    public static Duration parseDuration(String value)
    {
        return parseDuration(Listener.throwingListener(), value);
    }

    @Tested
    public static Duration parseDuration(Listener listener, String value)
    {
        var matcher = PATTERN.matcher(value);
        if (matcher.matches())
        {
            var quantity = Double.parseDouble(matcher.group("quantity"));
            var units = matcher.group("units");
            if (isOneOf(units, "nanoseconds", "nanosecond", "ns"))
            {
                return Duration.nanoseconds((long) quantity);
            }
            else if (isOneOf(units, "microseconds", "microsecond", "us"))
            {
                return Duration.microseconds(quantity);
            }
            else if (isOneOf(units, "milliseconds", "millisecond", "ms"))
            {
                return Duration.milliseconds(quantity);
            }
            else if (isOneOf(units, "seconds", "second", "s"))
            {
                return Duration.seconds(quantity);
            }
            else if (isOneOf(units, "minutes", "minute", "m"))
            {
                return Duration.minutes(quantity);
            }
            else if (isOneOf(units, "hours", "hour", "h"))
            {
                return Duration.hours(quantity);
            }
            else if (isOneOf(units, "days", "day", "d"))
            {
                return Duration.days(quantity);
            }
            else if (isOneOf(units, "weeks", "week"))
            {
                return Duration.weeks(quantity);
            }
            else if (isOneOf(units, "years", "year"))
            {
                return Duration.years(quantity);
            }
            else
            {
                listener.problem("Unrecognized units: ${debug}", value);
                return null;
            }
        }
        else
        {
            listener.problem("Unable to parse: ${debug}", value);
            return null;
        }
    }

    @Tested
    public static Duration seconds(double seconds, Restriction restriction)
    {
        return milliseconds(seconds * 1E3, restriction);
    }

    @Tested
    public static Duration seconds(double seconds)
    {
        return seconds(seconds, THROW_IF_NEGATIVE);
    }

    @Tested
    public static Duration untilNextSecond()
    {
        var now = Time.now();
        var then = now.roundUp(ONE_SECOND);
        return now.until(then);
    }

    @Tested
    public static Duration weeks(double weeks)
    {
        return days(7 * weeks);
    }

    public static Duration weeks(double weeks, Restriction restriction)
    {
        return days(7 * weeks);
    }

    @Tested
    public static Duration years(double years, Restriction restriction)
    {
        return days(365.25 * years);
    }

    @Tested
    public static Duration years(double years)
    {
        return years(years, THROW_IF_NEGATIVE);
    }

    /**
     * The range of values allowed for a given duration.
     * <p>
     * Negative durations cannot be directly constructed, but they can result from a subtract operation if that
     * operation explicitly allows negative values. This permits a sequence of operations to produce a valid positive
     * duration at the end, while steps in the computation along the way may temporarily result in negative durations.
     */
    public enum Restriction
    {
        /** Throw an exception if the duration is negative */
        THROW_IF_NEGATIVE,

        /** Force any negative duration to be positive */
        FORCE_POSITIVE,

        /** Allow positive or negative values */
        ALLOW_NEGATIVE
    }

    private final Nanoseconds nanoseconds;

    /**
     * Protected constructor forces use of static factory methods.
     *
     * @param nanoseconds The underlying duration in nanoseconds
     */
    @NoTestRequired
    protected Duration(Nanoseconds nanoseconds, Restriction restriction)
    {
        if (nanoseconds.isNegative())
        {
            if (restriction == THROW_IF_NEGATIVE)
            {
                throw new IllegalArgumentException("Negative time not allowed");
            }
            if (restriction == FORCE_POSITIVE)
            {
                nanoseconds = Nanoseconds.ZERO;
            }
        }

        this.nanoseconds = nanoseconds;
    }

    /**
     * For reflective construction
     */
    @NoTestRequired
    private Duration()
    {
        nanoseconds = Nanoseconds.ZERO;
    }

    @NoTestRequired
    public Frequency asFrequency()
    {
        return Frequency.every(this);
    }

    @Tested
    public double dividedBy(Duration that)
    {
        return nanoseconds().dividedBy(that.nanoseconds());
    }

    @Override
    @Tested
    public boolean equals(Object object)
    {
        if (object instanceof Duration)
        {
            var that = (Duration) object;
            return nanoseconds().equals(that.nanoseconds());
        }
        return false;
    }

    @Override
    @Tested
    public int hashCode()
    {
        return nanoseconds().hashCode();
    }

    @Tested
    public boolean isMaximum()
    {
        return equals(Duration.MAXIMUM);
    }

    @Tested
    public Duration longerBy(Percent percentage)
    {
        return nanoseconds(nanoseconds().times(1.0 + percentage.asUnitValue()));
    }

    @Tested
    public Duration maximum(Duration that)
    {
        return isGreaterThan(that) ? this : that;
    }

    @Tested
    public Duration minimum(Duration that)
    {
        return isLessThan(that) ? this : that;
    }

    /**
     * @return This duration minus that duration, but never a negative value
     */
    @Tested
    public Duration minus(Duration that)
    {
        return minus(that, FORCE_POSITIVE);
    }

    /**
     * @return This duration minus that duration, but restricted to the given range
     */
    @Tested
    public Duration minus(Duration that, Restriction restriction)
    {
        return nanoseconds(nanoseconds().minus(that.nanoseconds()), restriction);
    }

    @Override
    @Tested
    public Nanoseconds nanoseconds()
    {
        return nanoseconds;
    }

    public Duration nearest(Duration unit)
    {
        var units = dividedBy(unit);
        return unit.times((long) (units + 0.5));
    }

    @Tested
    public Duration nearestHour()
    {
        return nearest(hours(1));
    }

    @Tested
    public Duration nearestMinute()
    {
        return nearest(minutes(1));
    }

    @Override
    public Duration newDuration(Nanoseconds nanoseconds)
    {
        return new Duration(nanoseconds, Restriction.ALLOW_NEGATIVE);
    }

    @Tested
    public Percent percentageOf(Duration that)
    {
        return Percent.percent(nanoseconds()
                .times(100)
                .dividedBy(that.nanoseconds()));
    }

    /**
     * @return The sum of this duration and that one, but never a negative value.
     */
    @Tested
    public Duration plus(Duration that)
    {
        return plus(that, FORCE_POSITIVE);
    }

    /**
     * @return The sum of this duration and that duration, but restricted to the given range
     */
    @Tested
    public Duration plus(Duration that, Restriction restriction)
    {
        return new Duration(nanoseconds().plus(that.nanoseconds()), restriction);
    }

    @Tested
    public Duration shorterBy(Percent percentage)
    {
        return nanoseconds(nanoseconds.times(1.0 - percentage.asUnitValue()));
    }

    @Override
    @NoTestRequired
    public String toString()
    {
        return asString();
    }
}
