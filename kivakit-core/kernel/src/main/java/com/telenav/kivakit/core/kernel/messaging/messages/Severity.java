////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.messaging.messages;

import com.telenav.kivakit.core.kernel.interfaces.naming.Named;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.values.level.Level;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessaging;

import java.util.*;

/**
 * Represents the severity of the current state of an operation or a step in an operation.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessaging.class)
public final class Severity extends Level implements Named
{
    private static final Map<String, Severity> severities = new HashMap<>();

    public static final Severity TRACE = new Severity("Trace", 0);

    public static final Severity NONE = new Severity("None", 0.05);

    public static final Severity LOW = new Severity("Low", 0.1);

    public static final Severity MEDIUM = new Severity("Medium", 0.5);

    public static final Severity MEDIUM_HIGH = new Severity("Medium High", 0.6);

    public static final Severity HIGH = new Severity("High", 0.7);

    public static final Severity CRITICAL = new Severity("Critical", 0.9);

    public static Severity of(final String name)
    {
        return severities.get(name);
    }

    private String name;

    public Severity(final String name, final double severity)
    {
        super(severity);

        assert name != null;
        this.name = name;

        severities.put(name, this);
    }

    protected Severity()
    {
        super(0.0);
    }

    @Override
    public boolean equals(final Object object)
    {
        // Local fields are not considered
        return super.equals(object);
    }

    @SuppressWarnings("EmptyMethod")
    @Override
    public int hashCode()
    {
        // Local fields are not considered
        return super.hashCode();
    }

    @Override
    public String name()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
