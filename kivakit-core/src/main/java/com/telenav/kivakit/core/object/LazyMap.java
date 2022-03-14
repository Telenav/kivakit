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

package com.telenav.kivakit.core.object;

import com.telenav.kivakit.core.lexakai.DiagramObject;
import com.telenav.kivakit.interfaces.factory.MapFactory;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A lazy-initializing map.
 *
 * <p>
 * Given a {@link MapFactory} that creates values for keys, lazy-creates map entries when {@link #get(Key)} is called.
 * After that the value is cached in the map and {@link #get(Key)} will return the same value. {@link #clear()} can be
 * used to clear the map, and {@link #remove(Key)} can be used to remove the value for a particular key.
 * </p>
 *
 * <p><b>Example</b></p>
 *
 * <pre>
 *  LazyMap&lt;Identifier, Session&gt; session = MapLazy.of(Session::new);
 *
 *     [...]
 *
 *  session.get(identifier);</pre>
 *
 * @param <Value> The type of value to create
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramObject.class)
public class LazyMap<Key, Value>
{
    /**
     * @return A {@link LazyMap} for the given {@link MapFactory}.
     */
    public static <Key, Value> LazyMap<Key, Value> of(MapFactory<Key, Value> factory)
    {
        return new LazyMap<>(factory);
    }

    /** The factory to create a new value */
    private final MapFactory<Key, Value> factory;

    /** The value, or null if it doesn't exist */
    private final Map<Key, Value> map = new ConcurrentHashMap<>();

    protected LazyMap(MapFactory<Key, Value> factory)
    {
        this.factory = factory;
    }

    /**
     * Clears this map
     */
    public void clear()
    {
        map.clear();
    }

    /**
     * @return The value
     */
    public final Value get(Key key)
    {
        return map.computeIfAbsent(key, factory::newInstance);
    }

    /**
     * Clears the value for the given key
     */
    public void remove(Key key)
    {
        map.remove(key);
    }
}
