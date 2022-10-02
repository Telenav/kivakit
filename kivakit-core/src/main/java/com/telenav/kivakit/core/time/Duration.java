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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramTime;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.level.Percent;
import com.telenav.kivakit.interfaces.time.Awaitable;
import com.telenav.kivakit.interfaces.time.LengthOfTime;
import com.telenav.kivakit.interfaces.time.Nanoseconds;
import com.telenav.kivakit.interfaces.time.WakeState;
import com.telenav.kivakit.interfaces.value.DoubleValued;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.math.BigDecimal;
import java.util.regex.Pattern;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;
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
 * using these methods, and the {@link #nanoseconds()} method will provide the fully-accurate underlying
 * {@link BigDecimal} value.
 * </p>
 *
 * <p><b>Creation</b></p>
 *
 * <p>
 * These static factory methods allow easy construction of value objects:
 * </p>
 *
 * <ul>
 *     <li>{@link #cpuTime()}</li>
 *     <li>{@link #days(double)}</li>
 *     <li>{@link #days(double, Restriction)}</li>
 *     <li>{@link #hours(double)}</li>
 *     <li>{@link #hours(double, Restriction)}</li>
 *     <li>{@link #microseconds(double)}</li>
 *     <li>{@link #microseconds(double, Restriction)}</li>
 *     <li>{@link #milliseconds(double)}</li>
 *     <li>{@link #milliseconds(double, Restriction)}</li>
 *     <li>{@link #minutes(double)}</li>
 *     <li>{@link #minutes(double, Restriction)}</li>
 *     <li>{@link #nanoseconds(Nanoseconds)}</li>
 *     <li>{@link #nanoseconds(Nanoseconds, Restriction)}</li>
 *     <li>{@link #nanoseconds(double)} </li>
 *     <li>{@link #nanoseconds(double, Restriction)} </li>
 *     <li>{@link #parseDuration(Listener, String)}</li>
 *     <li>{@link #parseDuration(String)}</li>
 *     <li>{@link #seconds(double)}</li>
 *     <li>{@link #seconds(double, Restriction)}</li>
 *     <li>{@link #untilNextSecond()}</li>
 *     <li>{@link #weeks(double)}</li>
 *     <li>{@link #weeks(double, Restriction)}</li>
 *     <li>{@link #years(double)}</li>
 *     <li>{@link #years(double, Restriction)}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #nanoseconds()}</li>
 *     <li>{@link #doubleValue()}</li>
 *     <li>{@link #longValue()}</li>
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
 *     <li>{@link #asDays()}</li>
 *     <li>{@link #asFrequency()}</li>
 *     <li>{@link #asHours()}</li>
 *     <li>{@link #asJavaDuration()}</li>
 *     <li>{@link #asMicroseconds()}</li>
 *     <li>{@link #asMilliseconds()}</li>
 *     <li>{@link #asMinutes()}</li>
 *     <li>{@link #asSeconds()}</li>
 *     <li>{@link #asWeeks()}</li>
 *     <li>{@link #asYears()}</li>
 * </ul>
 *
 * <p><b>Minima and Maxima</b></p>
 *
 * <ul>
 *     <li>{@link #isMaximum()}</li>
 *     <li>{@link #maximum(Duration)}</li>
 *     <li>{@link #minimum(Duration)}</li>
 * </ul>
 *
 * <p><b>Arithmetic</b></p>
 *
 * <ul>
 *     <li>{@link #difference(Duration)}</li>
 *     <li>{@link #dividedBy(Duration)}</li>
 *     <li>{@link #dividedBy(double)}</li>
 *     <li>{@link #dividedBy(long)}</li>
 *     <li>{@link #isZero()}</li>
 *     <li>{@link #longerBy(Percent)}</li>
 *     <li>{@link #nearest(Duration)}</li>
 *     <li>{@link #nearestHour()}</li>
 *     <li>{@link #nearestMinute()}</li>
 *     <li>{@link #percentageOf(Duration)}</li>
 *     <li>{@link #shorterBy(Percent)}</li>
 *     <li>{@link #times(double)}</li>
 *     <li>{@link #times(int)}</li>
 * </ul>
 *
 * <p><b>Operations</b></p>
 *
 * <ul>
 *     <li>{@link #await(Awaitable)}</li>
 *     <li>{@link #sleep()}</li>
 * </ul>
 *
 * @author Jonathan Locke
 * @see Time
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramTime.class)
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class Duration implements
        LengthOfTime<Duration>,
        DoubleValued
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

    /** Constant for one minute. */
    public static final Duration ONE_MINUTE = minutes(1);

    /** Constant for one millisecond. */
    public static final Duration ONE_MILLISECOND = milliseconds(1);

    /** Constant for one second. */
    public static final Duration ONE_SECOND = seconds(1);

    /** Constant for one week. */
    public static final Duration ONE_WEEK = days(7);

    /** Constant for one year. */
    public static final Duration ONE_YEAR = years(1);

    /** Pattern to match strings */
    private static final Pattern PATTERN = Pattern.compile(
            "(?x) (?<quantity> \\d+ ([.,] \\d+)?) "
                    + "(\\s+ | - | _)?"
                    + "(?<units> d | h | m | s | ms | us | ns | ((nanosecond | microsecond | millisecond | second | minute | hour | day | week | year) s?))", CASE_INSENSITIVE);

    private static final ThreadMXBean cpu = ManagementFactory.getThreadMXBean();

    /**
     * Returns the current CPU time for the calling thread as a duration
     */
    public static Duration cpuTime()
    {
        if (!cpu.isThreadCpuTimeSupported() || !cpu.isThreadCpuTimeEnabled())
        {
            throw new UnsupportedOperationException();
        }
        return nanoseconds(cpu.getCurrentThreadCpuTime());
    }

    /**
     * Returns the number of days, not allowing a negative value
     */
    public static Duration days(double days)
    {
        return days(days, THROW_IF_NEGATIVE);
    }

    /**
     * Returns the number of days, restricting negative values or not based on the given {@link Restriction}
     */
    public static Duration days(double days, Restriction restriction)
    {
        return hours(days * 24, restriction);
    }

    /**
     * Returns the number of hours, not allowing a negative value
     */
    public static Duration hours(double hours)
    {
        return hours(hours, THROW_IF_NEGATIVE);
    }

    /**
     * Returns the number of hours, restricting negative values or not based on the given {@link Restriction}
     */
    public static Duration hours(double hours, Restriction restriction)
    {
        return minutes(hours * 60, restriction);
    }

    /**
     * Returns the number of microseconds, restricting negative values or not based on the given {@link Restriction}
     */
    public static Duration microseconds(double microseconds, Restriction restriction)
    {
        return nanoseconds(microseconds * 1E3, restriction);
    }

    /**
     * Returns the number of microseconds, not allowing a negative value
     */
    public static Duration microseconds(double microseconds)
    {
        return microseconds(microseconds, THROW_IF_NEGATIVE);
    }

    /**
     * Returns the number of milliseconds, restricting negative values or not based on the given {@link Restriction}
     */
    public static Duration milliseconds(double milliseconds, Restriction restriction)
    {
        return microseconds(milliseconds * 1E3, restriction);
    }

    /**
     * Returns the number of milliseconds, not allowing a negative value
     */
    public static Duration milliseconds(double milliseconds)
    {
        return milliseconds(milliseconds, THROW_IF_NEGATIVE);
    }

    /**
     * Returns the number of minutes, not allowing a negative value
     */
    public static Duration minutes(double minutes)
    {
        return minutes(minutes, THROW_IF_NEGATIVE);
    }

    /**
     * Returns the number of minutes, restricting negative values or not based on the given {@link Restriction}
     */
    public static Duration minutes(double minutes, Restriction restriction)
    {
        return seconds(minutes * 60, restriction);
    }

    /**
     * Returns the number of nanoseconds, restricting negative values or not based on the given {@link Restriction}
     */
    public static Duration nanoseconds(double nanoseconds, Restriction restriction)
    {
        return nanoseconds(Nanoseconds.nanoseconds(nanoseconds), restriction);
    }

    /**
     * Returns the number of nanoseconds, restricting negative values or not based on the given {@link Restriction}
     */
    public static Duration nanoseconds(Nanoseconds nanoseconds, Restriction restriction)
    {
        return new Duration(nanoseconds, restriction);
    }

    /**
     * Returns the number of nanoseconds, not allowing a negative value
     */
    public static Duration nanoseconds(Nanoseconds nanoseconds)
    {
        return nanoseconds(nanoseconds, THROW_IF_NEGATIVE);
    }

    /**
     * Returns the number of nanoseconds, not allowing a negative value
     */
    public static Duration nanoseconds(double nanoseconds)
    {
        return nanoseconds(nanoseconds, THROW_IF_NEGATIVE);
    }

    /**
     * Parses a duration from the given text, throwing an exception if the parse fails
     *
     * @param text The text to parse
     * @return The duration
     */
    public static Duration parseDuration(String text)
    {
        return parseDuration(Listener.throwingListener(), text);
    }

    /**
     * Parses a duration from the given text, calling the given listener with any problems
     *
     * @param listener The listener to notify of problems
     * @param text The text to parse
     * @return The duration
     */
    public static Duration parseDuration(Listener listener, String text)
    {
        var matcher = PATTERN.matcher(text);
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
                listener.problem("Unrecognized units: ${debug}", text);
                return null;
            }
        }
        else
        {
            listener.problem("Unable to parse: ${debug}", text);
            return null;
        }
    }

    /**
     * Returns the number of seconds, restricting negative values or not based on the given {@link Restriction}
     */
    public static Duration seconds(double seconds, Restriction restriction)
    {
        return milliseconds(seconds * 1E3, restriction);
    }

    /**
     * Returns the number of seconds, not allowing a negative value
     */
    public static Duration seconds(double seconds)
    {
        return seconds(seconds, THROW_IF_NEGATIVE);
    }

    /**
     * Returns the amount of time before the next integral number of seconds
     */
    public static Duration untilNextSecond()
    {
        var now = Time.now();
        var then = now.roundUp(ONE_SECOND);
        return now.until(then);
    }

    /**
     * Returns the number of weeks, not allowing a negative value
     */
    public static Duration weeks(double weeks)
    {
        return days(7 * weeks);
    }

    /**
     * Returns the number of weeks, restricting negative values or not based on the given {@link Restriction}
     */
    public static Duration weeks(double weeks, Restriction restriction)
    {
        return days(7 * weeks);
    }

    /**
     * Returns the number of years, restricting negative values or not based on the given {@link Restriction}
     */
    public static Duration years(double years, Restriction restriction)
    {
        return days(365.25 * years);
    }

    /**
     * Returns the number of years, not allowing a negative value
     */
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

    /** The number of nanoseconds for this duration */
    private final Nanoseconds nanoseconds;

    /**
     * Protected constructor forces use of static factory methods.
     *
     * @param nanoseconds The underlying duration in nanoseconds
     */
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
    private Duration()
    {
        nanoseconds = Nanoseconds.ZERO;
    }

    /**
     * Returns this duration as a frequency
     */
    public Frequency asFrequency()
    {
        return Frequency.every(this);
    }

    @Override
    public java.time.Duration asJavaDuration()
    {
        return LengthOfTime.super.asJavaDuration();
    }

    @Override
    public String asString(@NotNull Format format)
    {
        return LengthOfTime.super.asString(format);
    }

    @Override
    public WakeState await(Awaitable awaitable)
    {
        return LengthOfTime.super.await(awaitable);
    }

    @Override
    public int compareTo(LengthOfTime<?> that)
    {
        return LengthOfTime.super.compareTo(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Duration difference(Duration that)
    {
        return LengthOfTime.super.difference(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Duration dividedBy(double value)
    {
        return LengthOfTime.super.dividedBy(value);
    }

    @Override
    public Duration dividedBy(Duration that)
    {
        return LengthOfTime.super.dividedBy(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Duration dividedBy(long value)
    {
        return LengthOfTime.super.dividedBy(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double doubleValue()
    {
        return asMilliseconds();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Duration)
        {
            var that = (Duration) object;
            return nanoseconds().equals(that.nanoseconds());
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return nanoseconds().hashCode();
    }

    /**
     * True if this is the maximum duration
     */
    public boolean isMaximum()
    {
        return equals(Duration.MAXIMUM);
    }

    @Override
    public long longValue()
    {
        return LengthOfTime.super.longValue();
    }

    /**
     * This duration, made longer by the given percentage, for example 50% longer
     */
    public Duration longerBy(Percent percentage)
    {
        return nanoseconds(nanoseconds().times(1.0 + percentage.asUnitValue()));
    }

    /**
     * The maximum of this duration and the given duration
     */
    public Duration maximum(Duration that)
    {
        return isGreaterThan(that) ? this : that;
    }

    /**
     * The minimum of this duration and the given duration
     */
    public Duration minimum(Duration that)
    {
        return isLessThan(that) ? this : that;
    }

    /**
     * @return This duration minus that duration, but never a negative value
     */
    public Duration minus(Duration that)
    {
        return minus(that, FORCE_POSITIVE);
    }

    /**
     * @return This duration minus that duration, but restricted to the given range
     */
    public Duration minus(Duration that, Restriction restriction)
    {
        return nanoseconds(nanoseconds().minus(that.nanoseconds()), restriction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Nanoseconds nanoseconds()
    {
        return nanoseconds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Duration nearest(Duration unit)
    {
        var units = dividedBy(unit.doubleValue());
        return unit.times((long) (units.doubleValue() + 0.5));
    }

    /**
     * Returns the nearest hour
     */
    public Duration nearestHour()
    {
        return nearest(hours(1));
    }

    /**
     * Returns the nearest minute
     */
    public Duration nearestMinute()
    {
        return nearest(minutes(1));
    }

    @Override
    public Duration newDuration(Nanoseconds nanoseconds)
    {
        return new Duration(nanoseconds, Restriction.ALLOW_NEGATIVE);
    }

    /**
     * Returns the percentage of this duration out of the given duration
     */
    public Percent percentageOf(Duration that)
    {
        return Percent.percent(nanoseconds()
                .times(100)
                .dividedBy(that.nanoseconds()));
    }

    /**
     * @return The sum of this duration and that one, but never a negative value.
     */
    public Duration plus(Duration that)
    {
        return plus(that, FORCE_POSITIVE);
    }

    /**
     * @return The sum of this duration and that duration, but restricted to the given range
     */
    public Duration plus(Duration that, Restriction restriction)
    {
        return new Duration(nanoseconds().plus(that.nanoseconds()), restriction);
    }

    /**
     * Returns this duration made shorter by the given percentage, for example 10% shorter
     */
    public Duration shorterBy(Percent percentage)
    {
        return nanoseconds(nanoseconds.times(1.0 - percentage.asUnitValue()));
    }

    @Override
    public String toString()
    {
        return asString();
    }
}
