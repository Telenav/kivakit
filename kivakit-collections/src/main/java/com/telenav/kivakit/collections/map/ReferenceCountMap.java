////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.collections.map;

import com.telenav.kivakit.collections.project.lexakai.diagrams.DiagramMap;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.language.values.count.MutableCount;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.HashMap;
import java.util.Map;

/**
 * A non-thread-safe map of reference counts.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMap.class)
public class ReferenceCountMap<Key>
{
    private final Map<Key, MutableCount> referenceCount = new HashMap<>();

    /**
     * @return The reference count for the given object
     */
    public Count count(final Key object)
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
    public void count(final Key object, final Count count)
    {
        referenceCount.put(object, new MutableCount(count.asInt()));
    }

    /**
     * Decreases the reference count of the given object
     */
    public void dereference(final Key object)
    {
        referenceCount.get(object).decrement();
    }

    /**
     * @return True if the given object is referenced
     */
    public boolean isReferenced(final Key object)
    {
        return referenceCount.get(object).asLong() > 0L;
    }

    /**
     * Increases the reference count of the given object
     */
    public void reference(final Key key)
    {
        referenceCount.get(key).increment();
    }
}
