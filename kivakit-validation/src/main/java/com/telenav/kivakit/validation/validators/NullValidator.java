package com.telenav.kivakit.validation.validators;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.validation.BaseValidator;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A validator that does nothing
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class NullValidator extends BaseValidator
{
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onValidate()
    {
    }
}
