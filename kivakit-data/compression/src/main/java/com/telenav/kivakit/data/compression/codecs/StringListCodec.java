////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.compression.codecs;

import com.telenav.kivakit.data.compression.Codec;
import com.telenav.kivakit.data.compression.codecs.huffman.list.HuffmanStringListCodec;

/**
 * A codec that compresses lists of strings. The value null represents the end of a list.
 *
 * @author jonathanl (shibo)
 * @see Codec
 * @see HuffmanStringListCodec
 */
public interface StringListCodec extends StringCodec
{
}
