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

package com.telenav.kivakit.commandline.parsing;

import com.telenav.kivakit.commandline.Switch;
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.commandline.project.lexakai.DiagramCommandLine;
import com.telenav.kivakit.commandline.project.lexakai.DiagramSwitch;
import com.telenav.kivakit.commandline.project.lexakai.DiagramValidation;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.language.reflection.property.Property;
import com.telenav.kivakit.core.language.reflection.property.PropertyValues;
import com.telenav.kivakit.interfaces.string.Stringable;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

import java.util.Iterator;

/**
 * <b>Not Public API</b>
 * <p>
 * A list of switches for which values can be retrieved by {@link SwitchParser}s. Switches can only appear once on a
 * command line.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramSwitch.class)
@UmlClassDiagram(diagram = DiagramValidation.class)
@UmlClassDiagram(diagram = DiagramCommandLine.class)
@UmlExcludeSuperTypes
@UmlNotPublicApi
public class SwitchList implements
        Iterable<Switch>,
        Stringable,
        PropertyValues
{
    /** The switches */
    @UmlAggregation
    private final ObjectList<Switch> switches = new ObjectList<>();

    /**
     * Adds the given switch to this list
     */
    public void add(Switch _switch)
    {
        switches.add(_switch);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String asString(Format format)
    {
        return switches.join();
    }

    /**
     * @return The switch value for the given switch parser
     */
    public <T> T get(SwitchParser<T> parser)
    {
        var _switch = switchForName(parser.name());
        if (_switch != null)
        {
            return _switch.get(parser);
        }
        else
        {
            return parser.defaultValue();
        }
    }

    @Override
    public Iterator<Switch> iterator()
    {
        return switches.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object valueFor(Property property)
    {
        var _switch = switchForName(property.name());
        if (_switch != null)
        {
            return _switch.value();
        }
        return null;
    }

    /**
     * @return True if this switch list has a value for the given switch parser
     */
    boolean has(SwitchParser<?> parser)
    {
        return switchForName(parser.name()) != null;
    }

    private Switch switchForName(String name)
    {
        for (var _switch : switches)
        {
            if (_switch.name().equalsIgnoreCase(name))
            {
                return _switch;
            }
        }
        return null;
    }
}
