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

package com.telenav.kivakit.data.compression.codecs;

import com.telenav.kivakit.data.compression.Codec;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * A string codec compresses entire strings as symbols. Frequent strings in map data input, like "highway" can be
 * compressed to a single byte. Less frequent strings can be compressed one character at a time with {@link
 * CharacterCodec}.
 *
 * @author jonathanl (shibo)
 */
public interface StringCodec extends Codec<String>
{
    /**
     * Trains this string codec on the given string
     */
    default void train(final String string)
    {
        unsupported();
    }

    /**
     * Trains this codec on the given sequence of strings
     */
    default void train(final Iterable<String> strings)
    {
        for (final var string : strings)
        {
            train(string);
        }
    }
}
