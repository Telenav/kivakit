////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.formats.csv;

import com.telenav.kivakit.core.kernel.language.io.LookAheadReader;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.resource.Resource;
import com.telenav.kivakit.data.formats.project.lexakai.diagrams.DiagramCsv;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Parses a stream of simplified CSV information. This class does not handle quoted entries, so the records cannot
 * contain the delimiter. The records also cannot embed a newline. This class is useful for handling CSVs that do not
 * use CSV quote rules for records. Telenav TXD format files, for example, are built this way. It is similar to and
 * derived from class CsvReader except for the lack of double quote handling.
 *
 * @author jonathanl (shibo)
 * @author ericg
 * @author roberts
 */
@UmlClassDiagram(diagram = DiagramCsv.class)
public class UnquotedCsvReader extends CsvReader
{
    public UnquotedCsvReader(final Resource resource, final ProgressReporter reporter, final CsvSchema schema,
                             final char delimiter)
    {
        super(resource, reporter, schema, delimiter);
    }

    /**
     * Read the next CSV column from the input stream. When exiting this method the input stream should either be
     * pointing to a line end (0x0D), a column end (delimiter), or the end of the stream. This version ignores double
     * quotes, so the records cannot embed the delimiter.
     *
     * @param in The input stream to read data from pointing to the start of the CSV column.
     * @return A buffer populated with the column data or null if no more data exists.
     */
    @Override
    protected String extractNextColumn(final LookAheadReader in)
    {
        final var buffer = new StringBuffer();

        // Handle leading white spaces.
        processLeadingSpaces(in, buffer);

        // Determine what type of processing is needed.
        try
        {
            readColumn(in, buffer);
        }
        catch (final Exception e)
        {
            problem(e, "Invalid CSV format");
            return null;
        }

        return in.hasNext() || buffer.length() > 0 ? buffer.toString() : null;
    }

    /**
     * Reads a standard, non-quoted, CSV column. Note that when the method exits the stream should be pointing to the
     * line terminator, 0x0A, or be exhausted. This version ignores double quotes, so the records cannot embed the
     * delimiter.
     *
     * @param in The input stream to read from pointing to the start of the column.
     * @param buffer The buffer to be populated with the contents of the input data.
     */
    @Override
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
            else
            {
                buffer.append((char) in.current());
            }
            in.next();
        }
    }
}
