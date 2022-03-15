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

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.lexakai.DiagramConversionPrimitive;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

/**
 * Converts a float to and from a string.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionPrimitive.class)
@LexakaiJavadoc(complete = true)
public class FloatConverter extends BaseStringConverter<Float>
{
    /**
     * @param listener The listener to hear any conversion issues
     */
    public FloatConverter(Listener listener)
    {
        super(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    protected String onToString(Float value)
    {
        if (Float.isNaN(value) || Float.isInfinite(value))
        {
            return "N/A";
        }
        else
        {
            return value.toString();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    protected Float onToValue(String value)
    {
        return Float.valueOf(value);
    }
}
