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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.collections.internal.lexakai.DiagramMap;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Stores named values by name and also makes them accessible by index in the order in which the valAues were added,
 * where <i>0</i> is the first value and <i>n-1</i> is the last value in a map with <i>n</i> entries.
 *
 * <p><b>Example</b></p>
 *
 * <pre>  class Fruit implements Named
 * class Apple extends Fruit
 * class Banana extends Fruit
 *
 * var map = new IndexedNameMap&lt;Fruit&gt;();
 * map.add(new Apple());
 * map.add(new Banana());
 *
 * map.get(0);        // Apple
 * map.get("Apple");  // Apple
 * </pre>
 *
 * @param <T> The type implementing {@link Named}
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMap.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class IndexedNameMap<T extends Named> extends BaseIndexedMap<String, T>
{
    public IndexedNameMap()
    {
        super(Maximum.MAXIMUM);
    }

    public IndexedNameMap(Maximum maximumSize)
    {
        super(maximumSize);
    }

    /**
     * {@inheritDoc}
     *
     * @param value The value to add
     * @return True if the value was added
     */
    @Override
    public boolean onAdd(T value)
    {
        put(value.name(), value);
        return true;
    }
}
