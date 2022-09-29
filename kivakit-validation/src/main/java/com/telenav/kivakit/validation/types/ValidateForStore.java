package com.telenav.kivakit.validation.types;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.validation.ValidationType;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * A validation type that signifies that values should be validated for insertion into a store, such as a database.
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class ValidateForStore extends ValidationType
{
}
