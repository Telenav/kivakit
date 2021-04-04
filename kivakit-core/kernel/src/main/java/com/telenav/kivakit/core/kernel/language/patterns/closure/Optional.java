////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.patterns.closure;

import com.telenav.kivakit.core.kernel.language.patterns.Pattern;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguagePattern;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Makes any pattern optional by enclosing the pattern in an optionality expression. The expression will be something
 * equivalent to "(?:&lt;pattern&gt;)?".
 *
 * @author Jonathan Locke
 */
@UmlClassDiagram(diagram = DiagramLanguagePattern.class)
public final class Optional extends Pattern
{
    private final Pattern pattern;

    public Optional(final Pattern pattern)
    {
        this.pattern = pattern;
    }

    @Override
    public int bind(final int group)
    {
        return pattern.bind(group);
    }

    @Override
    public String toExpression()
    {
        return "(?:" + pattern + ")?";
    }
}
