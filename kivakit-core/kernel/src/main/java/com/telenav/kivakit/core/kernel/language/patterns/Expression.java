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
public class Expression extends Pattern
{
    private final java.util.regex.Pattern pattern;

    public Expression(final String pattern)
    {
        this.pattern = java.util.regex.Pattern.compile(pattern);
    }

    @Override
    public int bind(final int group)
    {
        return group;
    }

    @Override
    public String toExpression()
    {
        return pattern.pattern();
    }
}
