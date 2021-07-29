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

import com.telenav.kivakit.kernel.language.objects.Hash;
import com.telenav.kivakit.kernel.language.time.conversion.converters.HumanizedLocalDateTimeConverter;
import com.telenav.kivakit.kernel.language.time.conversion.converters.LocalDateConverter;
import com.telenav.kivakit.kernel.language.time.conversion.converters.LocalDateTimeConverter;
import com.telenav.kivakit.kernel.language.time.conversion.converters.LocalTimeConverter;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageTime;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static java.time.temporal.ChronoField.AMPM_OF_DAY;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.DAY_OF_YEAR;
import static java.time.temporal.ChronoField.EPOCH_DAY;
import static java.time.temporal.ChronoField.HOUR_OF_AMPM;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;

/**
 * Snapshot of local time at a specific timezone
 */
@UmlClassDiagram(diagram = DiagramLanguageTime.class)
public class LocalTime extends Time
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    public static LocalTime from(final ZoneId zone, final LocalDateTime dateTime)
    {
        final var milliseconds = dateTime.atZone(zone).toInstant().toEpochMilli();
        return of(zone, milliseconds(milliseconds));
    }

    public static ZoneId localTimeZone()
    {
        return ZoneId.systemDefault();
    }

    public static LocalTime milliseconds(final ZoneId zone, final long milliseconds)
    {
        return new LocalTime(zone, milliseconds);
    }

    public static LocalTime now()
    {
        return Time.now().localTime();
    }

    public static LocalTime of(final ZoneId zone, final LocalDateTime time)
    {
        final var zoned = time.atZone(zone);
        return milliseconds(zone, zoned.toInstant().toEpochMilli());
    }

    public static LocalTime of(final ZoneId zone, final Time time)
    {
        return milliseconds(zone, time.asMilliseconds());
    }

    public static LocalTime of(final ZoneId zone, final int year, final int month, final int dayOfMonth,
                               final int hour, final int minute, final int second, final Meridiem ampm)
    {
        return of(zone, LocalDateTime.of(year, month, dayOfMonth,
                ampm == Meridiem.AM ? hour : hour + 12, minute, second));
    }

    public static LocalTime parseDateTime(final String text)
    {
        return new LocalDateTimeConverter(Listener.none(), localTimeZone()).convert(text);
    }

    public static LocalTime seconds(final ZoneId zone, final long seconds)
    {
        return new LocalTime(zone, seconds * 1_000);
    }

    public static ZoneId utcTimeZone()
    {
        return ZoneId.of("UTC");
    }

    private ZoneId timeZone;

    protected LocalTime()
    {
    }

    protected LocalTime(final ZoneId zone, final long milliseconds)
    {
        super(milliseconds);
        timeZone = zone;
    }

    /**
     * Constructor that takes a number of milliseconds since January 1st 1970, and a TimeZone to represent a snapshot of
     * local time at a specific timezone.
     */
    protected LocalTime(final ZoneId zone, final Time time)
    {
        this(zone, time.asMilliseconds());
    }

    public String asDateString()
    {
        return asDateString(timeZone());
    }

    public String asDateString(final ZoneId zone)
    {
        return new LocalDateConverter(Listener.none(), zone).unconvert(this);
    }

    public String asDateTimeString()
    {
        return asDateTimeString(timeZone());
    }

    public String asDateTimeString(final ZoneId zone)
    {
        return new LocalDateTimeConverter(Listener.none(), zone).unconvert(this);
    }

    public String asTimeString()
    {
        return asTimeString(timeZone());
    }

    public String asTimeString(final ZoneId zone)
    {
        return new LocalTimeConverter(Listener.none(), zone).unconvert(this);
    }

    public long asZonedMilliseconds()
    {
        return javaLocalDateTime().atZone(timeZone()).toInstant().toEpochMilli();
    }

    /**
     * @return The day of year from 0-365 (or 366 in leap years)
     */
    public int day()
    {
        return javaLocalDateTime().getDayOfMonth();
    }

    /**
     * @return The day of week from 0-6
     */
    public DayOfWeek dayOfWeek()
    {
        return DayOfWeek.forJavaDayOfWeek(javaLocalDateTime().getDayOfWeek());
    }

    /**
     * @return The day of year from 0-365 (or 366 in leap years)
     */
    public int dayOfYear()
    {
        return javaLocalDateTime().getDayOfYear();
    }

    public int epochDay()
    {
        return (int) javaLocalDateTime().getLong(EPOCH_DAY);
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof LocalTime)
        {
            final var that = (LocalTime) object;
            return asZonedMilliseconds() == that.asZonedMilliseconds();
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return Hash.many(asZonedMilliseconds());
    }

    /**
     * @return The hour of day from 1 to 12
     */
    public int hour()
    {
        return javaLocalDateTime().get(HOUR_OF_AMPM);
    }

    /**
     * @return The hour of day from 1 to 24
     */
    public int hourOfDay()
    {
        return javaLocalDateTime().get(HOUR_OF_DAY);
    }

    public String humanizedDateTime()
    {
        return new HumanizedLocalDateTimeConverter(LOGGER).unconvert(this);
    }

    public LocalDateTime javaLocalDateTime()
    {
        return LocalDateTime.ofInstant(asInstant(), timeZone);
    }

    public java.time.LocalTime javaLocalTime()
    {
        return javaLocalDateTime().toLocalTime();
    }

    public ZonedDateTime javaZonedDate()
    {
        return ZonedDateTime.ofInstant(asInstant(), timeZone);
    }

    public ZonedDateTime javaZonedDateTime(final ZoneOffset offset)
    {
        return ZonedDateTime.ofInstant(asInstant(), ZoneId.ofOffset("", offset));
    }

    public Meridiem meridiem()
    {
        return javaLocalDateTime().getHour() > 12 ? Meridiem.PM : Meridiem.AM;
    }

    /**
     * @return The hour of day from 0-12
     */
    public int meridiemHour()
    {
        final var hour = hour();
        return hour > 12 ? hour - 12 : hour;
    }

    @Override
    public LocalTime minus(final Duration duration)
    {
        return of(timeZone(), super.minus(duration));
    }

    @Override
    public Duration minus(final Time time)
    {
        final var localTime = milliseconds(timeZone(), super.minus(time).asMilliseconds());
        return Duration.milliseconds(localTime.asMilliseconds());
    }

    /**
     * @return The minute from 0-59
     */
    public int minute()
    {
        return javaLocalDateTime().getMinute();
    }

    /**
     * @return The minute of the day from 0-1439
     */
    public int minuteOfDay()
    {
        return javaLocalDateTime().get(MINUTE_OF_DAY);
    }

    public int minuteOfHour()
    {
        return javaLocalDateTime().get(MINUTE_OF_HOUR);
    }

    public int month()
    {
        return javaLocalDateTime().getMonthValue();
    }

    @Override
    public LocalTime plus(final Duration duration)
    {
        return of(timeZone(), super.plus(duration));
    }

    public int quarter()
    {
        return (month() - 1) / 3 + 1;
    }

    public LocalTime startOfDay()
    {
        return seconds(timeZone(), javaZonedDate()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .toEpochSecond());
    }

    public LocalTime startOfHour()
    {
        return seconds(timeZone(), javaZonedDate()
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .toEpochSecond());
    }

    public LocalTime startOfNextHour()
    {
        return of(timeZone(), Time.now()).startOfHour().plus(Duration.ONE_HOUR);
    }

    public LocalTime startOfTomorrow()
    {
        return startOfDay().plus(Duration.ONE_DAY);
    }

    public ZoneId timeZone()
    {
        return timeZone;
    }

    @Override
    public String toString()
    {
        return year()
                + "." + String.format("%02d", month())
                + "." + String.format("%02d", day())
                + "_" + String.format("%02d", hour())
                + "." + String.format("%02d", minute())
                + meridiem()
                + "_" + TimeZones.displayName(timeZone);
    }

    @Override
    public LocalTime utc()
    {
        return milliseconds(ZoneId.of("UTC"), asMilliseconds());
    }

    /**
     * @return The week of year in 0-51-52 format. NOTE: Java week of year starts at 1.
     */
    public int weekOfYear()
    {
        return javaLocalDateTime().getDayOfYear() / 7;
    }

    public LocalTime withDayOfWeek(final int day)
    {
        return from(timeZone(), javaLocalDateTime().with(DAY_OF_WEEK, day));
    }

    public LocalTime withDayOfYear(final int dayOfYear)
    {
        return from(timeZone(), javaLocalDateTime().with(DAY_OF_YEAR, dayOfYear));
    }

    public LocalTime withEpochDay(final int day)
    {
        return from(timeZone(), javaLocalDateTime().with(EPOCH_DAY, day));
    }

    public LocalTime withHourOfDay(final int hour)
    {
        return from(timeZone(), javaLocalDateTime().with(HOUR_OF_DAY, hour));
    }

    public LocalTime withHourOfMeridiem(final int hour, final Meridiem meridiem)
    {
        return from(timeZone(), javaLocalDateTime()
                .with(HOUR_OF_AMPM, hour)
                .with(AMPM_OF_DAY, meridiem.ordinal()));
    }

    public LocalTime withMinuteOfHour(final int minute)
    {
        return from(timeZone(), javaLocalDateTime().with(MINUTE_OF_HOUR, minute));
    }

    public int year()
    {
        return javaLocalDateTime().getYear();
    }
}
