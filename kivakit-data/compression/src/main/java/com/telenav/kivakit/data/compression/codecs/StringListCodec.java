////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
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
