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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramCollections;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * This class implements an iterator which can deduplicate the objects returned by the wrapped iterator. Each element
 * returned by the wrapped iterator is kept in a {@link Set} and only values that have not been seen before are produced
 * as part of the sequence for the caller. The iterated values must correctly implement the {@link #hashCode()} /
 * {@link #equals(Object)} contract.
 *
 * @author Junwei
 * @author jonathanl (shibo)
 * @version 1.0.0 2012-8-27
 */
@SuppressWarnings("SpellCheckingInspection")
@UmlClassDiagram(diagram = DiagramCollections.class)
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class DeduplicatingIterator<Value> extends BaseIterator<Value>
{
    /** The iterator to deduplicate */
    private final Iterator<Value> iterator;

    /** The set of values already seen */
    private final Set<Value> seen = new HashSet<>();

    /**
     * @param iterator The iterator to deduplicate
     */
    public DeduplicatingIterator(Iterator<Value> iterator)
    {
        this.iterator = iterator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Value onNext()
    {
        if (iterator.hasNext())
        {
            var next = iterator.next();
            if (!seen.contains(next))
            {
                seen.add(next);
                return next;
            }
            else
            {
                return onNext();
            }
        }
        else
        {
            return null;
        }
    }
}
