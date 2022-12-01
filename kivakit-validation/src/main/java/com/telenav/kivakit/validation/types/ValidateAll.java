package com.telenav.kivakit.validation.types;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.validation.Validatable;
import com.telenav.kivakit.validation.ValidationType;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A validation type that includes all {@link Validatable} objects in validation.
 *
 * @author jonathanl (shibo)
 * @see Validatable
 */
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class ValidateAll extends ValidationType
{
    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Validatable> boolean shouldValidate(Class<T> type)
    {
        return true;
    }
}
