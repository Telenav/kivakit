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

package com.telenav.kivakit.core.collections.list;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.internal.lexakai.DiagramString;
import com.telenav.kivakit.core.string.AsciiArt;
import com.telenav.kivakit.core.string.StringConversions;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Function;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_INSUFFICIENT;
import static com.telenav.kivakit.core.os.Console.console;
import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.core.value.count.Maximum.MAXIMUM;
import static java.lang.Math.max;

/**
 * A list of strings, adding useful string operations to {@link ObjectList}. Inherited methods that return lists are
 * overridden with down-casting methods for convenience. String lists can be constructed using the constructors, but
 * also using factory methods. Inherited methods are documented in {@link ObjectList} and {@link BaseList}.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #repeat(String, int)} - A list of the given string repeated the given number of times</li>
 *     <li>{@link #split(String, String)} - A list of strings from the given string split on the given string</li>
 *     <li>{@link #split(String, char)} - A list of strings from the given string split on the given character</li>
 *     <li>{@link #splitOnPattern(String, String)} - A list of strings from the given string split on the given regular expression</li>
 *     <li>{@link #stringList(Collection)} - A string list of the given collection of strings</li>
 *     <li>{@link #stringList(Iterable)} - A string list of the given strings</li>
 *     <li>{@link #stringList(Maximum, Collection)} - A list of the strings in the given collection</li>
 *     <li>{@link #stringList(Maximum, Iterable)} - A list of the strings returned by the given iterable</li>
 *     <li>{@link #stringList(Maximum, String...)} - A string list of the given strings</li>
 *     <li>{@link #stringList(String...)} - A string list for the given strings</li>
 *     <li>{@link #words(String)} - A list of the whitespace-separated words in the given string</li>
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
 *     <li>{@link #asVariableMap()}</li>
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
 *     <li>{@link #println()} - Prints this list, separated by newlines</li>
 * </ul>
 */
@SuppressWarnings("unused") @UmlClassDiagram(diagram = DiagramString.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_INSUFFICIENT,
             documentation = DOCUMENTED)
public class StringList extends ObjectList<String>
{
    /**
     * Returns a list of the lines in the given text
     *
     * @param text The text
     */
    public static StringList lines(String text)
    {
        return split(text, "\n");
    }

    /**
     * Returns a string list of the given text repeated the given number of times
     */
    public static StringList repeat(String text, int times)
    {
        var list = new StringList();
        for (var i = 0; i < times; i++)
        {
            list.add(text);
        }
        return list;
    }

    /**
     * Returns the list of strings resulting from splitting the given string on a delimiter character. The
     * {@link StringList} is unbounded for backwards compatibility.
     */
    public static StringList split(String string, char delimiter)
    {
        return split(MAXIMUM, string, delimiter);
    }

    /**
     * Returns the list of strings resulting from splitting the given string on a delimiter string. The
     * {@link StringList} is unbounded for backwards compatibility.
     */
    public static StringList split(String string, String delimiter)
    {
        return split(MAXIMUM, string, delimiter);
    }

