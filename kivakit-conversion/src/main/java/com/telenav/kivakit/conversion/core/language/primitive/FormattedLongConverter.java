////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.conversion.core.language.primitive;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionPrimitive;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.text.DecimalFormat;
import java.text.ParseException;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Converts a Long to and from a formatted string.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionPrimitive.class)
@ApiQuality(stability = API_STABLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public class FormattedLongConverter extends BaseStringConverter<Long>
{
    /** The format for conversion */
    private final DecimalFormat format;

    /**
     * Constructs a converter that allows commas
     *
     * @param listener The listener to hear any conversion issues
     */
    public FormattedLongConverter(Listener listener)
    {
        this(listener, true);
    }

    /**
     * @param listener The listener to hear any conversion issues
     * @param commas True if the string representation has commas in it
     */
    public FormattedLongConverter(Listener listener, boolean commas)
    {
        this(listener, new DecimalFormat(commas ? "###,###" : "#"));
    }

    /**
     * Constructs a converter with the given decimal format
     *
     * @param listener The listener to hear any conversion issues
     * @param format The format to use
     */
    public FormattedLongConverter(Listener listener, DecimalFormat format)
    {
        super(listener);
        this.format = format;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    protected String onToString(Long value)
    {
        return format.format(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    protected Long onToValue(String value)
    {
        try
        {
            return format.parse(value).longValue();
        }
        catch (ParseException e)
        {
            problem(e, "Couldn't parse long");
            return null;
        }
    }
}
