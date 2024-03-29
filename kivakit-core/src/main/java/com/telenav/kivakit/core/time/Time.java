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
import com.telenav.kivakit.core.language.Try;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.interfaces.time.Nanoseconds;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.Instant;
import java.time.ZoneId;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.time.BaseTime.Topology.LINEAR;
import static com.telenav.kivakit.core.time.Day.dayOfMonth;
import static com.telenav.kivakit.core.time.Duration.ZERO_DURATION;
import static com.telenav.kivakit.core.time.Hour.militaryHour;
import static com.telenav.kivakit.core.time.KivaKitTimeFormats.KIVAKIT_DATE_TIME;
import static com.telenav.kivakit.core.time.LocalTime.localTime;
import static com.telenav.kivakit.core.time.LocalTime.localTimeZone;
import static com.telenav.kivakit.core.time.LocalTime.utcTimeZone;
import static com.telenav.kivakit.core.time.Minute.minute;
import static com.telenav.kivakit.core.time.Second.second;
import static com.telenav.kivakit.interfaces.time.Nanoseconds.ONE_NANOSECOND;
import static java.lang.Long.parseLong;
import static java.lang.System.currentTimeMillis;

/**
 * An immutable <code>Time</code> class that represents a specific point in UNIX time. The underlying representation is
 * a <code>long</code> value which holds a number of milliseconds since January 1, 1970, 0:00 GMT. To represent a
 * duration of time, such as "6 seconds", use the
 * <code>Duration</code> class. To represent a time period with a start and end time, use the
 * <code>TimeSpan</code> class.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #epochMilliseconds(long)}</li>
 *     <li>{@link #epochNanoseconds(Nanoseconds)}</li>
 *     <li>{@link #now()}</li>
 *     <li>{@link #utcTime(Year, Month, Day, Hour)}</li>
 *     <li>{@link #utcTime(Year, Month, Day)}</li>
 *     <li>{@link #utcTime(Year, Month)}</li>
 *     <li>{@link #utcTime(Year, Month, Day, Hour, Minute, Second)}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #isLocal()}</li>
 * </ul>
 *
 * <p><b>Computations</b></p>
 *
 * <ul>
 *     <li>{@link #decremented()}</li>
 *     <li>{@link #elapsedSince()}</li>
 *     <li>{@link #elapsedSince(Time)}</li>
 *     <li>{@link #incremented()}</li>
 *     <li>{@link #leftUntil(Duration)}</li>
 *     <li>{@link #minusUnits(double)}</li>
 *     <li>{@link #next()}</li>
 *     <li>{@link #plusUnits(double)}</li>
 *     <li>{@link #until(Time)}</li>
 *     <li>{@link #untilNow()}</li>
 * </ul>
 *
 * <p><b>Comparisons</b></p>
 *
 * <ul>
 *     <li>{@link #isNewerThan(Time)}</li>
 *     <li>{@link #isNewerThan(Duration)}</li>
 *     <li>{@link #isOlderThan(Time)}</li>
 *     <li>{@link #isOlderThan(Duration)}</li>
 *     <li>{@link #isNewerThanOrEqualTo(Time)}</li>
 *     <li>{@link #isOlderThanOrEqualTo(Time)}</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #asUtc()}</li>
 *     <li>{@link #asLocalTime()}</li>
 * </ul>
 *
 * <p><b>Time Zone</b></p>
 *
 * <ul>
 *     <li>{@link #asUtc()}</li>
 *     <li>{@link #timeZone()}</li>
 * </ul>
 *
 * @author Jonathan Locke
 * @see BaseTime
 * @since 1.2.6
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramTime.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class Time extends BaseTime<Time>
{
    /** The beginning of UNIX time: January 1, 1970, 0:00 GMT. */
    public static final Time START_OF_UNIX_TIME = epochMilliseconds(0);

    /** The minimum time value is the start of UNIX time */
    public static final Time MINIMUM = START_OF_UNIX_TIME;

    /** The end of time */
    public static final Time END_OF_UNIX_TIME = epochMilliseconds(Long.MAX_VALUE);

    /**
     * Retrieves a <code>Time</code> instance based on the given milliseconds.
     *
     * @param milliseconds the <code>Time</code> value in milliseconds
     * @return a corresponding immutable <code>Time</code> object
     */
    public static Time epochMilliseconds(long milliseconds)
    {
        return new Time(Nanoseconds.milliseconds(milliseconds));
    }

    public static Time epochMilliseconds(String milliseconds)
    {
        return milliseconds(milliseconds);
    }

    /**
     * Retrieves a <code>Time</code> instance based on the given nanoseconds.
     *
     * @param nanoseconds the <code>Time</code> value in nanoseconds since START_OF_UNIX_TIME
     * @return a corresponding immutable <code>Time</code> object
     */
    public static Time epochNanoseconds(Nanoseconds nanoseconds)
    {
        return new Time(nanoseconds);
    }

    public static Time milliseconds(String milliseconds)
    {
        return parseMilliseconds(throwingListener(), milliseconds);
    }

    /**
     * Retrieves a <code>Time</code> instance based on the current time.
     *
     * @return the current <code>Time</code>
     */
    public static Time now()
    {
        return epochMilliseconds(currentTimeMillis());
    }

    public static Time parseMilliseconds(Listener listener, String milliseconds)
    {
        return Try.tryCatch(listener, () -> epochMilliseconds(parseLong(milliseconds)), "Unable to parse $: ", milliseconds);
    }

    public static Time parseTime(Listener listener, String text)
    {
        var time = KIVAKIT_DATE_TIME.parse(text, Instant::from);
        return epochMilliseconds(time.toEpochMilli());
    }

    public static Time time(String text)
    {
        return parseTime(throwingListener(), text);
    }

    public static Time utcTime(Year year, Month month, Day dayOfMonth, Hour hour)
    {
        return utcTime(year, month, dayOfMonth, hour, minute(0), second(0));
    }

    public static Time utcTime(Year year, Month month, Day dayOfMonth)
    {
        return utcTime(year, month, dayOfMonth, militaryHour(0));
    }

    public static Time utcTime(Year year, Month month)
    {
        return utcTime(year, month, dayOfMonth(1), militaryHour(0));
    }

    public static Time utcTime(Year year,
                               Month month,
                               Day dayOfMonth,
                               Hour hour,
                               Minute minute,
                               Second second)
    {
        return epochNanoseconds(localTime(utcTimeZone(), year, month, dayOfMonth, hour, minute, second).nanoseconds());
    }

    /**
     * Private constructor forces use of static factory methods.
     *
     * @param nanoseconds the <code>Time</code> value in nanoseconds since START_OF_UNIX_TIME
     */
    protected Time(Nanoseconds nanoseconds)
    {
        super(nanoseconds);
    }

    protected Time()
    {
    }

    public LocalTime asLocalFilesystemTime()
    {
        return asLocalTime().roundDownToSeconds();
    }

    public LocalTime asLocalFilesystemTime(ZoneId zone)
    {
        return asLocalTime(zone).roundDownToSeconds();
    }

    public LocalTime asLocalTime()
    {
        return asLocalTime(localTimeZone());
    }

    public LocalTime asLocalTime(ZoneId zone)
    {
        return localTime(ensureNotNull(zone), this);
    }

    /**
     * Returns this time in the UTC timezone
     */
    public Time asUtc()
    {
        return this;
    }

    /**
     * Calculates the amount of time that has elapsed since this <code>Time</code> value.
     *
     * @return the amount of time that has elapsed since this <code>Time</code> value
     */
    public Duration elapsedSince()
    {
        return now().elapsedSince(this);
    }

    /**
     * Subtracts time from this and returns the difference as a <code>Duration</code> object.
     *
     * @param that The time to subtract
     * @return The <code>Duration</code> between this and that time
     */
    public Duration elapsedSince(Time that)
    {
        // If this time is after the given time,
        if (asUtc().isAtOrAfter(that.asUtc()))
        {
            // then we can subtract the UTC values to get the duration.
            return Duration.milliseconds(asUtc().asMilliseconds() - that.asUtc().asMilliseconds());
        }

        return ZERO_DURATION;
    }

    /**
     * Returns true if this time has a time zone
     */
    public boolean isLocal()
    {
        return false;
    }

    /**
     * Returns true if this time value is newer than the given {@link Duration}
     */
    public boolean isNewerThan(Duration duration)
    {
        return elapsedSince().isLessThan(duration);
    }

    /**
     * Returns true if this time value is newer than the given time value
     */
    public boolean isNewerThan(Time that)
    {
        return isGreaterThan(that);
    }

    /**
     * Returns true if this time value is newer than or equal to the given duration
     */
    public boolean isNewerThanOrEqual(Duration duration)
    {
        return elapsedSince().isLessThanOrEqualTo(duration);
    }

    /**
     * Returns true if this time value is newer than or equal to the given time value
     */
    public boolean isNewerThanOrEqualTo(Time that)
    {
        return isGreaterThanOrEqualTo(that);
    }

    /**
     * True if this time is now older than the given duration.
     *
     * @param duration Amount of time to be considered old
     * @return True if the time that has elapsed since this time is greater than the given duration
     */
    public boolean isOlderThan(Duration duration)
    {
        return elapsedSince().isGreaterThan(duration);
    }

    public boolean isOlderThan(Time that)
    {
        return isLessThan(that);
    }

    public boolean isOlderThanOrEqual(Duration duration)
    {
        return elapsedSince().isGreaterThanOrEqualTo(duration);
    }

    public boolean isOlderThanOrEqualTo(Time that)
    {
        return isLessThanOrEqualTo(that);
    }

    /**
     * Returns the amount of time left until the given amount of time has elapsed
     */
    public Duration leftUntil(Duration elapsed)
    {
        return elapsed.minus(elapsedSince());
    }

    @Override
    public Time maximum()
    {
        return END_OF_UNIX_TIME;
    }

    @Override
    public Time minimum()
    {
        return START_OF_UNIX_TIME;
    }

    @Override
    public Nanoseconds nanosecondsPerUnit()
    {
        return ONE_NANOSECOND;
    }

    @Override
    public Duration newDuration(Nanoseconds nanoseconds)
    {
        return Duration.nanoseconds(nanoseconds);
    }

    @Override
    public Time onNewTime(Nanoseconds nanoseconds)
    {
        return epochNanoseconds(nanoseconds);
    }

    /**
     * Returns the timezone for this time. For {@link Time} this will always be the UTC timezone. For the
     * {@link LocalTime} subclass this will be the local timezone.
     */
    public ZoneId timeZone()
    {
        return utcTimeZone();
    }

    @Override
    public String toString()
    {
        return asLocalTime(timeZone()).toString();
    }

    public Duration until(Time that)
    {
        return that.elapsedSince(this);
    }

    /**
     * Retrieves the <code>Duration</code> from now to this <code>Time</code> value. If this
     * <code>Time</code> value is in the past, then the <code>Duration</code> returned will be
     * negative. Otherwise, it will be duration from now to this <code>Time</code> .
     *
     * @return the <code>Duration</code> from now to this <code>Time</code> value
     */
    public Duration untilNow()
    {
        return until(now());
    }

    @Override
    protected Topology topology()
    {
        return LINEAR;
    }
}
