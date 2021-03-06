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

package com.telenav.kivakit.core.collections;

import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.interfaces.factory.Factory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author jonathanl (shibo)
 */
public class Maps
{
    /**
     * @return A copy of the given map
     */
    public static <Key, Value> Map<Key, Value> copy(Factory<Map<Key, Value>> factory,
                                                    Map<Key, Value> map)
    {
        return deepCopy(factory, map, value -> value);
    }

    /**
     * @return A copy of the given map
     */
    public static <Key, Value> Map<Key, Value> deepCopy(Map<Key, Value> map,
                                                        Function<Value, Value> clone)
    {
        return deepCopy(HashMap::new, map, clone);
    }

    /**
     * @return A copy of the given map
     */
    public static <Key, Value> Map<Key, Value> deepCopy(Factory<Map<Key, Value>> factory,
                                                        Map<Key, Value> map,
                                                        Function<Value, Value> clone)
    {
        var copy = factory.newInstance();
        for (var key : map.keySet())
        {
            var value = map.get(key);
            Ensure.ensureNotNull(value);
            var clonedValue = clone.apply(value);
            Ensure.ensureNotNull(clonedValue);
            copy.put(key, clonedValue);
        }
        return copy;
    }
}
