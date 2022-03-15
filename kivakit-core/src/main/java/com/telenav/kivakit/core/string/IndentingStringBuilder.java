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

import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.lexakai.DiagramString;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.List;

import static com.telenav.kivakit.core.string.IndentingStringBuilder.Style.HTML;
import static com.telenav.kivakit.core.string.IndentingStringBuilder.Style.TEXT;

/**
 * Builds a string with indentation levels. The constructor {@link #IndentingStringBuilder(Style, Indentation)} takes
 * the number of spaces per indentation level. Strings can be added to the builder with {@link #appendLine(String)} and
 * {@link #appendLines(String)}. When {@link #indent()} is called the indentation level is increased and when {@link
 * #unindent()} is called the indentation level is decreased. The indentation level can be retrieved with {@link
 * #indentationLevel()} and {@link #isIndented()}, and it can be set with {@link #level(int)}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramString.class)
public class IndentingStringBuilder
{
    @SuppressWarnings("SpellCheckingInspection")
    public static IndentingStringBuilder defaultTextIndenter()
    {
        return new IndentingStringBuilder(TEXT, new Indentation(4));
    }

    public enum Style
    {
        TEXT,
        HTML
    }

    public static class Indentation
    {
        public static Indentation of(int spaces)
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

    private final List<String> lines = new ArrayList<>();

    private final Style style;

    private final Indentation indentation;

    private int level;

    public IndentingStringBuilder()
    {
        this(TEXT, Indentation.of(4));
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

    public IndentingStringBuilder appendLine(String value)
    {
        lines.add(AsciiArt.repeat(level * indentation.asInt(), " ") + value);
        return this;
    }

    public IndentingStringBuilder appendLines(String value)
    {
        for (var line : Split.split(value, "\n"))
        {
            appendLine(line);
        }
        return this;
    }

    public boolean containsLine(String line)
    {
        return lines.contains(line);
    }

    public IndentingStringBuilder indent()
    {
        level++;
        return this;
    }

    public int indentationLevel()
    {
        return level;
    }

    public boolean isIndented()
    {
        return level > 0;
    }

    public IndentingStringBuilder level(int level)
    {
        Ensure.ensure(level >= 0);
        this.level = level;
        return this;
    }

    public Count lines()
    {
        return Count.count(lines);
    }

    public IndentingStringBuilder numbered()
    {
        for (int index = 0; index < lines.size(); index++)
        {
            lines.set(index, (index + 1) + ". " + lines.get(index));
        }
        return this;
    }

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
        return Join.join(lines, style == HTML ? "<br/>" : "\n");
    }

    @SuppressWarnings("SpellCheckingInspection")
    public IndentingStringBuilder unindent()
    {
        Ensure.ensure(level >= 1);
        level--;
        return this;
    }
}
