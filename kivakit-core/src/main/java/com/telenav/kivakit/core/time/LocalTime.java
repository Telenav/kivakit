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
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.interfaces.time.Nanoseconds;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.time.DayOfWeek.javaDayOfWeek;
import static com.telenav.kivakit.core.time.Hour.militaryHour;
import static com.telenav.kivakit.core.time.KivaKitTimeFormats.KIVAKIT_DATE;
import static com.telenav.kivakit.core.time.KivaKitTimeFormats.KIVAKIT_DATE_TIME;
import static com.telenav.kivakit.core.time.KivaKitTimeFormats.KIVAKIT_TIME;
import static com.telenav.kivakit.core.time.Second.second;
import static com.telenav.kivakit.core.time.TimeZones.shortDisplayName;
import static java.lang.String.format;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.DAY_OF_YEAR;
import static java.time.temporal.ChronoField.EPOCH_DAY;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.util.Objects.hash;

/**
 * The time in a specific, local timezone.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #localTime(ZoneId, LocalDateTime)}</li>
 *     <li>{@link #localTime(ZoneId, BaseTime)}</li>
 *     <li>{@link #localTime(ZoneId, Year, Month, Day, Hour)}</li>
 *     <li>{@link #localTime(ZoneId, Year, Month, Day)}</li>
 *     <li>{@link #localTime(ZoneId, Year, Month)}</li>
 *     <li>{@link #localTime(ZoneId, Year, Month, Day, Hour, Minute, Second)}</li>
 *     <li>{@link #now()}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #calendarQuarter()}</li>
 *     <li>{@link #dayOfMonth()}</li>
 *     <li>{@link #dayOfUnixEpoch()}</li>
 *     <li>{@link #dayOfWeek()}</li>
 *     <li>{@link #dayOfYear()}</li>
 *     <li>{@link #fiscalQuarter()}</li>
 *     <li>{@link #hourOfDay()}</li>
 *     <li>{@link #hourOfWeek()}</li>
 *     <li>{@link #isLocal()}</li>
 *     <li>{@link #meridiem()}</li>
 *     <li>{@link #minute()}</li>
 *     <li>{@link #minuteOfDay()}</li>
 *     <li>{@link #minuteOfHour()}</li>
 *     <li>{@link #month()}</li>
 *     <li>{@link #weekOfYear()}</li>
 *     <li>{@link #year()}</li>
 * </ul>
 *
 * <p><b>Functional</b></p>
 *
 * <ul>
 *     <li>{@link #withDay(Day)}</li>
 *     <li>{@link #withDayOfWeek(DayOfWeek)}</li>
 *     <li>{@link #withHourOfDay(Hour)}</li>
 *     <li>{@link #withMinute(Minute)}</li>
 *     <li>{@link #withUnixEpochDay(Day)}</li>
 * </ul>
 *
 * <p><b>Computations</b></p>
 *
 * <ul>
 *     <li>{@link #decremented()}</li>
 *     <li>{@link #elapsedSince()}</li>
 *     <li>{@link #incremented()}</li>
 *     <li>{@link #minus(Duration)}</li>
 *     <li>{@link #minusUnits(double)}</li>
 *     <li>{@link #next()}</li>
 *     <li>{@link #plus(Duration)}</li>
 *     <li>{@link #plusUnits(double)}</li>
 *     <li>{@link #startOfDay()}</li>
 *     <li>{@link #startOfHour()}</li>
 *     <li>{@link #startOfNextHour()}</li>
 *     <li>{@link #startOfTomorrow()}</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #asDateString()}</li>
 *     <li>{@link #asDateString(ZoneId)}</li>
 *     <li>{@link #asDateTimeString()}</li>
 *     <li>{@link #asDateTimeString()}</li>
 *     <li>{@link #asJavaLocalTime()}</li>
 *     <li>{@link #asJavaZonedDate()}</li>
 *     <li>{@link #asJavaZonedDateTime(ZoneOffset)}</li>
 *     <li>{@link #asJavaLocalDateTime()}</li>
 *     <li>{@link #asTimeString()}</li>
 *     <li>{@link #asTimeString(ZoneId)}</li>
 *     <li>{@link #asUtc()}</li>
 *     <li>{@link #asZonedMilliseconds()}</li>
 * </ul>
 *
 * <p><b>Time Zone</b></p>
 *
 * <ul>
 *     <li>{@link #asDateString(ZoneId)}</li>
 *     <li>{@link #asDateTimeString()}</li>
 *     <li>{@link #asTimeString(ZoneId)}</li>
 *     <li>{@link #asUtc()}</li>
 *     <li>{@link #asZonedMilliseconds()}</li>
 *     <li>{@link #asJavaZonedDate()}</li>
 *     <li>{@link #asJavaZonedDateTime(ZoneOffset)}</li>
 *     <li>{@link #localTimeZone()}</li>
 *     <li>{@link #timeZone()}</li>
 *     <li>{@link #utcTimeZone()}</li>
 * </ul>
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramTime.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class LocalTime extends Time
{
    /**
     * Creates a local time object for the given timezone and Java {@link LocalDateTime}
     *
     * @param zone The time zone
     * @param dateTime The date and time
     * @return The local time object
     */
    public static LocalTime localTime(ZoneId zone, LocalDateTime dateTime)
    {
        return milliseconds(zone, dateTime.atZone(zone).toInstant().toEpochMilli());
    }

    /**
     * Creates a local time object for the given timezone and Java {@link ZonedDateTime}
     *
     * @param dateTime The date and time
     * @return The local time object
     */
    public static LocalTime localTime(ZonedDateTime dateTime)
    {
        return milliseconds(dateTime.getZone(), dateTime.toInstant().toEpochMilli());
    }

    public static Time localTime(String text)
    {
        return parseLocalTime(throwingListener(), text);
    }

    public static Time localTime(String text, ZoneId zone)
    {
        return parseLocalTime(throwingListener(), text, zone);
    }

    /**
     * Creates a local time object for the given timezone and KivaKit time object
     *
     * @param zone The time zone
     * @param time The time object
     * @return The local time object
     */
    public static LocalTime localTime(ZoneId zone, BaseTime<?> time)
    {
        return nanoseconds(zone, time.nanoseconds());
    }

    /**
     * Creates a local time object for the given time zone, year, month, day of month, and hour
     *
     * @param zone The time zone
     * @param year The year
     * @param month The month
     * @param dayOfMonth The day of the month
     * @param hour The hour of the day
     * @return The local time object
     */
    public static LocalTime localTime(ZoneId zone, Year year, Month month, Day dayOfMonth, Hour hour)
    {
        return localTime(zone, year, month, dayOfMonth, hour, Minute.minute(0), second(0));
    }

    /**
     * Creates a local time object for the given time zone, year, month and day of month
     *
     * @param zone The time zone
     * @param year The year
     * @param month The month
     * @param dayOfMonth The day of the month
     * @return The local time object
     */
    public static LocalTime localTime(ZoneId zone, Year year, Month month, Day dayOfMonth)
    {
        return localTime(zone, year, month, dayOfMonth, militaryHour(0));
    }

    /**
     * Creates a local time object for the given time zone, year and month
     *
     * @param zone The time zone
     * @param year The year
     * @param month The month
     * @return The local time object
     */
    public static LocalTime localTime(ZoneId zone, Year year, Month month)
    {
        return localTime(zone, year, month, Day.dayOfMonth(1), militaryHour(0));
    }

    /**
     * Creates a local time object for the given time zone, year, month, day of month, hour, minute, and second
     *
     * @param zone The time zone
     * @param year The year
     * @param month The month
     * @param dayOfMonth The day of the month
     * @param hour The hour of the day
     * @param minute The minute of the hour
     * @param second The second of the minute
     * @return The local time object
     */
    public static LocalTime localTime(ZoneId zone,
                                      Year year,
                                      Month month,
                                      Day dayOfMonth,
                                      Hour hour,
                                      Minute minute,
                                      Second second)
    {
        return localTime(zone, LocalDateTime.of(
            year.asUnits(),
            month.monthOfYear(),
            dayOfMonth.asUnits(),
            hour.asMilitaryHour(),
            minute.asUnits(),
            second.asUnits()));
    }

    /**
     * Returns the local time zone
     */
    public static ZoneId localTimeZone()
    {
        return ZoneId.systemDefault();
    }

    /**
     * Creates a local time object for the given time zone and epoch milliseconds
     *
     * @param zone The time zone
     * @param epochMilliseconds The number of milliseconds since the start of the UNIX epoch
     * @return The local time object
     */
    public static LocalTime milliseconds(ZoneId zone, double epochMilliseconds)
    {
        return nanoseconds(zone, Nanoseconds.milliseconds(epochMilliseconds));
    }

    /**
     * Creates a local time object for the given time zone and epoch milliseconds
     *
     * @param zone The time zone
     * @param epochMilliseconds The number of milliseconds since the start of the UNIX epoch
     * @return The local time object
     */
    public static LocalTime milliseconds(ZoneId zone, long epochMilliseconds)
    {
        return milliseconds(zone, (double) epochMilliseconds);
    }

    /**
     * Creates a local time object for the given time zone and epoch nanoseconds
     *
     * @param zone The time zone
     * @param epochNanoseconds The number of nanoseconds since the start of the UNIX epoch
     * @return The local time object
     */
    public static LocalTime nanoseconds(ZoneId zone, Nanoseconds epochNanoseconds)
    {
        return new LocalTime(zone, epochNanoseconds);
    }

    /**
     * Returns the current time in the local time zone
     */
    public static LocalTime now()
    {
        return Time.now().asLocalTime();
    }

    public static LocalTime parseLocalTime(Listener listener, String text, ZoneId zone)
    {
        var time = KIVAKIT_DATE_TIME
            .withZone(zone)
            .parse(text, LocalDateTime::from);

        return LocalTime.localTime(zone, time);
    }

    public static Time parseLocalTime(Listener listener, String text)
    {
        return parseLocalTime(listener, text, ZoneId.systemDefault());
    }

    /**
     * Creates a local time object for the given time zone and epoch seconds
     *
     * @param zone The time zone
     * @param epochSeconds The number of seconds since the start of the UNIX epoch
     * @return The local time object
     */
    public static LocalTime seconds(ZoneId zone, long epochSeconds)
    {
        return milliseconds(zone, epochSeconds * 1_000);
    }

    /**
     * Returns the UTC time zone
     */
    public static ZoneId utcTimeZone()
    {
        return ZoneId.of("UTC");
    }

    /** The time zone for this {@link LocalTime} object */
    private ZoneId timeZone;

    protected LocalTime()
    {
    }

    /**
     * @param zone The time zone
     * @param epochNanoseconds The nanoseconds since the start of the UNIX epoch
     */
    protected LocalTime(ZoneId zone, Nanoseconds epochNanoseconds)
    {
        super(epochNanoseconds);

        timeZone = zone;
    }

    /**
     * @param zone The time zone
     * @param time The KivaKit time object
     */
    protected LocalTime(ZoneId zone, Time time)
    {
        this(zone, time.nanoseconds());
    }

    /**
     * Returns a string representation of this time's date in the local time zone
     */
    public String asDateString()
    {
        return asDateString(timeZone());
    }

    /**
     * Returns a string representation of this time's date in the given time zone
     */
    public String asDateString(ZoneId zone)
    {
        return KIVAKIT_DATE
            .withZone(zone)
            .format(asJavaInstant());
    }

    /**
     * Returns a string representation of this date and time in the local time zone
     */
    public String asDateTimeString()
    {
        return asDateTimeString(timeZone());
    }

    /**
     * Returns a string representation of this date and time in the given time zone
     */
    public String asDateTimeString(ZoneId zone)
    {
        return KIVAKIT_DATE_TIME
            .withZone(zone)
            .format(asJavaInstant()) + "_" + shortDisplayName(zone);
    }

    /**
     * Returns this local time as a Java {@link LocalDateTime}
     */
    public LocalDateTime asJavaLocalDateTime()
    {
        return LocalDateTime.ofInstant(asJavaInstant(), timeZone);
    }

    /**
     * Returns this local time as a Java {@link LocalTime}
     */
    public java.time.LocalTime asJavaLocalTime()
    {
        return asJavaLocalDateTime().toLocalTime();
    }

    /**
     * Returns this local time as a Java {@link ZonedDateTime}
     */
    public ZonedDateTime asJavaZonedDate()
    {
        return ZonedDateTime.ofInstant(asJavaInstant(), timeZone);
    }

    /**
     * Returns this local time as a Java {@link ZonedDateTime} using the given zone offset
     */
    public ZonedDateTime asJavaZonedDateTime(ZoneOffset offset)
    {
        return ZonedDateTime.ofInstant(asJavaInstant(), ZoneId.ofOffset("", offset));
    }

    /**
     * Returns a string representation of this time in the local time zone
     */
    public String asTimeString()
    {
        return asTimeString(timeZone());
    }

    /**
     * Returns a string representation of this time in the given time zone
     */
    public String asTimeString(ZoneId zone)
    {
        return KIVAKIT_TIME
            .withZone(zone)
            .format(asJavaInstant());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Time asUtc()
    {
        if (TimeZones.isUtc(timeZone))
        {
            return this;
        }
        return inTimeZone(utcTimeZone());
    }

    /**
     * Returns the epoch milliseconds for this time in the local timezone
     */
    public long asZonedMilliseconds()
    {
        return asJavaLocalDateTime().atZone(timeZone()).toInstant().toEpochMilli();
    }

    /**
     * Returns the calendar quarter for this local time
     */
    public Quarter calendarQuarter()
    {
        return month().calendarQuarter();
    }

    /**
     * Returns the day of year from 0-365 (or 366 in leap years)
     */
    public Day dayOfMonth()
    {
        return Day.dayOfMonth(asJavaLocalDateTime().getDayOfMonth());
    }

    /**
     * Returns the day since the start of the UNIX epoch
     */
    public Day dayOfUnixEpoch()
    {
        return Day.dayOfUnixEpoch((int) asJavaLocalDateTime().getLong(EPOCH_DAY));
    }

    /**
     * Returns the day of week from 0-6
     */
    public DayOfWeek dayOfWeek()
    {
        return javaDayOfWeek(asJavaLocalDateTime().getDayOfWeek().getValue());
    }

    /**
     * Returns the day of year from 0-365 (or 366 in leap years)
     */
    public Day dayOfYear()
    {
        return Day.dayOfYear(asJavaLocalDateTime().getDayOfYear());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Duration elapsedSince(Time thatTime)
    {
        var that = inLocalZone(thatTime);
        return Duration.milliseconds(asMilliseconds() - that.asMilliseconds());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Time)
        {
            var utc = asUtc();
            var thatUtc = ((Time) object).asUtc();
            return utc.nanoseconds().equals(thatUtc.nanoseconds());
        }
        return false;
    }

    /**
     * Returns the fiscal quarter for this local time
     */
    public Quarter fiscalQuarter()
    {
        return month().fiscalQuarter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return hash(asUtc().nanoseconds());
    }

    /**
     * Returns the hour of day from 0 to 23
     */
    public Hour hourOfDay()
    {
        return militaryHour(asJavaLocalDateTime().get(HOUR_OF_DAY));
    }

    public HourOfWeek hourOfWeek()
    {
        return HourOfWeek.hourOfWeek(dayOfWeek().asIsoOrdinal() * 24 + hourOfDay().asMilitaryHour());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLocal()
    {
        return true;
    }

    /**
     * Returns the meridiem (am or pm) of this local time
     */
    @SuppressWarnings("SpellCheckingInspection")
    public Meridiem meridiem()
    {
        return hourOfDay().meridiem();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalTime minus(Duration duration)
    {
        return localTime(timeZone(), super.minus(duration));
    }

    /**
     * Returns the minute from 0-59
     */
    public Minute minute()
    {
        return minuteOfHour();
    }

    /**
     * Returns the minute of the day from 0-1439
     */
    public int minuteOfDay()
    {
        return asJavaLocalDateTime().get(MINUTE_OF_DAY);
    }

    /**
     * Returns the minute of the house for this local time
     */
    public Minute minuteOfHour()
    {
        return Minute.minute(asJavaLocalDateTime().get(MINUTE_OF_HOUR));
    }

    /**
     * Returns the month for this local time
     */
    public Month month()
    {
        return Month.monthOfYear(asJavaLocalDateTime().getMonthValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalTime plus(Duration duration)
    {
        return localTime(timeZone(), super.plus(duration));
    }

    /**
     * Returns the start of the day (midnight) in local time
     */
    public LocalTime startOfDay()
    {
        return seconds(timeZone(), asJavaZonedDate()
            .withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
            .toEpochSecond());
    }

    /**
     * Returns the start of the hour in local time
     */
    public LocalTime startOfHour()
    {
        return seconds(timeZone(), asJavaZonedDate()
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
            .toEpochSecond());
    }

    /**
     * Returns the start of the next hour in local time
     */
    public LocalTime startOfNextHour()
    {
        return localTime(timeZone(), Time.now()).startOfHour().plus(Duration.ONE_HOUR);
    }

    /**
     * Returns the start of tomorrow in local time
     */
    public LocalTime startOfTomorrow()
    {
        return startOfDay().plus(Duration.ONE_DAY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ZoneId timeZone()
    {
        return timeZone;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return year()
            + "." + format("%02d", month().monthOfYear())
            + "." + format("%02d", dayOfMonth().asUnits())
            + "_" + format("%02d", hourOfDay().asMeridiemHour())
            + "." + format("%02d", minute().asUnits())
            + meridiem()
            + "_" + shortDisplayName(timeZone);
    }

    /**
     * Returns the week of year in 0-51-52 format. NOTE: Java week of year starts at 1.
     */
    public int weekOfYear()
    {
        return asJavaLocalDateTime().getDayOfYear() / 7;
    }

    /**
     * Returns this local time on the given day
     */
    public LocalTime withDay(Day day)
    {
        return switch (day.type())
            {
                case DAY_OF_WEEK -> withDayOfWeek(day.asDayOfWeek());
                case DAY_OF_MONTH -> localTime(timeZone(), asJavaLocalDateTime().with(DAY_OF_MONTH, day.asUnits()));
                case DAY_OF_YEAR -> localTime(timeZone(), asJavaLocalDateTime().with(DAY_OF_YEAR, day.asUnits()));
                case DAY_OF_UNIX_EPOCH -> localTime(timeZone(), asJavaLocalDateTime().with(EPOCH_DAY, day.asUnits()));
                default -> unsupported();
            };
    }

    /**
     * Returns a new instance of this local time with the given day of the week
     */
    public LocalTime withDayOfWeek(DayOfWeek day)
    {
        return localTime(timeZone(), asJavaLocalDateTime().with(DAY_OF_WEEK, day.asJavaOrdinal()));
    }

    /**
     * Returns a new instance of this local time with the given hour of the day
     */
    public LocalTime withHourOfDay(Hour hour)
    {
        return localTime(timeZone(), asJavaLocalDateTime().with(HOUR_OF_DAY, hour.asMilitaryHour()));
    }

    /**
     * Returns a new instance of this local time with the given minute
     */
    public LocalTime withMinute(Minute minute)
    {
        return localTime(timeZone(), asJavaLocalDateTime().with(MINUTE_OF_HOUR, (long) minute.asMinutes()));
    }

    /**
     * Returns a new instance of this local time with the given day of the epoch
     */
    public LocalTime withUnixEpochDay(Day epochDay)
    {
        return localTime(timeZone(), asJavaLocalDateTime().with(EPOCH_DAY, epochDay.asUnits()));
    }

    /**
     * Returns the year for this local time
     */
    public Year year()
    {
        return Year.year(asJavaLocalDateTime().getYear());
    }

    private LocalTime inLocalZone(Time that)
    {
        return that.isLocal()
            ? (LocalTime) that
            : that.inTimeZone(timeZone());
    }
}
