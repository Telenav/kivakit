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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.commandline.ArgumentParser;
import com.telenav.kivakit.commandline.internal.lexakai.DiagramArgument;
import com.telenav.kivakit.commandline.internal.lexakai.DiagramCommandLine;
import com.telenav.kivakit.commandline.internal.lexakai.DiagramValidation;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

import java.util.Iterator;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

/**
 * <b>Not Public API</b>
 * <p>
 * A list of zero or more {@link ArgumentParser}s, optionally followed by a single {@link ArgumentParser} which is
 * allowed multiple times, as determined by {@link ArgumentParser#isAllowedMultipleTimes()}. Argument lists where two
 * different argument parsers are repeated multiple times are not supported.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramArgument.class)
@UmlClassDiagram(diagram = DiagramValidation.class)
@UmlClassDiagram(diagram = DiagramCommandLine.class)
@UmlNotPublicApi
@UmlExcludeSuperTypes({ Iterable.class })
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE,
             audience = AUDIENCE_INTERNAL)
public class ArgumentParserList implements Iterable<ArgumentParser<?>>
{
    /** The list of argument parsers */
    @UmlAggregation
    private final ObjectList<ArgumentParser<?>> parsers = new ObjectList<>();

    /**
     * Adds the given argument parser to this list of parsers
     *
     * @param parser The argument parser to add
     */
    public boolean add(ArgumentParser<?> parser)
    {
        // If the argument parser specifies an argument that's allowed multiple times,
        if (parser.isAllowedMultipleTimes())
        {
            // check each parser in the list to make sure we don't already have such
            // an argument specified (only one is allowed)
            for (var current : parsers)
            {
                if (current.isAllowedMultipleTimes())
                {
                    throw new IllegalArgumentException("Argument " + parsers.get(parsers.size() - 1)
                            + " can only appear as the last element of an argument parser list"
                            + " because it can be repeated multiple times.");
                }
            }
        }
        return parsers.add(parser);
    }

    /**
     * @return A help string describing each argument in this list of argument parsers
     */
    public String help()
    {
        var descriptions = new StringList();
        if (parsers.isEmpty())
        {
            descriptions.add("<none>");
            return "  " + descriptions.join("\n  ");
        }
        else
        {
            for (var parser : parsers)
            {
                descriptions.add(parser.help());
            }
            return "  " + descriptions.numbered().join("\n  ");
        }
    }

    /**
     * Returns true if there are no argument parsers in this list
     */
    public boolean isEmpty()
    {
        return parsers.isEmpty();
    }

    @Override
    public Iterator<ArgumentParser<?>> iterator()
    {
        return parsers.iterator();
    }

    /**
     * Returns the last argument parser in this list
     */
    @UmlExcludeMember
    public ArgumentParser<?> last()
    {
        return parsers.last();
    }
}
