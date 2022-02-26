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

package com.telenav.kivakit.kernel.logging.logs.text.formatters;

import com.telenav.kivakit.interfaces.string.Stringable;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.strings.AsciiArt;
import com.telenav.kivakit.kernel.language.strings.Strings;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.language.values.count.Maximum;
import com.telenav.kivakit.kernel.logging.LogEntry;
import com.telenav.kivakit.kernel.logging.logs.text.LogFormatter;
import com.telenav.kivakit.kernel.messaging.messages.MessageFormatter;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLoggingLogs;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.List;

/**
 * Formats log entries into flexible delimited columns.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLoggingLogs.class)
public class ColumnarLogFormatter implements LogFormatter
{
    public static final ColumnarLogFormatter DEFAULT = new ColumnarLogFormatter();

    private static final boolean DURATION = true;

    /**
     * Layout rule to use when lines are too wide for the column they are in
     */
    @LexakaiJavadoc(complete = true)
    enum Layout
    {
        WRAP,
        CLIP_RIGHT,
        CLIP_LEFT
    }

    private static class Column
    {
        private final Layout layout;

        private final int maximumWidth;

        private int width;

        private Column(int minimumWidth, int maximumWidth, Layout layout)
        {
            this.maximumWidth = maximumWidth;
            width = minimumWidth;
            this.layout = layout;
        }

        private StringList format(String value)
        {
            var rows = new StringList();
            for (var line : StringList.split(value, "\n"))
            {
                rows.addAll(formatLine(line));
            }
            return rows;
        }

        private StringList formatLine(String value)
        {
            var rows = new StringList();
            while (!"".equals(value))
            {
                var width = value.length();
                if (width < this.width)
                {
                    rows.add(value + AsciiArt.repeat(this.width - width, ' '));
                    value = "";
                }
                else
                {
                    if (width < maximumWidth)
                    {
                        this.width = width;
                        rows.add(value);
                        value = "";
                    }
                    else
                    {
                        this.width = maximumWidth;
                        switch (layout)
                        {
                            case CLIP_LEFT:
                                rows.add(Strings.trailing(value, this.width));
                                value = "";
                                break;

                            case CLIP_RIGHT:
                                rows.add(value.substring(0, this.width));
                                value = "";
                                break;

                            case WRAP:
                                rows.add(value.substring(0, this.width));
                                value = value.substring(this.width);
                                break;
                        }
                    }
                }
            }
            return rows;
        }
    }

    /**
     * The output for a line as it is populated by column
     */
    @LexakaiJavadoc(complete = true)
    private static class LineOutput
    {
        private final List<Column> columns = new ArrayList<>();

        private int maximumRows = 1;

        private final List<StringList> output = new ArrayList<>();

        private void add(Column column, String value)
        {
            columns.add(column);
            var rows = column.format(value);
            if (value.contains("${nowrap}"))
            {
                output.add(new StringList(Maximum._1, Strings.replaceAll(rows.join(""), "${nowrap}", "")));
            }
            else
            {
                output.add(rows);
                if (rows.size() > maximumRows)
                {
                    maximumRows = rows.size();
                }
            }
        }

        private String format()
        {
            var builder = new StringBuilder();
            for (var row = 0; row < maximumRows; row++)
            {
                builder.append("| ");
                for (var column = 0; column < columns.size(); column++)
                {
                    if (row < output.get(column).size())
                    {
                        builder.append(output.get(column).get(row));
                    }
                    else
                    {
                        builder.append(AsciiArt.repeat(columns.get(column).width, ' '));
                    }
                    builder.append(" |");
                    if (column < columns.size() - 1)
                    {
                        builder.append(' ');
                    }
                }
                if (row < maximumRows - 1)
                {
                    builder.append("\n");
                }
            }
            return builder.toString();
        }
    }

    private final Column contextColumn = new Column(30, 30, Layout.CLIP_RIGHT);

    private final Column durationColumn = new Column(6, 10, Layout.CLIP_RIGHT);

    private final Column messageColumn = new Column(150, 150, Layout.WRAP);

    private final Column sequenceNumberColumn = new Column(6, 10, Layout.CLIP_RIGHT);

    private final Time start = Time.now();

    private final Column threadColumn = new Column(16, 24, Layout.CLIP_LEFT);

    private final Column timeColumn = new Column(21, 21, Layout.CLIP_RIGHT);

    private final Column typeColumn = new Column(12, 20, Layout.CLIP_RIGHT);

    @Override
    public String format(LogEntry entry, MessageFormatter.Format format)
    {
        // Create line output
        var line = new LineOutput();

        // Add each column and its content
        var message = entry.message();
        line.add(sequenceNumberColumn, String.valueOf(entry.sequenceNumber()));
        if (DURATION)
        {
            line.add(durationColumn, start.elapsedSince().asString(Stringable.Format.USER_LABEL));
        }
        else
        {
            line.add(timeColumn, message.created().utc().toString());
        }
        line.add(threadColumn, entry.threadName());
        line.add(contextColumn, entry.context().typeName());
        line.add(typeColumn, entry.messageType());
        line.add(messageColumn, entry.formattedMessage(format));

        // Return the formatted line
        return line.format();
    }
}
