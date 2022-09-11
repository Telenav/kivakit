package com.telenav.kivakit.core.logging.logs.text.formatters;

import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.logging.logs.text.LogFormatter;
import com.telenav.kivakit.core.string.AsciiArt;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.core.vm.JavaVirtualMachine;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseColumnarFormatter implements LogFormatter
{
    private static final String KIVAKIT_LOG_FORMATTER = JavaVirtualMachine.local().variables().get("KIVAKIT_LOG_FORMATTER");

    public static LogFormatter get()
    {
        return "Wide".equalsIgnoreCase(KIVAKIT_LOG_FORMATTER)
                ? WideLogFormatter.INSTANCE
                : NarrowLogFormatter.INSTANCE;
    }

    /**
     * Layout rule to use when lines are too wide for the column they are in
     */
    @LexakaiJavadoc(complete = true)
    protected enum Layout
    {
        WRAP,
        CLIP_RIGHT,
        CLIP_LEFT
    }

    protected static class Column
    {
        private final Layout layout;

        private final int maximumWidth;

        private int width;

        protected Column(int minimumWidth, int maximumWidth, Layout layout)
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
    protected static class LineOutput
    {
        private final List<Column> columns = new ArrayList<>();

        private int maximumRows = 1;

        private final List<StringList> output = new ArrayList<>();

        protected void add(Column column, String value)
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

        protected String format()
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
}
