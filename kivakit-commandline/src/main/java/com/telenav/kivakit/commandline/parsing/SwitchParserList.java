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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.collections.map.IndexedNameMap;
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.commandline.internal.lexakai.DiagramCommandLine;
import com.telenav.kivakit.commandline.internal.lexakai.DiagramValidation;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

import java.util.Iterator;

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static java.util.Comparator.comparing;

/**
 * <b>Not Public API</b>
 * <p>
 * A list of switch parsers, which can be retrieved by name with {@link #forName(String)} and for which there is
 * {@link #help()} available.
 *
 * @author jonathanl (shibo)
 */
@UmlNotPublicApi
@UmlClassDiagram(diagram = DiagramValidation.class)
@UmlClassDiagram(diagram = DiagramCommandLine.class)
@UmlExcludeSuperTypes
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED,
             audience = AUDIENCE_INTERNAL)
public class SwitchParserList implements Iterable<SwitchParser<?>>
{
    @UmlAggregation
    private final IndexedNameMap<SwitchParser<?>> parsers = new IndexedNameMap<>();

    /**
     * Adds the given switch parser to this list
     *
     * @param parser The parser
     */
    public void add(SwitchParser<?> parser)
    {
        parsers.add(parser);
    }

    /**
     * Returns the switch parser with the given name
     */
    public SwitchParser<?> forName(String name)
    {
        return parsers.get(name);
    }

    /**
     * Returns the help for the switch list
     */
    public String help()
    {
        var descriptions = new StringList();
        parsers.sort(comparing(SwitchParser::name));

        int required = 0;
        for (var parser : parsers)
        {
            if (parser.isRequired())
            {
                if (required == 0)
                {
                    required++;
                    descriptions.add("Required:\n");
                }
                descriptions.add(parser.help());
            }
        }

        int optional = 0;
        for (var parser : parsers)
        {
            if (!parser.isRequired())
            {
                if (optional == 0)
                {
                    optional++;
                    descriptions.add((required > 0 ? "\n" : "") + "Optional:\n");
                }
                descriptions.add(parser.help());
            }
        }
        return "  " + descriptions.join("\n  ");
    }

    @Override
    public Iterator<SwitchParser<?>> iterator()
    {
        return parsers.iterator();
    }
}
