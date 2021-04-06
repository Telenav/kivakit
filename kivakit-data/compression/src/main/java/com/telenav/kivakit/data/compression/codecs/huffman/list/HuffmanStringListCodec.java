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

package com.telenav.kivakit.data.compression.codecs.huffman.list;

import com.telenav.kivakit.core.collections.primitive.list.ByteList;
import com.telenav.kivakit.core.kernel.language.values.mutable.MutableIndex;
import com.telenav.kivakit.data.compression.SymbolConsumer;
import com.telenav.kivakit.data.compression.SymbolProducer;
import com.telenav.kivakit.data.compression.codecs.CharacterCodec;
import com.telenav.kivakit.data.compression.codecs.StringCodec;
import com.telenav.kivakit.data.compression.codecs.StringListCodec;
import com.telenav.kivakit.data.compression.codecs.huffman.HuffmanCodec;

/**
 * Compression and decompression of lists of strings using a {@link StringCodec} and a {@link CharacterCodec}. The
 * method {@link #encode(ByteList, SymbolProducer)} receives a sequence of strings produced by the {@link
 * SymbolProducer} interface. If the string codec cannot be used to compress the entire string at once, the character
 * codec will be used. When the {@link #decode(ByteList, SymbolConsumer)} method is called, this codec will send the
 * same strings in the same order to the {@link SymbolConsumer} consumer.
 *
 * @author jonathanl (shibo)
 * @see HuffmanCodec
 * @see StringCodec
 * @see CharacterCodec
 * @see ByteList
 * @see SymbolProducer
 * @see SymbolConsumer
 */
public class HuffmanStringListCodec implements StringListCodec
{
    /** The underlying codec that does symbol encoding a whole string at a time */
    private StringCodec stringCodec;

    /** Underlying codec that encodes strings a character at a time (when the string codec cannot encode a string) */
    private CharacterCodec characterCodec;

    /**
     * @param stringCodec Codec that can encode entire strings at once
     * @param characterCodec Codec that can encode strings by character if the string codec cannot encode a string
     */
    public HuffmanStringListCodec(final StringCodec stringCodec, final CharacterCodec characterCodec)
    {
        this.characterCodec = characterCodec;
        this.stringCodec = stringCodec;
    }

    protected HuffmanStringListCodec()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canEncode(final String s)
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decode(final ByteList input, final SymbolConsumer<String> consumer)
    {
        // Get the length of the list
        final int size = input.readFlexibleShort();
        assert size > 0;

        // the read a map of which indexes are string-encoded and which are character-encoded
        final var isStringEncoded = input.readBooleans(size);
        assert isStringEncoded.length == size;
        final var stringEncoded = trueValues(isStringEncoded);

        // decode all the string-encoded symbols
        final var at = new MutableIndex();
        if (stringEncoded > 0)
        {
            stringCodec.decode(input, (ordinal, value) ->
            {
                for (var i = at.get(); i < size; i++)
                {
                    if (isStringEncoded[i])
                    {
                        consumer.next(i, value);
                        at.set(i + 1);
                        return ordinal < stringEncoded - 1 ? SymbolConsumer.Directive.CONTINUE : SymbolConsumer.Directive.STOP;
                    }
                }
                return SymbolConsumer.Directive.STOP;
            });
        }

        // and then the character-encoded symbols
        final var characterEncoded = size - stringEncoded;
        if (characterEncoded > 0)
        {
            at.set(0);
            characterCodec.decode(input, (ordinal, value) ->
            {
                for (var i = at.get(); i < size; i++)
                {
                    if (!isStringEncoded[i])
                    {
                        consumer.next(i, value);
                        at.set(i + 1);
                        return ordinal < characterEncoded - 1 ? SymbolConsumer.Directive.CONTINUE : SymbolConsumer.Directive.STOP;
                    }
                }
                return SymbolConsumer.Directive.STOP;
            });
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ByteList encode(final ByteList output, final SymbolProducer<String> producer)
    {
        // Get the number of symbols that will be produced
        final var size = producer.size();

        // and write it to the output (we only support 64K entry string lists)
        output.writeFlexibleShort((short) size);

        // then create a boolean map of which strings in the list will be string-encoded and which will be character-encoded
        final var isStringEncoded = new boolean[size];

        // by going through
        for (var index = 0; index < size; index++)
        {
            // each symbol in the inputs
            final var symbol = producer.get(index);

            // and storing whether it is string or character encoded
            isStringEncoded[index] = stringCodec.canEncode(symbol);
        }

        // write the map out,
        output.writeBooleans(isStringEncoded);

        // then write out the string-encoded values
        final var stringEncoded = trueValues(isStringEncoded);
        if (stringEncoded > 0)
        {
            final var index = new MutableIndex();
            stringCodec.encode(output, ignored ->
            {
                while (true)
                {
                    final var at = index.increment();
                    if (at < size)
                    {
                        if (isStringEncoded[at])
                        {
                            return producer.get(at);
                        }
                    }
                    else
                    {
                        return null;
                    }
                }
            });
        }

        // and finally the character-encoded values
        final var characterEncoded = size - stringEncoded;
        if (characterEncoded > 0)
        {
            final var index = new MutableIndex();
            characterCodec.encode(output, ignored ->
            {
                while (true)
                {
                    final var at = index.increment();
                    if (at < size)
                    {
                        if (!isStringEncoded[at])
                        {
                            return producer.get(at);
                        }
                    }
                    else
                    {
                        return null;
                    }
                }
            });
        }

        return output;
    }

    @Override
    public String toString()
    {
        return "[HuffmanStringListCodec]\n" + stringCodec + "\n" + characterCodec + "\n";
    }

    private int trueValues(final boolean[] values)
    {
        var count = 0;
        for (final var value : values)
        {
            if (value)
            {
                count++;
            }
        }
        return count;
    }
}
