package com.telenav.kivakit.core.messaging;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Specifies what kind of formatting to apply to a message
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public enum MessageFormat
{
    /** The message should be formatted */
    FORMATTED,

    /** The message should not be formatted */
    UNFORMATTED
}
