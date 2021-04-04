////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.collections.map.string;

import com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.core.kernel.interfaces.naming.Named;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.kernel.language.values.name.Name;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageCollectionsMap;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Map;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;

/**
 * A map to look up {@link Named} objects by their name. Objects can be added to the map with {@link #add(Named)}
 * instead of put() and their name will be used as the map key. To retrieve a value, {@link #get(Name)} or {@link
 * #get(String)} can be called. Map keys are case-insensitive by default. If case sensitivity is desired, {@link
 * #caseSensitive(boolean)} can be used.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("ConstantConditions")
@UmlClassDiagram(diagram = DiagramLanguageCollectionsMap.class)
public class NameMap<NamedObject extends Named> extends BaseStringMap<NamedObject>
{
    /** True if this name map is case sensitive (false by default) */
    private boolean caseSensitive;

    /**
     * Unbounded name map
     */
    public NameMap()
    {
        super(Maximum.MAXIMUM);
    }

    /**
     * Bounded name map
     */
    public NameMap(final Maximum maximumSize)
    {
        super(maximumSize);
    }

    /**
     * Bounded name map with initial values
     */
    public NameMap(final Maximum maximumSize, final Map<String, NamedObject> map)
    {
        super(maximumSize, map);
    }

    /**
     * Adds the given value by its name, as retrieved from {@link Named#name()}.
     */
    public void add(final NamedObject value)
    {
        Ensure.ensure(value != null);
        Ensure.ensure(value.name() != null);
        if (caseSensitive)
        {
            put(value.name(), value);
        }
        else
        {
            put(value.name().toLowerCase(), value);
        }
    }

    /**
     * @param caseSensitive True if this map should be case-sensitive
     */
    public void caseSensitive(final boolean caseSensitive)
    {
        this.caseSensitive = caseSensitive;
    }

    /**
     * @return The object for the given name
     */
    public final NamedObject get(final String name)
    {
        if (caseSensitive)
        {
            return getOrCreate(name);
        }
        return getOrCreate(name == null ? null : name.toLowerCase());
    }

    /**
     * @return The object for the given name
     */
    public final NamedObject get(final Name name)
    {
        return get(name.name());
    }

    @Override
    public NamedObject get(final Object key)
    {
        return get(key.toString());
    }
}
