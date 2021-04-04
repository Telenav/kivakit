////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.values.identifier;

import com.telenav.kivakit.core.kernel.interfaces.factory.Factory;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A thread-safe factory that produces identifiers
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageValue.class)
public class IdentifierFactory implements Factory<Identifier>
{
    private final AtomicLong next;

    public IdentifierFactory()
    {
        this(0);
    }

    public IdentifierFactory(final long base)
    {
        next = new AtomicLong(base);
    }

    @Override
    public Identifier newInstance()
    {
        return new Identifier(next.incrementAndGet());
    }
}
