package com.telenav.kivakit.core.messaging;

import com.telenav.kivakit.annotations.code.ApiQuality;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_ENUM_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * Specifies what kind of formatting to apply to a message
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = STABLE_ENUM_EXPANDABLE,
            testing = TESTING_NOT_NEEDED,
            documentation = FULLY_DOCUMENTED)
public enum MessageFormat
{
    /** The message should be formatted */
    FORMATTED,

    /** The message should not be formatted */
    UNFORMATTED,

    /** The formatted message should include any exception */
    WITH_EXCEPTION,

    /** The formatted message should not include any exception */
    WITHOUT_EXCEPTION
}
