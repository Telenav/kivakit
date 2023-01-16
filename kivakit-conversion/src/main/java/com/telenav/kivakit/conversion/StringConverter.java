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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversion;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.interfaces.string.Parsable;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.collections.list.StringList.split;

/**
 * A bidirectional converter between {@link String} values and values of the given type. The {@link Converter} interface
 * converts from {@link String} to type &lt;Value&gt; and the method {@link #unconvert(Object)} converts a &lt;Value&gt;
 * back to a {@link String}.
 *
 * <p><b>Parsing</b></p>
 *
 * <ul>
 *     <li>{@link #parse(String)} - Implements {@link Parsable} by calling {@link #convert(Object)}</li>
 * </ul>
 *
 * <p><b>Collections</b></p>
 *
 * <ul>
 *     <li>{@link #convertToList(String, String)} - Converts the given text and separator to an {@link ObjectList} using this converter</li>
 *     <li>{@link #convertToList(Iterable)} - Converts the given sequence of objects to an {@link ObjectList} using this converter</li>
 *     <li>{@link #convertToSet(String, String)} - Converts the given text and separator to an {@link ObjectSet} using this converter</li>
 *     <li>{@link #convertToSet(Iterable)} - Converts the given sequence of objects to an {@link ObjectSet} using this converter</li>
 *     <li>{@link #listConverter(Class)} - Returns a StringConverter&lt;ObjectList&gt; that converts text to an object list using this converter</li>
 *     <li>{@link #listConverter(Class, String)} - Returns a StringConverter&lt;ObjectList&gt; that converts text and a separator to an object list using this converter</li>
 *     <li>{@link #setConverter(Class)} - Returns a StringConverter&lt;ObjectSet&gt; that converts text to an object set using this converter</li>
 *     <li>{@link #setConverter(Class, String)} - Returns a StringConverter&lt;ObjectSet&gt; that converts text and a separator to an object set using this converter</li>
 * </ul>
 *
 * <p><b>Reverse Conversion</b></p>
 *
 * <ul>
 *     <li>{@link #unconvertAll(Iterable)} - Returns a sequence of values to a {@link StringList}</li>
 * </ul>
 *
 * @param <Value> The value to convert to and from
 * @author jonathanl (shibo)
 * @see BaseStringConverter
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramConversion.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public interface StringConverter<Value> extends
    TwoWayConverter<String, Value>,
    Parsable<Value>
{
    class ObjectListConverter<Value> extends BaseStringConverter<ObjectList<Value>>
    {
        private final String delimiter;

        protected ObjectListConverter(Listener listener, Class<ObjectList<Value>> toType, String delimiter)
        {
            super(listener, toType);
            this.delimiter = delimiter;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected ObjectList<Value> onToValue(String value)
        {
            return (ObjectList<Value>) convertToList(value, delimiter);
        }
    }

    class ObjectSetConverter<Value> extends BaseStringConverter<ObjectSet<Value>>
    {
        private final String delimiter;

        protected ObjectSetConverter(Listener listener, Class<ObjectSet<Value>> toType, String delimiter)
        {
            super(listener, toType);
            this.delimiter = delimiter;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected ObjectSet<Value> onToValue(String value)
        {
            return (ObjectSet<Value>) convertToSet(value, delimiter);
        }
    }

    /**
     * Converts the given delimited string to a list of objects using this converter
     *
     * @param value The string to convert
     * @param delimiter The delimiter
     * @return The list of converted values
     */
    default ObjectList<Value> convertToList(String value, String delimiter)
    {
        return convertToList(split(value, delimiter));
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
        return convertToSet(split(value, delimiter));
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
    default StringConverter<ObjectList<Value>> listConverter(Class<ObjectList<Value>> type, String delimiter)
    {
        var outer = this;
        return new ObjectListConverter<>(this, type, delimiter);
    }

    /**
     * Returns a list converter that separates items with commas
     */
    default StringConverter<ObjectList<Value>> listConverter(Class<ObjectList<Value>> type)
    {
        return listConverter(type, ",");
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
     * Returns a list converter that uses the given delimiter
     *
     * @param delimiter The delimiter
     */
    default StringConverter<ObjectSet<Value>> setConverter(Class<ObjectSet<Value>> type, String delimiter)
    {
        return new ObjectSetConverter<>(this, type, delimiter);
    }

    /**
     * Returns a list converter that separates items with commas
     */
    default StringConverter<ObjectSet<Value>> setConverter(Class<ObjectSet<Value>> type)
    {
        return setConverter(type, ",");
    }

    /**
     * Converts the given {@link Iterable} of objects to a list of strings
     *
     * @return A list of strings, one for each unconverted value
     */
    @SuppressWarnings("SpellCheckingInspection")
    default StringList unconvertAll(Iterable<Value> values)
    {
        var list = new StringList();
        for (var value : values)
        {
            list.addIfNotNull(unconvert(value));
        }
        return list;
    }
}
