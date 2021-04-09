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

package com.telenav.kivakit.data.formats.csv;

import com.telenav.kivakit.core.kernel.language.io.LookAheadReader;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.resource.Resource;
import com.telenav.kivakit.core.resource.reading.BaseObjectReader;
import com.telenav.kivakit.data.formats.csv.project.lexakai.diagrams.DiagramCsv;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;

/**
 * Parses a stream of CSV information. The rules outlined
 * <a href="http://en.wikipedia.org/wiki/Comma-separated_values">here</a> dictate valid CSV format.
 * In particular, this class can handle quoted strings, line breaks within quotes, comments, empty lines, and some of
 * the other standard formatting issues.
 *
 * <p><b>Processing CSV Lines</b></p>
 * <p>
 * A reader instance is constructed with {@link #CsvReader(Resource, CsvSchema, char, ProgressReporter)}. Since the
 * class is {@link Iterable}, lines can then be retrieved as objects with code similar to:
 *
 * <pre>
 * try (var reader = new CsvReader(resource, schema, ',', ProgressReporter.NULL))
 * {
 *     for (var line : reader)
 *     {
 *         var employee = line.asObject(Employee.class);
 *
 *         [...]
 *     }
 * }
 * </pre>
 *
 * @author jonathanl (shibo)
 * @see CsvSchema
 * @see Resource
 * @see ProgressReporter
 * @see CsvLine
 */
@UmlClassDiagram(diagram = DiagramCsv.class)
@UmlRelation(label = "reads", referent = CsvLine.class)
@LexakaiJavadoc(complete = true)
public class CsvReader extends BaseObjectReader<CsvLine>
{
    /** The separator for CSV (can be changed with setDelimiter) */
    protected char delimiter;

    /** The input */
    protected final LookAheadReader in;

    /** The reporter for progress reading the CSV resource */
    private final ProgressReporter reporter;

    /** The CSV schema for lines to read */
    @UmlAggregation(label = "uses")
    private final CsvSchema schema;

    /** Quote character */
    private char quote = '"';

    /**
     * Constructs a reader for the given input stream and CSV schema
     */
    public CsvReader(final Resource resource,
                     final CsvSchema schema,
                     final char delimiter,
                     final ProgressReporter reporter)
    {
        this.reporter = reporter;
        this.schema = schema;
        this.delimiter = delimiter;
        final var reader = resource.reader(reporter).textReader();
        if (reader == null)
        {
            throw new IllegalArgumentException("Unable to read: " + resource);
        }
        in = new LookAheadReader(reader);
    }

    /**
     * Closes the input
     */
    @Override
    public void close()
    {
        in.close();
    }

    /**
     * Sets the CSV delimiter (it's "," by default)
     */
    public void delimiter(final char delimiter)
    {
        this.delimiter = delimiter;
    }

    /**
     * @return The current line number in the input
     */
    public int lineNumber()
    {
        return in.lineNumber();
    }

    public Iterable<CsvLine> lines()
    {
        return () -> CsvReader.this;
    }

    public void quote(final char quote)
    {
        this.quote = quote;
    }

    /**
     * @return The schema for lines being read
     */
    public CsvSchema schema()
    {
        return schema;
    }

    /**
     * Skips the given number of lines
     */
    @SuppressWarnings("UnusedReturnValue")
    public CsvReader skipLines(final int numberOfLinesToSkip)
    {
        for (var i = 0; i < numberOfLinesToSkip; i++)
        {
            next();
        }
        return this;
    }

