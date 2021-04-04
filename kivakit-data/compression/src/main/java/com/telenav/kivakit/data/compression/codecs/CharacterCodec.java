////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
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
