////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.compression.codecs.huffman.tree;

import com.telenav.kivakit.core.collections.primitive.array.bits.io.BitReader;

/**
 * An interior node in a Huffman tree, having a left and right sub-tree. The frequency of the node is the sum of the
 * frequencies of the sub-trees. A symbol can be decoded with {@link #decode(BitReader)}.
 */
public class Node<T> extends Tree<T>
{
    public Tree<T> left, right;

    public Node(final Tree<T> left, final Tree<T> right)
    {
        super(left.frequency() + right.frequency());
        this.left = left;
        this.right = right;
    }

    protected Node()
    {
    }

    @Override
    public CodedSymbol<T> decode(final BitReader bits)
    {
        if (bits.hasNext())
        {
            if (bits.readBit())
            {
                return right.decode(bits);
            }
            else
            {
                return left.decode(bits);
            }
        }
        return null;
    }
}
