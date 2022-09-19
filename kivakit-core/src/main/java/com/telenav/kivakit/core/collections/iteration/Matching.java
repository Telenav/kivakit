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

import java.util.Iterator;

import static com.telenav.kivakit.annotations.code.DocumentationQuality.COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.NONE;

/**
 * An iterable that does matching during iteration.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramIteration.class)
@CodeQuality(stability = ApiStability.STABLE,
             testing = NONE,
             documentation = COMPLETE)
public abstract class Matching<T> extends BaseIterable<T>
{
    private final Matcher<T> matcher;

    protected Matching(Matcher<T> matcher)
    {
        this.matcher = matcher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected NextValue<T> newNext()
    {
        return new NextValue<>()
        {
            private final Iterator<T> values = values();

            @Override
            public T next()
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

    /**
     * @return Iterator over values
     */
    protected abstract Iterator<T> values();
}
