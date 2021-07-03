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

package com.telenav.kivakit.kernel.language.collections.list;

import com.telenav.kivakit.kernel.data.conversion.string.StringConverter;
import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.language.collections.map.string.VariableMap;
import com.telenav.kivakit.kernel.language.strings.AsciiArt;
import com.telenav.kivakit.kernel.language.strings.StringTo;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.language.values.count.Maximum;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.messages.status.Information;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageString;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Function;

/**
 * A list of strings, adding useful string operations to {@link ObjectList}. Inherited methods that return lists are
 * overridden with down-casting methods for convenience. String lists can be constructed using the constructors, but
 * also using factory methods:
 *
 * <p><b>Factory Methods</b></p>
 *
 * <ul>
 *     <li>{@link #stringList(String...)} - A string list for the given strings</li>
 *     <li>{@link #stringList(Iterable)} - A string list of the given objects</li>
 *     <li>{@link #words(String)} - A list of the whitespace-separated words in the given string</li>
 *     <li>{@link #repeat(String, int)} - A list of the given string repeated the given number of times</li>
 *     <li>{@link #split(String, char)} - A list of strings from the given string split on the given character</li>
 *     <li>{@link #split(String, String)} - A list of strings from the given string split on the given string</li>
 *     <li>{@link #splitOnPattern(String, String)} - A list of strings from the given string split on the given regular expression</li>
 * </ul>
 *
 * <p><b>Length</b></p>
 *
 * <ul>
 *     <li>{@link #longest()} - The length of the longest string in this list</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #asStringArray()} - This string list as a string array</li>
 *     <li>{@link #asObjectList(StringConverter)} - This string list as an array of objects using the given string converter</li>
 *     <li>{@link #asVariableMap()} - This string list as a variable map where keys and values alternate</li>
 * </ul>
 *
 * <p><b>String Operations</b></p>
 *
 * <ul>
 *     <li>{@link #doubleQuoted()} - This string list with all strings in double quotes</li>
 *     <li>{@link #indented(int)} - This string list indented the given number of spaces</li>
 *     <li>{@link #numbered()} - This string list with each string prefixed by a number starting at 1</li>
 *     <li>{@link #prefixedWith(String)} - This string list with each element prefixed with the given string</li>
 *     <li>{@link #singleQuoted()} - This string list with all strings in single quotes</li>
 *     <li>{@link #titledBox(String)} - This string list in a titled box</li>
 *     <li>{@link #titledBox(Listener, String)} - This string list in a titled box sent as information to the given listener</li>
 * </ul>
 */
@UmlClassDiagram(diagram = DiagramLanguageString.class)
public class StringList extends ObjectList<String>
{
    /**
     * @return An empty string list
     */
    public static StringList create()
    {
        return stringList();
    }

    /**
     * @return A string list of the given text repeated the given number of times
     */
    public static StringList repeat(final String text, final int times)
    {
        final var list = new StringList();
        for (var i = 0; i < times; i++)
        {
            list.add(text);
        }
        return list;
    }

    /**
     * @return The list of strings resulting from splitting the given string on a delimiter character. The {@link
     * StringList} is unbounded in size for backwards compatibility.
     */
    public static StringList split(final String string, final char delimiter)
    {
        return split(Maximum.MAXIMUM, string, delimiter);
    }

    /**
     * @return The list of strings resulting from splitting the given string on a delimiter string. The {@link
     * StringList} is unbounded in size for backwards compatibility.
     */
    public static StringList split(final String string, final String delimiter)
    {
        return split(Maximum.MAXIMUM, string, delimiter);
    }

    /**
     * @return A string list of the given maximum size from the given text split on the given delimiter
     */
    public static StringList split(final Maximum maximumSize, final String string, final char delimiter)
    {
        if (string == null)
        {
            return new StringList(maximumSize);
        }
        final var strings = new StringList(maximumSize);
        var pos = 0;
        while (true)
        {
            final var next = string.indexOf(delimiter, pos);
            if (next == -1)
            {
                strings.add(string.substring(pos));
                break;
            }
            else
            {
                strings.add(string.substring(pos, next));
            }
            pos = next + 1;
        }
        return strings;
    }

