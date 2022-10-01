package com.telenav.kivakit.validation.validators;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.validation.BaseValidator;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * A validator that does nothing
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
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
