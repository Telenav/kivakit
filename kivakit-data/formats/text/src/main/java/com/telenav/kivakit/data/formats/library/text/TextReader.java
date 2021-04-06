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

package com.telenav.kivakit.data.formats.library.text;

import com.telenav.kivakit.core.kernel.language.collections.map.string.NameMap;
import com.telenav.kivakit.core.resource.reading.BaseObjectReader;
import com.telenav.kivakit.data.formats.library.text.project.lexakai.diagrams.DiagramText;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Parses a stream of text lines.
 *
 * @author jonathanl (shibo)
 * @author ericg
 */
@UmlClassDiagram(diagram = DiagramText.class)
@UmlRelation(label = "reads", referent = TextLine.class)
public class TextReader extends BaseObjectReader<TextLine>
{
    @UmlAggregation
    private final NameMap<TextColumn> columnForName;

    @UmlAggregation
    private final LineNumberReader in;

    @UmlAggregation
    private final Pattern pattern;

    /**
     * @param in The input to read from
     * @param columns The list of columns in each line
     * @param pattern The pattern to match against each line to produce columns, where the column indexes are represent
     * the zero-based index of the corresponding pattern group (which are normally one-based)
     */
    public TextReader(final Reader in, final List<TextColumn> columns, final Pattern pattern)
    {
        columnForName = new NameMap<>();
        this.pattern = pattern;
        this.in = new LineNumberReader(in);
        for (final var column : columns)
        {
            columnForName.put(column.name(), column);
        }
    }

    @Override
    public void close()
    {
        try
        {
            in.close();
        }
        catch (final IOException e)
        {
            throw new IllegalStateException("Couldn't close input", e);
        }
    }

    public TextColumn columnForName(final String name)
    {
        return columnForName.get(name);
    }

    public void skipLines(final int numberOfLinesToSkip)
    {
        for (var i = 0; i < numberOfLinesToSkip; i++)
        {
            // Don't use next() to advance because that will result in the
            // framework trying to actually parse the line. Instead just advance
            // the reader.
            try
            {
                in.readLine();
            }
            catch (final IOException e)
            {
                throw new IllegalStateException("Unable to advance feed", e);
            }
        }
    }

    @Override
    protected TextLine onNext()
    {
        try
        {
            final var text = in.readLine();
            if (text != null)
            {
                final var matcher = pattern.matcher(text);
                if (matcher.matches())
                {
                    final var line = new TextLine(this);
                    for (var i = 1; i <= matcher.groupCount(); i++)
                    {
                        line.add(matcher.group(i));
                    }
                    return line;
                }
                else
                {
                    onPatternMatchFailure();
                    return findNext();
                }
            }
        }
        catch (final IOException e)
        {
            throw new IllegalStateException("Error reading lines", e);
        }
        return null;
    }

    protected void onPatternMatchFailure()
    {
        warning("StringFormat error on line ${debug}", in.getLineNumber());
    }
}