    /**
     * @return A string list of the given maximum size from the given text split on the given delimiter
     */
    public static StringList split(final Maximum maximumSize, final String text, final String delimiter)
    {
        if (text == null)
        {
            return new StringList(maximumSize);
        }
        final var strings = new StringList(maximumSize);
        final var delimiterLength = delimiter.length();
        var pos = 0;
        while (true)
        {
            final var next = text.indexOf(delimiter, pos);
            if (next == -1)
            {
                strings.add(text.substring(pos));
                break;
            }
            else
            {
                strings.add(text.substring(pos, next));
            }
            pos = next + delimiterLength;
        }
        return strings;
    }

    /**
     * @return A string list split from the given text using a regular expression pattern
     */
    public static StringList splitOnPattern(final String text, final String pattern)
    {
        final var list = new StringList();
        list.addAll(text.split(pattern));
        return list;
    }

    /**
     * @return A list of strings from the given iterable
     */
    public static <T> StringList stringList(final Iterable<T> values)
    {
        final var list = new StringList();
        for (final var value : values)
        {
            list.addIfNotNull(value.toString());
        }
        return list;
    }

    /**
     * @return A list of strings from the given iterable
     */
    public static <T> StringList stringList(final Iterable<T> values, final StringConverter<T> converter)
    {
        final var list = new StringList();
        for (final var value : values)
        {
            list.addIfNotNull(converter.unconvert(value));
        }
        return list;
    }

    /**
     * @return The given list of objects with a maximum size
     */
    public static StringList stringList(final Maximum maximumSize, final String... strings)
    {
        final var list = new StringList(maximumSize);
        list.addAll(Arrays.asList(strings));
        return list;
    }

    /**
     * @return The given list of objects
     */
    public static StringList stringList(final String... strings)
    {
        return stringList(Maximum._1024, strings);
    }

    /**
     * @return A list of words from the given text with word breaks occurring on whitespace
     */
    public static StringList words(final String text)
    {
        final var list = new StringList();

        var startOfWord = -1;
        final var length = text.length();
        for (int at = 0; at < length; at++)
        {
            switch (text.charAt(at))
            {
                case ' ':
                case '\t':
                case '\n':
                    if (startOfWord >= 0)
                    {
                        list.add(text.substring(startOfWord, at));
                        startOfWord = -1;
                    }
                    break;

                default:
                    if (startOfWord < 0)
                    {
                        startOfWord = at;
                    }
                    break;
            }
        }
        if (startOfWord >= 0 && startOfWord < length)
        {
            list.add(text.substring(startOfWord));
        }

        return list;
    }

    public StringList()
    {
        this(Maximum.MAXIMUM);
    }

    public StringList(final Iterable<?> iterable)
    {
        this(Maximum.MAXIMUM, iterable);
    }

    public StringList(final Iterator<?> iterator)
    {
        this(Maximum.MAXIMUM, iterator);
    }

    public StringList(final Maximum maximumSize)
    {
        super(maximumSize);
    }

    public StringList(final Maximum maximumSize, final Iterable<?> iterable)
    {
        super(maximumSize);
        for (final Object object : iterable)
        {
            add(objectToString(object));
        }
    }

    public StringList(final Maximum maximumSize, final Iterator<?> iterator)
    {
        super(maximumSize);
        while (iterator.hasNext())
        {
            add(objectToString(iterator.next()));
        }
    }

    public StringList(final Maximum maximumSize, final String... strings)
    {
        super(maximumSize);
        addAll(Arrays.asList(strings));
    }

    /**
     * @return Adds the given formatted message to this string list
     */
    public StringList add(final String message, final Object... arguments)
    {
        add(Message.format(message, arguments));
        return this;
    }

    @Override
    public StringList append(final String s)
    {
        return (StringList) super.append(s);
    }

    @Override
    public StringList appendAll(final Iterable<? extends String> objects)
    {
        return (StringList) super.appendAll(objects);
    }

    @Override
    public StringList appendAll(final Iterator<? extends String> objects)
    {
        return (StringList) super.appendAll(objects);
    }

    @Override
    public StringList appendAll(final String[] objects)
    {
        return (StringList) super.appendAll(objects);
    }

