package com.telenav.kivakit.core.kernel.interfaces.comparison;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceComparison;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.HashSet;
import java.util.Set;

/**
 * A set of matchers with convenient methods to find out if:
 * <ul>
 *     <li>{@link #allMatch()} - all matchers match</li>
 *     <li>{@link #anyMatches()} - at least one matcher matches</li>
 *     <li>{@link #noneMatches()} - no matcher matches</li>
 * </ul>
 * The interface {@link Matcher} is implemented by {@link MatcherSet} by returning true
 * if any matcher matches.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceComparison.class)
public class MatcherSet<T> implements Matcher<T>
{
    private final Set<Matcher<T>> matchers = new HashSet<>();

    public MatcherSet<T> add(final Matcher<T> matcher)
    {
        matchers.add(matcher);
        return this;
    }

    public Matcher<T> allMatch()
    {
        return value ->
        {
            for (final Matcher<T> matcher : matchers)
            {
                if (!matcher.matches(value))
                {
                    return false;
                }
            }
            return true;
        };
    }

    public Matcher<T> anyMatches()
    {
        return value ->
        {
            for (final Matcher<T> matcher : matchers)
            {
                if (matcher.matches(value))
                {
                    return true;
                }
            }
            return false;
        };
    }

    @Override
    public boolean matches(final T value)
    {
        return anyMatches().matches(value);
    }

    public Matcher<T> noneMatches()
    {
        return value ->
        {
            for (final Matcher<T> matcher : matchers)
            {
                if (matcher.matches(value))
                {
                    return false;
                }
            }
            return true;
        };
    }
}
