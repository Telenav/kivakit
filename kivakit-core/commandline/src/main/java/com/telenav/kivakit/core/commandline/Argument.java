////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.commandline;

import com.telenav.kivakit.core.commandline.project.lexakai.diagrams.DiagramArgument;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

/**
 * The string value of a single argument in a command line. The string value of the argument can be retrieved with
 * {@link #value()} and it can be retrieved as a typed value with {@link #get(ArgumentParser)}.
 *
 * @author jonathanl (shibo)
 * @see ArgumentParser
 * @see ArgumentList
 * @see CommandLine
 */
@UmlClassDiagram(diagram = DiagramArgument.class)
@UmlRelation(label = "gets value with", referent = ArgumentParser.class)
public class Argument
{
    /** The argument's string value */
    private final String value;

    public Argument(final String value)
    {
        this.value = value;
    }

    /**
     * @param parser The argument parser
     * @return The value of this argument using the given argument parser
     */
    public <T> T get(final ArgumentParser<T> parser)
    {
        final var value = parser.get(this);
        if (value == null)
        {
            parser.parent().exit("Unable to parse argument: " + this.value);
        }
        return value;
    }

    @Override
    public String toString()
    {
        return value;
    }

    /**
     * @return The string value of this argument
     */
    public String value()
    {
        return value;
    }
}
