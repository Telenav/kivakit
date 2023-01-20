package com.telenav.kivakit.core.registry;

import com.telenav.lexakai.annotations.visibility.UmlExcludeType;

/**
 * <b>Not public API</b>
 */
@UmlExcludeType
record RegistryKey(Class<?> type, InstanceIdentifier identifier)
{
    @Override
    public String toString()
    {
        return type.getSimpleName()
            + (identifier.isSingleton()
            ? ""
            : ":" + identifier);
    }
}
