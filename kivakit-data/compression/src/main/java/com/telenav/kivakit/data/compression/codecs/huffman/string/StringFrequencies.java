////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
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
