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
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static java.lang.Integer.parseInt;

/**
 * Converts between integers and strings
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionPrimitive.class)
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class IntegerConverter extends BaseStringConverter<Integer>
{
    /**
     * @param listener The listener to hear any conversion issues
     */
    public IntegerConverter(Listener listener)
    {
        super(listener, Integer.class);
    }

    public IntegerConverter()
    {
        this(throwingListener());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    protected Integer onToValue(String value)
    {
        return parseInt(value);
    }
}
