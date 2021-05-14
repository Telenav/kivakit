////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.kernel.language.strings.conversion;

import com.telenav.kivakit.kernel.language.collections.set.Sets;
import com.telenav.kivakit.kernel.language.reflection.Type;
import com.telenav.kivakit.kernel.language.reflection.property.Property;
import com.telenav.kivakit.kernel.language.reflection.property.PropertyFilter;
import com.telenav.kivakit.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.reflection.property.filters.KivaKitProperties;
import com.telenav.kivakit.kernel.language.strings.CaseFormat;
import com.telenav.kivakit.kernel.language.strings.StringTo;
import com.telenav.kivakit.kernel.language.strings.formatting.IndentingStringBuilder;
import com.telenav.kivakit.kernel.language.strings.formatting.IndentingStringBuilder.Style;
import com.telenav.kivakit.kernel.language.values.count.Maximum;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageString;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static com.telenav.kivakit.kernel.language.strings.formatting.IndentingStringBuilder.Indentation;

/**
 * Holds state information during an object tree traversal by {@link AsIndentedString}.
 * <p>
 * The {@link #add(String, Object...)} method will add a new line at the current indentation level. The {@link
 * #label(String)} method will add a label on a line by itself and {@link #labeled(String, Object)} will add a label and
 * the given object as a string on the same line.
 * <p>
 * The current indentation level is returned by {@link #indentationLevel()}. An indented block can be added with {@link
 * #indented(String, Runnable)} or {@link #indented(Runnable)}. A list of elements can be added inside a bracketed
 * indented block with {@link #bracketed(Iterable, Consumer)}.
 * <p>
 * Recursion can be controlled with {@link #haveVisited(Object)}, {@link #isLeaf(Object)} and {@link
 * #canExplore(Object)}.
 *
 * @author jonathanl (shibo)
 * @see AsIndentedString
 * @see AsString
 * @see IndentingStringBuilder
 * @see KivaKitIncludeProperty
 * @see Property
 * @see PropertyFilter
 * @see Type
 */
@UmlClassDiagram(diagram = DiagramLanguageString.class)
public class AsStringIndenter
{
    private final StringFormat format;

    /** Property filter to use to determine which properties and fields to include */
    private final PropertyFilter filter;

    /** String indenter */
    private final IndentingStringBuilder indenter;

    /** Objects we have visited */
    private final Set<Object> visited = Sets.identitySet();

    /** The maximum number of levels of recursion allowed */
    private Maximum levels = Maximum._8;

    /** Leaf classes that we should not explore further */
    private final Set<Class<?>> leaves = new HashSet<>();

    /**
     * By default, an indenter includes all properties and fields explicitly marked with {@link KivaKitIncludeProperty}
     */
    public AsStringIndenter(final StringFormat format)
    {
        this(format, 0, KivaKitProperties.INCLUDED_PROPERTIES_AND_FIELDS);
    }

    /**
     * By default, an indenter includes all properties and fields explicitly marked with {@link KivaKitIncludeProperty}
     */
    public AsStringIndenter(final StringFormat format, final int level)
    {
        this(format, level, KivaKitProperties.INCLUDED_PROPERTIES_AND_FIELDS);
    }

    /**
     * @param filter The filter to determine what properties to include
     */
    public AsStringIndenter(final StringFormat format, final int level, final PropertyFilter filter)
    {
        this.format = format;
        this.filter = filter;

        if (format == StringFormat.HTML)
        {
            indenter = new IndentingStringBuilder(Style.HTML, Indentation.of(12));
        }
        else
        {
            indenter = new IndentingStringBuilder(Style.TEXT, Indentation.of(2));
        }

        indenter.level(level);
    }

    /**
     * Adds the given line to the traversal
     */
    public void add(final String line, final Object... arguments)
    {
        indenter.appendLine(Message.format(line, arguments));
    }

