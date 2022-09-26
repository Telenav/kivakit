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

import com.telenav.kivakit.core.internal.lexakai.DiagramCollections;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A convenient implementation of {@link Set} using {@link ConcurrentHashMap}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramCollections.class)
@LexakaiJavadoc(complete = true)
public class IdentitySet<Value> extends BaseSet<Value>
{
    private final IdentityHashMap<Value, Value> map = new IdentityHashMap<>();

    public IdentitySet()
    {
        this(Maximum.MAXIMUM);
    }

    public IdentitySet(Maximum maximumSize)
    {
        this(maximumSize, Collections.emptySet());
    }

    public IdentitySet(Maximum maximumSize, Collection<Value> values)
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

    @Override
    public IdentitySet<Value> onNewInstance()
    {
        return (IdentitySet<Value>) newSet();
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

    @Override
    protected Set<Value> newSet()
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
            @SuppressWarnings("SuspiciousMethodCalls")
            public boolean contains(Object value)
            {
                return map.containsKey(value);
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

    @Override
    protected IdentitySet<Value> onNewCollection()
    {
        return new IdentitySet<>();
    }

    @Override
    protected IdentitySet<Value> set()
    {
        return (IdentitySet<Value>) super.set();
    }
}
