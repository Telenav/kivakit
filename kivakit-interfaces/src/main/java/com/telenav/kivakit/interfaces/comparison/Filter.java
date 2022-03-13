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

import com.telenav.kivakit.interfaces.project.lexakai.DiagramComparison;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * A filter which matches values allowing for boolean expressions
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramComparison.class)
public interface Filter<Value> extends Matcher<Value>
{
    static <T> Filter<T> all()
    {
        return ignored -> true;
    }

    static <T> Filter<T> none()
    {
        return ignored -> false;
    }

    boolean accepts(Value value);

    default Filter<Value> and(@NotNull Predicate<? super Value> predicate)
    {
        return filter(value -> accepts(value) && predicate.test(value));
    }

    default Filter<Value> exclude(Predicate<Value> predicate)
    {
        return and(predicate.negate());
    }

    default <T> Filter<T> filter(Predicate<T> predicate)
    {
        return predicate::test;
    }

    default Filter<Value> include(Predicate<Value> predicate)
    {
        return filter(or(predicate));
    }

    @Override
    default boolean matches(Value value)
    {
        return accepts(value);
    }

    default Filter<Value> not()
    {
        return filter(negate());
    }

    default Filter<Value> or(@NotNull Predicate<? super Value> predicate)
    {
        return filter(value -> accepts(value) || predicate.test(value));
    }

    @Override
    default boolean test(Value value)
    {
        return accepts(value);
    }
}
