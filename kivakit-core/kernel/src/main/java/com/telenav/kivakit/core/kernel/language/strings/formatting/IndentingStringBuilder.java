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

package com.telenav.kivakit.core.kernel.language.strings.formatting;

import com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.core.kernel.language.strings.AsciiArt;
import com.telenav.kivakit.core.kernel.language.strings.Split;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageString;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.kernel.language.strings.formatting.IndentingStringBuilder.Style.HTML;
import static com.telenav.kivakit.core.kernel.language.strings.formatting.IndentingStringBuilder.Style.TEXT;

/**
 * Builds a string with indentation levels. The constructor {@link #IndentingStringBuilder(Style, Indentation)} takes
 * the number of spaces per indentation level. Strings can be added to the builder with {@link #appendLine(String)} and
 * {@link #appendLines(String)}. When {@link #indent()} is called the indentation level is increased and when {@link
 * #unindent()} is called the indentation level is decreased. The indentation level can be retrieved with {@link
 * #indentationLevel()} and {@link #isIndented()}, and it can be set with {@link #level(int)}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageString.class)
public class IndentingStringBuilder
{
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
        public static Indentation of(final int spaces)
        {
            return new Indentation(spaces);
        }

        private final int spaces;

        protected Indentation(final int spaces)
        {
            this.spaces = spaces;
        }

        public int asInt()
        {
            return spaces;
        }
    }

    private final StringList lines = new StringList();

    private final Style style;

    private final Indentation indentation;

    private int level;

    /**
     * @param indentation The number of spaces per indentation level
     */
    public IndentingStringBuilder(final Style style, final Indentation indentation)
    {
        this.style = style;
        this.indentation = indentation;
    }

    public void appendLine(final String value)
    {
        lines.append(AsciiArt.repeat(level * indentation.asInt(), " ") + value);
    }

    public void appendLines(final String value)
    {
        for (final var line : Split.split(value, '\n'))
        {
            appendLine(line);
        }
    }

    public boolean containsLine(final String line)
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

    public void level(final int level)
    {
        Ensure.ensure(level >= 0);
        this.level = level;
    }

    public Count lines()
    {
        return lines.count();
    }

    public void removeLastLine()
    {
        if (!lines.isEmpty())
        {
            lines.removeLast();
        }
    }

    @Override
    public String toString()
    {
        return lines.join(style == HTML ? "<br/>" : "\n");
    }

    public void unindent()
    {
        Ensure.ensure(level >= 1);
        level--;
    }
}