    /**
     * Recursively formats the given object, including nested {@link Collection}s, fields and methods marked with {@link
     * KivaKitIncludeProperty} and sub-objects implementing {@link AsIndentedString}.
     */
    public AsStringIndenter asString(final Object object)
    {
        if (haveVisited(object))
        {
            return this;
        }

        visited(object);

        // If we have a valid object
        if (object != null)
        {
            // but we can't explore it,
            if (!canExplore(object))
            {
                // just add a simple string for the object
                add(toString(object));
            }
            // otherwise, if it's a collection,
            else if (object instanceof Collection)
            {
                // add the elements in brackets
                final var collection = (Collection<?>) object;
                bracketed(collection, object1 -> asString(format));
            }
            // otherwise, if it's an AsIndentedString and we're recursing (to avoid infinite recursion),
            else if (object instanceof AsIndentedString && indentationLevel() > 1 && canExplore(object))
            {
                // format the sub-object using AsIndentedString.toDebugString,
                ((AsIndentedString) object).asString(format, this);
            }
            else
            {
                // and last of all, if we just have a vanilla object, loop through the properties of the object,
                final var type = Type.of(object);
                final var properties = type.properties(filter);
                if (properties.isEmpty())
                {
                    labeled(CaseFormat.camelCaseToHyphenated(type.name()), toString(object));
                }
                else
                {
                    for (final var property : properties)
                    {
                        // get the property value
                        final var hyphenated = CaseFormat.camelCaseToHyphenated(property.name());
                        final var value = property.get(object);
                        if (value != null)
                        {
                            // and if it's an AsIndentedString,
                            if (value instanceof AsIndentedString && canExplore(value))
                            {
                                // recurse to add the indented property
                                indented(hyphenated, () -> asString(value));
                            }
                            else
                            {
                                // otherwise, just add the value with toString,
                                labeled(hyphenated, toString(value));
                            }
                        }
                        else
                        {
                            // or if there was no value, label it as not available.
                            labeled(hyphenated, "N/A");
                        }
                    }
                }
            }
        }
        return this;
    }

    /**
     * Calls the consumer for each element int the given collection within indented curly brackets
     */
    public AsStringIndenter bracketed(final Iterable<?> iterable, final Consumer<Object> consumer)
    {
        text("{");
        indented(() ->
                {
                    for (final var at : iterable)
                    {
                        consumer.accept(at);
                    }
                }
        );
        text("}");
        return this;
    }

    /**
     * @return True if it is allowable to recurse on the given object
     */
    public boolean canExplore(final Object value)
    {
        return !isLeaf(value) && indentationLevel() < levels.asInt();
    }

    /**
     * @return True if the given object has already been visited
     */
    public boolean haveVisited(final Object object)
    {
        return visited.contains(object);
    }

    public int indentationLevel()
    {
        return indenter.indentationLevel();
    }

    /**
     * Adds the given label and then indents what is in the code block
     */
    public AsStringIndenter indented(final String label, final Runnable code)
    {
        label(label);
        indented(code);
        return this;
    }

    /**
     * Increases the indent level, executes the given code and then decreases the indent level again
     */
    public AsStringIndenter indented(final Runnable code)
    {
        try
        {
            indenter.indent();
            code.run();
        }
        finally
        {
            indenter.unindent();
        }
        return this;
    }

    public boolean isLeaf(final Object object)
    {
        if (indentationLevel() > 0)
        {
            for (final var at : leaves)
            {
                if (at.isAssignableFrom(object.getClass()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Adds the given label on a line by itself
     */
    public AsStringIndenter label(final String label)
    {
        return text(label + ":");
    }

    /**
     * Adds a one-line labeled object
     */
    public AsStringIndenter labeled(final String label, final Object object)
    {
        if (format.isHtml())
        {
            add("<span class='label'><b>" + label + ":</b></span> <span class='value'>" + toString(object) + "</span>");
        }
        else
        {
            add(label + ": " + toString(object));
        }
        return this;
    }

    /**
     * @param levels The maximum number of levels of recursion allowed
     */
    public AsStringIndenter levels(final Maximum levels)
    {
        this.levels = levels;
        return this;
    }

    /**
     * Designates that given class as a leaf which should not be explored further
     */
    public AsStringIndenter pruneAt(final Class<?> leaf)
    {
        leaves.add(leaf);
        return this;
    }

    /**
     * Adds the given label on a line by itself
     */
    public AsStringIndenter text(final String label)
    {
        if (format.isHtml())
        {
            if (indenter.indentationLevel() == 0)
            {
                add("<p class='section0'>$</p>", label);
            }
            else
            {
                add("<span class='section$'>$</span>", indenter.indentationLevel(), label);
            }
        }
        else
        {
            add(label);
        }
        return this;
    }

    /**
     * @return The indented debug string
     */
    @Override
    public String toString()
    {
        return indenter.toString();
    }

    /**
     * Marks the given object as visited
     */
    public void visited(final Object object)
    {
        visited.add(object);
    }

    private String toString(final Object object)
    {
        if (object == null)
        {
            return format.isHtml() ? "<span class='not-available'>N/A</font>" : "N/A";
        }
        return StringTo.string(object);
    }
}
