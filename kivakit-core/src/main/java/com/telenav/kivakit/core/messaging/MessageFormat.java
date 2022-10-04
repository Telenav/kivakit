package com.telenav.kivakit.core.messaging;

import com.telenav.kivakit.annotations.code.ApiQuality;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_ENUM_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * Specifies what kind of formatting to apply to a message
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = API_STABLE_ENUM_EXTENSIBLE,
            testing = TESTING_NOT_NEEDED,
            documentation = DOCUMENTATION_COMPLETE)
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
