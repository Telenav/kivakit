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

import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.interfaces.comparison.Matcher;

import java.util.Collection;
import java.util.Set;

/**
 * @author jonathanl (shibo)
 */
public class ObjectSet<T> extends BaseSet<T>
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

    public ObjectSet(Maximum maximumSize, Set<T> set)
    {
        super(maximumSize, set);
    }

    public ObjectSet(Set<T> set)
    {
        super(Maximum.MAXIMUM, set);
    }

    public ObjectSet()
    {
        this(Maximum.MAXIMUM);
    }

    public boolean addIfNotNull(T object)
    {
        if (object != null)
        {
            return add(object);
        }
        return false;
    }

    @Override
    public ObjectSet<T> copy()
    {
        return (ObjectSet<T>) super.copy();
    }

    @Override
    public ObjectSet<T> matching(final Matcher<T> matcher)
    {
        return (ObjectSet<T>) super.matching(matcher);
    }

    @Override
    public ObjectSet<T> onNewInstance()
    {
        return new ObjectSet<>();
    }

    @Override
    public ObjectSet<T> with(Collection<T> that)
    {
        return (ObjectSet<T>) super.with(that);
    }
}
