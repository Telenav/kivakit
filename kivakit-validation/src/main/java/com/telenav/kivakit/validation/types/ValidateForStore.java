package com.telenav.kivakit.validation.types;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.validation.ValidationType;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

/**
 * A validation type that signifies that values should be validated for insertion into a store, such as a database.
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABILITY_STABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class ValidateForStore extends ValidationType
{
}
