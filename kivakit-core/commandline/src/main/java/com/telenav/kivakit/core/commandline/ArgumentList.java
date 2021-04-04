////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.commandline;

import com.telenav.kivakit.core.commandline.project.lexakai.diagrams.DiagramArgument;
import com.telenav.kivakit.core.commandline.project.lexakai.diagrams.DiagramCommandLine;
import com.telenav.kivakit.core.commandline.project.lexakai.diagrams.DiagramValidation;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import com.telenav.kivakit.core.kernel.interfaces.numeric.Sized;
import com.telenav.kivakit.core.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.core.kernel.language.strings.conversion.AsString;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A list of {@link Argument}s passed on a command line and retrieved with {@link CommandLine#arguments()} after command
 * line parsing with {@link CommandLineParser}. Individual arguments values can be retrieved as typed values with {@link
 * Argument#get(ArgumentParser)}. If the arguments are all of the same type they can be retrieved as a list with {@link
 * #argumentValues(ArgumentParser)}.
 *
 * @author jonathanl (shibo)
 * @see CommandLine
 * @see Argument
 */
@UmlClassDiagram(diagram = DiagramArgument.class)
@UmlClassDiagram(diagram = DiagramValidation.class)
@UmlClassDiagram(diagram = DiagramCommandLine.class)
@UmlExcludeSuperTypes
public class ArgumentList implements Iterable<Argument>, AsString, Sized
{
    /** The arguments */
    @UmlAggregation
    private final ObjectList<Argument> arguments = new ObjectList<>();

    /**
     * Adds an argument to this list
     */
    public void add(final Argument argument)
    {
        arguments.add(argument);
    }

    /**
     * @return All values in this list converted using the given parser
     */
    @UmlExcludeMember
    public <T> List<T> argumentValues(final ArgumentParser<T> parser)
    {
        final var arguments = new ArrayList<T>();
        for (final var argument : this)
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
    public Argument get(final int index)
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

    @NotNull
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
}
