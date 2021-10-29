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
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.collections.set.ObjectSet;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramDataConversionOther;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Collections;

/**
 * Converts between sets of objects and comma delimited strings. The string list returned by {@link
 * #onConvertToStringList(ObjectSet)} will be sorted unless {@link #sort(StringList)} is overridden to provide a
 * different sorting or no sorting.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataConversionOther.class)
public abstract class BaseSetConverter<T> extends BaseCollectionConverter<ObjectSet<T>>
{
    private final StringConverter<T> converter;

    public BaseSetConverter(Listener listener, StringConverter<T> converter, String delimiter)
    {
        super(listener, delimiter);
        this.converter = converter;
    }

    @Override
    protected ObjectSet<T> onConvertToObject(StringList columns)
    {
        var set = new ObjectSet<T>();
        for (var element : columns)
        {
            set.add(converter.convert(element.trim()));
        }
        return set;
    }

    @Override
    protected StringList onConvertToStringList(ObjectSet<T> value)
    {
        var list = new StringList();
        for (var element : value)
        {
            list.add(converter.unconvert(element));
        }
        sort(list);
        return list;
    }

    protected void sort(StringList list)
    {
        Collections.sort(list);
    }
}
