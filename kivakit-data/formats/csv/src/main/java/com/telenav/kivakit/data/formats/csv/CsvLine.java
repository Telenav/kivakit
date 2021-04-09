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

import com.telenav.kivakit.core.kernel.language.collections.list.StringList;
import com.telenav.kivakit.core.kernel.language.reflection.Type;
import com.telenav.kivakit.core.kernel.language.reflection.populator.ObjectPopulator;
import com.telenav.kivakit.core.kernel.language.reflection.property.Property;
import com.telenav.kivakit.core.kernel.language.reflection.property.PropertyValueSource;
import com.telenav.kivakit.core.kernel.language.strings.Strings;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.kernel.messaging.repeaters.RepeaterTrait;
import com.telenav.kivakit.data.formats.csv.project.lexakai.diagrams.DiagramCsv;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A model of a line in a CSV (Comma Separated Variable) file. {@link CsvLine} objects are produced by {@link CsvReader}
 * and consumed by {@link CsvWriter}.
 *
 * <p><b>Properties</b></p>
 *
 * <p>
 * This model has a line number available with {@link #lineNumber()} and it conforms to a {@link CsvSchema}, available
 * with {@link #schema()}. Columns are separated in the CSV file with a delimiter character, normally a comma, that is
 * passed to the constructor {@link #CsvLine(CsvSchema, char)}. Values from the line can be retrieved by column with
 * {@link #get(CsvColumn)} and they can be provided with {@link #set(CsvColumn, Object)}.
 * </p>
 *
 * <p><b>Converting a Line to an Object</b></p>
 *
 * <p>
 * A {@link CsvLine} can be converted directly to an object with {@link #asObject(Class)}. A new instance of the class
 * is created and its properties are populated using the {@link CsvSchema} of this line. For details, see {@link
 * #asObject(Class)} and {@link #valueFor(Property)}.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see CsvSchema
 * @see CsvColumn
 * @see CsvReader
 * @see CsvWriter
 */
@UmlClassDiagram(diagram = DiagramCsv.class)
@LexakaiJavadoc(complete = true)
public class CsvLine extends StringList implements PropertyValueSource, RepeaterTrait
{
    /** The schema that this line obeys */
    private final transient CsvSchema schema;

    /**
     * The line number of this CSV line in the file or input stream being read. If the object is being written, the
     * value will be -1.
     */
    private int lineNumber = -1;

    /** The value separator, by default a comma */
    private final char delimiter;

    /**
     * Construct with a given schema and delimiter
     */
    public CsvLine(final CsvSchema schema, final char delimiter)
    {
        super(Maximum._10_000);

        this.schema = schema;
        this.delimiter = delimiter;
    }

    /**
     * @return An object of the given type with its properties populated by {@link ObjectPopulator} using {@link
     * CsvPropertyFilter}. Properties of the object that correspond to {@link CsvColumn}s using KivaKit property naming
     * are retrieved with {@link PropertyValueSource#valueFor(Property)} (see below) and set on the new object by
     * reflection. The result is an object corresponding to this line.
     */
    public <T> T asObject(final Class<T> type)
    {
        try
        {
            return new ObjectPopulator(this, new CsvPropertyFilter(schema()), this)
                    .populate(Type.forClass(type).newInstance());
        }
        catch (final Exception e)
        {
            problem(e, "Unable to create or populate ${debug}", type);
            return null;
        }
    }

    /**
     * @return The value of the given column
     */
    public <T> T get(final CsvColumn<T> column)
    {
        final var text = string(column);
        return text == null ? null : column.asType(text);
    }

    /**
     * @return The line number of this CSV line in the input, or -1 if the line was not read from an input source (if it
     * was constructed to be written)
     */
    public int lineNumber()
    {
        return lineNumber;
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

    /**
     * @return The unconverted string value for the given column
     */
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

    /**
     * Implementation of {@link PropertyValueSource} used by {@link ObjectPopulator} in {@link #asObject(Class)} to get
     * the value of the given property using the property name to find the {@link CsvColumn}.
     */
    @Override
    public Object valueFor(final Property property)
    {
        final var column = schema().columnForName(property.name());
        if (column != null)
        {
            return get(column);
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
