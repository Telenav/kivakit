package com.telenav.kivakit.interfaces.value;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.interfaces.string.StringFormattable;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_DEFAULT_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = API_STABLE_DEFAULT_EXTENSIBLE,
            testing = TESTING_NOT_NEEDED,
            documentation = DOCUMENTATION_COMPLETE)
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
    default String asString(StringFormattable.@NotNull Format format)
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
