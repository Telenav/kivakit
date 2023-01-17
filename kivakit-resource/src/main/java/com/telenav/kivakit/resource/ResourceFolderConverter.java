package com.telenav.kivakit.resource;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Converts to and from {@link ResourceFolder}s by resolving strings via {@link ResourceFolderIdentifier}s.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("rawtypes")
@TypeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED) public
class ResourceFolderConverter extends BaseStringConverter<ResourceFolder>
{
    public ResourceFolderConverter(Listener listener)
    {
        super(listener, ResourceFolder.class);
    }

    @Override
    protected ResourceFolder<?> onToValue(String value)
    {
        return new ResourceFolderIdentifier(value).resolve(this);
    }
}
