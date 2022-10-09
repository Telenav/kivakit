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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionPrimitive;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.string.AsciiArt;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.text.DecimalFormat;
import java.text.ParseException;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

/**
 * Converts a double to and from a formatted string, with or without commas and with a given number of decimal places.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionPrimitive.class)
@CodeQuality(stability = STABILITY_STABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class FormattedDoubleConverter extends BaseStringConverter<Double>
{
    /** The format for conversion */
    private final DecimalFormat format;

    /**
     * Constructs a converter with commas and two decimal places
     */
    public FormattedDoubleConverter(Listener listener)
    {
        this(listener, true, 2);
    }

    /**
     * @param listener The listener to hear any conversion issues
     * @param commas True if the string representation has commas in it
     * @param outputPlaces The number of decimal places to include
     */
    public FormattedDoubleConverter(Listener listener, boolean commas, int outputPlaces)
    {
        this(listener, new DecimalFormat((commas ? "###,###" : "#") + "." + AsciiArt.repeat(outputPlaces, '#')));
    }

    /**
     * Constructs a converter with the given decimal format
     *
     * @param listener The listener to hear any conversion issues
     * @param format The format to use
     */
    public FormattedDoubleConverter(Listener listener, DecimalFormat format)
    {
        super(listener);
        this.format = format;
    }

    /**
     * Constructs a converter with commas and the given number of decimal places
     *
     * @param listener The listener to hear any conversion issues
     * @param outputPlaces The number of decimal places to include
     */
    public FormattedDoubleConverter(Listener listener, int outputPlaces)
    {
        this(listener, true, outputPlaces);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    protected String onToString(Double value)
    {
        if (Double.isNaN(value) || Double.isInfinite(value))
        {
            return "N/A";
        }
        else
        {
            return format.format(value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    protected Double onToValue(String value)
    {
        try
        {
            return format.parse(value).doubleValue();
        }
        catch (ParseException e)
        {
            problem(e, "Couldn't parse double");
            return null;
        }
    }
}
