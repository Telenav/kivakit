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

@UmlClassDiagram(diagram = DiagramLanguagePattern.class)
public final class ZeroOrMore extends Closure
{
    private final Pattern pattern;

    public ZeroOrMore(final Pattern pattern)
    {
        this(pattern, false);
    }

    public ZeroOrMore(final Pattern pattern, final boolean greedy)
    {
        super(greedy);
        this.pattern = pattern;
    }

    @Override
    public int bind(final int group)
    {
        return pattern.bind(group);
    }

    public ZeroOrMore greedy()
    {
        return new ZeroOrMore(pattern, true);
    }

    public ZeroOrMore nonGreedy()
    {
        return new ZeroOrMore(pattern, false);
    }

    @Override
    public String toExpression()
    {
        return pattern + "*" + greed();
    }
}
