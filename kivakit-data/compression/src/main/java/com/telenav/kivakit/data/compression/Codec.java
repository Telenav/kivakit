////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.compression;

import com.telenav.kivakit.core.collections.primitive.list.ByteList;
import com.telenav.kivakit.data.compression.codecs.huffman.FastHuffmanDecoder;
import com.telenav.kivakit.data.compression.codecs.huffman.HuffmanCodec;

import java.util.Iterator;

/**
 * Interface to a compression codec, where codec stands for "Compressor / Decompressor". A {@link Codec}  that can
 * encode input symbols into an output byte array with {@link #encode(ByteList, SymbolProducer)} and then decode the
 * same bytes later with {@link #decode(ByteList, SymbolConsumer)}.
 * <p>
 * The use of the {@link SymbolProducer} and {@link SymbolConsumer} interfaces is a design that improves performance by
 * not requiring that collections be created or populated with encoded or decoded symbols. A different sequence
 * interface like {@link Iterable} or {@link Iterator} was not used because several features beyond getting the next
 * symbol are required for a codec.
 * <p>
 * <b>Compression and Decompression</b>
 * <ul>
 *     <li>{@link SymbolProducer} -&gt; {@link ByteList}</li>
 *     <li>{@link ByteList} -&gt; {@link SymbolConsumer}</li>
 * </ul>
 * <p>
 * This interface should be compatible with most compression algorithms, but it was designed to be suitable for Huffman
 * (table-based, "fast") coding, as implemented by {@link HuffmanCodec} and {@link FastHuffmanDecoder}.
 *
 * @author jonathanl (shibo)
 * @see ByteList
 * @see SymbolProducer
 * @see SymbolConsumer
 * @see HuffmanCodec
 * @see FastHuffmanDecoder
 */
public interface Codec<Symbol>
{
    /**
     * @return True if this codec can encode the given symbol and false if it must be escaped
     */
    boolean canEncode(Symbol symbol);

    /**
     * Reads the compressed data in the input, calling the consumer with a sequence of decoded symbols
     *
     * @param input The compressed data to decode
     * @param consumer The consumer to call with decoded data
     */
    void decode(ByteList input, SymbolConsumer<Symbol> consumer);

    /**
     * Encodes the input symbols produced into the output
     *
     * @param output The encoded data output
     * @param producer The source of symbols to encode
     * @return The encoded data (the output parameter that was passed in)
     */
    ByteList encode(ByteList output, SymbolProducer<Symbol> producer);
}
