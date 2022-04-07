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
import com.telenav.kivakit.interfaces.numeric.Quantizable;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.core.time.Hour.militaryHour;
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
public class Time extends BaseTime<Time> implements Quantizable
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
        return milliseconds(LocalTime.localTime(utcTimeZone(), year, month, dayOfMonth, hour, minute, second).asMilliseconds());
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
    public Time newInstance(long count)
    {
        return milliseconds(count);
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
