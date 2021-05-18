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

package com.telenav.kivakit.kernel.language.iteration;

import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageIteration;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.util.Iterator;

/**
 * Implements the {@link Iterable} interface by using a {@link Next} object to find the next value when iterating.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageIteration.class)
public abstract class BaseIterable<T> implements Iterable<T>
{
    @Override
    public final Iterator<T> iterator()
    {
        return new BaseIterator<>()
        {
            private final Next<T> next = newNext();

            @Override
            protected T onNext()
            {
                return next.onNext();
            }
        };
    }

    /**
     * @return A new {@link Next} implementation for finding the next value in a sequence
     */
    @UmlRelation(label = "creates")
    protected abstract Next<T> newNext();
}
