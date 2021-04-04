////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.compression.project;

import com.telenav.kivakit.core.serialization.kryo.KryoTypes;
import com.telenav.kivakit.data.compression.codecs.huffman.FastHuffmanDecoder;
import com.telenav.kivakit.data.compression.codecs.huffman.HuffmanCodec;
import com.telenav.kivakit.data.compression.codecs.huffman.character.HuffmanCharacterCodec;
import com.telenav.kivakit.data.compression.codecs.huffman.list.HuffmanStringListCodec;
import com.telenav.kivakit.data.compression.codecs.huffman.string.HuffmanStringCodec;
import com.telenav.kivakit.data.compression.codecs.huffman.tree.Code;
import com.telenav.kivakit.data.compression.codecs.huffman.tree.CodedSymbol;
import com.telenav.kivakit.data.compression.codecs.huffman.tree.Leaf;
import com.telenav.kivakit.data.compression.codecs.huffman.tree.Node;
import com.telenav.kivakit.data.compression.codecs.huffman.tree.Symbols;
import com.telenav.kivakit.data.compression.codecs.huffman.tree.Tree;

/**
 * @author jonathanl (shibo)
 */
public class DataCompressionKryoTypes extends KryoTypes
{
    public DataCompressionKryoTypes()
    {
        //----------------------------------------------------------------------------------------------
        // NOTE: To maintain backward compatibility of serialization, registration groups and the types
        // in each registration group must remain in the same order.
        //----------------------------------------------------------------------------------------------

        group("huffman", () ->
        {
            register(HuffmanCodec.class);
            register(HuffmanCharacterCodec.class);
            register(HuffmanStringListCodec.class);
            register(CodedSymbol.class);
            register(Code.class);
            register(Leaf.class);
            register(Node.class);
            register(Tree.class);
            register(Symbols.class);
            register(FastHuffmanDecoder.class);
            register(FastHuffmanDecoder.Table.class);
            register(FastHuffmanDecoder.Table.Entry.class);
            register(FastHuffmanDecoder.Table.Entry[].class);
            register(HuffmanStringCodec.class);
        });
    }
}
