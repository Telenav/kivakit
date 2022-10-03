package com.telenav.kivakit.core.logging.logs.text.formatters;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.logging.logs.text.LogFormatter;
import com.telenav.kivakit.core.string.AsciiArt;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.kivakit.core.value.count.Maximum;

import java.util.ArrayList;
import java.util.List;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_ENUM_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A columnar log formatter. {@link Column} models can be created by subclasses and added to the {@link LineOutput} with
 * {@link LineOutput#addColumnText(Column, String)}. When text has been added for all columns, the
 * {@link LineOutput#format()} method can be called to get the formatted text.
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public abstract class BaseColumnarFormatter implements LogFormatter
{
    /**
     * ColumnLayout rules to use when lines are too wide for the column they are in
     */
    @ApiQuality(stability = API_STABLE_ENUM_EXTENSIBLE,
                testing = TESTING_NOT_NEEDED,
                documentation = DOCUMENTATION_COMPLETE)
    protected enum ColumnLayout
    {
        /** Column should wrap */
        WRAP,

        /** Column should be clipped on the right */
        CLIP_RIGHT,

        /** Column should be clipped on the left */
        CLIP_LEFT
    }

    /**
     * Data structure for modeling a text column
     */
    @ApiQuality(stability = API_STABLE_EXTENSIBLE,
                testing = TESTING_NONE,
                documentation = DOCUMENTATION_COMPLETE)
    protected static class Column
    {
        /** The layout of the column */
        private final ColumnLayout layout;

        /** The maximum width of the column */
        private final int maximumWidth;

        /** The current width of the column */
        private int width;

        /**
         * Creates a column
         *
         * @param initialWidth The initial width of the column in characters
         * @param maximumWidth The maximum width of the column in characters
         * @param layout The layout rule for the column
         */
        protected Column(int initialWidth, int maximumWidth, ColumnLayout layout)
        {
            this.maximumWidth = maximumWidth;
            width = initialWidth;
            this.layout = layout;
        }

        /**
         * Returns the string as a list of lines, formatted according to the rules
         *
         * @param value The value to format
         * @return The list of lines
         */
        private StringList format(String value)
        {
            var rows = new StringList();
            for (var line : StringList.split(value, "\n"))
            {
                rows.addAll(formatLine(line));
            }
            return rows;
        }

        /**
         * Formats a single text value into a list of lines, clipping or wrapping as necessary
         *
         * @param value The value to format
         * @return The list of lines
         */
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
    @ApiQuality(stability = API_STABLE_EXTENSIBLE,
                testing = TESTING_NONE,
                documentation = DOCUMENTATION_COMPLETE)
    protected static class LineOutput
    {
        /** The column definitions */
        private final List<Column> columns = new ArrayList<>();

        /** The maximum number of rows */
        private int maximumRows = 1;

        /** The output */
        private final List<StringList> output = new ArrayList<>();

        /**
         * Adds the given value for the given column
         *
         * @param column The column
         * @param value The text value
         */
        protected void addColumnText(Column column, String value)
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
