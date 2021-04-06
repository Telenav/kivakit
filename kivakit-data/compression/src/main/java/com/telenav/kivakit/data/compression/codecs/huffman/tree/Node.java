////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
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
