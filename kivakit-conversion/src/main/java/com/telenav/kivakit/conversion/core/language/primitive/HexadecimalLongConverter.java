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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionPrimitive;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.conversion.core.language.primitive.HexadecimalLongConverter.Style.JAVA;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;

/**
 * Converts between hexadecimal strings and long values. The constructor parameter {@link Style} allows conversion of
 * {@link Style#JAVA} (0x prefixed), {@link Style#CSS} (# prefixed) and {@link Style#NONE} (no prefix) values.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionPrimitive.class)
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class HexadecimalLongConverter extends BaseStringConverter<Long>
{
    @TypeQuality(stability = STABLE,
                 testing = TESTING_NOT_NEEDED,
                 documentation = DOCUMENTED)
    public enum Style
    {
        NONE(""),
        JAVA("0x"),
        CSS("#");

        private final String prefix;

        Style(String prefix)
        {
            this.prefix = prefix;
        }

        @Override
        public String toString()
        {
            return prefix;
        }
    }

    private final Style style;

    /**
     * @param listener The listener to hear any conversion issues
     */
    public HexadecimalLongConverter(Listener listener)
    {
        this(listener, JAVA);
    }

    public HexadecimalLongConverter()
    {
        this(throwingListener());
    }

    /**
     * @param listener The listener to hear any conversion issues
     */
    public HexadecimalLongConverter(Listener listener, Style style)
    {
        super(listener, Long.class);
        this.style = style;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    protected Long onToValue(String value)
    {
        if (value.startsWith(style.prefix))
        {
            return Long.parseLong(value.substring(style.prefix.length()), 16);
        }
        else
        {
            problem("Hexadecimal long does not start with '" + style + "': ${debug}", value);
            return null;
        }
    }
}
