////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.formats.csv;

import com.telenav.kivakit.core.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.core.kernel.language.collections.map.string.NameMap;
import com.telenav.kivakit.core.kernel.language.collections.set.ObjectSet;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.data.formats.project.lexakai.diagrams.DiagramCsv;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * An ordered list of {@link CsvColumn} objects specifying the structure of a line in a CSV (Comma Separated Data)
 * file.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramCsv.class)
public class CsvSchema extends BaseRepeater
{
    public static CsvSchema of(final CsvColumn<?>... columns)
    {
        return new CsvSchema(columns);
    }

    /** Columns by name */
    private final NameMap<CsvColumn<?>> columnForName = new NameMap<>(Maximum._1_000, new LinkedHashMap<>());

    /** List of columns */
    @UmlAggregation
    private final ObjectList<CsvColumn<?>> columns = new ObjectList<>(Maximum._1_000);

    /** Column set for contains testing */
    private final ObjectSet<CsvColumn<?>> included = new ObjectSet<>(Maximum._1_000);

    /** The column index to assign to the next added column */
    private int index;

    /**
     * Constructs a schema with the given columns
     */
    protected CsvSchema(final CsvColumn<?>... columns)
    {
        addAll(columns);
    }

    /**
     * Adds a column to this schema
     */
    public CsvSchema add(final CsvColumn<?> column)
    {
        if (column != null)
        {
            column.schema(this);
            column.index(index++);
            columnForName.add(column);
            columns.add(column);
            included.add(column);
        }
        return this;
    }

    /**
     * Adds a list of columns to this schema
     */
    public void addAll(final List<? extends CsvColumn<?>> columns)
    {
        for (final CsvColumn<?> column : columns)
        {
            add(column);
        }
    }

    /**
     * Adds the given columns to this schema
     */
    public CsvSchema addAll(final CsvColumn<?>... columns)
    {
        for (final var column : columns)
        {
            add(column);
        }
        return this;
    }

    /**
     * @return The named column or null if no such column exists
     */
    public CsvColumn<?> columnForName(final String name)
    {
        return columnForName.get(name);
    }

    /**
     * @return True if this schema contains the given column
     */
    public boolean contains(final CsvColumn<?> column)
    {
        return included.contains(column);
    }

    /**
     * @return The columns in this schema joined by commas
     */
    @Override
    public String toString()
    {
        return columns.join(",");
    }
}
