package com.telenav.kivakit.resource;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Converts to and from {@link ResourcePath}s
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class ResourcePathConverter extends BaseStringConverter<ResourcePath>
{
    public ResourcePathConverter(@NotNull Listener listener)
    {
        super(listener, ResourcePath.class);
    }

    @Override
    protected ResourcePath onToValue(String value)
    {
        return ResourcePath.parseResourcePath(this, value);
    }
}
