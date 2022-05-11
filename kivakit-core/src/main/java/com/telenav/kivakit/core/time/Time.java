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
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.ZoneId;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.time.Duration.ZERO_DURATION;
import static com.telenav.kivakit.core.time.Hour.militaryHour;
import static com.telenav.kivakit.core.time.LocalTime.localTimeZone;
import static com.telenav.kivakit.core.time.LocalTime.utcTimeZone;
import static com.telenav.kivakit.core.time.Second.second;

/**
 * An immutable <code>Time</code> class that represents a specific point in UNIX time. The underlying representation is
 * a <code>long</code> value which holds a number of milliseconds since January 1, 1970, 0:00 GMT. To represent a
 * duration of time, such as "6 seconds", use the
 * <code>Duration</code> class. To represent a time period with a start and end time, use the
 * <code>TimeSpan</code> class.
 *
 * @author Jonathan Locke
 * @since 1.2.6
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramTime.class)
public class Time extends BaseTime<Time>
{
    /** The beginning of UNIX time: January 1, 1970, 0:00 GMT. */
    public static final Time START_OF_UNIX_TIME = milliseconds(0);

    /** The minimum time value is the start of UNIX time */
    public static final Time MINIMUM = START_OF_UNIX_TIME;

    /** The end of time */
    public static final Time MAXIMUM = milliseconds(Long.MAX_VALUE);

    /**
     * Retrieves a <code>Time</code> instance based on the given milliseconds.
     *
     * @param milliseconds the <code>Time</code> value in milliseconds since START_OF_UNIX_TIME
     * @return a corresponding immutable <code>Time</code> object
     */
    public static Time milliseconds(long milliseconds)
    {
        return new Time(milliseconds);
    }

    /**
     * Retrieves a <code>Time</code> instance based on the given nanoseconds.
     *
     * @param nanoseconds the <code>Time</code> value in nanoseconds since START_OF_UNIX_TIME
     * @return a corresponding immutable <code>Time</code> object
     */
    public static Time nanoseconds(long nanoseconds)
    {
        return new Time(nanoseconds / 1_000_000);
    }

    /**
     * Retrieves a <code>Time</code> instance based on the current time.
     *
     * @return the current <code>Time</code>
     */
    public static Time now()
    {
        return milliseconds(System.currentTimeMillis());
    }

    /**
     * @return A <code>Time</code> object representing the given number of seconds since START_OF_UNIX_TIME
     */
    public static Time seconds(double seconds)
    {
        return milliseconds((long) (seconds * 1000));
    }

    public static Time utcTime(Year year, Month month, Day dayOfMonth, Hour hour)
    {
        return utcTime(year, month, dayOfMonth, hour, Minute.minute(0), second(0));
    }

    public static Time utcTime(Year year, Month month, Day dayOfMonth)
    {
        return utcTime(year, month, dayOfMonth, militaryHour(0));
    }

    public static Time utcTime(Year year, Month month)
    {
        return utcTime(year, month, Day.dayOfMonth(1), militaryHour(0));
    }

    public static Time utcTime(Year year,
                               Month month,
                               Day dayOfMonth,
                               Hour hour,
                               Minute minute,
                               Second second)
    {
        return nanoseconds(LocalTime.localTime(utcTimeZone(), year, month, dayOfMonth, hour, minute, second).nanoseconds());
    }

    /**
     * Private constructor forces use of static factory methods.
     *
     * @param milliseconds the <code>Time</code> value in milliseconds since START_OF_UNIX_TIME
     */
    protected Time(long milliseconds)
    {
        super(milliseconds);
    }

    protected Time()
    {
    }

    public LocalTime asLocalTime()
    {
        return inTimeZone(timeZone());
    }

    public Time asUtc()
    {
        return inTimeZone(utcTimeZone());
    }

    /**
     * Calculates the amount of time that has elapsed since this <code>Time</code> value.
     *
     * @return the amount of time that has elapsed since this <code>Time</code> value
     */
    public Duration elapsedSince()
    {
        return elapsedSince(Time.now());
    }

    /**
     * Subtract time from this and returns the difference as a <code>Duration</code> object.
     *
     * @param that The time to subtract
     * @return The <code>Duration</code> between this and that time
     */
    public Duration elapsedSince(Time that)
    {
        // If this time is after the given time,
        if (isAfter(that))
        {
            // then we can subtract to get the duration.
            return Duration.milliseconds(asMilliseconds() - that.asUtc().asMilliseconds());
        }

        return ZERO_DURATION;
    }

    public LocalTime inTimeZone(ZoneId zone)
    {
        return LocalTime.localTime(ensureNotNull(zone), this);
    }

    /**
     * Returns true if this time has a time zone
     */
    public boolean isLocal()
    {
        return false;
    }

    /**
     * @return True if this time value is newer than the given {@link Duration}
     */
    public boolean isNewerThan(Duration duration)
    {
        return elapsedSince().isLessThan(duration);
    }

    /**
     * @return True if this time value is newer than the given time value
     */
    public boolean isNewerThan(Time that)
    {
        return isGreaterThan(that);
    }

    /**
     * @return True if this time value is newer than or equal to the given duration
     */
    public boolean isNewerThanOrEqual(Duration duration)
    {
        return elapsedSince().isLessThanOrEqualTo(duration);
    }

    /**
     * @return True if this time value is newer than or equal to the given time value
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
     * @return The amount of time left until the given amount of time has elapsed
     */
    public Duration leftUntil(Duration elapsed)
    {
        return elapsed.minus(elapsedSince());
    }

    public LocalTime localTime()
    {
        return inTimeZone(localTimeZone());
    }

    @Override
    public Time maximum()
    {
        return MAXIMUM;
    }

    @Override
    public Time minimum()
    {
        return START_OF_UNIX_TIME;
    }

    @Override
    public long nanosecondsPerUnit()
    {
        return 1;
    }

    @Override
    public Duration newDuration(long nanoseconds)
    {
        return Duration.nanoseconds(nanoseconds);
    }

    @Override
    public Time newTime(long nanoseconds)
    {
        return Time.nanoseconds(nanoseconds);
    }

    public ZoneId timeZone()
    {
        return utcTimeZone();
    }

    @Override
    public String toString()
    {
        return localTime().toString();
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
        return until(Time.now());
    }

    public LocalTime utc()
    {
        return localTime().utc();
    }
}
