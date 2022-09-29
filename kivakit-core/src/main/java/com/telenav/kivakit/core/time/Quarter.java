package com.telenav.kivakit.core.time;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.language.primitive.Ints;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.time.Quarter.Type.CALENDAR;
import static com.telenav.kivakit.core.time.Quarter.Type.FISCAL;

/**
 * Represents calendar and fiscal quarters. Fiscal quarters start in July rather than January.
 *
 * <p><b>Access</b></p>
 *
 * <ul>
 *     <li>{@link #calendarQuarter(int)}</li>
 *     <li>{@link #fiscalQuarter(int)}</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #asCalendarQuarter()}</li>
 *     <li>{@link #asFiscalQuarter()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@ApiQuality(stability = STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public enum Quarter
{
    /** Jan-Mar */
    Q1(CALENDAR, 1),

    /** Apr-Jun */
    Q2(CALENDAR, 2),

    /** Jul-Sep */
    Q3(CALENDAR, 3),

    /** Oct-Dec */
    Q4(CALENDAR, 4),

    /** Jul-Sep */
    FISCAL_Q1(FISCAL, 1),

    /** Oct-Dec */
    FISCAL_Q2(FISCAL, 2),

    /** Jan-Mar */
    FISCAL_Q3(FISCAL, 3),

    /** Apr-Jun */
    FISCAL_Q4(FISCAL, 4);

    /**
     * Returns the given calendar quarter
     */
    public static Quarter calendarQuarter(int quarter)
    {
        ensure(Ints.isBetweenInclusive(quarter, 1, 4));
        return new Quarter[] { Q1, Q2, Q3, Q4 }[quarter - 1];
    }

    /**
     * Returns the given fiscal quarter
     */
    public static Quarter fiscalQuarter(int quarter)
    {
        ensure(Ints.isBetweenInclusive(quarter, 1, 4));
        return new Quarter[] { FISCAL_Q1, FISCAL_Q2, FISCAL_Q3, FISCAL_Q4 }[quarter - 1];
    }

    /**
     * The type of quarter
     */
    @ApiQuality(stability = STABLE,
                testing = UNTESTED,
                documentation = FULLY_DOCUMENTED)
    public enum Type
    {
        CALENDAR,
        FISCAL
    }

    /** The quarter number */
    private final int quarter;

    /** The type of quarter: calendar or fiscal */
    private final Type type;

    Quarter(Type type, int quarter)
    {
        ensure(Ints.isBetweenInclusive(quarter, 1, 4));
        this.type = type;
        this.quarter = quarter;
    }

    /**
     * Returns this quarter as a calendar quarter
     */
    public Quarter asCalendarQuarter()
    {
        return calendarQuarter(type == CALENDAR ? quarter : (quarter + 2) % 4);
    }

    /**
     * Returns this quarter as a fiscal quarter
     */
    public Quarter asFiscalQuarter()
    {
        return fiscalQuarter(type == FISCAL ? quarter : (quarter + 2) % 4);
    }

    /**
     * Returns the quarter of the year
     */
    public int quarter()
    {
        return quarter;
    }

    /**
     * Returns the type of quarter: calendar or fiscal
     */
    public Type type()
    {
        return type;
    }
}
