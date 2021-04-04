////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.formats.csv;

import com.telenav.kivakit.core.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.core.kernel.interfaces.messaging.Transmittable;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;
import com.telenav.kivakit.core.kernel.language.reflection.Type;
import com.telenav.kivakit.core.kernel.language.reflection.populator.ObjectPopulator;
import com.telenav.kivakit.core.kernel.language.reflection.property.Property;
import com.telenav.kivakit.core.kernel.language.reflection.property.PropertyValueSource;
import com.telenav.kivakit.core.kernel.language.strings.Strings;
import com.telenav.kivakit.core.kernel.language.threading.context.CodeContext;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.kernel.messaging.Repeater;
import com.telenav.kivakit.core.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.data.formats.project.lexakai.diagrams.DiagramCsv;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitProperties.INCLUDED_PROPERTIES_AND_FIELDS;

/**
 * A line in a CSV (Comma Separated Variable file.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramCsv.class)
public class CsvLine extends StringList implements PropertyValueSource, Repeater
{
    /** The schema that this line obeys */
    private final transient CsvSchema schema;

    /** A repeater to notify of conversion problems */
    private final transient Repeater repeater;

    /**
     * The line number of this CSV line in the file or input stream being read. If the object is being written, the
     * value will be -1.
     */
    private int lineNumber = -1;

    /** The value separator, by default a comma */
    private final char delimiter;

    /**
     * Construct with a given listener and schema
     */
    public CsvLine(final CsvSchema schema, final char delimiter)
    {
        super(Maximum._1_000);
        repeater = new BaseRepeater(getClass());
        this.schema = schema;
        this.delimiter = delimiter;
    }

    @Override
    public void addListener(final Listener listener, final Filter<Transmittable> filter)
    {
        repeater.addListener(listener, filter);
    }

    @Override
    public void addListener(final Listener listener)
    {
        repeater.addListener(listener);
    }

    /**
     * @return An object of the given type populated by {@link ObjectPopulator}
     */
    public <T> T asObject(final Class<T> type)
    {
        try
        {
            return new ObjectPopulator(this, INCLUDED_PROPERTIES_AND_FIELDS, this).populate(Type.forClass(type).newInstance());
        }
        catch (final Exception e)
        {
            problem(e, "Unable to populate ${debug}", type);
            return null;
        }
    }

    @Override
    public void clearListeners()
    {
        repeater.clearListeners();
    }

    @Override
    public void debugCodeContext(final CodeContext context)
    {
        repeater.debugCodeContext(context);
    }

    @Override
    public CodeContext debugCodeContext()
    {
        return repeater.debugCodeContext();
    }

    /**
     * @return The value of the given column
     */
    public <T> T get(final CsvColumn<T> column)
    {
        final var text = string(column);
        return text == null ? null : column.asType(text);
    }

    @Override
    public boolean hasListeners()
    {
        return repeater.hasListeners();
    }

    /**
     * @return The line number of this CSV line in the input, or -1 if the line was not read from an input source (if it
     * was constructed to be written)
     */
    public int lineNumber()
    {
        return lineNumber;
    }

    @Override
    public void onMessage(final Message message)
    {
        repeater.onMessage(message);
    }

    @Override
    public void removeListener(final Listener listener)
    {
        repeater.removeListener(listener);
    }

    /**
     * @return The schema for this line
     */
    public CsvSchema schema()
    {
        return schema;
    }

    /**
     * Sets the given column to the given value
     */
    public <T> void set(final CsvColumn<T> column, final T value)
    {
        set(column, column.asString(value));
    }

    /**
     * Sets the given column to the given value
     */
    public void set(final CsvColumn<?> column, final String value)
    {
        if (column != null)
        {
            final var index = column.index();
            while (index >= size())
            {
                add("");
            }
            set(index, value);
        }
    }

    public String string(final CsvColumn<?> column)
    {
        final var index = column.index();
        return index >= size() ? null : get(column.index());
    }

    @Override
    public String toString()
    {
        return join(delimiter());
    }

    @Override
    public String toString(String value)
    {
        // Escape quotes
        if (value.contains("\""))
        {
            value = Strings.replaceAll(value, "\"", "\"\"");
        }

        // And quote values with separators in them
        if (value.indexOf(delimiter()) >= 0)
        {
            value = '"' + value + '"';
        }
        return value;
    }

    @Override
    public void transmit(final Message message)
    {
        repeater.transmit(message);
    }

    /**
     * Value of the column with the given property name
     */
    @Override
    public String valueFor(final Property property)
    {
        final var column = schema().columnForName(property.name());
        if (column != null)
        {
            return string(column);
        }
        return null;
    }

    /**
     * @return The separator used in this CSV line
     */
    protected char delimiter()
    {
        return delimiter;
    }

    @Override
    protected String separator()
    {
        return Character.toString(delimiter());
    }

    /**
     * Used by CSV reader to set the line number for this line
     */
    void lineNumber(final int lineNumber)
    {
        this.lineNumber = lineNumber;
    }
}
