////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.comparison;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceComparison;

import java.util.function.Predicate;

/**
 * A matcher when the term predicate is often not the right meaning to use. Implements Predicate for interoperation.
 *
 * @param <Value> The type of value to match
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceComparison.class)
public interface Matcher<Value> extends Predicate<Value>
{
    /**
     * @return True if the given value matches
     */
    boolean matches(Value value);

    /**
     * {@inheritDoc}
     */
    @Override
    default boolean test(final Value value)
    {
        return matches(value);
    }
}
