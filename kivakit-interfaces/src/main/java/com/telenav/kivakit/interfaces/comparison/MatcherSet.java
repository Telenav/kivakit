////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.interfaces.comparison;

import com.telenav.kivakit.interfaces.internal.lexakai.DiagramComparison;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.IdentityHashMap;
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
@UmlClassDiagram(diagram = DiagramComparison.class)
public class MatcherSet<T> implements Matcher<T>
{
    private final IdentityHashMap<Matcher<T>, Boolean> matchers = new IdentityHashMap<>();

    public MatcherSet<T> add(final Matcher<T> matcher)
    {
        matchers.put(matcher, true);
        return this;
    }

    public Matcher<T> allMatch()
    {
        return value ->
        {
            for (final Matcher<T> matcher : matchers())
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
            for (final Matcher<T> matcher : matchers())
            {
                if (matcher.matches(value))
                {
                    return true;
                }
            }
            return false;
        };
    }

    public MatcherSet<T> copy()
    {
        var copy = new MatcherSet<T>();
        copy.matchers.putAll(matchers);
        return copy;
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
            for (final Matcher<T> matcher : matchers())
            {
                if (matcher.matches(value))
                {
                    return false;
                }
            }
            return true;
        };
    }

    @NotNull
    private Set<Matcher<T>> matchers()
    {
        return matchers.keySet();
    }
}
