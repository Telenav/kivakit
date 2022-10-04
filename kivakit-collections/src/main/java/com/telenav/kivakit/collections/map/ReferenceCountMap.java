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

package com.telenav.kivakit.collections.map;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.collections.internal.lexakai.DiagramMap;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.MutableCount;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.IdentityHashMap;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A non-thread-safe map of reference counts. Counts are increased by calling {@link #reference(Object)}, and decreased
 * by calling {@link #dereference(Object)}. The method {@link #isReferenced(Object)} returns true if there is any
 * remaining reference to the given object.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMap.class)
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public class ReferenceCountMap<Key>
{
    private final Map<Key, MutableCount> referenceCount = new IdentityHashMap<>();

    /**
     * @return The reference count for the given object
     */
    public Count count(Key object)
    {
        var count = referenceCount.get(object);
        if (count == null || count.isZero())
        {
            count = new MutableCount(1);
            referenceCount.put(object, count);
        }
        return count.asCount();
    }

    /**
     * Sets the count of the given object
     */
    public void count(Key object, Count count)
    {
        referenceCount.put(object, new MutableCount(count.asInt()));
    }

    /**
     * Decreases the reference count of the given object
     */
    public void dereference(Key object)
    {
        referenceCount.get(object).decrement();
    }

    /**
     * @return True if the given object is referenced
     */
    public boolean isReferenced(Key object)
    {
        return referenceCount.get(object).asLong() > 0L;
    }

    /**
     * Increases the reference count of the given object
     */
    public void reference(Key key)
    {
        referenceCount.get(key).increment();
    }
}
