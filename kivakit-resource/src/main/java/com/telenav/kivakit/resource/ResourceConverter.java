package com.telenav.kivakit.resource;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Converts to and from {@link Resource}s by resolving {@link ResourceIdentifier}s.
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public class ResourceConverter extends BaseStringConverter<Resource>
{
    public ResourceConverter(@NotNull Listener listener)
    {
        super(listener, Resource.class);
    }

    @Override
    protected Resource onToValue(String value)
    {
        return new ResourceIdentifier(value).resolve(this);
    }
}
