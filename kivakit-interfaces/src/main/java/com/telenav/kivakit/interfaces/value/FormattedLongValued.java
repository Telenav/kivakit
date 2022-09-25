package com.telenav.kivakit.interfaces.value;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.interfaces.string.StringFormattable;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_DEFAULT_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_REQUIRED;

/**
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = STABLE_DEFAULT_EXPANDABLE,
            testing = TESTING_NOT_REQUIRED,
            documentation = FULLY_DOCUMENTED)
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
