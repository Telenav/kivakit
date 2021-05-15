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

package com.telenav.kivakit.kernel.language.time;

import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.interfaces.numeric.Quantizable;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageTime;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.Instant;
import java.time.ZoneId;

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
@UmlClassDiagram(diagram = DiagramLanguageTime.class)
public class Time implements Quantizable
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
    public static Time milliseconds(final long milliseconds)
    {
        return new Time(milliseconds);
    }

    /**
     * Retrieves a <code>Time</code> instance based on the given nanoseconds.
     *
     * @param nanoseconds the <code>Time</code> value in nanoseconds since START_OF_UNIX_TIME
     * @return a corresponding immutable <code>Time</code> object
     */
    public static Time nanoseconds(final long nanoseconds)
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
    public static Time seconds(final double seconds)
    {
        return milliseconds((long) (seconds * 1000));
    }

    /** The number of milliseconds since start of UNIX time */
    private long milliseconds;

    /**
     * Private constructor forces use of static factory methods.
     *
     * @param milliseconds the <code>Time</code> value in milliseconds since START_OF_UNIX_TIME
     */
    protected Time(final long milliseconds)
    {
        Ensure.ensure(milliseconds >= 0);
        this.milliseconds = milliseconds;
    }

    protected Time()
    {
    }

    public Instant asInstant()
    {
        return Instant.ofEpochMilli(asMilliseconds());
    }

    /**
     * Converts this time to a UNIX time stamp (milliseconds since the start of UNIX time on January 1, 1970)
     *
     * @return This time as milliseconds since 1970
     */
    public long asMilliseconds()
    {
        return milliseconds;
    }

    public int asSeconds()
    {
        return (int) (asMilliseconds() / 1000);
    }

    public int compareTo(final Time that)
    {
        return Long.compare(asMilliseconds(), that.asMilliseconds());
    }

    /**
     * Calculates the amount of time that has elapsed since this <code>Time</code> value.
     *
     * @return the amount of time that has elapsed since this <code>Time</code> value
     */
    public Duration elapsedSince()
    {
        final var now = now();
        if (isAfter(now))
        {
            return Duration.NONE;
        }
        return now.minus(this);
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof Time)
        {
            final var that = (Time) object;
            return milliseconds == that.milliseconds;
        }
        return false;
    }

    /**
     * Retrieves the <code>Duration</code> from now to this <code>Time</code> value. If this
     * <code>Time</code> value is in the past, then the <code>Duration</code> returned will be
     * negative. Otherwise, it will be the number of milliseconds from now to this <code>Time</code> .
     *
     * @return the <code>Duration</code> from now to this <code>Time</code> value
     */
    public Duration fromNow()
    {
        final var now = now();
        if (isAfter(now))
        {
            return minus(now);
        }
        return Duration.NONE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return Long.hashCode(milliseconds);
    }

    public boolean isAfter(final Time that)
    {
        return milliseconds > that.milliseconds;
    }

    public boolean isAtOrAfter(final Time that)
    {
        return milliseconds >= that.milliseconds;
    }

    public boolean isAtOrBefore(final Time that)
    {
        return milliseconds <= that.milliseconds;
    }

    public boolean isBefore(final Time that)
    {
        return milliseconds < that.milliseconds;
    }

    public boolean isNewerThan(final Duration duration)
    {
        return elapsedSince().isLessThan(duration);
    }

    public boolean isNewerThanOrEqual(final Duration duration)
    {
        return elapsedSince().isLessThanOrEqualTo(duration);
    }

    /**
     * True if this time is now older than the given duration.
     *
     * @param duration Amount of time to be considered old
     * @return True if the time that has elapsed since this time is greater than the given duration
     */
    public boolean isOlderThan(final Duration duration)
    {
        return elapsedSince().isGreaterThan(duration);
    }

    public boolean isOlderThanOrEqual(final Duration duration)
    {
        return elapsedSince().isGreaterThanOrEqualTo(duration);
    }

    /**
     * @return The amount of time left until the given amount of time has elapsed
     */
    public Duration leftUntil(final Duration elapsed)
    {
        return elapsed.minus(elapsedSince());
    }

    public LocalTime localTime()
    {
        return new LocalTime(LocalTime.localTimeZone(), this);
    }

    public LocalTime localTime(final String zone)
    {
        return new LocalTime(ZoneId.of(zone), this);
    }

    public LocalTime localTime(final ZoneId zone)
    {
        return new LocalTime(zone, this);
    }

    public Time maximum(final Time that)
    {
        return isAfter(that) ? this : that;
    }

    public Time minimum(final Time that)
    {
        return isBefore(that) ? this : that;
    }

    /**
     * Subtracts the given <code>Duration</code> from this <code>Time</code> object, moving the time into the past.
     *
     * @param duration the <code>Duration</code> to subtract
     * @return this duration of time
     */
    public Time minus(final Duration duration)
    {
        return milliseconds(milliseconds - duration.asMilliseconds());
    }

    /**
     * Subtract time from this and returns the difference as a <code>Duration</code> object.
     *
     * @param that the time to subtract
     * @return the <code>Duration</code> between this and that time
     */
    public Duration minus(final Time that)
    {
        return Duration.milliseconds(milliseconds - that.milliseconds);
    }

    public Time nearest(final Duration unit)
    {
        return plus(unit.divide(2)).roundDown(unit);
    }

    /**
     * Adds the given <code>Duration</code> to this <code>Time</code> object, moving the time into the future.
     *
     * @param duration the <code>Duration</code> to add
     * @return this <code>Time</code> + <code>Duration</code>
     */
    public Time plus(final Duration duration)
    {
        return milliseconds(milliseconds + duration.asMilliseconds());
    }

    @Override
    public long quantum()
    {
        return milliseconds;
    }

    public Time roundDown(final Duration unit)
    {
        return milliseconds(milliseconds * unit.asMilliseconds() / unit.asMilliseconds());
    }

    public Time roundUp(final Duration unit)
    {
        return roundDown(unit).plus(unit);
    }

    @Override
    public String toString()
    {
        return localTime().toString();
    }

    public LocalTime utc()
    {
        return localTime().utc();
    }
}