////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.data.conversion.string.collection;

import com.telenav.kivakit.core.kernel.data.conversion.string.StringConverter;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataConversionOther;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Converts between sets of objects and comma delimited strings. The string list returned by {@link
 * #onConvertToStringList(Set)} will be sorted unless {@link #sort(StringList)} is overridden to provide a different
 * sorting or no sorting.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataConversionOther.class)
public abstract class BaseSetConverter<T> extends BaseCollectionConverter<Set<T>>
{
    private final StringConverter<T> converter;

    public BaseSetConverter(final Listener listener, final StringConverter<T> converter, final String delimiter)
    {
        super(listener, delimiter);
        this.converter = converter;
    }

    @Override
    protected Set<T> onConvertToObject(final StringList columns)
    {
        final var set = new HashSet<T>();
        for (final var element : columns)
        {
            set.add(converter.convert(element));
        }
        return set;
    }

    @Override
    protected StringList onConvertToStringList(final Set<T> value)
    {
        final var list = new StringList();
        for (final var element : value)
        {
            list.add(converter.toString(element));
        }
        sort(list);
        return list;
    }

    protected void sort(final StringList list)
    {
        Collections.sort(list);
    }
}
