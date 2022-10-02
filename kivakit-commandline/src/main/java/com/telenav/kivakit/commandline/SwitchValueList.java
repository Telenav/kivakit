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

package com.telenav.kivakit.commandline;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.commandline.internal.lexakai.DiagramCommandLine;
import com.telenav.kivakit.commandline.internal.lexakai.DiagramSwitch;
import com.telenav.kivakit.commandline.internal.lexakai.DiagramValidation;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.language.reflection.property.Property;
import com.telenav.kivakit.core.language.reflection.property.PropertyValue;
import com.telenav.kivakit.interfaces.string.StringFormattable;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;

/**
 * <b>Not Public API</b>
 * <p>
 * A list of switches for which values can be retrieved by {@link SwitchParser}s. Switches can only appear once on a
 * command line.
 *
 * <p><b>Adding</b></p>
 *
 * <ul>
 *     <li>{@link #add(SwitchValue)}</li>
 * </ul>
 *
 * <p><b>Retrieval</b></p>
 *
 * <ul>
 *     <li>{@link #get(SwitchParser)}</li>
 *     <li>{@link #has(SwitchParser)}</li>
 *     <li>{@link #iterator()}</li>
 *     <li>{@link #propertyValue(Property)}</li>
 * </ul>
 *
 * <p><b>Conversion</b></p>
 *
 * <ul>
 *     <li>{@link #asString(Format)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramSwitch.class)
@UmlClassDiagram(diagram = DiagramValidation.class)
@UmlClassDiagram(diagram = DiagramCommandLine.class)
@UmlExcludeSuperTypes
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public class SwitchValueList implements
        Iterable<SwitchValue>,
        StringFormattable,
        PropertyValue
{
    /** The switches */
    @UmlAggregation
    private final ObjectList<SwitchValue> switchValues = new ObjectList<>();

    /**
     * Adds the given switch to this list
     */
    public void add(@NotNull SwitchValue that)
    {
        switchValues.add(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String asString(@NotNull Format format)
    {
        return switchValues.join();
    }

    /**
     * Returns the switch value for the given switch parser
     */
    public <T> T get(@NotNull SwitchParser<T> parser)
    {
        ensureNotNull(parser);

        var switchValue = switchForName(parser.name());
        if (switchValue != null)
        {
            return switchValue.get(parser);
        }
        else
        {
            return parser.defaultValue();
        }
    }

    /**
     * Returns true if this switch list has a value for the given switch parser
     */
    public boolean has(@NotNull SwitchParser<?> parser)
    {
        return switchForName(parser.name()) != null;
    }

    @Override
    public Iterator<SwitchValue> iterator()
    {
        return switchValues.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object propertyValue(@NotNull Property property)
    {
        var switchValue = switchForName(property.name());
        if (switchValue != null)
        {
            return switchValue.value();
        }
        return null;
    }

    private SwitchValue switchForName(@NotNull String name)
    {
        for (var switchValue : switchValues)
        {
            if (switchValue.name().equalsIgnoreCase(name))
            {
                return switchValue;
            }
        }
        return null;
    }
}
