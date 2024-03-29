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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.interfaces.factory.Factory;

import java.util.Map;
import java.util.function.Function;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;

/**
 * Methods for working with maps.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class Maps
{
    /**
     * @param factory Creates a new map
     * @param map The map to copy
     * @param clone The function to copy values
     * @return A deep copy of the given map
     */
    public static <Key, Value> Map<Key, Value> deepCopy(Factory<Map<Key, Value>> factory,
                                                        Map<Key, Value> map,
                                                        Function<Value, Value> clone)
    {
        var copy = factory.newInstance();
        for (var key : map.keySet())
        {
            var value = ensureNotNull(map.get(key));
            copy.put(key, ensureNotNull(clone.apply(value)));
        }
        return copy;
    }
}
