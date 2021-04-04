////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.compression.codecs.huffman.character;

import com.telenav.kivakit.core.kernel.language.collections.map.count.CountMap;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.kernel.language.values.count.Minimum;
import com.telenav.kivakit.data.compression.codecs.huffman.tree.Symbols;

/**
 * A helper class for building character {@link Symbols}.
 *
 * @author jonathanl (shibo)
 * @see Symbols
 */
public class CharacterFrequencies
{
    /** Maximum ASCII character value */
    private static final int MAXIMUM_ASCII = 255;

    /** Character frequencies */
    private final CountMap<Character> frequencies = new CountMap<>();

    /**
     * Adds all the characters in the given string to this symbol set
     */
    public CharacterFrequencies add(final String string)
    {
        for (var i = 0; i < string.length(); i++)
        {
            final var at = string.charAt(i);
            if (at < MAXIMUM_ASCII)
            {
                frequencies.increment(at);
            }
            else
            {
                // All 16 bit unicode characters are escaped at this time
                frequencies.increment(HuffmanCharacterCodec.ESCAPE);
            }
        }
        frequencies.increment(HuffmanCharacterCodec.END_OF_STRING);
        return this;
    }

    public Count escaped(final Maximum occurrences)
    {
        var escaped = frequencies.count(HuffmanCharacterCodec.ESCAPE).asLong();
        for (final Character character : frequencies().keySet())
        {
            if (frequencies.count(character).isLessThan(occurrences))
            {
                escaped++;
            }
        }
        return Count.count(escaped);
    }

    public CountMap<Character> frequencies()
    {
        return frequencies;
    }

    /**
     * @return The set of symbols appearing at least the given minimum number of times
     */
    public Symbols<Character> symbols(final Minimum occurrences)
    {
        return new Symbols<>(frequencies, HuffmanCharacterCodec.ESCAPE, occurrences);
    }

    /**
     * @return The set of symbols appearing at least once
     */
    public Symbols<Character> symbols()
    {
        return symbols(Minimum._1);
    }
}
