////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.compression.codecs.huffman.tree;

import com.telenav.kivakit.core.collections.primitive.array.bits.io.BitReader;

/**
 * A terminal leaf in a Huffman {@link Tree}, having a symbol and a frequency.
 */
public class Leaf<T> extends Tree<T>
{
    private CodedSymbol<T> symbol;

    /**
     * @param symbol The leaf's symbol
     */
    public Leaf(final CodedSymbol<T> symbol)
    {
        super(symbol.frequency());
        this.symbol = symbol;
    }

    protected Leaf()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CodedSymbol<T> decode(final BitReader bits)
    {
        return symbol;
    }

    /**
     * @return This leaf's symbol
     */
    CodedSymbol<T> symbol()
    {
        return symbol;
    }
}
