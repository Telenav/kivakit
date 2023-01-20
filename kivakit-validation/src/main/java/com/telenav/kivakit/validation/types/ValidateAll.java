package com.telenav.kivakit.validation.types;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.validation.Validatable;
import com.telenav.kivakit.validation.ValidationType;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A validation type that includes all {@link Validatable} objects in validation.
 *
 * @author jonathanl (shibo)
 * @see Validatable
 */
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
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
