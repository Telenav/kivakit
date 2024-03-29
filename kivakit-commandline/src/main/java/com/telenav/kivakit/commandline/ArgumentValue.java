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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.commandline.internal.lexakai.DiagramArgument;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.fail;

/**
 * The string value of a single argument in a command line. The string value of the argument can be retrieved with
 * {@link #value()} and it can be retrieved as a typed value with {@link #get(ArgumentParser)}.
 *
 * @author jonathanl (shibo)
 * @see ArgumentParser
 * @see ArgumentValueList
 * @see CommandLine
 */
@UmlClassDiagram(diagram = DiagramArgument.class)
@UmlRelation(label = "gets value with", referent = ArgumentParser.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class ArgumentValue
{
    /** The argument's string value */
    private final String value;

    /**
     * @param value The argument value
     */
    public ArgumentValue(@NotNull String value)
    {
        this.value = value;
    }

    /**
     * @param parser The argument parser
     * @return The value of this argument using the given argument parser
     */
    public <T> T get(@NotNull ArgumentParser<T> parser)
    {
        var value = parser.asObject(this);
        if (value == null)
        {
            var parent = parser.parent();
            if (parent != null)
            {
                parent.exit("Unable to parse argument: " + this.value);
            }
            else
            {
                fail("Argument parser was not added in Application.argumentParsers(): $", parser);
            }
        }
        return value;
    }

    @Override
    public String toString()
    {
        return value;
    }

    /**
     * Returns the string value of this argument
     */
    public String value()
    {
        return value;
    }
}
