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

import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.commandline.project.lexakai.diagrams.DiagramCommandLine;
import com.telenav.kivakit.commandline.project.lexakai.diagrams.DiagramValidation;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.collections.map.string.IndexedNameMap;
import com.telenav.kivakit.kernel.project.KernelLimits;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Iterator;

/**
 * <b>Not Public API</b>
 * <p>
 * A list of switch parsers, which can be retrieved by name with {@link #forName(String)} and for which there is {@link
 * #help()} available.
 *
 * @author jonathanl (shibo)
 */
@UmlNotPublicApi
@UmlClassDiagram(diagram = DiagramValidation.class)
@UmlClassDiagram(diagram = DiagramCommandLine.class)
@UmlExcludeSuperTypes
public class SwitchParserList implements Iterable<SwitchParser<?>>
{
    @UmlAggregation
    private final IndexedNameMap<SwitchParser<?>> parsers = new IndexedNameMap<>();

    /**
     * Adds the given switch parser to this list
     *
     * @param parser The parser
     */
    public void add(final SwitchParser<?> parser)
    {
        parsers.put(parser);
    }

    /**
     * @return The switch parser with the given name
     */
    public SwitchParser<?> forName(final String name)
    {
        return parsers.forName(name);
    }

    /**
     * @return The help for the switch list
     */
    public String help()
    {
        final var descriptions = new StringList(KernelLimits.COMMAND_LINE_SWITCHES);
        parsers.sort(Comparator.comparing(SwitchParser::name));

        int required = 0;
        for (final var parser : parsers)
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
        for (final var parser : parsers)
        {
            if (!parser.isRequired())
            {
                if (optional == 0)
                {
                    optional++;
                    descriptions.add((required > 0 ? "\n" : "") + "  Optional:\n");
                }
                descriptions.add(parser.help());
            }
        }
        return "  " + descriptions.join("\n  ");
    }

    @NotNull
    @Override
    public Iterator<SwitchParser<?>> iterator()
    {
        return parsers.iterator();
    }
}
