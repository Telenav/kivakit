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

package com.telenav.kivakit.kernel.language.collections.set;

import com.telenav.kivakit.kernel.language.values.count.Maximum;

import java.util.Collection;
import java.util.Set;

/**
 * @author jonathanl (shibo)
 */
public class ObjectSet<T> extends BaseSet<T>
{
    public static ObjectSet<T> of(T... objects)
    {
        var set = new ObjectSet<>();
        set.addAll(objects);
        return set;
    }

    public ObjectSet(final Maximum maximumSize)
    {
        super(maximumSize);
    }

    public ObjectSet(final Maximum maximumSize, final Set<T> set)
    {
        super(maximumSize, set);
    }

    public ObjectSet()
    {
        this(Maximum.MAXIMUM);
    }

    @Override
    public ObjectSet<T> copy()
    {
        return (ObjectSet<T>) super.copy();
    }

    @Override
    public ObjectSet<T> onNewInstance()
    {
        return new ObjectSet<>();
    }

    @Override
    public ObjectSet<T> with(final Collection<T> that)
    {
        return (ObjectSet<T>) super.with(that);
    }
}
