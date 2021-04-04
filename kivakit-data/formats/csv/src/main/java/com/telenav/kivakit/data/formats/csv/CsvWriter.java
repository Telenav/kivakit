////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.formats.csv;

import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.resource.writing.BaseWriter;
import com.telenav.kivakit.data.formats.project.lexakai.diagrams.DiagramCsv;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.io.PrintWriter;

/**
 * Writes CSV to a {@link PrintWriter} using a given {@link CsvSchema}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramCsv.class)
@UmlRelation(label = "writes", referent = CsvLine.class)
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
        out.println("// " + schema);
    }

    /**
     * Closes output
     */
    @Override
    public void close()
    {
        out.close();
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
        out.write("// " + comment + "\n");
    }
}
