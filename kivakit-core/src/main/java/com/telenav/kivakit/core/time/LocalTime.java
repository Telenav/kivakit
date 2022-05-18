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
import com.telenav.kivakit.interfaces.time.Nanoseconds;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.time.DayOfWeek.javaDayOfWeek;
import static com.telenav.kivakit.core.time.Hour.militaryHour;
import static com.telenav.kivakit.core.time.Second.second;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.DAY_OF_YEAR;
import static java.time.temporal.ChronoField.EPOCH_DAY;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;

/**
 * Snapshot of local time at a specific timezone
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramTime.class)
public class LocalTime extends Time
{
    public static LocalTime localTime(ZoneId zone, LocalDateTime dateTime)
    {
        return milliseconds(zone, dateTime.atZone(zone).toInstant().toEpochMilli());
    }

    public static LocalTime localTime(ZoneId zone, BaseTime<?> time)
    {
        return nanoseconds(zone, time.nanoseconds());
    }

    public static LocalTime localTime(ZoneId zone, Year year, Month month, Day dayOfMonth, Hour hour)
    {
        return localTime(zone, year, month, dayOfMonth, hour, Minute.minute(0), second(0));
    }

    public static LocalTime localTime(ZoneId zone, Year year, Month month, Day dayOfMonth)
    {
        return localTime(zone, year, month, dayOfMonth, militaryHour(0));
    }

    public static LocalTime localTime(ZoneId zone, Year year, Month month)
    {
        return localTime(zone, year, month, Day.dayOfMonth(1), militaryHour(0));
    }

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

    public static ZoneId localTimeZone()
    {
        return ZoneId.systemDefault();
    }

    public static LocalTime milliseconds(ZoneId zone, double milliseconds)
    {
        return nanoseconds(zone, Nanoseconds.milliseconds(milliseconds));
    }

    public static LocalTime milliseconds(ZoneId zone, long milliseconds)
    {
        return milliseconds(zone, (double) milliseconds);
    }

    public static LocalTime nanoseconds(ZoneId zone, Nanoseconds nanoseconds)
    {
        return new LocalTime(zone, nanoseconds);
    }

    public static LocalTime now()
    {
        return Time.now().asLocalTime();
    }

    public static LocalTime seconds(ZoneId zone, long seconds)
    {
        return milliseconds(zone, seconds * 1_000);
    }

    public static ZoneId utcTimeZone()
    {
        return ZoneId.of("UTC");
    }

    private ZoneId timeZone;

    protected LocalTime()
    {
    }

    protected LocalTime(ZoneId zone, Nanoseconds nanoseconds)
    {
        super(nanoseconds);

        timeZone = zone;
    }

    /**
     * Constructor that takes a number of milliseconds since January 1st 1970, and a TimeZone to represent a snapshot of
     * local time at a specific timezone.
     */
    protected LocalTime(ZoneId zone, Time time)
    {
        this(zone, time.nanoseconds());
    }

    public String asDateString()
    {
        return asDateString(timeZone());
    }

    public String asDateString(ZoneId zone)
    {
        return TimeFormats.KIVAKIT_DATE.format(asJavaInstant()) + "_" + TimeZones.shortDisplayName(zone);
    }

    public String asDateTimeString()
    {
        return asDateTimeString(timeZone());
    }

    public String asDateTimeString(ZoneId zone)
    {
        return TimeFormats.KIVAKIT_DATE_TIME
                .withZone(zone)
                .format(asJavaInstant()) + "_" + TimeZones.shortDisplayName(zone);
    }

    public String asTimeString()
    {
        return asTimeString(timeZone());
    }

    public String asTimeString(ZoneId zone)
    {
        return TimeFormats.KIVAKIT_TIME.format(asJavaInstant()) + "_" + TimeZones.shortDisplayName(zone);
    }

    @Override
    public Time asUtc()
    {
        if (TimeZones.isUtc(timeZone))
        {
            return this;
        }
        return inTimeZone(utcTimeZone());
    }

    public long asZonedMilliseconds()
    {
        return javaLocalDateTime().atZone(timeZone()).toInstant().toEpochMilli();
    }

    public Quarter calendarQuarter()
    {
        return month().calendarQuarter();
    }

    /**
     * @return The day of year from 0-365 (or 366 in leap years)
     */
    public Day dayOfMonth()
    {
        return Day.dayOfMonth(javaLocalDateTime().getDayOfMonth());
    }

    public Day dayOfUnixEpoch()
    {
        return Day.dayOfUnixEpoch((int) javaLocalDateTime().getLong(EPOCH_DAY));
    }

    /**
     * @return The day of week from 0-6
     */
    public DayOfWeek dayOfWeek()
    {
        return javaDayOfWeek(javaLocalDateTime().getDayOfWeek().getValue());
    }

    /**
     * @return The day of year from 0-365 (or 366 in leap years)
     */
    public Day dayOfYear()
    {
        return Day.dayOfYear(javaLocalDateTime().getDayOfYear());
    }

    @Override
    public Duration elapsedSince(Time thatTime)
    {
        var that = inLocalZone(thatTime);
        return Duration.milliseconds(asMilliseconds() - that.asMilliseconds());
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof Time)
        {
            var utc = asUtc();
            var thatUtc = ((Time) object).asUtc();
            return utc.nanoseconds().equals(thatUtc.nanoseconds());
        }
        return false;
    }

    public Quarter fiscalQuarter()
    {
        return month().fiscalQuarter();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(asUtc().nanoseconds());
    }

    /**
     * @return The hour of day from 0 to 23
     */
    public Hour hourOfDay()
    {
        return militaryHour(javaLocalDateTime().get(HOUR_OF_DAY));
    }

    public HourOfWeek hourOfWeek()
    {
        return HourOfWeek.hourOfWeek(dayOfWeek().asIsoOrdinal() * 24 + hourOfDay().asMilitaryHour());
    }

    @Override
    public boolean isLocal()
    {
        return true;
    }

    public LocalDateTime javaLocalDateTime()
    {
        return LocalDateTime.ofInstant(asJavaInstant(), timeZone);
    }

    public java.time.LocalTime javaLocalTime()
    {
        return javaLocalDateTime().toLocalTime();
    }

    public ZonedDateTime javaZonedDate()
    {
        return ZonedDateTime.ofInstant(asJavaInstant(), timeZone);
    }

    public ZonedDateTime javaZonedDateTime(ZoneOffset offset)
    {
        return ZonedDateTime.ofInstant(asJavaInstant(), ZoneId.ofOffset("", offset));
    }

    public Meridiem meridiem()
    {
        return hourOfDay().meridiem();
    }

    @Override
    public LocalTime minus(Duration duration)
    {
        return localTime(timeZone(), super.minus(duration));
    }

    /**
     * @return The minute from 0-59
     */
    public Minute minute()
    {
        return minuteOfHour();
    }

    /**
     * @return The minute of the day from 0-1439
     */
    public int minuteOfDay()
    {
        return javaLocalDateTime().get(MINUTE_OF_DAY);
    }

    public Minute minuteOfHour()
    {
        return Minute.minute(javaLocalDateTime().get(MINUTE_OF_HOUR));
    }

    public Month month()
    {
        return Month.monthOfYear(javaLocalDateTime().getMonthValue());
    }

    @Override
    public LocalTime plus(Duration duration)
    {
        return localTime(timeZone(), super.plus(duration));
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
        return localTime(timeZone(), Time.now()).startOfHour().plus(Duration.ONE_HOUR);
    }

    public LocalTime startOfTomorrow()
    {
        return startOfDay().plus(Duration.ONE_DAY);
    }

    @Override
    public ZoneId timeZone()
    {
        return timeZone;
    }

    @Override
    public String toString()
    {
        return year()
                + "." + String.format("%02d", month().monthOfYear())
                + "." + String.format("%02d", dayOfMonth().asUnits())
                + "_" + String.format("%02d", hourOfDay().asMeridiemHour())
                + "." + String.format("%02d", minute().asUnits())
                + meridiem()
                + "_" + TimeZones.shortDisplayName(timeZone);
    }

    @Override
    public LocalTime utc()
    {
        return nanoseconds(ZoneId.of("UTC"), nanoseconds());
    }

    /**
     * @return The week of year in 0-51-52 format. NOTE: Java week of year starts at 1.
     */
    public int weekOfYear()
    {
        return javaLocalDateTime().getDayOfYear() / 7;
    }

    /**
     * @return This local time on the given day
     */
    public LocalTime withDay(Day day)
    {
        switch (day.type())
        {
            case DAY_OF_WEEK:
                return withDayOfWeek(day.asDayOfWeek());

            case DAY_OF_MONTH:
                return localTime(timeZone(), javaLocalDateTime().with(DAY_OF_MONTH, day.asUnits()));

            case DAY_OF_YEAR:
                return localTime(timeZone(), javaLocalDateTime().with(DAY_OF_YEAR, day.asUnits()));

            case DAY_OF_UNIX_EPOCH:
                return localTime(timeZone(), javaLocalDateTime().with(EPOCH_DAY, day.asUnits()));

            case DAY:
            default:
                return unsupported();
        }
    }

    public LocalTime withDayOfWeek(DayOfWeek day)
    {
        return localTime(timeZone(), javaLocalDateTime().with(DAY_OF_WEEK, day.asJavaOrdinal()));
    }

    public LocalTime withHourOfDay(Hour hour)
    {
        return localTime(timeZone(), javaLocalDateTime().with(HOUR_OF_DAY, hour.asMilitaryHour()));
    }

    public LocalTime withMinute(Minute minute)
    {
        return localTime(timeZone(), javaLocalDateTime().with(MINUTE_OF_HOUR, (long) minute.asMinutes()));
    }

    public LocalTime withUnixEpochDay(Day day)
    {
        return localTime(timeZone(), javaLocalDateTime().with(EPOCH_DAY, day.asUnits()));
    }

    public Year year()
    {
        return Year.year(javaLocalDateTime().getYear());
    }

    private LocalTime inLocalZone(Time that)
    {
        return that.isLocal()
                ? (LocalTime) that
                : that.inTimeZone(timeZone());
    }
}
