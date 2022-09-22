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

package com.telenav.kivakit.core.collections.iteration;

import com.telenav.kivakit.annotations.code.ApiStability;
import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramIteration;
import com.telenav.kivakit.interfaces.collection.NextValue;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static com.telenav.kivakit.annotations.code.DocumentationQuality.SUFFICIENT;
import static com.telenav.kivakit.annotations.code.TestingQuality.NONE;

/**
 * Implements the {@link Iterable} interface by using a {@link NextValue} object to find the next value when iterating.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramIteration.class)
@CodeQuality(stability = ApiStability.STABLE,
             testing = NONE,
             documentation = SUFFICIENT)
public abstract class BaseIterable<T> implements Iterable<T>
{
    /** A filter to restrict values in the sequence */
    private Matcher<T> matcher = Matcher.matchAll();

    /**
     * @param filter The filter to apply to this sequence
     */
    public BaseIterable<T> matching(Matcher<T> filter)
    {
        this.matcher = filter;
        return this;
    }

    @Override
    public final @NotNull Iterator<T> iterator()
    {
        return new BaseIterator<>()
        {
            private final NextValue<T> next = newNext();

            @Override
            protected T onNext()
            {
                for (var at = next.next(); at != null; at = next.next())
                {
                    if (matcher.matches(at))
                    {
                        return at;
                    }
                }
                return null;
            }
        };
    }

    /**
     * @return A new {@link NextValue} implementation for finding the next value in a sequence
     */
    @UmlRelation(label = "creates")
    protected abstract NextValue<T> newNext();
}
