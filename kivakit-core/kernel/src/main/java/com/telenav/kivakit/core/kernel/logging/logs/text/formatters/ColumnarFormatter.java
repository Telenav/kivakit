////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.logging.logs.text.formatters;

import com.telenav.kivakit.core.kernel.logging.LogEntry;
import com.telenav.kivakit.core.kernel.logging.logs.text.LogEntryFormatter;
import com.telenav.kivakit.core.kernel.messaging.messages.MessageFormatter;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;
import com.telenav.kivakit.core.kernel.language.strings.AsciiArt;
import com.telenav.kivakit.core.kernel.language.strings.Strings;
import com.telenav.kivakit.core.kernel.language.strings.conversion.StringFormat;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLoggingLogs;

import java.util.ArrayList;
import java.util.List;

/**
 * Formats log entries into flexible delimited columns.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLoggingLogs.class)
public class ColumnarFormatter implements LogEntryFormatter
{
    public static final ColumnarFormatter DEFAULT = new ColumnarFormatter();

    private static final boolean DURATION = true;

    enum Layout
    {
        WRAP,
        CLIP_RIGHT,
        CLIP_LEFT
    }

    private static class Column
    {
        private final int maximumWidth;

        private int width;

        private final Layout layout;

        private Column(final int minimumWidth, final int maximumWidth, final Layout layout)
        {
            this.maximumWidth = maximumWidth;
            width = minimumWidth;
            this.layout = layout;
        }

        private StringList format(final String value)
        {
            final var rows = new StringList();
            for (final var line : StringList.split(value, "\n"))
            {
                rows.addAll(formatLine(line));
            }
            return rows;
        }

        private StringList formatLine(String value)
        {
            final var rows = new StringList();
            while (!"".equals(value))
            {
                final var width = value.length();
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

    private static class LineOutput
    {
        private int maximumRows = 1;

        private final List<Column> columns = new ArrayList<>();

        private final List<StringList> output = new ArrayList<>();

        private void add(final Column column, final String value)
        {
            columns.add(column);
            final var rows = column.format(value);
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
            final var builder = new StringBuilder();
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

    private final Column sequenceNumberColumn = new Column(6, 10, Layout.CLIP_RIGHT);

    private final Column timeColumn = new Column(21, 21, Layout.CLIP_RIGHT);

    private final Column durationColumn = new Column(6, 10, Layout.CLIP_RIGHT);

    private final Column typeColumn = new Column(12, 20, Layout.CLIP_RIGHT);

    private final Column threadColumn = new Column(16, 24, Layout.CLIP_LEFT);

    private final Column contextColumn = new Column(30, 30, Layout.CLIP_RIGHT);

    private final Column messageColumn = new Column(150, 150, Layout.WRAP);

    private final Time start = Time.now();

    @Override
    public String format(final LogEntry entry, final MessageFormatter.Format format)
    {
        // Create line output
        final var line = new LineOutput();

        // Add each column and its content
        final var message = entry.message();
        line.add(sequenceNumberColumn, String.valueOf(entry.sequenceNumber()));
        if (DURATION)
        {
            line.add(durationColumn, start.elapsedSince().asString(StringFormat.USER_LABEL));
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
