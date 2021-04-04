////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.iteration;

import com.telenav.kivakit.core.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageMatchers;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Iterator;

/**
 * An iterable that does matching during iteration.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageMatchers.class)
public abstract class Matching<T> extends BaseIterable<T>
{
    private final Matcher<T> matcher;

    protected Matching(final Matcher<T> matcher)
    {
        this.matcher = matcher;
    }

    @Override
    protected Next<T> newNext()
    {
        return new Next<>()
        {
            private final Iterator<T> values = values();

            @Override
            public T onNext()
            {
                while (values.hasNext())
                {
                    final var value = values.next();
                    if (matcher.matches(value))
                    {
                        return value;
                    }
                }
                return null;
            }
        };
    }

    protected abstract Iterator<T> values();
}
