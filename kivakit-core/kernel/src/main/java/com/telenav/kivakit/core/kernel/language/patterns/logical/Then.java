////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.patterns.logical;

import com.telenav.kivakit.core.kernel.language.patterns.Pattern;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguagePattern;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramLanguagePattern.class)
public final class Then extends Pattern
{
    private final Pattern a;

    private final Pattern b;

    public Then(final Pattern a, final Pattern b)
    {
        this.a = a;
        this.b = b;
    }

    @Override
    public int bind(final int group)
    {
        return b.bind(a.bind(group));
    }

    @Override
    public String toExpression()
    {
        return a.toExpression() + b.toExpression();
    }
}
