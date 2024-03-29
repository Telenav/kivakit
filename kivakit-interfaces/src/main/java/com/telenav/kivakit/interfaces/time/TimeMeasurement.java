package com.telenav.kivakit.interfaces.time;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.interfaces.value.LongValued;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Interface for all time measurements, whether a {@link LengthOfTime} or a {@link PointInTime}.
 *
 * <p><b>Measurement</b></p>
 *
 * <ul>
 *     <li>{@link #nanoseconds()}</li>
 *     <li>{@link #milliseconds()}</li>
 * </ul>
 *
 * <p><b>Conversion</b></p>
 *
 * <p>
 * A length of time can be converted to specific time units by calling one of the following methods:
 * </p>
 *
 * <ul>
 *     <li>{@link #asNanoseconds()}</li>
 *     <li>{@link #asMicroseconds()}</li>
 *     <li>{@link #asMilliseconds()}</li>
 *     <li>{@link #asSeconds()}</li>
 *     <li>{@link #asMinutes()}</li>
 *     <li>{@link #asHours()}</li>
 *     <li>{@link #asDays()}</li>
 *     <li>{@link #asWeeks()}</li>
 *     <li>{@link #asYears()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public interface TimeMeasurement extends LongValued
{
    /**
     * Returns the number of days for this time measurement
     */
    default double asDays()
    {
        return asHours() / 24.0;
    }

    /**
     * Returns the number of hours for this time measurement
     */
    default double asHours()
    {
        return asMinutes() / 60.0;
    }

    @NotNull
    default String asHumanReadableString()
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
            if (asMilliseconds() >= 1.0)
            {
                return unitString(asMilliseconds(), "millisecond");
            }
            if (asMicroseconds() >= 1.0)
            {
                return unitString(asMicroseconds(), "microsecond");
            }

            return unitString(asNanoseconds(), "nanosecond");
        }
        else
        {
            return "N/A";
        }
    }

    /**
     * Returns the number of microseconds for this time measurement
     */
    default double asMicroseconds()
    {
        return asNanoseconds() / 1E3;
    }

    /**
     * Returns the number of milliseconds for this time measurement
     */
    default double asMilliseconds()
    {
        return asMicroseconds() / 1E3;
    }

    /**
     * Returns the number of minutes for this time measurement
     */
    default double asMinutes()
    {
        return asSeconds() / 60.0;
    }

    /**
     * Returns the number of nanoseconds for this time measurement
     */
    default double asNanoseconds()
    {
        return nanoseconds().asDouble();
    }

    /**
     * Returns the number of seconds for this time measurement
     */
    default double asSeconds()
    {
        return asMilliseconds() / 1E3;
    }

    /**
     * Returns the number of weeks for this time measurement
     */
    default double asWeeks()
    {
        return asDays() / 7;
    }

    /**
     * Returns the number of years for this time measurement
     */
    default double asYears()
    {
        return asWeeks() / 52.177457;
    }

    /**
     * Returns the number of milliseconds for this time measurement
     */
    default long epochMilliseconds()
    {
        return milliseconds();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default long longValue()
    {
        return milliseconds();
    }

    /**
     * Returns the number of milliseconds for this time measurement
     */
    default long milliseconds()
    {
        return (long) asMilliseconds();
    }

    /**
     * Returns the number of nanoseconds for this measurement
     */
    Nanoseconds nanoseconds();

    /**
     * Converts a value to a unit-suffixed value, taking care of English singular/plural suffix.
     *
     * @param value a double value to format
     * @param units the units to apply singular or plural suffix to
     * @return a String representation
     */
    default String unitString(double value, String units)
    {
        var format = new DecimalFormat("###,###.##");
        return format.format(value) + " " + units + (value > 1.0 ? "s" : "");
    }
}
