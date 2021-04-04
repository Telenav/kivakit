////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.values.level;

import com.telenav.kivakit.core.kernel.interfaces.naming.Named;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UmlClassDiagram(diagram = DiagramLanguageValue.class)
public class Priority extends Level implements Named, Comparable<Priority>
{
    private static final Map<Double, Priority> priorityForDouble = new HashMap<>();

    public static final Priority BLOCKER = new Priority("Blocker", 0.95);

    public static final Priority HIGH = new Priority("High", 0.9);

    public static final Priority MEDIUM = new Priority("Medium", 0.5);

    public static final Priority LOW = new Priority("Low", 0.1);

    public static List<Priority> all()
    {
        final List<Priority> all = new ArrayList<>(priorityForDouble.values());
        Collections.sort(all);
        return all;
    }

    public static Priority priority(final double value)
    {
        return priorityForDouble.get(value);
    }

    private String name;

    public Priority(final double value)
    {
        super(value);
    }

    private Priority(final String name, final double value)
    {
        super(value);
        this.name = name;
        priorityForDouble.put(value, this);
    }

    @Override
    public int compareTo(final Priority that)
    {
        return Double.compare(that.asZeroToOne(), asZeroToOne());
    }

    @Override
    public Priority divide(final Level that)
    {
        return (Priority) super.divide(that);
    }

    @Override
    public Priority inverse()
    {
        return (Priority) super.inverse();
    }

    public Priority maximum(final Priority that)
    {
        return asZeroToOne() > that.asZeroToOne() ? this : that;
    }

    @Override
    public Priority minus(final Level that)
    {
        return (Priority) super.minus(that);
    }

    @Override
    public String name()
    {
        return name;
    }

    @Override
    public Priority plus(final Level that)
    {
        return (Priority) super.plus(that);
    }

    @Override
    public Priority reciprocal()
    {
        return (Priority) super.reciprocal();
    }

    @Override
    public Priority times(final Level that)
    {
        return (Priority) super.times(that);
    }

    @Override
    public String toString()
    {
        return name != null ? name() : super.toString();
    }

    @Override
    protected Level onNewInstance(final double value)
    {
        return new Priority(name, value);
    }
}
