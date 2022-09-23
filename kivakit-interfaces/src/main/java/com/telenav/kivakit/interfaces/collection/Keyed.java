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

import com.telenav.kivakit.annotations.code.CodeQuality;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNNECESSARY;

/**
 * An object which can retrieve a value given a key.
 *
 * @param <Key> The type of key
 * @param <Value> The type of value
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@CodeQuality(stability = STABLE,
             testing = UNNECESSARY,
             documentation = COMPLETE)
public interface Keyed<Key, Value>
{
    /**
     * @return The value for the given key
     */
    Value get(Key key);
}
