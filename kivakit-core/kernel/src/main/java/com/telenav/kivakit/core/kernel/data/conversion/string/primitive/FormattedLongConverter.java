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

package com.telenav.kivakit.core.kernel.data.conversion.string.primitive;

import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataConversionPrimitive;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * Converts a Long to and from a formatted string.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataConversionPrimitive.class)
public class FormattedLongConverter extends BaseStringConverter<Long>
{
    /** The format for conversion */
    private final DecimalFormat format;

    /**
     * Constructs a converter that allows commas
     *
     * @param listener The listener to hear any conversion issues
     */
    public FormattedLongConverter(final Listener listener)
    {
        this(listener, true);
    }

    /**
     * @param listener The listener to hear any conversion issues
     * @param commas True if the string representation has commas in it
     */
    public FormattedLongConverter(final Listener listener, final boolean commas)
    {
        this(listener, new DecimalFormat(commas ? "###,###" : "#"));
    }

    /**
     * Constructs a converter with the given decimal format
     *
     * @param listener The listener to hear any conversion issues
     * @param format The format to use
     */
    public FormattedLongConverter(final Listener listener, final DecimalFormat format)
    {
        super(listener);
        this.format = format;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    protected Long onConvertToObject(final String value)
    {
        try
        {
            return format.parse(value).longValue();
        }
        catch (final ParseException e)
        {
            problem(e, "Couldn't parse long");
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    protected String onConvertToString(final Long value)
    {
        return format.format(value);
    }
}
