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

import java.util.regex.Pattern;

/**
 * A {@link Matcher} that matches objects whose name matches the given regular expression {@link Pattern}
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageMatchers.class)
public class WithNameMatching<T extends Named> implements Matcher<T>
{
    private final Pattern pattern;

    public WithNameMatching(final Pattern pattern)
    {
        this.pattern = pattern;
    }

    @Override
    public boolean matches(final T value)
    {
        return pattern.matcher(value.name()).matches();
    }
}
