////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.comparison;

import com.telenav.kivakit.core.kernel.messaging.filters.operators.All;
import com.telenav.kivakit.core.kernel.messaging.filters.operators.Not;
import com.telenav.kivakit.core.kernel.messaging.filters.operators.And;
import com.telenav.kivakit.core.kernel.messaging.filters.operators.None;
import com.telenav.kivakit.core.kernel.messaging.filters.operators.Or;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceComparison;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A filter which matches values allowing for boolean expressions
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceComparison.class)
public interface Filter<T> extends Matcher<T>
{
    boolean accepts(T value);

    default Filter<T> all()
    {
        return new All<>();
    }

    default Filter<T> and(final Filter<T> b)
    {
        return new And<>(this, b);
    }

    default Filter<T> exclude(final Filter<T> b)
    {
        return and(b.not());
    }

    default Filter<T> include(final Filter<T> b)
    {
        return or(b);
    }

    @Override
    default boolean matches(final T value)
    {
        return accepts(value);
    }

    default Filter<T> none()
    {
        return new None<>();
    }

    default Filter<T> not()
    {
        return new Not<>(this);
    }

    default Filter<T> or(final Filter<T> b)
    {
        return new Or<>(this, b);
    }
}
