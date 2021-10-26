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
import com.telenav.kivakit.kernel.data.conversion.string.primitive.FormattedDoubleConverter;
import com.telenav.kivakit.kernel.data.conversion.string.primitive.LongConverter;
import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.interfaces.code.Callback;
import com.telenav.kivakit.kernel.interfaces.numeric.Quantizable;
import com.telenav.kivakit.kernel.language.strings.conversion.AsString;
import com.telenav.kivakit.kernel.language.strings.conversion.StringFormat;
import com.telenav.kivakit.kernel.language.values.level.Percent;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.messages.status.Information;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageTime;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.regex.Pattern;

import static com.telenav.kivakit.kernel.language.strings.conversion.StringFormat.PROGRAMMATIC_IDENTIFIER;
import static com.telenav.kivakit.kernel.language.strings.conversion.StringFormat.USER_LABEL_IDENTIFIER;
import static com.telenav.kivakit.kernel.language.strings.conversion.StringFormat.USER_MULTILINE_IDENTIFIER;

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
public class Duration implements Comparable<Duration>, AsString, Quantizable
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

    private static final double WEEKS_PER_YEAR = 52.177457;

    /** Constant for one year. */
    public static final Duration ONE_YEAR = years(1);

    private static final Logger LOGGER = LoggerFactory.newLogger();

    public static Duration days(final double days)
    {
        return hours(24.0 * days);
    }

    public static Duration days(final int days)
    {
        return hours(24 * days);
    }

    public static Duration hours(final double hours)
    {
        return minutes(60.0 * hours);
    }

    public static Duration hours(final int hours)
    {
        return minutes(60 * hours);
    }

    public static Duration milliseconds(final double milliseconds)
    {
        return milliseconds((long) milliseconds);
    }

    public static Duration milliseconds(final long milliseconds)
    {
        return new Duration(milliseconds, Range.POSITIVE_ONLY);
    }

    public static Duration minutes(final double minutes)
    {
        return seconds(60.0 * minutes);
    }

    public static Duration minutes(final int minutes)
    {
        return seconds(60 * minutes);
    }

    public static Duration nanoseconds(final long nanoseconds)
    {
        return new Duration(nanoseconds / 1_000_000L, Range.POSITIVE_ONLY);
    }

    public static Duration parse(final String string)
    {
        return new Converter(LOGGER).convert(string);
    }

    public static void profile(final Listener listener, final String message, final Runnable code)
    {
        listener.receive(new Information("$ took $", message, profile(code)));
    }

    public static Duration profile(final Runnable code)
    {
        final var start = Time.now();
        code.run();
        return start.elapsedSince();
    }

    public static Duration seconds(final double seconds)
    {
        return milliseconds(seconds * 1000.0);
    }

    public static Duration seconds(final int seconds)
    {
        return milliseconds(seconds * 1000L);
    }

    public static Duration untilNextSecond()
    {
        final var now = Time.now();
        return now.roundUp(ONE_SECOND).minus(now);
    }

    public static Duration weeks(final double scalar)
    {
        return days(7 * scalar);
    }

    public static Duration years(final double scalar)
    {
        return weeks(WEEKS_PER_YEAR * scalar);
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
                "(?<quantity>[0-9]+([.,][0-9]+)?)(\\s+|-|_)(?<units>millisecond|second|minute|hour|day|week|year)s?", Pattern.CASE_INSENSITIVE);

        public Converter(final Listener listener)
        {
            super(listener);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Duration onToValue(final String value)
        {
            final var matcher = PATTERN.matcher(value);
            if (matcher.matches())
            {
                final var quantity = Double.parseDouble(matcher.group("quantity"));
                final var units = matcher.group("units");
                if ("millisecond".equalsIgnoreCase(units))
                {
                    return milliseconds(quantity);
                }
                else if ("second".equalsIgnoreCase(units))
                {
                    return seconds(quantity);
                }
                else if ("minute".equalsIgnoreCase(units))
                {
                    return minutes(quantity);
                }
                else if ("hour".equalsIgnoreCase(units))
                {
                    return hours(quantity);
                }
                else if ("day".equalsIgnoreCase(units))
                {
                    return days(quantity);
                }
                else if ("week".equalsIgnoreCase(units))
                {
                    return weeks(quantity);
                }
                else if ("year".equalsIgnoreCase(units))
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

        public MillisecondsConverter(final Listener listener)
        {
            super(listener);
            longConverter = new LongConverter(listener);
        }

        @Override
        protected Duration onToValue(final String value)
        {
            final var milliseconds = longConverter.convert(value);
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
        public SecondsConverter(final Listener listener)
        {
            super(listener);
        }

        @Override
        protected Duration onToValue(final String value)
        {
            final var seconds = Double.parseDouble(value);
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
    protected Duration(final long milliseconds, final Range range)
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
    public Duration add(final Duration that)
    {
        return add(that, Range.POSITIVE_ONLY);
    }

    /**
     * @return The sum of this duration and that duration, but restricted to the given range
     */
    public Duration add(final Duration that, final Range range)
    {
        final var sum = asMilliseconds() + that.asMilliseconds();
        if (range == Range.POSITIVE_ONLY && sum < 0)
        {
            return NONE;
        }
        return new Duration(sum, range);
    }

    public void after(final Callback<Timer> onTimer)
    {
        final var timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onTimer.callback(timer);
            }
        }, asMilliseconds());
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

    public Frequency asFrequency()
    {
        return Frequency.every(this);
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

    public java.time.Duration asJavaDuration()
    {
        return java.time.Duration.ofMillis(asMilliseconds());
    }

    /**
     * @return Number of milliseconds in this duration
     */
    public long asMilliseconds()
    {
        return milliseconds;
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

    /**
     * Retrieves the number of seconds of the current <code>Duration</code>.
     *
     * @return number of seconds of the current <code>Duration</code>
     */
    public final double asSeconds()
    {
        return asMilliseconds() / 1000.0;
    }

    @Override
    public String asString(final StringFormat format)
    {
        switch (format.identifier())
        {
            case USER_LABEL_IDENTIFIER:
            {
                final String dateFormatPattern;
                if (asHours() > 1.0)
                {
                    dateFormatPattern = "H:mm:ss";
                }
                else if (asMinutes() > 1.0)
                {
                    dateFormatPattern = "m:ss'm'";
                }
                else if (asSeconds() > 1.0)
                {
                    dateFormatPattern = "s's'";
                }
                else
                {
                    dateFormatPattern = "S'ms'";
                }
                final SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                final Date date = new Date(asMilliseconds());
                return dateFormat.format(date);
            }

            case PROGRAMMATIC_IDENTIFIER:
                return Long.toString(asMilliseconds());

            case USER_MULTILINE_IDENTIFIER:
            default:
                return toString();
        }
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

    public boolean await(final Condition condition)
    {
        try
        {
            return condition.await(milliseconds, TimeUnit.MILLISECONDS);
        }
        catch (final InterruptedException ignored)
        {
        }
        return false;
    }

    @Override
    public int compareTo(final Duration that)
    {
        return Long.compare(asMilliseconds(), that.asMilliseconds());
    }

    public Duration difference(final Duration that)
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

    public double divide(final Duration that)
    {
        return (double) milliseconds / that.milliseconds;
    }

    public Duration divide(final int divisor)
    {
        return milliseconds(milliseconds / divisor);
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof Duration)
        {
            final var that = (Duration) object;
            return milliseconds == that.milliseconds;
        }
        return false;
    }

    public void every(final Callback<Timer> onTimer)
    {
        final var timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                onTimer.callback(timer);
            }
        }, 0L, asMilliseconds());
    }

    /**
     * @return A String representation of the time of week represented by this duration, assuming it starts on Monday,
     * 00:00, modulo the length of a week.
     */
    public String fromStartOfWeekModuloWeekLength()
    {
        // There are 10080 minutes in a week
        final var duration = (int) Math.floor(asMinutes() % 10080);
        // There are 1440 minutes in a day
        final var days = duration / (1440);
        final var hoursRest = duration % 1440;
        // There are 60 minutes in an hour
        final var hours = hoursRest / 60;
        final var minutes = hoursRest % 60;
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

    public boolean isApproximately(final Duration that, final Duration within)
    {
        return difference(that).isLessThanOrEqualTo(within);
    }

    public boolean isGreaterThan(final Duration that)
    {
        return milliseconds > that.milliseconds;
    }

    public boolean isGreaterThanOrEqualTo(final Duration that)
    {
        return milliseconds >= that.milliseconds;
    }

    public boolean isLessThan(final Duration that)
    {
        return milliseconds < that.milliseconds;
    }

    public boolean isLessThanOrEqualTo(final Duration that)
    {
        return milliseconds <= that.milliseconds;
    }

    public boolean isNone()
    {
        return equals(NONE);
    }

    public boolean isSome()
    {
        return !isNone();
    }

    public Duration longer(final Percent percentage)
    {
        return milliseconds(milliseconds * (1.0 + percentage.asUnitValue()));
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void loop(final Listener listener, final Runnable runnable)
    {
        while (true)
        {
            try
            {
                runnable.run();
            }
            catch (final Throwable e)
            {
                listener.problem(e, "Loop threw exception");
            }
            sleep();
        }
    }

    public Duration maximum(final Duration that)
    {
        return isGreaterThan(that) ? this : that;
    }

    public Duration minimum(final Duration that)
    {
        return isLessThan(that) ? this : that;
    }

    /**
     * @return This duration minus that duration, but never a negative value
     */
    public Duration minus(final Duration that)
    {
        return minus(that, Range.POSITIVE_ONLY);
    }

    /**
     * @return This duration minus that duration, but restricted to the given range
     */
    public Duration minus(final Duration that, final Range range)
    {
        if (range == Range.POSITIVE_ONLY)
        {
            if (that.isGreaterThan(this))
            {
                return NONE;
            }
        }
        return new Duration(asMilliseconds() - that.asMilliseconds(), range);
    }

    public Duration modulus(final Duration that)
    {
        return milliseconds(milliseconds % that.milliseconds);
    }

    public Duration nearestHour()
    {
        return hours(Math.round(asHours()));
    }

    public Percent percentageOf(final Duration that)
    {
        return Percent.of(100.0 * milliseconds / that.milliseconds);
    }

    public Duration plus(final Duration that)
    {
        return milliseconds(milliseconds + that.milliseconds);
    }

    @Override
    public long quantum()
    {
        return milliseconds;
    }

    public Duration shorter(final Percent percentage)
    {
        return milliseconds(milliseconds * (1.0 - percentage.asUnitValue()));
    }

    /**
     * Sleeps for the current <code>Duration</code>.
     */
    public final void sleep()
    {
        if (milliseconds > 0)
        {
            try
            {
                Thread.sleep(milliseconds);
            }
            catch (final InterruptedException e)
            {
                // Ignored
            }
        }
    }

    public Duration times(final double multiplier)
    {
        return milliseconds(milliseconds * multiplier);
    }

    /**
     * Retrieves the <code>String</code> representation of this <code>Duration</code> in days, hours, minutes, seconds
     * or milliseconds, as appropriate.
     *
     * @return a <code>String</code> representation
     */
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
            return asMilliseconds() + " millisecond" + (milliseconds != 1 ? "s" : "");
        }
        else
        {
            return "N/A";
        }
    }

    /**
     * Wait for this duration on the given monitor. Note that a duration of NONE is considered to be a wait time of zero
     * milliseconds, whereas the underlying Java {@link #wait(long)} considers zero milliseconds to be infinite wait
     * time.
     *
     * @param monitor The monitor to wait on
     * @return True if the thread waited, false if it was interrupted
     */
    @SuppressWarnings({ "UnusedReturnValue", "SynchronizationOnLocalVariableOrMethodParameter" })
    public boolean wait(final Object monitor)
    {
        synchronized (monitor)
        {
            try
            {
                final var milliseconds = asMilliseconds();
                if (milliseconds > 0)
                {
                    monitor.wait(milliseconds);
                }
                return true;
            }
            catch (final InterruptedException e)
            {
                return false;
            }
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
