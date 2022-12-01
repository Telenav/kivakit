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
import com.telenav.kivakit.core.internal.lexakai.DiagramIteration;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Iterator;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * An {@link Iterable} that wraps another iterable, removing duplicate values. The iterated values must correctly
 * implement the {@link #hashCode()} / {@link #equals(Object)} contract.
 *
 * @author Junwei
 * @version 1.0.0 2013-7-1
 * @see DeduplicatingIterator
 */
@SuppressWarnings("SpellCheckingInspection")
@UmlClassDiagram(diagram = DiagramIteration.class)
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class DeduplicatingIterable<Value> implements Iterable<Value>
{
    /** The underlying iterable */
    private final Iterable<Value> iterable;

    /**
     * @param iterable The iterable to deduplicate
     */
    public DeduplicatingIterable(Iterable<Value> iterable)
    {
        this.iterable = iterable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Value> iterator()
    {
        return new DeduplicatingIterator<>(iterable.iterator());
    }
}
