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

package com.telenav.kivakit.kernel.data.conversion.string;

import com.telenav.kivakit.kernel.data.conversion.Converter;
import com.telenav.kivakit.kernel.data.conversion.TwoWayConverter;
import com.telenav.kivakit.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramDataConversion;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A bi-directional converter between {@link String} values and values of the given type. The {@link Converter}
 * interface converts from {@link String} to type &lt;Value&gt; and the method {@link #unconvert(Object)} converts a
 * &lt;Value&gt; back to a {@link String}.
 *
 * @param <Value> The value to convert to and from
 * @author jonathanl (shibo)
 * @see BaseStringConverter
 */
@UmlClassDiagram(diagram = DiagramDataConversion.class)
public interface StringConverter<Value> extends TwoWayConverter<String, Value>
{
    StringConverter<String> IDENTITY = new BaseStringConverter<>(Listener.none())
    {
        @Override
        protected String onToValue(String value)
        {
            return value;
        }
    };

    /**
     * @return The given string list with each string converted to an object using this converter
     */
    default ObjectList<Value> asObjectList(StringList list)
    {
        return list.asObjectList(this);
    }

    /**
     * @return The given string list with each string converted to an object using this converter
     */
    default StringList asStringList(ObjectList<Value> list)
    {
        return list.asStringList(this);
    }
}
