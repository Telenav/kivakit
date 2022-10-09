package com.telenav.kivakit.validation.validators;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.validation.BaseValidator;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

/**
 * A validator that does nothing
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABILITY_STABLE,
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
