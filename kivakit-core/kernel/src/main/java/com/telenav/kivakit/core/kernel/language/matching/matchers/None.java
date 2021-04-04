////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.matching.matchers;

import com.telenav.kivakit.core.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageMatchers;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A {@link Matcher} that matches no values.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageMatchers.class)
public class None<T> implements Matcher<T>
{
    @Override
    public boolean matches(final T value)
    {
        return false;
    }
}
