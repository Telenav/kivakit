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

package com.telenav.kivakit.data.compression;

import com.telenav.kivakit.core.collections.primitive.list.ByteList;

/**
 * Consumer of a sequence of decoded symbols. The {@link #next(int, Object)} method is called with each decoded symbol
 * and its ordinal index in the input. The next() method can examine the symbol and return a {@link Directive} to the
 * codec to either {@link Directive#CONTINUE} decoding or to {@link Directive#STOP}. When the codec reads an escape
 * symbol, it will advance the input to the next byte and call the {@link #onEscape(ByteList)} method to permit it to
 * read the next symbol using the coding scheme implemented in {@link SymbolProducer}.
 */
@FunctionalInterface
public
interface SymbolConsumer<Symbol>
{
    enum Directive
    {
        CONTINUE,
        STOP
    }

    /**
     * @param ordinal The zero-based ordinal number of the symbol in the sequence
     * @param symbol The next decoded symbol in the sequence
     * @return {@link Directive#CONTINUE} if decoding of the sequence of symbols should continue, {@link Directive#STOP}
     * if the end of the sequence has been reached
     */
    Directive next(int ordinal, Symbol symbol);

    /**
     * Called when the codec reads an escape symbol. The method implementer can read the escaped symbol using the coding
     * scheme implemented in {@link SymbolProducer}.
     *
     * @param input The output to write the escaped symbol to
     */
    default Directive onEscape(final ByteList input)
    {
        return Directive.CONTINUE;
    }
}
