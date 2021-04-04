////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.commandline;

import com.telenav.kivakit.core.commandline.project.lexakai.diagrams.DiagramSwitch;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.kivakit.core.kernel.interfaces.naming.Named;
import com.telenav.kivakit.core.kernel.language.objects.Hash;

/**
 * Represents a switch argument, with a name and value.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramSwitch.class)
@UmlRelation(label = "gets value with", referent = SwitchParser.class)
public class Switch extends Argument implements Named
{
    /** The name of this switch argument */
    private final String name;

    /**
     * @param name The name of the switch
     * @param value The switch's value
     */
    public Switch(final String name, final String value)
    {
        super(value);
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof Switch)
        {
            final var that = (Switch) object;
            return name.equals(that.name);
        }
        return false;
    }

    /**
     * @return The typed value of this switch converted using the given switch parser
     */
    public <T> T get(final SwitchParser<T> parser)
    {
        return parser.get(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return Hash.many(name());
    }

    /**
     * @return The name of this switch
     */
    @Override
    public String name()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return "-" + name() + "=" + value();
    }
}
