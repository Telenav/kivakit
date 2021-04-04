////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.patterns.group;

import com.telenav.kivakit.core.kernel.language.patterns.Pattern;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguagePattern;
import com.telenav.kivakit.core.kernel.data.conversion.Converter;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.regex.Matcher;

/**
 * A Group is a piece of a regular expression (referenced by some Java field or local variable) that forms a "capturing
 * group" within the larger regular expression. A Group is bound to a regular expression MetaPattern when a matcher is
 * retrieved for the pattern by calling one of the matcher() methods. Once bound, a Group cannot be rebound.
 *
 * @author Jonathan Locke
 */
@UmlClassDiagram(diagram = DiagramLanguagePattern.class)
public class Group<T> extends Pattern
{
    /** The capturing group that this Group is bound to. */
    private int group = -1;

    /** The type converter for this group */
    private final Converter<String, T> converter;

    /** The group pattern */
    private final Pattern pattern;

    /**
     * Constructor.
     *
     * @param pattern MetaPattern to capture
     */
    public Group(final Pattern pattern, final Converter<String, T> converter)
    {
        this.pattern = pattern;
        this.converter = converter;
    }

    /**
     * Binds this capture expression if not already bound.
     *
     * @param group The group to bind to
     */
    @Override
    public int bind(final int group)
    {
        if (this.group == -1)
        {
            this.group = group + 1;
        }
        else
        {
            throw new RuntimeException("Group already bound");
        }
        return pattern.bind(group + 1);
    }

    public Group<T> copy()
    {
        return new Group<>(pattern, converter);
    }

    /**
     * Thread-safe method to retrieve contents of this captured group.
     *
     * @param matcher The matcher from which to retrieve this Group's group
     * @return The captured characters
     */
    public final T get(final Matcher matcher, final T defaultValue)
    {
        final var value = value(matcher);
        return value == null ? defaultValue : converter.convert(value);
    }

    /**
     * Thread-safe method to retrieve contents of this captured group.
     *
     * @param matcher The matcher from which to retrieve this Group's group
     * @return The captured characters
     */
    public final T get(final Matcher matcher)
    {
        final var value = value(matcher);
        return value == null ? null : converter.convert(value);
    }

    @Override
    public String toExpression()
    {
        return "(" + pattern + ")";
    }

    private String value(final Matcher matcher)
    {
        if (group == -1)
        {
            throw new RuntimeException("Group not bound");
        }
        return matcher.group(group);
    }
}
