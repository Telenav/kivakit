////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.data.conversion.string.collection;

import com.telenav.kivakit.core.kernel.data.conversion.string.StringConverter;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataConversionOther;

/**
 * Converts between lists of objects and comma delimited strings.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataConversionOther.class)
public abstract class BaseListConverter<T> extends BaseCollectionConverter<ObjectList<T>>
{
    /** The element converter */
    private final StringConverter<T> converter;

    /**
     * @param listener Conversion listener
     * @param converter The converter to use for converting each element
     * @param delimiter The delimiter between elements
     */
    public BaseListConverter(final Listener listener, final StringConverter<T> converter, final String delimiter)
    {
        super(listener, delimiter);
        this.converter = converter;
    }

    @Override
    protected ObjectList<T> onConvertToObject(final StringList columns)
    {
        final var list = new ObjectList<T>(columns.maximumSize());
        for (final var element : columns)
        {
            list.addIfNotNull(converter.convert(element));
        }
        return list;
    }

    @Override
    protected StringList onConvertToStringList(final ObjectList<T> values)
    {
        final var list = new StringList();
        for (final var value : values)
        {
            list.addIfNotNull(converter.toString(value));
        }
        return list;
    }
}
