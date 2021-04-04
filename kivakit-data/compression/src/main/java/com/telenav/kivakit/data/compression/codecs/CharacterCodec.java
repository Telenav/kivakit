////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.compression.codecs;

import com.telenav.kivakit.data.compression.Codec;
import com.telenav.kivakit.data.compression.codecs.huffman.HuffmanCodec;

/**
 * A character codec compresses strings one character at a time, assigning shorter bit sequences to more frequent
 * characters and longer bit sequences to less frequent characters.
 *
 * @author jonathanl (shibo)
 * @see HuffmanCodec
 * @see Codec
 * @see StringCodec
 */
public interface CharacterCodec extends StringCodec
{
}
