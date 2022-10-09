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

package com.telenav.kivakit.core.collections.set;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramCollections;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_INSUFFICIENT;

/**
 * A convenient implementation of {@link Set} using {@link ConcurrentHashMap}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramCollections.class)
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_INSUFFICIENT,
             documentation = DOCUMENTATION_COMPLETE)
public class ConcurrentHashSet<Value> extends BaseSet<Value>
{
    /** The backing map */
    private final ConcurrentHashMap<Value, Value> map = new ConcurrentHashMap<>();

    public ConcurrentHashSet()
    {
        this(Maximum.MAXIMUM);
    }

    public ConcurrentHashSet(Maximum maximumSize)
    {
        this(maximumSize, new HashSet<>());
    }

    public ConcurrentHashSet(Maximum maximumSize, Collection<Value> values)
    {
        super(maximumSize, values);
    }

    /**
     * Gets the value currently in the set is equal to the given prototype object
     *
     * @param prototype The object to match against
     * @return Any object in the current set that matches the given object
     */
    public Value get(Value prototype)
    {
        return map.get(prototype);
    }

    /**
     * Takes any object matching the given prototype out of the set, returning the set's value (but not necessarily the
     * prototype).
     *
     * @param prototype The object to match against
     * @return Any matching object after removing it from the set
     */
    public Value take(Value prototype)
    {
        return map.remove(prototype);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Set<Value> onNewBackingSet()
    {
        return new AbstractSet<>()
        {
            @Override
            public boolean add(Value value)
            {
                map.put(value, value);
                return true;
            }

            @Override
            public void clear()
            {
                map.clear();
            }

            @Override
            public boolean contains(Object value)
            {
                return map.contains(value);
            }

            @Override
            public Iterator<Value> iterator()
            {
                return map.keySet().iterator();
            }

            @Override
            public boolean remove(Object value)
            {
                map.remove(value);
                return true;
            }

            @Override
            public int size()
            {
                return map.size();
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ConcurrentHashSet<Value> onNewCollection()
    {
        return new ConcurrentHashSet<>();
    }
}
