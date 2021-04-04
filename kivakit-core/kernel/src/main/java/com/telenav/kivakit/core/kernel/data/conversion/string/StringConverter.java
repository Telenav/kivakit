////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.data.conversion.string;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.data.conversion.Converter;
import com.telenav.kivakit.core.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataConversion;

/**
 * A bi-directional converter between {@link String} values and values of the given type. The {@link Converter}
 * interface converts from {@link String} to type &lt;Value&gt; and the method {@link #toString(Object)} converts a
 * &lt;Value&gt; back to a {@link String}.
 *
 * @param <Value> The value to convert to and from
 * @author jonathanl (shibo)
 * @see BaseStringConverter
 */
@UmlClassDiagram(diagram = DiagramDataConversion.class)
public interface StringConverter<Value> extends Converter<String, Value>
{
    StringConverter<String> IDENTITY = new BaseStringConverter<String>(Listener.none())
    {
        @Override
        protected String onConvertToObject(final String value)
        {
            return value;
        }
    };

    /**
     * @return The given string list with each string converted to an object using this converter
     */
    default ObjectList<Value> asObjectList(final StringList list)
    {
        return list.asObjectList(this);
    }

    /**
     * @return The given string list with each string converted to an object using this converter
     */
    default StringList asStringList(final ObjectList<Value> list)
    {
        return list.asStringList(this);
    }

    /**
     * Converts in the reverse direction, from the given value type to a {@link String}
     */
    String toString(final Value value);
}

