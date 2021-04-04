////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
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
