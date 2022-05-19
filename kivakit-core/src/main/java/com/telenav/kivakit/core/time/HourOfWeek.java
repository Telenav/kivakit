package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.test.NoTestRequired;
import com.telenav.kivakit.core.test.Tested;
import com.telenav.kivakit.core.value.count.BaseCount;
import com.telenav.kivakit.interfaces.time.Nanoseconds;

import java.time.ZoneId;
import java.util.Objects;
import java.util.TimeZone;

import static com.telenav.kivakit.core.ensure.Ensure.ensureBetweenExclusive;
import static com.telenav.kivakit.core.time.BaseTime.Topology.CYCLIC;
import static com.telenav.kivakit.core.time.DayOfWeek.isoDayOfWeek;
import static com.telenav.kivakit.core.time.Hour.nanosecondsPerHour;

/**
 * Represents an hour of the week, for example Thursday at 1pm. This class stores its count value in the fields {@link
 * #dayOfWeek()} and {@link #hourOfDay()}.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public class HourOfWeek extends BaseTime<HourOfWeek>
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
        return hourOfWeek((dayOfWeek.asIsoOrdinal() * 24 + hourOfDay.asUnits()));
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
        super(dayOfWeek.nanoseconds().plus(hourOfDay.nanoseconds()));

        this.dayOfWeek = dayOfWeek;
        this.hourOfDay = hourOfDay;
    }

    public Time asEpochTime()
    {
        return Time.nanoseconds(nanoseconds());
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

    /**
     * Converts this hour of the week, assumed to be in localtime, to UTC.
     *
     * @param localZone The time zone for this hour of the week
     * @return The hour of the week in UTC time
     */
    @Tested
    public HourOfWeek asUtc(ZoneId localZone)
    {
        return offset(localZone, true);
    }

    /**
     * @return The day of the week
     */
    @Tested
    public DayOfWeek dayOfWeek()
    {
        return dayOfWeek;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof HourOfWeek)
        {
            HourOfWeek that = (HourOfWeek) object;
            return this.dayOfWeek().equals(that.dayOfWeek())
                    && this.hourOfDay().equals(that.hourOfDay());
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(dayOfWeek(), hourOfDay());
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
    public HourOfWeek maximum()
    {
        return hourOfWeek(7 * 24 - 1);
    }

    @Override
    public HourOfWeek minimum()
    {
        return hourOfWeek(0);
    }

    @Override
    @Tested
    public HourOfWeek minusUnits(double count)
    {
        return offset(-count);
    }

    @Override
    public Nanoseconds nanosecondsPerUnit()
    {
        return nanosecondsPerHour;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NoTestRequired
    public HourOfWeek onNewTime(Nanoseconds nanoseconds)
    {
        return hourOfWeek((int) nanosecondsToUnits(nanoseconds));
    }

    @Override
    @Tested
    public HourOfWeek plusUnits(double count)
    {
        return offset(count);
    }

    @Override
    @NoTestRequired
    public String toString()
    {
        return dayOfWeek() + " at " + hourOfDay();
    }

    @Override
    protected Topology topology()
    {
        return CYCLIC;
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

        return offset(offset);
    }

    private HourOfWeek offset(double offset)
    {
        var maximumExclusive = this.maximum().asUnits() + 1;
        return hourOfWeek((int) ((asUnits() + offset + maximumExclusive) % maximumExclusive));
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
        var offsetInMilliseconds = timeZone.getOffset(Time.now().milliseconds());

        // and convert the offset to hours.
        return (int) Math.round((double) offsetInMilliseconds / 1_000 / 60 / 60);
    }
}
