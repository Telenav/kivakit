package com.telenav.kivakit.core.registry;

import com.telenav.kivakit.core.value.identifier.StringIdentifier;
import com.telenav.lexakai.annotations.visibility.UmlExcludeType;

/**
 * <b>Not public API</b>
 */
@UmlExcludeType
class RegistryKey extends StringIdentifier
{
    RegistryKey(String identifier)
    {
        super(identifier);
    }
}