    /**
     * Returns a string list of the given maximum size from the given text split on the given delimiter
     */
    public static StringList split(Maximum maximumSize, String string, char delimiter)
    {
        if (string == null)
        {
            return new StringList(maximumSize);
        }
        var strings = new StringList(maximumSize);
        var pos = 0;
        while (true)
        {
            var next = string.indexOf(delimiter, pos);
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
     * Returns a string list of the given maximum size from the given text split on the given delimiter
     */
    public static StringList split(Maximum maximumSize, String text, String delimiter)
    {
        if (text == null)
        {
            return new StringList(maximumSize);
        }
        var strings = new StringList(maximumSize);
        var delimiterLength = delimiter.length();
        var pos = 0;
        while (true)
        {
            var next = text.indexOf(delimiter, pos);
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
     * Returns a string list split from the given text using a regular expression pattern
     */
    public static StringList splitOnPattern(String text, String pattern)
    {
        var list = new StringList();
        list.addAll(text.split(pattern));
        return list;
    }

    /**
     * Returns a list of strings from the given iterable
     */
    public static <T> StringList stringList(Maximum maximumSize, Collection<T> values)
    {
        return new StringList(maximumSize, values);
    }

    /**
     * Returns a list of strings from the given iterable
     */
    public static <T> StringList stringList(Maximum maximumSize, Iterable<T> values)
    {
        var list = new StringList(maximumSize);
        for (var value : values)
        {
            list.addIfNotNull(value.toString());
        }
        return list;
    }

    /**
     * Returns a list of strings from the given iterable
     */
    public static <T> StringList stringList(Collection<T> values)
    {
        return new StringList(values);
    }

    /**
     * Returns a list of strings from the given iterable
     */
    public static <T> StringList stringList(Iterable<T> values)
    {
        var list = new StringList();
        for (var value : values)
        {
            list.addIfNotNull(value.toString());
        }
        return list;
    }

    /**
     * Returns the given list of objects with a maximum size
     */
    public static StringList stringList(Maximum maximumSize, String... strings)
    {
        var list = new StringList(maximumSize);
        list.addAll(Arrays.asList(strings));
        return list;
    }

    /**
     * Returns the given list of objects
     */
    public static StringList stringList(String... strings)
    {
        return stringList(MAXIMUM, strings);
    }

    /**
     * Returns a list of words from the given text with word breaks occurring on whitespace
     */
    public static StringList words(String text)
    {
        var list = new StringList();

        var startOfWord = -1;
        var length = text.length();
        for (int at = 0; at < length; at++)
        {
            switch (text.charAt(at))
            {
                case ' ', '\t', '\n' ->
                {
                    if (startOfWord >= 0)
                    {
                        list.add(text.substring(startOfWord, at));
                        startOfWord = -1;
                    }
                }

                default ->
                {
                    if (startOfWord < 0)
                    {
                        startOfWord = at;
                    }
                }
            }
        }

        if (startOfWord >= 0)
        {
            list.add(text.substring(startOfWord));
        }

        return list;
    }

    public StringList()
    {
        this(MAXIMUM);
    }

    public StringList(Iterable<?> iterable)
    {
        this(MAXIMUM, iterable);
    }

    public StringList(Collection<String> collection)
    {
        super(collection);
    }

    public StringList(Iterator<?> iterator)
    {
        this(MAXIMUM, iterator);
    }

    public StringList(Maximum maximumSize)
    {
        super(maximumSize);
    }

    public StringList(Maximum maximumSize, Iterable<?> iterable)
    {
        super(maximumSize);
        for (Object object : iterable)
        {
            add(objectToString(object));
        }
    }

    public StringList(Maximum maximumSize, Iterator<?> iterator)
    {
        super(maximumSize);
        while (iterator.hasNext())
        {
            add(objectToString(iterator.next()));
        }
    }

    public StringList(Maximum maximumSize, String... strings)
    {
        super(maximumSize);
        addAll(Arrays.asList(strings));
    }

    /**
     * Returns adds the given formatted message to this string list
     */
    public StringList add(String message, Object... arguments)
    {
        add(format(message, arguments));
        return this;
    }

    public StringList addObject(Object object)
    {
        add(object.toString());
        return this;
    }

    @Override
    public StringList appending(Iterable<? extends String> values)
    {
        return (StringList) super.appending(values);
    }

    @Override
    public StringList appending(String value)
    {
        return (StringList) super.appending(value);
    }

    @Override
    public StringList appendingIfNotNull(String value)
    {
        return (StringList) super.appendingIfNotNull(value);
    }

    public String[] asStringArray()
    {
        return toArray(new String[size()]);
    }

    /**
     * Returns this list of strings as a variable map where the even elements are keys and the odd elements are values.
     */
    public VariableMap<String> asVariableMap()
    {
        var variables = new VariableMap<String>();
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
     * Returns this list with braces around it indented by 4 spaces
     */
    public StringList bracketed()
    {
        return copy()
            .prepending("{")
            .appending("}");
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
     * Returns this string list with each element in double quotes
     */
    public StringList doubleQuoted()
    {
        var quoted = new StringList(maximumSize());
        for (var value : this)
        {
            quoted.add("\"" + value + "\"");
        }
        return quoted;
    }

    /**
     * Returns this string list alternated with blank lines
     */
    public StringList doubleSpaced()
    {
        var doubleSpaced = stringList();
        for (var at : this)
        {
            if (doubleSpaced.isNonEmpty())
            {
                doubleSpaced.add("");
            }
            doubleSpaced.add(at);
        }
        return doubleSpaced;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringList first(Count count)
    {
        return (StringList) super.first(count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringList first(int count)
    {
        return (StringList) super.first(count);
    }

    /**
     * Returns this string list indented the given number of spaces
     */
    public StringList indented(int spaces)
    {
        return indented(spaces, " ");
    }

    /**
     * Returns this string list indented the given number of indent strings
     */
    public StringList indented(int spaces, String indent)
    {
        var copy = new StringList();
        var i = 0;
        for (var string : this)
        {
            copy.add(AsciiArt.repeat(spaces, indent) + string);
        }
        return copy;
    }

    /**
     * Returns true if all elements of this string list are blank
     */
    public boolean isBlank()
    {
        for (var at : this)
        {
            if (!at.isBlank())
            {
                return false;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringList leftOf(int index)
    {
        return (StringList) super.leftOf(index);
    }

    /**
     * Returns the length of the longest string in this list
     */
    public Count longest()
    {
        int count = 0;
        for (var at : this)
        {
            count = max(at.length(), count);
        }
        return Count.count(count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <To> ObjectList<To> map(Function<String, To> mapper)
    {
        return super.map(mapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringList matching(Matcher<String> matcher)
    {
        return (StringList) super.matching(matcher);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringList maybeReversed(boolean reverse)
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
     * Returns this list with all elements numbered starting at 1
     */
    @Override
    public StringList numbered()
    {
        var numbered = newInstance();
        var number = 1;
        for (var value : this)
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
     * Returns this string list with each element prefixed with the given prefix
     */
    public StringList prefixedWith(String prefix)
    {
        var prefixed = newInstance();
        for (var string : this)
        {
            prefixed.add(prefix + string);
        }
        return prefixed;
    }

    @Override
    public StringList prepending(String value)
    {
        return (StringList) super.prepending(value);
    }

    @Override
    public StringList prepending(Iterable<? extends String> values)
    {
        return (StringList) super.prepending(values);
    }

    @Override
    public StringList prependingIfNotNull(String value)
    {
        return (StringList) super.prependingIfNotNull(value);
    }

    /**
     * Prints the values in this string list to the console, separated by newlines
     */
    public StringList println()
    {
        console().println(join("\n"));
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
    public StringList rightOf(int index)
    {
        return (StringList) super.rightOf(index);
    }

    @Override
    public StringList separator(String separator)
    {
        return (StringList) super.separator(separator);
    }

    /**
     * Returns this string list with each element in single quotes
     */
    public StringList singleQuoted()
    {
        var quoted = newInstance();
        for (var value : this)
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
    public StringList sorted(Comparator<String> comparator)
    {
        return (StringList) super.sorted(comparator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringList subList(int start, int end)
    {
        return (StringList) super.subList(start, end);
    }

    @Override
    public StringList tail()
    {
        return stringList(super.tail());
    }

    /**
     * Removes all leading and trailing blank lines
     */
    public StringList trim()
    {
        var trimmed = copy();
        while (trimmed.first().isBlank())
        {
            trimmed = trimmed.tail();
        }
        while (trimmed.last().isBlank())
        {
            trimmed = trimmed.first(trimmed.size() - 1);
        }
        return trimmed;
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
    public StringList with(Iterable<String> value)
    {
        return (StringList) super.with(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringList with(String value)
    {
        return (StringList) super.with(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringList with(Collection<String> value)
    {
        return (StringList) super.with(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringList with(String... value)
    {
        return (StringList) super.with(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringList without(Matcher<String> matcher)
    {
        return (StringList) super.without(matcher);
    }

    @Override
    public StringList without(Collection<String> that)
    {
        return (StringList) super.without(that);
    }

    @Override
    public StringList without(String s)
    {
        return (StringList) super.without(s);
    }

    @Override
    public StringList without(String[] that)
    {
        return (StringList) super.without(that);
    }

    protected String objectToString(Object object)
    {
        return StringConversions.toHumanizedString(object);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected StringList onNewList()
    {
        return new StringList();
    }
}
