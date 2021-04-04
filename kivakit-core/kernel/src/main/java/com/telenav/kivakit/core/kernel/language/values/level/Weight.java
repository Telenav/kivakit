////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.values.level;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A weight from 0 to 1 for weighting in mathematics.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageValue.class)
public class Weight extends Level
{
    public static Weight weight(final double value)
    {
        return new Weight(value);
    }

    protected Weight()
    {
        super();
    }

    private Weight(final double value)
    {
        super(value);
    }

    @Override
    public Weight divide(final Level that)
    {
        return (Weight) super.divide(that);
    }

    @Override
    public Weight inverse()
    {
        return (Weight) super.inverse();
    }

    @Override
    public Weight minus(final Level that)
    {
        return (Weight) super.minus(that);
    }

    @Override
    public Weight plus(final Level that)
    {
        return (Weight) super.plus(that);
    }

    @Override
    public Weight reciprocal()
    {
        return (Weight) super.reciprocal();
    }

    @Override
    public Weight times(final Level that)
    {
        return (Weight) super.times(that);
    }

    @Override
    protected Level onNewInstance(final double value)
    {
        return new Weight(value);
    }
}
