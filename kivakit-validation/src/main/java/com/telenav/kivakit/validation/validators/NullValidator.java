package com.telenav.kivakit.validation.validators;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.validation.BaseValidator;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A validator that does nothing
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = API_STABLE,
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
