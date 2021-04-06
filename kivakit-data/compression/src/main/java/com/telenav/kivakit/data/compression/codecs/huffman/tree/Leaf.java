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
