package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.language.primitive.Ints;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.time.Quarter.Type.CALENDAR;
import static com.telenav.kivakit.core.time.Quarter.Type.FISCAL;

@SuppressWarnings("unused")
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

    public static Quarter calendarQuarter(int quarter)
    {
        ensure(Ints.isBetweenInclusive(quarter, 1, 4));
        return new Quarter[] { Q1, Q2, Q3, Q4 }[quarter - 1];
    }

    public static Quarter fiscalQuarter(int quarter)
    {
        ensure(Ints.isBetweenInclusive(quarter, 1, 4));
        return new Quarter[] { FISCAL_Q1, FISCAL_Q2, FISCAL_Q3, FISCAL_Q4 }[quarter - 1];
    }

    public enum Type
    {
        CALENDAR,
        FISCAL
    }

    private final int quarter;

    private final Type type;

    Quarter(Type type, int quarter)
    {
        ensure(Ints.isBetweenInclusive(quarter, 1, 4));
        this.type = type;
        this.quarter = quarter;
    }

    public Quarter asCalendar()
    {
        return calendarQuarter(type == CALENDAR ? quarter : (quarter + 2) % 4);
    }

    public Quarter asFiscal()
    {
        return fiscalQuarter(type == FISCAL ? quarter : (quarter + 2) % 4);
    }

    public int quarter()
    {
        return quarter;
    }

    public Type type()
    {
        return type;
    }
}
