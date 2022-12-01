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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.commandline.internal.lexakai.DiagramArgument;
import com.telenav.kivakit.commandline.internal.lexakai.DiagramCommandLine;
import com.telenav.kivakit.commandline.internal.lexakai.DiagramValidation;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.interfaces.collection.Sized;
import com.telenav.kivakit.interfaces.string.StringFormattable;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A list of {@link ArgumentValue}s passed on a command line and retrieved with {@link CommandLine#argumentValues()}
 * after command line parsing with {@link CommandLineParser}. Individual arguments values can be retrieved as typed
 * values with {@link ArgumentValue#get(ArgumentParser)}. If the arguments are all the same type they can be retrieved
 * as a list with {@link #arguments(ArgumentParser)}.
 *
 * <p><b>Arguments</b></p>
 *
 * <ul>
 *     <li>{@link #arguments(ArgumentParser)}</li>
 *     <li>{@link #firstArgumentValue()}</li>
 *     <li>{@link #argumentValue(int)}</li>
 *     <li>{@link #isEmpty()}</li>
 *     <li>{@link #iterator()}</li>
 *     <li>{@link #size()}</li>
 * </ul>
 *
 * <p><b>Adding</b></p>
 *
 * <ul>
 *     <li>{@link #add(ArgumentValue)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see CommandLine
 * @see ArgumentValue
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramArgument.class)
@UmlClassDiagram(diagram = DiagramValidation.class)
@UmlClassDiagram(diagram = DiagramCommandLine.class)
@UmlExcludeSuperTypes
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class ArgumentValueList implements
        Iterable<ArgumentValue>,
        StringFormattable,
        Sized
{
    /** The arguments */
    @UmlAggregation
    private final ObjectList<ArgumentValue> argumentValues = new ObjectList<>();

    /**
     * Adds an argument to this list
     */
    public void add(ArgumentValue argumentValue)
    {
        argumentValues.add(argumentValue);
    }

    /**
     * Returns the argument at the given index in this list
     */
    public ArgumentValue argumentValue(int index)
    {
        return argumentValues.get(index);
    }

    /**
     * Returns all values in this list converted using the given parser
     */
    @UmlExcludeMember
    public <T> ObjectList<T> arguments(ArgumentParser<T> parser)
    {
        var arguments = new ObjectList<T>();
        for (var argument : this)
        {
            arguments.add(argument.get(parser));
        }
        return arguments;
    }

    @Override
    @UmlExcludeMember
    public String asString(@NotNull Format format)
    {
        return argumentValues.join();
    }

    /**
     * Returns the first argument in this list
     */
    public ArgumentValue firstArgumentValue()
    {
        return argumentValues.first();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    public boolean isEmpty()
    {
        return argumentValues.isEmpty();
    }

    @Override
    public Iterator<ArgumentValue> iterator()
    {
        return argumentValues.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size()
    {
        return argumentValues.size();
    }

    @Override
    public String toString()
    {
        return asString();
    }
}
