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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.collections.internal.lexakai.DiagramMap;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Comparator;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.value.count.Maximum.MAXIMUM;

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
 * <p><b>Access</b></p>
 *
 * <ul>
 *     <li>{@link #compute(Object, BiFunction)}</li>
 *     <li>{@link #computeIfAbsent(Object, Function)}</li>
 *     <li>{@link #computeIfPresent(Object, BiFunction)}</li>
 *     <li>{@link #get(Object)}</li>
 *     <li>{@link #get(Object, Object)}</li>
 *     <li>{@link #get(int)}</li>
 *     <li>{@link #iterator()}</li>
 *     <li>{@link #keySet()}</li>
 *     <li>{@link #put(Object, Object)}</li>
 *     <li>{@link #putAll(Map)}</li>
 *     <li>{@link #putIfAbsent(Object, Object)}</li>
 *     <li>{@link #putIfNotNull(Object, Object)}</li>
 *     <li>{@link #values()}</li>
 * </ul>
 *
 * <p><b>Operations</b></p>
 *
 * <ul>
 *     <li>{@link #clear()}</li>
 *     <li>{@link #join()}</li>
 *     <li>{@link #join(String)}</li>
 *     <li>{@link #join(char)}</li>
 *     <li>{@link #remove(Object)}</li>
 *     <li>{@link #sort(Comparator)}</li>
 * </ul>
 *
 * @param <T> The type implementing {@link Named}
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMap.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class IndexedNameMap<T extends Named> extends BaseIndexedMap<String, T>
{
    public IndexedNameMap()
    {
        super(MAXIMUM);
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
