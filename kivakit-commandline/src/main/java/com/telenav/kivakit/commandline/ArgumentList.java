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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A list of {@link Argument}s passed on a command line and retrieved with {@link CommandLine#arguments()} after command
 * line parsing with {@link CommandLineParser}. Individual arguments values can be retrieved as typed values with
 * {@link Argument#get(ArgumentParser)}. If the arguments are all the same type they can be retrieved as a list with
 * {@link #argumentValues(ArgumentParser)}.
 *
 * @author jonathanl (shibo)
 * @see CommandLine
 * @see Argument
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramArgument.class)
@UmlClassDiagram(diagram = DiagramValidation.class)
@UmlClassDiagram(diagram = DiagramCommandLine.class)
@UmlExcludeSuperTypes
public class ArgumentList implements Iterable<Argument>, StringFormattable, Sized
{
    /** The arguments */
    @UmlAggregation
    private final ObjectList<Argument> arguments = new ObjectList<>();

    /**
     * Adds an argument to this list
     */
    public void add(Argument argument)
    {
        arguments.add(argument);
    }

    /**
     * @return All values in this list converted using the given parser
     */
    @UmlExcludeMember
    public <T> List<T> argumentValues(ArgumentParser<T> parser)
    {
        var arguments = new ArrayList<T>();
        for (var argument : this)
        {
            arguments.add(argument.get(parser));
        }
        return arguments;
    }

    @Override
    @UmlExcludeMember
    public String asString()
    {
        return arguments.join();
    }

    /**
     * @return The first argument in this list
     */
    public Argument first()
    {
        return arguments.first();
    }

    /**
     * @return The argument at the given index in this list
     */
    public Argument get(int index)
    {
        return arguments.get(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    public boolean isEmpty()
    {
        return arguments.isEmpty();
    }

    @Override
    public Iterator<Argument> iterator()
    {
        return arguments.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size()
    {
        return arguments.size();
    }

    @Override
    public String toString()
    {
        return asString();
    }
}
