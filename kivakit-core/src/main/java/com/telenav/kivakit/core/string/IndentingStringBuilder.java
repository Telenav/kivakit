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

package com.telenav.kivakit.core.string;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.internal.lexakai.DiagramString;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.string.AsciiArt.repeat;
import static com.telenav.kivakit.core.string.IndentingStringBuilder.Indentation.indentation;
import static com.telenav.kivakit.core.string.IndentingStringBuilder.Style.HTML;
import static com.telenav.kivakit.core.string.IndentingStringBuilder.Style.TEXT;
import static com.telenav.kivakit.core.string.Join.join;
import static com.telenav.kivakit.core.string.Split.split;
import static com.telenav.kivakit.core.value.count.Count.count;

/**
 * Builds a string with indentation levels. The constructor {@link #IndentingStringBuilder(Style, Indentation)} takes
 * the number of spaces per indentation level. Strings can be added to the builder with {@link #appendLine(String)} and
 * {@link #appendLines(String)}. When {@link #indent()} is called the indentation level is increased and when
 * {@link #unindent()} is called the indentation level is decreased. The indentation level can be retrieved with
 * {@link #indentationLevel()} and {@link #isIndented()}, and it can be set with {@link #level(int)}.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramString.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class IndentingStringBuilder
{
    /**
     * Returns a text indenter that indents by 4 spaces
     */
    public static IndentingStringBuilder defaultTextIndenter()
    {
        return new IndentingStringBuilder(TEXT, new Indentation(4));
    }

    public enum Style
    {
        TEXT,
        HTML
    }

    /**
     * A level of indentation in spaces
     */
    public static class Indentation
    {
        public static Indentation indentation(int spaces)
        {
            return new Indentation(spaces);
        }

        private final int spaces;

        protected Indentation(int spaces)
        {
            this.spaces = spaces;
        }

        public int asInt()
        {
            return spaces;
        }
    }

    /** The indented lines */
    private final StringList lines = new StringList();

    /** The style of indentation */
    private final Style style;

    /** The indentation size */
    private final Indentation indentation;

    /** The level of indentation */
    private int level;

    public IndentingStringBuilder()
    {
        this(TEXT, indentation(4));
    }

    public IndentingStringBuilder(Indentation indentation)
    {
        this(TEXT, indentation);
    }

    /**
     * @param indentation The number of spaces per indentation level
     */
    public IndentingStringBuilder(Style style, Indentation indentation)
    {
        this.style = style;
        this.indentation = indentation;
    }

    /**
     * Appends the given line to the list of indented lines
     */
    public IndentingStringBuilder appendLine(String line)
    {
        lines.add(repeat(level * indentation.asInt(), " ") + line);
        return this;
    }

    /**
     * Appends the given lines to the list of indented lines
     */
    public IndentingStringBuilder appendLines(String lines)
    {
        for (var line : split(lines, "\n"))
        {
            appendLine(line);
        }
        return this;
    }

    public boolean containsLine(String line)
    {
        return lines.contains(line);
    }

    /**
     * Increases the indent level
     */
    public IndentingStringBuilder indent()
    {
        level++;
        return this;
    }

    /**
     * The current indentation level
     */
    public int indentationLevel()
    {
        return level;
    }

    /**
     * Returns true if we are indented
     */
    public boolean isIndented()
    {
        return level > 0;
    }

    /**
     * Sets the level of indentation
     */
    public IndentingStringBuilder level(int level)
    {
        ensure(level >= 0);
        this.level = level;
        return this;
    }

    /**
     * Returns the number of indented lines
     */
    public Count lineCount()
    {
        return count(lines);
    }

    /**
     * The indented lines as a string list
     */
    public StringList lines()
    {
        return lines;
    }

    /**
     * Numbers the indented lines
     */
    public IndentingStringBuilder numbered()
    {
        for (int index = 0; index < lines.size(); index++)
        {
            lines.set(index, (index + 1) + ". " + lines.get(index));
        }
        return this;
    }

    /**
     * Removes the last line
     */
    public IndentingStringBuilder removeLastLine()
    {
        if (!lines.isEmpty())
        {
            lines.remove(lines.size() - 1);
        }
        return this;
    }

    @Override
    public String toString()
    {
        return join(lines, style == HTML ? "<br/>" : "\n");
    }

    /**
     * Decreases the indent level
     */
    @SuppressWarnings("SpellCheckingInspection")
    public IndentingStringBuilder unindent()
    {
        ensure(level >= 1);
        level--;
        return this;
    }
}