    /**
     * @return This string list as an object list using the given converter
     */
    public <T> ObjectList<T> asObjectList(final StringConverter<T> converter)
    {
        final var objects = new ObjectList<T>();
        for (final var string : this)
        {
            objects.add(converter.convert(string));
        }
        return objects;
    }

    public String[] asStringArray()
    {
        return toArray(new String[size()]);
    }

    /**
     * @return This list of strings as a variable map where the even elements are keys and the odd elements are values.
     */
    public VariableMap<String> asVariableMap()
    {
        final var variables = new VariableMap<String>();
        for (var i = 0; i < size(); i += 2)
        {
            if (i + 1 < size())
            {
                variables.add(get(i), get(i + 1));
            }
        }
        return variables;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringList copy()
    {
        return (StringList) super.copy();
    }

    /**
     * @return This string list with each element in double quotes
     */
    public StringList doubleQuoted()
    {
        final var quoted = new StringList(maximumSize());
        for (final var value : this)
        {
            quoted.add("\"" + value + "\"");
        }
        return quoted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringList filtered(final Matcher<String> matcher)
    {
        return (StringList) super.filtered(matcher);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringList first(final Count count)
    {
        return (StringList) super.first(count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringList first(final int count)
    {
        return (StringList) super.first(count);
    }

    /**
     * @return This string list indented the given number of spaces
     */
    public StringList indented(final int spaces)
    {
        final var copy = new StringList();
        var i = 0;
        for (final var string : this)
        {
            copy.add((i++ > 0 ? "\n" : "") + AsciiArt.repeat(spaces, ' ') + string);
        }
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringList leftOf(final int index)
    {
        return (StringList) super.leftOf(index);
    }

    /**
     * @return The length of the longest string in this list
     */
    public Count longest()
    {
        int count = 0;
        for (final var at : this)
        {
            count = Math.max(at.length(), count);
        }
        return Count.count(count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <To> ObjectList<To> mapped(final Function<String, To> mapper)
    {
        return super.mapped(mapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringList maybeReversed(final boolean reverse)
    {
        return (StringList) super.maybeReversed(reverse);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringList newInstance()
    {
        return (StringList) super.newInstance();
    }

    /**
     * @return This list with all elements numbered starting at 1
     */
    public StringList numbered()
    {
        final var numbered = newInstance();
        var number = 1;
        for (final var value : this)
        {
            numbered.add(number + ". " + value);
            number++;
        }
        return numbered;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringList onNewInstance()
    {
        return new StringList();
    }

    /**
     * @return This string list with each element prefixed with the given prefix
     */
    public StringList prefixedWith(final String prefix)
    {
        final var prefixed = newInstance();
        for (final var string : this)
        {
            prefixed.add(prefix + string);
        }
        return prefixed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringList prepend(final String element)
    {
        return (StringList) super.prepend(element);
    }

    /**
     * Prints the values in this string list to the console
     */
    public StringList println()
    {
        System.out.println(join("\n"));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringList reversed()
    {
        return (StringList) super.reversed();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringList rightOf(final int index)
    {
        return (StringList) super.rightOf(index);
    }

    /**
     * @return This string list with each element in single quotes
     */
    public StringList singleQuoted()
    {
        final var quoted = newInstance();
        for (final var value : this)
        {
            quoted.add("'" + value + "'");
        }
        return quoted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringList sorted()
    {
        return (StringList) super.sorted();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringList sorted(final Comparator<String> comparator)
    {
        return (StringList) super.sorted(comparator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringList subList(final int start, final int end)
    {
        return (StringList) super.subList(start, end);
    }

    /**
     * @return Sends a titled box of this string list to the given listener as {@link Information}
     */
    public StringList titledBox(final Listener listener, final String title)
    {
        listener.information(titledBox(title));
        return this;
    }

    /**
     * @return This list of strings as an ASCII art text box with the given title
     */
    public String titledBox(final String title)
    {
        return AsciiArt.textBox(title, join("\n"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringList uniqued()
    {
        return (StringList) super.uniqued();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringList without(final Matcher<String> matcher)
    {
        return (StringList) super.without(matcher);
    }

    protected String objectToString(final Object object)
    {
        return StringTo.string(object);
    }
}
