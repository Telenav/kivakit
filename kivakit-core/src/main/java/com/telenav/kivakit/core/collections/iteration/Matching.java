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

import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.core.lexakai.DiagramIteration;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Iterator;

/**
 * An iterable that does matching during iteration.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramIteration.class)
public abstract class Matching<T> extends BaseIterable<T>
{
    private final Matcher<T> matcher;

    protected Matching(Matcher<T> matcher)
    {
        this.matcher = matcher;
    }

    @Override
    protected Next<T> newNext()
    {
        return new Next<>()
        {
            private final Iterator<T> values = values();

            @Override
            public T onNext()
            {
                while (values.hasNext())
                {
                    var value = values.next();
                    if (matcher.matches(value))
                    {
                        return value;
                    }
                }
                return null;
            }
        };
    }

    protected abstract Iterator<T> values();
}
