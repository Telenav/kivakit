////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
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
