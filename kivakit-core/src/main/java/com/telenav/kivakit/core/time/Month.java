package com.telenav.kivakit.core.time;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * Represents months of the year.
 *
 * <p><b>Access</b></p>
 *
 * <ul>
 *     <li>{@link #monthForIndex(int)}</li>
 *     <li>{@link #monthOfYear()}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #calendarQuarter()}</li>
 *     <li>{@link #fiscalQuarter()}</li>
 *     <li>{@link #monthIndex()}</li>
 *     <li>{@link #monthOfYear()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABILITY_STABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
@SuppressWarnings("unused")
public enum Month
{
    JANUARY(1),
    FEBRUARY(2),
    MARCH(3),
    APRIL(4),
    MAY(5),
    JUNE(6),
    JULY(7),
    AUGUST(8),
    SEPTEMBER(9),
    OCTOBER(10),
    NOVEMBER(11),
    DECEMBER(12);

    /**
     * Returns a month for the given zero-based index
     */
    public static Month monthForIndex(int index)
    {
        return monthOfYear(index + 1);
    }

    /**
     * Returns the given one-based month of the year
     */
    public static Month monthOfYear(int monthOfYear)
    {
        for (var month : values())
        {
            if (month.monthOfYear() == monthOfYear)
            {
                return month;
            }
        }
        return unsupported();
    }

    private final int monthOfYear;

    Month(int monthOfYear)
    {
        this.monthOfYear = monthOfYear;
    }

    /**
     * Returns the calendar quarter for this month
     */
    public Quarter calendarQuarter()
    {
        var quarterIndex = monthIndex() / 3;
        return Quarter.calendarQuarter(quarterIndex + 1);
    }

    /**
     * Returns the fiscal quarter for this month
     */
    public Quarter fiscalQuarter()
    {
        var quarterIndex = monthIndex() / 3;
        var fiscalIndex = (quarterIndex + 2) % 4;
        return Quarter.fiscalQuarter(fiscalIndex + 1);
    }

    /**
     * Returns the zero-based index for this month
     */
    public int monthIndex()
    {
        return monthOfYear() - 1;
    }

    /**
     * Returns the one-based identifier for this month
     */
    public int monthOfYear()
    {
        return monthOfYear;
    }
}
