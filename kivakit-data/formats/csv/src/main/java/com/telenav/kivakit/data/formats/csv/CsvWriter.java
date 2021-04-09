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

import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.resource.writing.BaseWriter;
import com.telenav.kivakit.data.formats.csv.project.lexakai.diagrams.DiagramCsv;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.io.PrintWriter;

/**
 * Writes {@link CsvLine}s to a {@link PrintWriter} using a given {@link CsvSchema}.
 *
 * @author jonathanl (shibo)
 * @see CsvSchema
 * @see CsvLine
 */
@UmlClassDiagram(diagram = DiagramCsv.class)
@UmlRelation(label = "writes", referent = CsvLine.class)
@LexakaiJavadoc(complete = true)
public class CsvWriter extends BaseWriter
{
    /** The CSV schema being written to */
    @UmlAggregation(label = "uses")
    private final CsvSchema schema;

    /** The output destination */
    private final PrintWriter out;

    /** Progress as CSV is being written */
    private final ProgressReporter reporter;

    /**
     * Constructs a writer with the given output destination and schema
     */
    public CsvWriter(final PrintWriter out, final ProgressReporter reporter, final CsvSchema schema)
    {
        this.out = out;
        this.reporter = reporter;
        this.schema = schema;

        reporter.start();

        writeComment(schema.toString());
    }

    /**
     * Closes output
     */
    @Override
    public void close()
    {
        out.close();
        reporter.end();
    }

    /**
     * @return The schema being used by this writer
     */
    public CsvSchema schema()
    {
        return schema;
    }

    /**
     * Writes the given CSV line
     */
    public void write(final CsvLine line)
    {
        reporter.next();
        out.write(line.toString() + "\n");
    }

    /**
     * Writes the given comment
     */
    public void writeComment(final String comment)
    {
        out.println("// " + comment);
    }
}
