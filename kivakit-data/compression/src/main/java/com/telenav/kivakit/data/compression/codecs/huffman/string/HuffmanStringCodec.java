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

package com.telenav.kivakit.data.compression.codecs.huffman.string;

import com.telenav.kivakit.core.collections.primitive.list.ByteList;
import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.resource.resources.other.PropertyMap;
import com.telenav.kivakit.data.compression.SymbolConsumer;
import com.telenav.kivakit.data.compression.SymbolProducer;
import com.telenav.kivakit.data.compression.codecs.CharacterCodec;
import com.telenav.kivakit.data.compression.codecs.StringCodec;
import com.telenav.kivakit.data.compression.codecs.huffman.HuffmanCodec;
import com.telenav.kivakit.data.compression.codecs.huffman.character.HuffmanCharacterCodec;
import com.telenav.kivakit.data.compression.codecs.huffman.tree.Symbols;

/**
 * A Huffman compression codec where strings are treated as symbols instead of characters. Frequent strings in map data
 * input, like "highway" can often be compressed to a single byte this way. Less frequent strings can be compressed one
 * character at a time with a {@link CharacterCodec} like {@link HuffmanCharacterCodec}.
 * <p>
 * A Huffman string codec can be created by calling {@link #from(Symbols, Maximum)} with a set of symbols and their
 * frequencies and a maximum number of bits for the longest allowable code. A codec can also be loaded from a .codec
 * file containing string frequencies with {@link #from(PropertyMap)}. For details on how to create codec symbols, see
 * {@link Symbols}.
 * <p>
 * Note that this codec doesn't support escaping of symbols because symbols that cannot be encoded are instead encoded
 * by a character codec. In the case of a symbol that can't be encoded, {@link #canEncode(String)} will return false.
 *
 * @author jonathanl (shibo)
 * @see HuffmanCodec
 * @see Symbols
 * @see StringCodec
 * @see CharacterCodec
 * @see HuffmanCharacterCodec
 */
public class HuffmanStringCodec implements StringCodec
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /**
     * @return A codec from the symbol frequencies in the given properties object
     */
    public static HuffmanStringCodec from(final PropertyMap frequencies)
    {
        return from(Symbols.load(frequencies, new Converter(LOGGER)));
    }

    /**
     * @param symbols A set of symbols and their frequencies
     * @param bits The maximum number of bits allowed for a code
     * @return A codec for the symbols
     */
    public static HuffmanStringCodec from(final Symbols<String> symbols, final Maximum bits)
    {
        return new HuffmanStringCodec(symbols, bits);
    }

    /**
     * @param symbols A set of symbols and their frequencies
     * @return A codec for the symbols
     */
    public static HuffmanStringCodec from(final Symbols<String> symbols)
    {
        return from(symbols, Maximum._16);
    }

    public static class Converter extends BaseStringConverter<String>
    {
        public Converter(final Listener listener)
        {
            super(listener);
        }

        @Override
        protected String onConvertToObject(final String value)
        {
            return value;
        }
    }

    /** The underlying huffman codec that does the symbol encoding */
    private HuffmanCodec<String> codec;

    /** Symbols used to construct the codec */
    private Symbols<String> symbols;

    protected HuffmanStringCodec()
    {
    }

    private HuffmanStringCodec(final Symbols<String> symbols, final Maximum bits)
    {
        this.symbols = symbols;
        codec = HuffmanCodec.from(symbols, bits);
    }

    /**
     * @return This codec's symbols as a property map
     */
    public PropertyMap asProperties()
    {
        return symbols.asProperties(new Converter(LOGGER), value -> null);
    }

    @Override
    public boolean canEncode(final String value)
    {
        return codec.canEncode(value);
    }

    @Override
    public void decode(final ByteList input, final SymbolConsumer<String> consumer)
    {
        codec.decode(input, consumer);
    }

    @Override
    public ByteList encode(final ByteList output, final SymbolProducer<String> producer)
    {
        return codec.encode(output, producer);
    }

    @Override
    public String toString()
    {
        return codec.toString();
    }
}
