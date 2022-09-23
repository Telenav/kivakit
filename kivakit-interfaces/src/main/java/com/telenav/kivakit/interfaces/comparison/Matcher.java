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

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.interfaces.internal.lexakai.DiagramComparison;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.SUFFICIENT;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNNECESSARY;

/**
 * A matcher implements {@link Predicate}, but adds some KivaKit-specific functionality.
 *
 * <p><b>Interface Methods</b></p>
 *
 * <ul>
 *     <li>boolean matches(Value value) - Functional interface that matches against a single value, returning
 *     true if it matches and false if it does not</li>
 *     <li>{@link #matcher()} - Default method that implements {@link Matchable} so that all {@link Matcher}s
 *      are {@link Matchable}</li>
 * </ul>
 *
 * <p><b>Factories</b></p>
 *
 * <ul>
 *     <li>{@link #matchAll()} - Matches all values</li>
 *     <li>{@link #matchNothing()} - Matches nothing</li>
 *     <li>{@link #matching(Pattern)} - Matches the given regular expression {@link Pattern}</li>
 *     <li>{@link #matching(Predicate)} - Matches the given {@link Predicate}</li>
 *     <li>{@link #matchObjectNamed(String)} - Matches any {@link Named} object with the given name</li>
 * </ul>
 *
 * @param <Value> The type of value to match
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramComparison.class)
@CodeQuality(stability = STABLE,
             testing = UNNECESSARY,
             documentation = SUFFICIENT)
public interface Matcher<Value> extends
        Matchable<Value>,
        Predicate<Value>
{
    /**
     * @return A matcher that matches all values
     */
    static <T> Matcher<T> matchAll()
    {
        return ignored -> true;
    }

    /**
     * @return A matcher that matches nothing
     */
    static <T> Matcher<T> matchNothing()
    {
        return ignored -> false;
    }

    /**
     * A matcher that matches against a {@link Named} object (case independent).
     *
     * @param name The string to match
     * @return The matcher that performs the match
     */
    static <T extends Named> Matcher<T> matchObjectNamed(String name)
    {
        return named -> named.name().equalsIgnoreCase(name);
    }

    /**
     * @return A matcher matching the given predicate
     */
    static <T> Matcher<T> matching(Predicate<T> predicate)
    {
        return predicate::test;
    }

    /**
     * @param pattern The pattern
     * @return A matcher that matches a regular expression {@link Pattern}
     */
    static Matcher<String> matching(Pattern pattern)
    {
        return value -> pattern.matcher(value).matches();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default Matcher<Value> matcher()
    {
        return this;
    }

    /**
     * @return True if the given value matches this matcher
     */
    boolean matches(Value value);

    @Override
    default boolean test(Value value)
    {
        return matches(value);
    }
}
