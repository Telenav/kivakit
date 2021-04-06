////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.comparison;

import com.telenav.kivakit.core.kernel.messaging.filters.operators.All;
import com.telenav.kivakit.core.kernel.messaging.filters.operators.Not;
import com.telenav.kivakit.core.kernel.messaging.filters.operators.And;
import com.telenav.kivakit.core.kernel.messaging.filters.operators.None;
import com.telenav.kivakit.core.kernel.messaging.filters.operators.Or;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceComparison;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A filter which matches values allowing for boolean expressions
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceComparison.class)
public interface Filter<T> extends Matcher<T>
{
    boolean accepts(T value);

    default Filter<T> all()
    {
        return new All<>();
    }

    default Filter<T> and(final Filter<T> b)
    {
        return new And<>(this, b);
    }

    default Filter<T> exclude(final Filter<T> b)
    {
        return and(b.not());
    }

    default Filter<T> include(final Filter<T> b)
    {
        return or(b);
    }

    @Override
    default boolean matches(final T value)
    {
        return accepts(value);
    }

    default Filter<T> none()
    {
        return new None<>();
    }

    default Filter<T> not()
    {
        return new Not<>(this);
    }

    default Filter<T> or(final Filter<T> b)
    {
        return new Or<>(this, b);
    }
}
