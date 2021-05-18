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

package com.telenav.kivakit.kernel.language.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Collection utility methods
 *
 * @author jonathanl (shibo)
 */
public class Collections
{
    public static <T> T first(final Collection<T> collection)
    {
        final var iterator = collection.iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    public static <T> void repeatedAdd(final Collection<T> collection, final T value, final int times)
    {
        for (var index = 0; index < times; index++)
        {
            collection.add(value);
        }
    }

    public static <T extends Comparable<T>> List<T> sorted(final Collection<T> collection)
    {
        final var list = new ArrayList<>(collection == null ? Set.of() : collection);
        java.util.Collections.sort(list);
        return list;
    }

    public <T> List<T> toList(final Collection<T> collection)
    {
        return new ArrayList<>(collection);
    }

    public <T> Set<T> toSet(final Collection<T> collection)
    {
        return new HashSet<>(collection);
    }
}
