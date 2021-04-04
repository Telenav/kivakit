////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.formats.csv;

import com.telenav.kivakit.core.kernel.data.conversion.string.StringConverter;
import com.telenav.kivakit.core.kernel.language.values.name.Name;
import com.telenav.kivakit.data.formats.project.lexakai.diagrams.DiagramCsv;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * The name and index of a column in a CSV schema
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramCsv.class)
public class CsvColumn<Type> extends Name
{
    public static CsvColumn<String> of(final String name)
    {
        return new CsvColumn<>(name, StringConverter.IDENTITY);
    }

    public static <T> CsvColumn<T> of(final String name, final StringConverter<T> converter)
    {
        return new CsvColumn<>(name, converter);
    }

    /** The zero-based index of the column in the CSV schema */
    private int index;

    /** The schema that this column has been added to */
    private CsvSchema schema;

    /** The converter to convert this column to and from its type */
    private final StringConverter<Type> converter;

    /**
     * Construct the named column
     */
    protected CsvColumn(final String name, final StringConverter<Type> converter)
    {
        super(name);
        this.converter = converter;
    }

    /**
     * @return The text for the given value in this column
     */
    public String asString(final Type value)
    {
        return converter.toString(value);
    }

    /**
     * @return The value of the given text if it is in this column
     */
    public Type asType(final String text)
    {
        return converter.convert(text);
    }

    /**
     * @return The schema that defines this column
     */
    public CsvSchema schema()
    {
        return schema;
    }

    /**
     * @return The index of this column in the schema that references it
     */
    int index()
    {
        return index;
    }

    /**
     * Sets the index for this column
     */
    void index(final int index)
    {
        this.index = index;
    }

    /**
     * Sets the schema for this column
     */
    void schema(final CsvSchema schema)
    {
        if (this.schema != null)
        {
            throw new IllegalStateException("Column " + this + " has already been added to schema " + this.schema);
        }
        this.schema = schema;
    }
}
