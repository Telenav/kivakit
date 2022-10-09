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

package com.telenav.kivakit.conversion;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversion;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.interfaces.string.Parsable;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

/**
 * A bidirectional converter between {@link String} values and values of the given type. The {@link Converter} interface
 * converts from {@link String} to type &lt;Value&gt; and the method {@link #unconvert(Object)} converts a &lt;Value&gt;
 * back to a {@link String}.
 *
 * @param <Value> The value to convert to and from
 * @author jonathanl (shibo)
 * @see BaseStringConverter
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramConversion.class)
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public interface StringConverter<Value> extends
        TwoWayConverter<String, Value>,
        Parsable<Value>
{
    /**
     * Converts the given delimited string to a list of objects using this converter
     *
     * @param value The string to convert
     * @param delimiter The delimiter
     * @return The list of converted values
     */
    default ObjectList<Value> convertToList(String value, String delimiter)
    {
        return convertToList(StringList.split(value, delimiter));
    }

    /**
     * Converts the given {@link Iterable} of strings to a list of objects using this string converter
     *
     * @return The given string list with each string converted to an object using this converter
     */
    default ObjectList<Value> convertToList(Iterable<String> list)
    {
        var objects = new ObjectList<Value>();
        for (var string : list)
        {
            objects.addIfNotNull(convert(string));
        }
        return objects;
    }

    /**
     * Converts the given delimited string to a set of objects using this string converter
     *
     * @param value The string to convert
     * @param delimiter The delimiter
     * @return The set of converted values
     */
    default ObjectSet<Value> convertToSet(String value, String delimiter)
    {
        return convertToSet(StringList.split(value, delimiter));
    }

    /**
     * Converts the given {@link Iterable} of strings to a set of objects using this string converter
     *
     * @return The given string list with each string converted to an object using this converter
     */
    default ObjectSet<Value> convertToSet(Iterable<String> list)
    {
        var objects = new ObjectSet<Value>();
        for (var string : list)
        {
            objects.addIfNotNull(convert(string));
        }
        return objects;
    }

    /**
     * Returns a list converter that uses the given delimiter
     *
     * @param delimiter The delimiter
     */
    default StringConverter<ObjectList<Value>> listConverter(String delimiter)
    {
        var outer = this;
        return new BaseStringConverter<>(this)
        {
            @Override
            protected ObjectList<Value> onToValue(String value)
            {
                return outer.convertToList(value, delimiter);
            }
        };
    }

    /**
     * Returns a list converter that separates items with commas
     */
    default StringConverter<ObjectList<Value>> listConverter()
    {
        return listConverter(",");
    }

    /**
     * Parses the given text into a value using this converter
     *
     * @param text The text
     */
    @Override
    default Value parse(String text)
    {
        return convert(text);
    }

    /**
     * Converts the given {@link Iterable} of objects to a list of strings
     *
     * @return A list of strings, one for each unconverted value
     */
    @SuppressWarnings("SpellCheckingInspection")
    default StringList unconvertCollection(Iterable<Value> values)
    {
        var list = new StringList();
        for (var value : values)
        {
            list.addIfNotNull(unconvert(value));
        }
        return list;
    }
}
