package com.telenav.kivakit.validation.types;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.validation.ValidationType;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A validation type that signifies that values that should be validated came from user input
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class ValidateUserInput extends ValidationType
{
}
