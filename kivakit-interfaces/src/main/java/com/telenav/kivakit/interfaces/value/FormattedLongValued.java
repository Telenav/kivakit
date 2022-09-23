package com.telenav.kivakit.interfaces.value;

import com.telenav.kivakit.interfaces.string.StringFormattable;

/**
 * @author jonathanl (shibo)
 */
public interface FormattedLongValued extends
        LongValued,
        StringFormattable
{
    default String asCommaSeparatedString()
    {
        return String.format("%,d", longValue());
    }

    default String asSimpleString()
    {
        return Long.toString(longValue());
    }

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    default String asString(StringFormattable.Format format)
    {
        switch (format)
        {
            case PROGRAMMATIC:
                return asSimpleString();

            default:
                return asCommaSeparatedString();
        }
    }
}
