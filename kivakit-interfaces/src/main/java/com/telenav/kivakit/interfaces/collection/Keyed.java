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

package com.telenav.kivakit.interfaces.collection;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_UNDETERMINED;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * An object which can retrieve a value given a key.
 *
 * @param <Key> The type of key
 * @param <Value> The type of value
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@FunctionalInterface
@TypeQuality(stability = STABILITY_UNDETERMINED,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED,
             reviews = 1,
             reviewers = "shibo")
public interface Keyed<Key, Value>
{
    /**
     * Returns the value for the given key
     */
    Value get(Key key);

    /**
     * @param key The key to access
     * @param defaultValue The default value to use if there is no value for the key
     * @return The value for the given key, or if it is null, the default value
     */
    default Value getOrDefault(Key key, Value defaultValue)
    {
        var value = get(key);
        return value == null ? defaultValue : value;
    }
}
