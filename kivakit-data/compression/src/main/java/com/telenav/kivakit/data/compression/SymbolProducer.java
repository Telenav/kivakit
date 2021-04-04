////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.compression;

import com.telenav.kivakit.core.collections.primitive.list.ByteList;

import java.util.Collections;
import java.util.List;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * A source of input symbols for a {@link Codec} to compress. Symbols can be retrieved by the codec through the {@link
 * #get(int)} method. When the codec cannot encode a symbol, it will call {@link #onEscape(ByteList, Object)} to allow
 * the implementer of this interface to write the symbol to codec output.
 */
public interface SymbolProducer<Symbol>
{
    static <Symbol> SymbolProducer<Symbol> fromList(final List<Symbol> symbols)
    {
        return new SymbolProducer<>()
        {
            @Override
            public Symbol get(final int ordinal)
            {
                return ordinal < size() ? symbols.get(ordinal) : null;
            }

            @Override
            public int size()
            {
                return symbols.size();
            }
        };
    }

    static <Symbol> SymbolProducer<Symbol> singleton(final Symbol symbol)
    {
        return fromList(Collections.singletonList(symbol));
    }

    /**
     * @return The next symbol in the sequence or null if the sequence has ended. The ordinal value is useful because
     * the implementation of this interface may be an anonymous inner class.
     */
    Symbol get(int ordinal);

    /**
     * Called by the codec when it cannot encode an input symbol. The codec will have already written an escape symbol
     * and moved to the next byte in the output. The implementer of this method simply needs to write the symbol to the
     * output in a format where it can be read back with a corresponding {@link SymbolConsumer} implementation.
     *
     * @param output The output to write the escaped symbol to
     * @param symbol The symbol to write
     */
    default void onEscape(final ByteList output, final Symbol symbol)
    {
        unsupported();
    }

    /**
     * If supported, resets the symbol sequence to the beginning so the symbols can be read again
     */
    default void reset()
    {
        unsupported();
    }

    /**
     * @return The number of symbols that will be encoded, or -1 if this value is unknown
     */
    default int size()
    {
        return -1;
    }
}
