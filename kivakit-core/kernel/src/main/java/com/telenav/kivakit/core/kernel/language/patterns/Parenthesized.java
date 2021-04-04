////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.patterns;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguagePattern;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramLanguagePattern.class)
public final class Parenthesized extends Pattern
{
    private final Pattern pattern;

    public Parenthesized(final Pattern pattern)
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
        return "(?:" + pattern + ")";
    }
}
