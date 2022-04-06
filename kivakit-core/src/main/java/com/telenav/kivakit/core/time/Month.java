package com.telenav.kivakit.core.time;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

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

    public static Month monthForIndex(int index)
    {
        return monthOfYear(index + 1);
    }

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

    public Quarter calendarQuarter()
    {
        var quarterIndex = monthIndex() / 3;
        return Quarter.calendarQuarter(quarterIndex + 1);
    }

    public Quarter fiscalQuarter()
    {
        var quarterIndex = monthIndex() / 3;
        var fiscalIndex = (quarterIndex + 2) % 4;
        return Quarter.fiscalQuarter(fiscalIndex + 1);
    }

    public int monthIndex()
    {
        return monthOfYear() - 1;
    }

    public int monthOfYear()
    {
        return monthOfYear;
    }
}
