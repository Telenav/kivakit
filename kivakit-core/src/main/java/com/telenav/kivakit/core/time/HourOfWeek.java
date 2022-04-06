package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.test.NoTestRequired;
import com.telenav.kivakit.core.test.Tested;
import com.telenav.kivakit.core.value.count.BaseCount;

import java.time.ZoneId;
import java.util.TimeZone;

import static com.telenav.kivakit.core.ensure.Ensure.ensureBetweenExclusive;
import static com.telenav.kivakit.core.time.DayOfWeek.isoDayOfWeek;

/**
 * Represents an hour of the week, for example Thursday at 1pm. This class stores its count value in the fields {@link
 * #dayOfWeek()} and {@link #hourOfDay()}, and it overrides {@link #asLong()} to provide a count value to the superclass
 * based on those two fields.
 *
 * @author jonathanl (shibo)
 */
public class HourOfWeek extends BaseCount<HourOfWeek>
{
    /**
     * Returns an {@link HourOfWeek} for the given ordinal value
     *
     * @param hourOfWeek The hour of the week from 0 to 7 * 24 (168), exclusive.
     */
    @Tested
    public static HourOfWeek hourOfWeek(int hourOfWeek)
    {
        ensureBetweenExclusive(hourOfWeek, 0, 24 * 7, "Hour of week $ is out of range", hourOfWeek);

        var dayOfWeek = hourOfWeek / 24;
        var hourOfDay = hourOfWeek % 24;

        return new HourOfWeek(isoDayOfWeek(dayOfWeek), Hour.militaryHour(hourOfDay));
    }

    /**
     * Returns an {@link HourOfWeek} for the given ordinal value
     *
     * @param hourOfWeek The hour of the week from 0 to 7 * 24 (168), exclusive.
     */
    @Tested
    public static HourOfWeek hourOfWeek(BaseCount<?> hourOfWeek)
    {
        return hourOfWeek(hourOfWeek.asInt());
    }

    /**
     * Returns an {@link HourOfWeek} for the given {@link DayOfWeek} and {@link Hour}.
     *
     * @param dayOfWeek The day of the week
     * @param hourOfDay The hour of the day
     * @return The hour of the week
     */
    @Tested
    public static HourOfWeek hourOfWeek(DayOfWeek dayOfWeek, Hour hourOfDay)
    {
        return hourOfWeek((dayOfWeek.asIso() * 24 + hourOfDay.asInt()));
    }

    /** The day of the week used to compute the hour of the week */
    private DayOfWeek dayOfWeek;

    /** The hour of the day used to compute the hour of the week */
    private Hour hourOfDay;

    @NoTestRequired
    protected HourOfWeek()
    {
    }

    @NoTestRequired
    protected HourOfWeek(DayOfWeek dayOfWeek, Hour hourOfDay)
    {
        // This value is not used because
        super(0);

        this.dayOfWeek = dayOfWeek;
        this.hourOfDay = hourOfDay;
    }

    /**
     * Converts this hour of the week, assumed to be in UTC, to the given time zone.
     *
     * @param zone The time zone to offset by
     * @return The hour of the week in the given time zone
     */
    @Tested
    public HourOfWeek asLocalTime(ZoneId zone)
    {
        return offset(zone, false);
    }

    @Override
    @Tested
    public long asLong()
    {
        return dayOfWeek.asIso() * 24L + hourOfDay.asInt();
    }

    /**
     * Converts this hour of the week, assumed to be in localtime, to UTC.
     *
     * @param zone The time zone for this hour of the week
     * @return The hour of the week in UTC time
     */
    @Tested
    public HourOfWeek asUtc(ZoneId zone)
    {
        return offset(zone, true);
    }

    /**
     * @return The day of the week
     */
    @Tested
    public DayOfWeek dayOfWeek()
    {
        return dayOfWeek;
    }

    /**
     * @return The hour of the day
     */
    @Tested
    public Hour hourOfDay()
    {
        return hourOfDay;
    }

    @Override
    @Tested
    public HourOfWeek maximumInclusive()
    {
        return hourOfWeek(7 * 24 - 1);
    }

    @Override
    @Tested
    public HourOfWeek minus(long count)
    {
        return wrappedOffset(-count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NoTestRequired
    public HourOfWeek newInstance(long count)
    {
        return hourOfWeek((int) count);
    }

    @Override
    @Tested
    public HourOfWeek plus(long count)
    {
        return wrappedOffset(count);
    }

    @Override
    @NoTestRequired
    public String toString()
    {
        return dayOfWeek() + " at " + hourOfDay();
    }

    /**
     * Computes the hour of the week in the given timezone.
     *
     * @param zone The time zone to use to offset this hour of the week
     * @return The hour of the week offset by the daylight-savings adjusted offset in hours
     */
    private HourOfWeek offset(ZoneId zone, boolean subtract)
    {
        var offset = subtract
                ? -offsetInHours(zone)
                : offsetInHours(zone);

        return hourOfWeek(wrappedOffset(offset));
    }

    /**
     * @param zone The time zone identifier
     * @return The offset in hours of the identified time zone, using the present moment to determine any daylight
     * savings adjustment.
     */
    private int offsetInHours(final ZoneId zone)
    {
        // Get the TimeZone object for the specified zone id,
        var timeZone = TimeZone.getTimeZone(zone);

        // use that to get the time zone offset in milliseconds (using the DST adjustment for the present moment),
        var offsetInMilliseconds = timeZone.getOffset(Time.now().asMilliseconds());

        // and convert the offset to hours.
        return (int) Math.round((double) offsetInMilliseconds / 1_000 / 60 / 60);
    }

    private HourOfWeek wrappedOffset(long offset)
    {
        var maximum = this.maximumInclusive().asInt() + 1;
        return hourOfWeek((int) ((asLong() + offset + maximum) % maximum));
    }
}
