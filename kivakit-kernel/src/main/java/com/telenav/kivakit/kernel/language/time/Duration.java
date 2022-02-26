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

import com.telenav.kivakit.interfaces.numeric.Quantizable;
import com.telenav.kivakit.interfaces.string.Stringable;
import com.telenav.kivakit.interfaces.time.LengthOfTime;
import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.data.conversion.string.primitive.LongConverter;
import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.language.values.level.Percent;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.messages.status.Information;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageTime;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.regex.Pattern;

import static com.telenav.kivakit.kernel.language.strings.Strings.isOneOf;
import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * A <code>Duration</code> is an immutable length of time stored as a number of milliseconds. Various factory and
 * conversion methods are available for convenience.
 * <p>
 * These static factory methods allow easy construction of value objects using either long values like
 * <code>seconds(30)</code> or <code>hours(3)</code>:
 * <ul>
 * <li><code>Duration.milliseconds(long)</code>
 * <li><code>Duration.seconds(int)</code>
 * <li><code>Duration.minutes(int)</code>
 * <li><code>Duration.hours(int)</code>
 * <li><code>Duration.days(int)</code>
 * </ul>
 * ...or double-precision floating point values like <code>days(3.2)</code>:
 * <ul>
 * <li><code>Duration.milliseconds(double)</code>
 * <li><code>Duration.seconds(double)</code>
 * <li><code>Duration.minutes(double)</code>
 * <li><code>Duration.hours(double)</code>
 * <li><code>Duration.days(double)</code>
 * </ul>
 * The precise number of milliseconds represented by a <code>Duration</code> object can be retrieved
 * by calling the <code>milliseconds</code> method. The value of a <code>Duration</code> object
 * in a given unit like days or hours can be retrieved by calling one of the following unit methods,
 * each of which returns a double-precision floating point number:
 * <ul>
 * <li><code>{#asSeconds()}</code>
 * <li><code>{#asMinutes()}</code>
 * <li><code>{#asHours()}</code>
 * <li><code>{#asDays()}</code>
 * <li><code>{#asWeeks()}</code>
 * <li><code>{#asYears()}</code>
 * </ul>
 * <p>
 * Values can be added and subtracted using the <code>{#add(Duration)}</code> and
 * <code>#{subtract(Duration)}</code> methods, each of which returns a new immutable
 * <code>Duration</code> object.
 * <p>
 * Finally, the <code>sleep</code> method will sleep for the value of a <code>Duration</code>.
 *
 * @author Jonathan Locke
 * @see Time
 */
@UmlClassDiagram(diagram = DiagramLanguageTime.class)
public class Duration implements
        Stringable,
        Quantizable,
        LengthOfTime
{
    /** Constant for maximum duration. */
    public static final Duration MAXIMUM = milliseconds(Long.MAX_VALUE);

    /** Constant for no duration. */
    public static final Duration NONE = milliseconds(0);

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

    public static Duration days(double days)
    {
        return hours(24.0 * days);
    }

    public static Duration days(int days)
    {
        return hours(24 * days);
    }

    public static Duration hours(double hours)
    {
        return minutes(60.0 * hours);
    }

    public static Duration hours(int hours)
    {
        return minutes(60 * hours);
    }

    public static Duration milliseconds(double milliseconds)
    {
        return milliseconds((long) milliseconds);
    }

    public static Duration milliseconds(long milliseconds)
    {
        return new Duration(milliseconds, Range.POSITIVE_ONLY);
    }

    public static Duration minutes(double minutes)
    {
        return seconds(60.0 * minutes);
    }

    public static Duration minutes(int minutes)
    {
        return seconds(60 * minutes);
    }

    public static Duration nanoseconds(long nanoseconds)
    {
        return new Duration(nanoseconds / 1_000_000L, Range.POSITIVE_ONLY);
    }

    public static Duration parse(Listener listener, String string)
    {
        return new Converter(listener).convert(string);
    }

    public static void profile(Listener listener, String message, Runnable code)
    {
        listener.receive(new Information("$ took $", message, profile(code)));
    }

    public static Duration profile(Runnable code)
    {
        var start = Time.now();
        code.run();
        return start.elapsedSince();
    }

    public static Duration seconds(double seconds)
    {
        return milliseconds(seconds * 1000.0);
    }

    public static Duration seconds(int seconds)
    {
        return milliseconds(seconds * 1000L);
    }

    public static Duration untilNextSecond()
    {
        var now = Time.now();
        return now.roundUp(ONE_SECOND).minus(now);
    }

    public static Duration weeks(double scalar)
    {
        return days(7 * scalar);
    }

    public static Duration years(double scalar)
    {
        return weeks(LengthOfTime.WEEKS_PER_YEAR * scalar);
    }

    /**
     * The range of values allowed for a given duration.
     * <p>
     * Negative durations cannot be directly constructed, but they can result from a subtract operation if that
     * operation explicitly allows negative values. This permits a sequence of operations to produce a valid positive
     * duration at the end, while steps in the computation along the way may temporarily result in negative durations.
     */
    public enum Range
    {
        POSITIVE_ONLY,
        ALLOW_NEGATIVE
    }

    /**
     * Converts the given <code>String</code> to a new <code>Duration</code> object. The string can take the form of a
     * floating point number followed by a number of milliseconds, seconds, minutes, hours or days. For example "6
     * hours" or "3.4 days". Parsing is case-insensitive.
     *
     * @author jonathanl (shibo)
     */
    public static class Converter extends BaseStringConverter<Duration>
    {
        /** Pattern to match strings */
        private final Pattern PATTERN = Pattern.compile(
                "(?x) (?<quantity> [0-9]+ ([.,] [0-9]+)?) "
                        + "(\\s+ | - | _)?"
                        + "(?<units> d | h | m | s | ms | ((millisecond | second | minute | hour | day | week | year) s?))", CASE_INSENSITIVE);

        public Converter(Listener listener)
        {
            super(listener);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Duration onToValue(String value)
        {
            var matcher = PATTERN.matcher(value);
            if (matcher.matches())
            {
                var quantity = Double.parseDouble(matcher.group("quantity"));
                var units = matcher.group("units");
                if (isOneOf(units, "milliseconds", "millisecond", "ms"))
                {
                    return milliseconds(quantity);
                }
                else if (isOneOf(units, "seconds", "second", "s"))
                {
                    return seconds(quantity);
                }
                else if (isOneOf(units, "minutes", "minute", "m"))
                {
                    return minutes(quantity);
                }
                else if (isOneOf(units, "hours", "hour", "h"))
                {
                    return hours(quantity);
                }
                else if (isOneOf(units, "days", "day", "d"))
                {
                    return days(quantity);
                }
                else if (isOneOf(units, "weeks", "week"))
                {
                    return weeks(quantity);
                }
                else if (isOneOf(units, "years", "year"))
                {
                    return years(quantity);
                }
                else
                {
                    problem("Unrecognized units: ${debug}", value);
                    return null;
                }
            }
            else
            {
                problem("Unable to parse: ${debug}", value);
                return null;
            }
        }
    }

    /**
     * Converts milliseconds to and from {@link Duration}
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public static class MillisecondsConverter extends BaseStringConverter<Duration>
    {
        private final LongConverter longConverter;

        public MillisecondsConverter(Listener listener)
        {
            super(listener);
            longConverter = new LongConverter(listener);
        }

        @Override
        protected Duration onToValue(String value)
        {
            var milliseconds = longConverter.convert(value);
            return milliseconds == null ? null : milliseconds(milliseconds);
        }
    }

    /**
     * Converts seconds to and from {@link Duration}
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public static class SecondsConverter extends BaseStringConverter<Duration>
    {
        public SecondsConverter(Listener listener)
        {
            super(listener);
        }

        @Override
        protected Duration onToValue(String value)
        {
            var seconds = Double.parseDouble(value);
            if (seconds >= 0)
            {
                return seconds(seconds);
            }
            return null;
        }
    }

    private final long milliseconds;

    /**
     * For reflective construction
     */
    public Duration()
    {
        milliseconds = 0;
    }

    /**
     * Protected constructor forces use of static factory methods.
     *
     * @param milliseconds Number of milliseconds in this <code>Duration</code>
     */
    protected Duration(long milliseconds, Range range)
    {
        if (range == Range.POSITIVE_ONLY && milliseconds < 0)
        {
            Ensure.fail("Negative time not allowed");
        }
        this.milliseconds = milliseconds;
    }

    /**
     * @return The sum of this duration and that one, but never a negative value.
     */
    public Duration add(Duration that)
    {
        return add(that, Range.POSITIVE_ONLY);
    }

    /**
     * @return The sum of this duration and that duration, but restricted to the given range
     */
    public Duration add(Duration that, Range range)
    {
        var sum = milliseconds() + that.milliseconds();
        if (range == Range.POSITIVE_ONLY && sum < 0)
        {
            return NONE;
        }
        return new Duration(sum, range);
    }

    public Frequency asFrequency()
    {
        return Frequency.every(this);
    }

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

    public double dividedBy(Duration that)
    {
        return (double) milliseconds / that.milliseconds;
    }

    public Duration dividedBy(int divisor)
    {
        return milliseconds(milliseconds / divisor);
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Duration)
        {
            var that = (Duration) object;
            return milliseconds == that.milliseconds;
        }
        return false;
    }

    /**
     * @return A String representation of the time of week represented by this duration, assuming it starts on Monday,
     * 00:00, modulo the length of a week.
     */
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
        return DayOfWeek.forIsoConstant(days).toString() + ", "
                + (String.valueOf(hours).length() == 2 ? hours : ("0" + hours)) + ":"
                + (String.valueOf(minutes).length() == 2 ? minutes : ("0" + minutes));
    }

    @Override
    public int hashCode()
    {
        return Long.toString(milliseconds).hashCode();
    }

    public Duration longer(Percent percentage)
    {
        return milliseconds(milliseconds * (1.0 + percentage.asUnitValue()));
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void loop(Listener listener, Runnable runnable)
    {
        while (true)
        {
            try
            {
                runnable.run();
            }
            catch (Throwable e)
            {
                listener.problem(e, "Loop threw exception");
            }
            sleep();
        }
    }

    public Duration maximum(Duration that)
    {
        return isGreaterThan(that) ? this : that;
    }

    @Override
    public long milliseconds()
    {
        return milliseconds;
    }

    public Duration minimum(Duration that)
    {
        return isLessThan(that) ? this : that;
    }

    /**
     * @return This duration minus that duration, but never a negative value
     */
    public Duration minus(Duration that)
    {
        return minus(that, Range.POSITIVE_ONLY);
    }

    /**
     * @return This duration minus that duration, but restricted to the given range
     */
    public Duration minus(Duration that, Range range)
    {
        if (range == Range.POSITIVE_ONLY)
        {
            if (that.isGreaterThan(this))
            {
                return NONE;
            }
        }
        return new Duration(milliseconds() - that.milliseconds(), range);
    }

    public Duration modulus(Duration that)
    {
        return milliseconds(milliseconds % that.milliseconds);
    }

    public Duration nearestHour()
    {
        return hours(Math.round(asHours()));
    }

    public Percent percentageOf(Duration that)
    {
        return Percent.of(100.0 * milliseconds / that.milliseconds);
    }

    public Duration plus(Duration that)
    {
        return milliseconds(milliseconds + that.milliseconds);
    }

    public Duration shorter(Percent percentage)
    {
        return milliseconds(milliseconds * (1.0 - percentage.asUnitValue()));
    }

    public Duration times(double multiplier)
    {
        return milliseconds(milliseconds * multiplier);
    }

    public String toString()
    {
        return asString();
    }
}
