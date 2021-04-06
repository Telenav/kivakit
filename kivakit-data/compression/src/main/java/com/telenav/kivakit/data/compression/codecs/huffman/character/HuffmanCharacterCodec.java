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

package com.telenav.kivakit.data.compression.codecs.huffman.character;

import com.telenav.kivakit.core.collections.primitive.array.scalars.ByteArray;
import com.telenav.kivakit.core.collections.primitive.list.ByteList;
import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.language.primitives.Ints;
import com.telenav.kivakit.core.kernel.language.primitives.Longs;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.kernel.language.values.count.MutableCount;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.resource.resources.other.PropertyMap;
import com.telenav.kivakit.data.compression.SymbolConsumer;
import com.telenav.kivakit.data.compression.SymbolProducer;
import com.telenav.kivakit.data.compression.codecs.CharacterCodec;
import com.telenav.kivakit.data.compression.codecs.huffman.HuffmanCodec;
import com.telenav.kivakit.data.compression.codecs.huffman.tree.Symbols;

import static com.telenav.kivakit.data.compression.SymbolConsumer.Directive.CONTINUE;

/**
 * Huffman compression of an entire <b>sequence</b> of strings using {@link HuffmanCodec} to compress individual
 * characters in each string. A Huffman character codec can be created by calling {@link #from(Symbols)} or {@link
 * #from(Symbols, Maximum)}, passing in a symbol set and optionally the maximum number of bits to allow for a code.
 * Compressed bits are written to a {@link ByteList} with {@link #encode(ByteList, SymbolProducer)} and read back with
 * {@link #decode(ByteList, SymbolConsumer)}.
 * <p>
 * A Huffman character codec can be created by calling {@link #from(Symbols, Maximum)} with a set of symbols and their
 * frequencies and a maximum number of bits for the longest allowable code. A codec can also be loaded from a .codec
 * file containing string frequencies with {@link #from(PropertyMap, Character)}. For details on how to create codec
 * files, see {@link Symbols}.
 *
 * @author jonathanl (shibo)
 * @see Symbols
 * @see HuffmanCodec
 * @see ByteArray
 */
public class HuffmanCharacterCodec implements CharacterCodec
{
    /** Character used for end of string */
    public static final char END_OF_STRING = 0;

    /** Character that indicates the following character is not encoded */
    public static final char ESCAPE = 1;

    /** Number of special characters */
    public static final int SPECIAL_CHARACTERS = 2;

    private static final Logger LOGGER = LoggerFactory.newLogger();

    /**
     * @return A codec from the symbol frequencies in the given properties object
     */
    public static HuffmanCharacterCodec from(final PropertyMap frequencies, final Character escape)
    {
        return from(Symbols.load(frequencies, escape, new Converter(LOGGER)));
    }

    /**
     * @param symbols A set of symbols and their frequencies
     * @param bits The maximum number of bits allowed for a code
     * @return A codec for the symbols
     */
    public static HuffmanCharacterCodec from(final Symbols<Character> symbols, final Maximum bits)
    {
        return new HuffmanCharacterCodec(symbols, bits);
    }

    /**
     * @param symbols A set of symbols and their frequencies
     * @return A codec for the symbols
     */
    public static HuffmanCharacterCodec from(final Symbols<Character> symbols)
    {
        return from(symbols, Maximum._16);
    }

    public static class Converter extends BaseStringConverter<Character>
    {
        public Converter(final Listener listener)
        {
            super(listener);
        }

        @Override
        protected Character onConvertToObject(final String value)
        {
            return (char) Longs.parseHex(value);
        }

        @Override
        protected String onConvertToString(final Character character)
        {
            return "0x" + Ints.toHex(character, 2);
        }
    }

    /** The symbols used to create this codec */
    private Symbols<Character> symbols;

    /** The underlying Huffman codec that does the symbol encoding */
    private HuffmanCodec<Character> codec;

    protected HuffmanCharacterCodec()
    {
    }

    private HuffmanCharacterCodec(final Symbols<Character> symbols, final Maximum bits)
    {
        this.symbols = symbols;
        codec = HuffmanCodec.from(symbols, bits);
    }

    /**
     * @return This codec's symbols as a property map
     */
    public PropertyMap asProperties()
    {
        return symbols.asProperties(new Converter(LOGGER), symbol ->
        {
            if (symbol == END_OF_STRING)
            {
                return "END_OF_STRING";
            }
            if (symbol == ESCAPE)
            {
                return "ESCAPE";
            }
            return !Character.isISOControl(symbol) ? "'" + symbol + "'" : Character.getName(symbol);
        });
    }

    @Override
    public boolean canEncode(final String value)
    {
        // We can encode any string because we can always escape a character it if it doesn't have a code
        return true;
    }

    /**
     * Decodes strings character by character using a Huffman codec, passing each decoded string to the consumer until
     * the consumer returns {@link SymbolConsumer.Directive#STOP}
     */
    @Override
    public void decode(final ByteList input, final SymbolConsumer<String> consumer)
    {
        final var count = new MutableCount();
        final var builder = new StringBuilder();
        codec.decode(input, new SymbolConsumer<>()
        {
            @Override
            public Directive next(final int ordinal, final Character character)
            {
                if (character == END_OF_STRING)
                {
                    final var value = builder.toString();
                    builder.setLength(0);
                    return consumer.next((int) count.increment(), value);
                }

                builder.append(character.charValue());
                return CONTINUE;
            }

            @Override
            public Directive onEscape(final ByteList input)
            {
                builder.append(input.readFlexibleChar());
                return CONTINUE;
            }
        });
    }

    /**
     * Encodes the given string value into the output
     */
    @Override
    public ByteList encode(final ByteList output, final SymbolProducer<String> producer)
    {
        return codec.encode(output, new SymbolProducer<>()
        {
            // The ordinal of the string we are encoding to help the producer
            int ordinal;

            // The current string we are encoding
            String value;

            // The character index in the string being encoded (or -1 if the current string has been fully encoded)
            int at;

            @Override
            public Character get(final int ignored)
            {
                // If we're done processing the current value,
                if (at == -1)
                {
                    // we'll need a new one
                    value = null;
                }

                // If we don't have a value
                if (value == null)
                {
                    // get one
                    value = producer.get(ordinal++);
                    at = 0;
                }

                // If we have a value,
                if (value != null)
                {
                    // but we're at the end,
                    if (at == value.length())
                    {
                        // encode end of string and signal that we need a new value
                        at = -1;
                        return END_OF_STRING;
                    }

                    // otherwise, get the next character
                    return value.charAt(at++);
                }

                return null;
            }

            @Override
            public void onEscape(final ByteList output, final Character character)
            {
                output.writeFlexibleChar(character);
            }
        });
    }

    @Override
    public String toString()
    {
        return codec.toString();
    }
}
