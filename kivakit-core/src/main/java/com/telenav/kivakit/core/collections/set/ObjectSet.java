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

import com.telenav.kivakit.core.collections.BaseCollection;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.interfaces.comparison.Matcher;

import java.util.Collection;
import java.util.Set;

/**
 * @author jonathanl (shibo)
 */
public class ObjectSet<Value> extends BaseSet<Value>
{
    public static <T> ObjectSet<T> emptyObjectSet()
    {
        return new ObjectSet<>();
    }

    @SafeVarargs
    public static <T> ObjectSet<T> objectSet(T... objects)
    {
        var set = new ObjectSet<T>();
        set.addAll(objects);
        return set;
    }

    public static <T> ObjectSet<T> objectSet(Collection<T> objects)
    {
        var set = new ObjectSet<T>();
        set.addAll(objects);
        return set;
    }

    public ObjectSet(Maximum maximumSize)
    {
        super(maximumSize);
    }

    public ObjectSet(Maximum maximumSize, Set<Value> set)
    {
        super(maximumSize, set);
    }

    public ObjectSet(Set<Value> set)
    {
        super(Maximum.MAXIMUM, set);
    }

    public ObjectSet()
    {
        this(Maximum.MAXIMUM);
    }

    @Override
    public boolean addIfNotNull(Value object)
    {
        if (object != null)
        {
            return add(object);
        }
        return false;
    }

    @Override
    public ObjectList<Value> asList()
    {
        return (ObjectList<Value>) super.asList();
    }

    @Override
    public ObjectSet<Value> copy()
    {
        return (ObjectSet<Value>) super.copy();
    }

    @Override
    public ObjectSet<Value> matching(Matcher<Value> matcher)
    {
        return (ObjectSet<Value>) super.matching(matcher);
    }

    @Override
    public BaseSet<Value> onNewInstance()
    {
        return newSet();
    }

    @Override
    public ObjectSet<Value> with(Value value)
    {
        return (ObjectSet<Value>) super.with(value);
    }

    @Override
    public ObjectSet<Value> with(Collection<Value> that)
    {
        return (ObjectSet<Value>) super.with(that);
    }

    @Override
    protected ObjectSet<Value> newSet()
    {
        return objectSet();
    }

    @Override
    protected BaseCollection<Value> onNewCollection()
    {
        return objectSet();
    }
}
