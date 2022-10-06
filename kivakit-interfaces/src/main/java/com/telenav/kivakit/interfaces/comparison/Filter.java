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
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Predicate;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A filter which matches values allowing for boolean expressions. All {@link Filter}s are {@link Matcher}s and
 * therefore also {@link Predicate}s.
 *
 * <p><b>Interface Methods</b></p>
 *
 * <ul>
 *     <li>{@link #accepts(Object)} - True if this filter accepts the given value</li>
 * </ul>
 *
 * <p><b>Factory Methods</b></p>
 *
 * <ul>
 *     <li>{@link #accept(Predicate)}</li>
 *     <li>{@link #accept(Collection)}</li>
 *     <li>{@link #acceptAll()}</li>
 *     <li>{@link #acceptNone()}</li>
 * </ul>
 *
 * <p><b>Logical Operations</b></p>
 *
 * <ul>
 *     <li>{@link #and(Predicate)}</li>
 *     <li>{@link #or(Predicate)}</li>
 *     <li>{@link #not()}</li>
 *     <li>{@link #include(Predicate)}</li>
 *     <li>{@link #exclude(Predicate)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramComparison.class)
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public interface Filter<Value> extends Matcher<Value>
{
    /**
     * @return A filter that accepts value matching the given predicate
     * <p>
     * Note: All {@link Matcher}s and {@link Filter}s are {@link Predicate}s).
     * </p>
     */
    static <T> Filter<T> accept(Predicate<T> predicate)
    {
        return predicate::test;
    }

    /**
     * @param values Collection of values that are acceptable
     * @return A filter that accepts values in the given collection
     */
    static <T> Filter<T> accept(Collection<T> values)
    {
        return values::contains;
    }

    /**
     * @return A filter that accepts all values
     */
    static <T> Filter<T> acceptAll()
    {
        return ignored -> true;
    }

    /**
     * @return A filter that accepts no values
     */
    static <T> Filter<T> acceptNone()
    {
        return ignored -> false;
    }

    /**
     * Returns true if this filter accepts the given value
     *
     * @return True if this filter accepts the given value
     */
    boolean accepts(Value value);

    /**
     * @return A filter that accepts a value if this filter and the given predicate both accept the value.
     * <p>
     * Note: All {@link Matcher}s and {@link Filter}s are {@link Predicate}s).
     * </p>
     */
    @Override
    default Filter<Value> and(@NotNull Predicate<? super Value> predicate)
    {
        return value -> accepts(value) && predicate.test(value);
    }

    /**
     * @return A filter that excludes values matched by the given predicate.
     * <p>
     * Note: All {@link Matcher}s and {@link Filter}s are {@link Predicate}s).
     * </p>
     */
    default Filter<Value> exclude(Predicate<Value> predicate)
    {
        return and(predicate.negate());
    }

    /**
     * @return A filter that includes values matched by this filter or the given predicate.
     * <p>
     * Note: All {@link Matcher}s and {@link Filter}s are {@link Predicate}s).
     * </p>
     */
    default Filter<Value> include(Predicate<Value> predicate)
    {
        return or(predicate);
    }

    @Override
    default boolean matches(Value value)
    {
        return accepts(value);
    }

    /**
     * @return A filter that matches values that this filter does not match.
     */
    default Filter<Value> not()
    {
        return value -> !accepts(value);
    }

    /**
     * @return A filter that includes values matched by this filter or the given predicate.
     * <p>
     * Note: All {@link Matcher}s and {@link Filter}s are {@link Predicate}s).
     * </p>
     */
    @Override
    default Filter<Value> or(@NotNull Predicate<? super Value> predicate)
    {
        return value -> accepts(value) || predicate.test(value);
    }
}
