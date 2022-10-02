package com.telenav.kivakit.core.language.reflection.property;

import com.telenav.kivakit.annotations.code.ApiQuality;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_ENUM_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * The purpose for labeling a property with {@link KivaKitIncludeProperty} or {@link KivaKitExcludeProperty}. This value
 * is useful when a member has more than one annotation. For example, it might be included for one purpose, but not for
 * another.
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = API_STABLE_ENUM_EXTENSIBLE,
            testing = TESTING_NOT_NEEDED,
            documentation = DOCUMENTATION_COMPLETE)
public enum PropertyPurpose
{
    /** The property is being selected for formatting purposes */
    FORMATTING,

    /** The property is being labeled to participate in conversion */
    CONVERSION
}
