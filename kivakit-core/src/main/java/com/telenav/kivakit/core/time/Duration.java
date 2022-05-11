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
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.regex.Pattern;

import static com.telenav.kivakit.core.string.Strings.isOneOf;
import static com.telenav.kivakit.core.time.DayOfWeek.isoDayOfWeek;
import static com.telenav.kivakit.core.time.Duration.Restriction.FORCE_POSITIVE;
import static com.telenav.kivakit.core.time.Duration.Restriction.THROW_IF_NEGATIVE;
import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * A {@link Duration} is an immutable length of time stored as a number of milliseconds. Various factory and conversion
 * methods are available for convenience.
 *
 * <p><b>Creation</b></p>
 *
 * <p>
 * These static factory methods allow easy construction of value objects:
 * </p>
 *
 * <ul>
 *     <li>{@link #nanoseconds(long)} </li>
 *     <li>{@link #microseconds(double)}</li>
 *     <li>{@link #milliseconds(long)}</li>
 *     <li>{@link #milliseconds(double)}</li>
 *     <li>{@link #seconds(double)}</li>
 *     <li>{@link #days(double)}</li>
 *     <li>{@link #weeks(double)}</li>
 *     <li>{@link #years(double)}</li>
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
        return hours(24.0 * days);
    }

    @Tested
    public static Duration hours(double hours)
    {
        return minutes(60.0 * hours);
    }

    @Tested
    public static Duration microseconds(double microseconds)
    {
        return nanoseconds((long) (microseconds * 1E3));
    }

    @Tested
    public static Duration milliseconds(double milliseconds)
    {
        return milliseconds((long) (milliseconds + 0.5));
    }

    @Tested
    public static Duration milliseconds(long milliseconds)
    {
        return nanoseconds(milliseconds * 1_000_000);
    }

    @Tested
    public static Duration minutes(double minutes)
    {
        return seconds(60.0 * minutes);
    }

    @Tested
    public static Duration nanoseconds(long nanoseconds)
    {
        return new Duration(nanoseconds, THROW_IF_NEGATIVE);
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
    public static Duration seconds(double seconds)
    {
        return milliseconds(seconds * 1E3);
    }

    @Tested
    public static Duration untilNextSecond()
    {
        var now = Time.now();
        var then = now.roundUp(ONE_SECOND);
        return now.until(then);
    }

    @Tested
    public static Duration weeks(double scalar)
    {
        return days(7 * scalar);
    }

    @Tested
    public static Duration years(double scalar)
    {
        return weeks(52 * scalar);
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

    private final long nanoseconds;

    /**
     * For reflective construction
     */
    @NoTestRequired
    public Duration()
    {
        nanoseconds = 0;
    }

    /**
     * Protected constructor forces use of static factory methods.
     *
     * @param nanoseconds Number of nanoseconds in this <code>Duration</code>
     */
    @NoTestRequired
    protected Duration(long nanoseconds, Restriction restriction)
    {
        if (restriction == THROW_IF_NEGATIVE && nanoseconds < 0)
        {
            throw new IllegalArgumentException("Negative time not allowed");
        }

        if (restriction == FORCE_POSITIVE && nanoseconds < 0)
        {
            nanoseconds = 0;
        }

        this.nanoseconds = nanoseconds;
    }

    /**
     * @return The sum of this duration and that one, but never a negative value.
     */
    @Tested
    public Duration add(Duration that)
    {
        return add(that, FORCE_POSITIVE);
    }

    /**
     * @return The sum of this duration and that duration, but restricted to the given range
     */
    @Tested
    public Duration add(Duration that, Restriction restriction)
    {
        return new Duration(nanoseconds() + that.nanoseconds(), restriction);
    }

    @NoTestRequired
    public Frequency asFrequency()
    {
        return Frequency.every(this);
    }

    @Tested
    public Duration difference(Duration that)
    {
        if (isGreaterThan(that))
        {
            return minus(that);
        }
        else
        {
            return that.minus(this);
        }
    }

    @Tested
    public double dividedBy(Duration that)
    {
        return (double) nanoseconds / that.nanoseconds;
    }

    @Override
    @Tested
    public Duration dividedBy(double divisor)
    {
        return nanoseconds((long) (nanoseconds / divisor));
    }

    @Override
    @Tested
    public boolean equals(Object object)
    {
        if (object instanceof Duration)
        {
            var that = (Duration) object;
            return nanoseconds == that.nanoseconds;
        }
        return false;
    }

    /**
     * @return A String representation of the time of week represented by this duration, assuming it starts on Monday,
     * 00:00, modulo the length of a week.
     */
    @Tested
    public String fromStartOfWeekModuloWeekLength()
    {
        // There are 10080 minutes in a week
        var duration = (int) Math.floor(asMinutes() % 10080);
        // There are 1440 minutes in a day
        var days = duration / (1440);
        var hoursRest = duration % 1440;
        // There are 60 minutes in an hour
        var hours = hoursRest / 60;
        var minutes = hoursRest % 60;
        // Ex. "TUESDAY, 15:32"
        return isoDayOfWeek(days) + ", "
                + (String.valueOf(hours).length() == 2 ? hours : ("0" + hours)) + ":"
                + (String.valueOf(minutes).length() == 2 ? minutes : ("0" + minutes));
    }

    @Override
    @Tested
    public int hashCode()
    {
        return Long.toString(nanoseconds).hashCode();
    }

    @Tested
    public boolean isMaximum()
    {
        return equals(Duration.MAXIMUM);
    }

    @Tested
    public Duration longerBy(Percent percentage)
    {
        return nanoseconds((long) (nanoseconds * (1.0 + percentage.asUnitValue())));
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
        return new Duration(nanoseconds() - that.nanoseconds(), restriction);
    }

    @Tested
    public Duration modulus(Duration that)
    {
        return newDuration(nanoseconds % that.nanoseconds);
    }

    @Override
    @Tested
    public long nanoseconds()
    {
        return nanoseconds;
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
    public Duration newDuration(long nanoseconds)
    {
        return nanoseconds(nanoseconds);
    }

    @Tested
    public Percent percentageOf(Duration that)
    {
        return Percent.percent(100.0 * nanoseconds / that.nanoseconds);
    }

    @Tested
    public Duration shorterBy(Percent percentage)
    {
        return newDuration((long) (nanoseconds * (1.0 - percentage.asUnitValue())));
    }

    @Override
    @NoTestRequired
    public String toString()
    {
        return asString();
    }
}
