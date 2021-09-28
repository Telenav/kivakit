////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.kernel.data.conversion.string.collection;

import com.telenav.kivakit.kernel.data.conversion.string.StringConverter;
import com.telenav.kivakit.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramDataConversionOther;
import com.telenav.lexakai.annotations.UmlClassDiagram;

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
            list.addIfNotNull(converter.convert(element.trim()));
        }
        return list;
    }

    @Override
    protected StringList onConvertToStringList(final ObjectList<T> values)
    {
        final var list = new StringList();
        for (final var value : values)
        {
            list.addIfNotNull(converter.unconvert(value));
        }
        return list;
    }
}
