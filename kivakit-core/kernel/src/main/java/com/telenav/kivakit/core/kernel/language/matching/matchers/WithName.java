////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.matching.matchers;

import com.telenav.kivakit.core.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.core.kernel.interfaces.naming.Named;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageMatchers;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A {@link Matcher} that matches objects with the given name
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageMatchers.class)
public class WithName<T extends Named> implements Matcher<T>
{
    private final String name;

    public WithName(final Named named)
    {
        name = named.name();
    }

    public WithName(final String name)
    {
        this.name = name;
    }

    @Override
    public boolean matches(final T value)
    {
        return value.name().equals(name);
    }
}
