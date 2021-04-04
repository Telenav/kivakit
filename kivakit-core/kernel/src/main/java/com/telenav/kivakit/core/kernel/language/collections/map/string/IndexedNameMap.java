////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.collections.map.string;

import com.telenav.kivakit.core.kernel.interfaces.naming.Named;
import com.telenav.kivakit.core.kernel.language.collections.map.BaseIndexedMap;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;

/**
 * Stores named values by name and also makes them accessible by index.
 *
 * @param <T> The type implementing {@link Named}
 * @author jonathanl (shibo)
 */
public class IndexedNameMap<T extends Named> extends BaseIndexedMap<String, T>
{
    public IndexedNameMap()
    {
        super(Maximum.MAXIMUM);
    }

    public IndexedNameMap(final Maximum maximumSize)
    {
        super(maximumSize);
    }

    public final T forName(final String name)
    {
        return get(name);
    }

    public void put(final T value)
    {
        put(value.name(), value);
    }
}
