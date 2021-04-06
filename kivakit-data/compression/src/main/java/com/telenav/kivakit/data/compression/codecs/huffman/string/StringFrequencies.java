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

package com.telenav.kivakit.data.compression.codecs.huffman.string;

import com.telenav.kivakit.core.kernel.language.collections.map.count.CountMap;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.kernel.language.values.count.Minimum;
import com.telenav.kivakit.data.compression.codecs.huffman.tree.Symbols;

/**
 * A helper class for building string {@link Symbols}.
 *
 * @author jonathanl (shibo)
 * @see Symbols
 */
public class StringFrequencies
{
    /** String frequencies */
    private final CountMap<String> frequencies;

    /** The maximum number of strings to track */
    private final Maximum maximum;

    public StringFrequencies(final Count initialSize, final Maximum maximum)
    {
        frequencies = new CountMap<>(initialSize);
        this.maximum = maximum;
    }

    private StringFrequencies(final CountMap<String> frequencies, final Maximum maximum)
    {
        this.frequencies = frequencies;
        this.maximum = maximum;
    }

    /**
     * Adds all the characters in the given string to this symbol set
     */
    public StringFrequencies add(final String string)
    {
        frequencies.increment(string);

        // If the number of strings exceeds the maximum,
        if (frequencies.size() > maximum.asInt())
        {
            // remove the bottom 25%
            final var bottom = frequencies.bottom(maximum.times(0.25));
            frequencies.removeAll(bottom);
        }
        return this;
    }

    public CountMap<String> frequencies()
    {
        return frequencies;
    }

    /**
     * @return The set of symbols appearing at least once
     */
    public Symbols<String> symbols()
    {
        return symbols(Minimum._1);
    }

    /**
     * @return The set of symbols appearing at least the given minimum number of times
     */
    public Symbols<String> symbols(final Minimum occurrences)
    {
        return new Symbols<>(frequencies, occurrences);
    }

    public StringFrequencies top(final Count count)
    {
        return new StringFrequencies(frequencies.top(count), maximum);
    }
}
