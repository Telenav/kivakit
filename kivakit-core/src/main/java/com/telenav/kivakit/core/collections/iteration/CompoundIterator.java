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

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramCollections;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * An {@link Iterator} that wraps and iterates through all objects in each of a sequence of {@link Iterator}s in the
 * order they are added with {@link #add(Iterator)} or {@link #addAll(Collection)}.
 *
 * @author jonathanl (shibo)
 * @see BaseIterator
 */
@UmlClassDiagram(diagram = DiagramCollections.class)
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class CompoundIterator<Value> extends BaseIterator<Value>
{
    /** The iterators to use */
    final List<Iterator<Value>> iterators = new ArrayList<>();

    /** Which iterator is being used */
    private int index;

    /**
     * Adds the given sub-iterator to this compound iterator
     *
     * @param iterator The iterator to add
     */
    public void add(Iterator<Value> iterator)
    {
        iterators.add(iterator);
    }

    /**
     * Adds the given sub-iterators to this compound iterator
     *
     * @param iterators The iterators to add
     */
    public void addAll(Collection<Iterator<Value>> iterators)
    {
        this.iterators.addAll(iterators);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Value onNext()
    {
        if (index < iterators.size())
        {
            var iterator = iterators.get(index);
            if (iterator.hasNext())
            {
                return iterator.next();
            }
            else
            {
                index++;
                return findNext();
            }
        }
        return null;
    }
}
