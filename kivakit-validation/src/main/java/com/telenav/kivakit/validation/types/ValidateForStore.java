package com.telenav.kivakit.validation.types;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.validation.ValidationType;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A validation type that signifies that values should be validated for insertion into a store, such as a database.
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class ValidateForStore extends ValidationType
{
}
