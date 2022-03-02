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

import com.telenav.kivakit.core.project.lexakai.DiagramCollections;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * An {@link Iterator} that wraps and iterates through a sequence of {@link Iterator}s in the order they are added,
 * either by constructor or with {@link #add(Iterator)}.
 *
 * @author jonathanl (shibo)
 * @see BaseIterator
 */
@UmlClassDiagram(diagram = DiagramCollections.class)
public class CompoundIterator<Element> extends BaseIterator<Element>
{
    final List<Iterator<Element>> iterators = new ArrayList<>();

    private int index;

    public CompoundIterator()
    {
    }

    @SafeVarargs
    public CompoundIterator(Iterator<Element> iterator, Iterator<Element>... iterators)
    {
        add(iterator);
        Collections.addAll(this.iterators, iterators);
    }

    public void add(Iterator<Element> iterator)
    {
        iterators.add(iterator);
    }

    public void addAll(Collection<Iterator<Element>> iterators)
    {
        this.iterators.addAll(iterators);
    }

    @Override
    protected Element onNext()
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
