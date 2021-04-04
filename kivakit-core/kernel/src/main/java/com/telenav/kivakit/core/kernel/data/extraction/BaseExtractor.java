////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.data.extraction;

import com.telenav.kivakit.core.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import com.telenav.kivakit.core.kernel.interfaces.collection.Keyed;
import com.telenav.kivakit.core.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataExtraction;

import java.util.Map;

/**
 * A base implementation for extractors that handles edge conditions like exceptions as well as extracting lists from
 * arrays, maps and {@link Keyed} objects.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataExtraction.class)
public abstract class BaseExtractor<Value, From> extends BaseRepeater implements Extractor<Value, From>
{
    /**
     * @param listener The listener to hear any conversion issues
     */
    protected BaseExtractor(final Listener listener)
    {
        addListener(listener);
    }

    /**
     * @return The value extracted from the given object, or null if an exception is thrown
     */
    @Override
    @UmlExcludeMember
    public Value extract(final From object)
    {
        try
        {
            return onExtract(object);
        }
        catch (final Exception e)
        {
            problem(e, "Extractor threw exception");
            return null;
        }
    }

    /**
     * @return A list of values extracted from the given array of objects
     */
    public ObjectList<Value> extract(final From[] values)
    {
        final var extracted = new ObjectList<Value>();
        for (final var value : values)
        {
            extracted.add(extract(value));
        }
        return extracted;
    }

    /**
     * @return A list of values extracted from the map using the given key and separator
     */
    @SuppressWarnings("unchecked")
    public ObjectList<Value> extractList(final Map<String, String> map, final From key, final String separator)
    {
        final var extracted = new ObjectList<Value>();
        final var value = map.get(key.toString());
        for (final var part : value.split(separator))
        {
            extracted.add(extract((From) part));
        }
        return extracted;
    }

    /**
     * @return A list of values extracted from the keyed object using the given key and separator
     */
    @SuppressWarnings("unchecked")
    public ObjectList<Value> extractList(final Keyed<String, String> map, final From key, final String separator)
    {
        final var extracted = new ObjectList<Value>();
        final var value = map.get(key.toString());
        if (value != null)
        {
            for (final var part : value.split(separator))
            {
                extracted.add(extract((From) part));
            }
        }
        return extracted;
    }

    /**
     * @return A list of values extracted from the keyed object using the given key and the separator '|'
     */
    public ObjectList<Value> extractList(final Keyed<String, String> map, final From key)
    {
        return extractList(map, key, "\\|");
    }

    /**
     * @return A list of values extracted from the given map using the given key and the separator '|'
     */
    public ObjectList<Value> extractList(final Map<String, String> map, final From key)
    {
        return extractList(map, key, "\\|");
    }

    /**
     * @return Called to extract a value from the given object
     */
    public abstract Value onExtract(From object);
}