    /**
     * Read the next CSV column from the input stream. When exiting this method the input stream should either be
     * pointing to a line end (0x0D), a column end (delimiter), or the end of the stream.
     *
     * @param in The input stream to read data from pointing to the start of the CSV column.
     * @return A buffer populated with the column data or null if no more data exists.
     */
    protected String extractNextColumn(final LookAheadReader in)
    {
        final var buffer = new StringBuffer();

        // Handle leading white spaces.
        processLeadingSpaces(in, buffer);

        // Determine what type of processing is needed.
        try
        {
            if (in.current() == quote)
            {
                readQuotedColumn(in, buffer);
            }
            else
            {
                readColumn(in, buffer);
            }
        }
        catch (final Exception e)
        {
            problem(e, "Invalid CSV format");
            return null;
        }

        return in.hasNext() || buffer.length() > 0 ? buffer.toString() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected CsvLine onNext()
    {
        if (in.hasNext())
        {
            final var line = new CsvLine(schema, delimiter);
            line.addListener(this);
            final var lineNumber = in.lineNumber();
            line.lineNumber(lineNumber);
            if (lineNumber == 0)
            {
                reporter.start();
            }

            // Take care of comments.
            if (in.current() == '#' || (in.current() == '/' && in.lookAhead() == '/'))
            {
                skipLine(in);
                return findNext();
            }

            // Loop through pulling out the columns until an end of line or end
            // of file identifier is encountered.
            String column;
            while ((column = extractNextColumn(in)) != null)
            {
                line.add(column);

                if (in.atEndOfLine())
                {
                    // The end of the line has been reached, break out after
                    // advancing for the next entry.
                    in.next();
                    break;
                }
                if (in.current() == delimiter)
                {
                    in.next();
                    if (!in.hasNext())
                    {
                        // This is the case where there is a delimiter followed
                        // by the end of the file. So add an empty line and
                        // exit.
                        line.add("");
                        break;
                    }
                }
            }
            reporter.next();
            return line;
        }
        reporter.end();
        return null;
    }

    /**
     * Trims off the spaces at the beginning of a CSV column and copies them to the buffer.
     *
     * @param in The input stream to read from pointing to the start of the spaces.
     * @param buffer The buffer to be populated with the spaces.
     */
    protected void processLeadingSpaces(final LookAheadReader in, final StringBuffer buffer)
    {
        while (in.hasNext() && in.current() == ' ')
        {
            buffer.append(' ');
            in.next();
        }
    }

    /**
     * Reads a standard, non-quoted, CSV column. Note that when the method exits the stream should be pointing to the
     * line terminator, 0x0A, or be exhausted.
     *
     * @param in The input stream to read from pointing to the start of the column.
     * @param buffer The buffer to be populated with the contents of the input data.
     */
    protected void readColumn(final LookAheadReader in, final StringBuffer buffer)
    {
        while (in.hasNext() && !in.atEndOfLine())
        {
            if (in.current() == delimiter)
            {
                // Just catch this case and break out of the loop, it will be
                // handled later.
                break;
            }
            else if (in.current() == quote)
            {
                throw new IllegalArgumentException("Non quoted string contains quotes at line " + lineNumber());
            }
            else
            {
                buffer.append((char) in.current());
            }
            in.next();
        }
    }

    /**
     * Reads a CSV column which begins with a quote. Note that when the method exits the stream should be pointing to
     * the line terminator, 0x0A, or be exhausted.
     *
     * @param in The input stream to read from pointing to the starting quote.
     * @param buffer The buffer to be populated with the contents of the input data.
     */
    protected void readQuotedColumn(final LookAheadReader in, final StringBuffer buffer)
    {
        boolean inQuotes;

        // Skip the first double quote
        in.next();

        // If we're starting off with two double quotes ("")
        if (in.current() == quote)
        {
            // we need to read the third character
            in.next();

            // and if it's also a quote then we have a triple quote (""")
            if (in.current() == quote)
            {
                // which is a quoted column starting with an escaped quote character!
                inQuotes = true;
                buffer.append(quote);
                in.next();
            }
            else
            {
                // If we don't have a quote, then we have only a double quote (""), so we append the
                // escaped quote and whatever the third character is
                inQuotes = false;
                buffer.append(quote);
            }
        }
        else
        {
            // We have a simple quoted column, we skip our quote
            inQuotes = true;
        }

        while (in.hasNext())
        {
            if (!inQuotes && in.atEndOfLine())
            {
                break;
            }
            else if (!inQuotes && in.current() == delimiter)
            {
                // Just catch this case and break out of the loop, it will be
                // handled later.
                break;
            }
            else if (in.current() == quote)
            {
                if (in.lookAhead() == quote)
                {
                    buffer.append(quote);
                    in.next();
                }
                else
                {
                    // Must be the end of the quotes.
                    inQuotes = false;
                }
            }
            else
            {
                buffer.append((char) in.current());
            }
            in.next();
        }

        // Ensure to see if we just exhausted the stream without ever closing the
        // column quote.
        if (inQuotes)
        {
            throw new IllegalArgumentException(
                    "Quoted column never closed with matching quote at line " + lineNumber());
        }
    }

    /**
     * Advances the input stream to the next line. This method can be used when comments, delimited by '#' are
     * encountered.
     *
     * @param in The input stream to be advanced.
     */
    protected void skipLine(final LookAheadReader in)
    {
        while (in.hasNext())
        {
            if (in.atEndOfLine())
            {
                in.next();
                break;
            }
            in.next();
        }
    }
}
