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

package com.telenav.kivakit.data.compression.codecs.huffman;

import com.telenav.kivakit.core.collections.primitive.array.bits.BitArray;
import com.telenav.kivakit.core.collections.primitive.array.bits.io.BitReader;
import com.telenav.kivakit.core.collections.primitive.list.ByteList;
import com.telenav.kivakit.core.kernel.data.conversion.string.StringConverter;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;
import com.telenav.kivakit.core.kernel.language.primitives.Ints;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.resource.resources.other.PropertyMap;
import com.telenav.kivakit.data.compression.Codec;
import com.telenav.kivakit.data.compression.SymbolConsumer;
import com.telenav.kivakit.data.compression.SymbolProducer;
import com.telenav.kivakit.data.compression.codecs.huffman.tree.Code;
import com.telenav.kivakit.data.compression.codecs.huffman.tree.CodedSymbol;
import com.telenav.kivakit.data.compression.codecs.huffman.tree.Symbols;
import com.telenav.kivakit.data.compression.codecs.huffman.tree.Tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;

/**
 * A general-purpose Huffman coder implementing {@link Codec} which works for any arbitrary symbol type. This design
 * allows for both characters and strings to be Huffman-coded, which is useful when compressing tags because certain key
 * and value strings occur very frequently. Table-based Huffman decoding of symbols from {@link ByteList} input is
 * implemented with {@link FastHuffmanDecoder}.
 * <p>
 * A codec can be constructed with {@link #from(Symbols, Maximum)}, passing in the symbols to be used and the maximum
 * number of bits for the longest allowable Huffman code. The {@link #toString()} method returns a useful representation
 * of codes and their bit patterns.
 * <p>
 * Once the codec has been constructed, it can be used like any other {@link Codec}, by calling {@link
 * Codec#encode(ByteList, SymbolProducer)} to encode a series of values and {@link Codec#decode(ByteList,
 * SymbolConsumer)} to decode values from a byte array.
 *
 * @param <Symbol> The symbol value type to compress and decompress, for example Character or String
 * @author jonathanl (shibo)
 * @see ByteList
 * @see SymbolConsumer
 * @see SymbolProducer
 * @see FastHuffmanDecoder
 * @see <a href="https://en.wikipedia.org/wiki/Huffman_coding">Huffman Coding</a>
 * @see <a href="http://people.ucalgary.ca/~dfeder/449/Huffman.pdf">Fast Huffman Decoding</a>
 */
public class HuffmanCodec<Symbol> implements Codec<Symbol>
{
    /**
     * @return A Huffman codec for the given symbol frequencies and escape symbol where no code is longer than the given
     * number of bits. Only symbols that have at least the given number of occurrences are included.
     */
    public static <Symbol> HuffmanCodec<Symbol> from(final Symbols<Symbol> symbols, final Maximum bits)
    {
        return new HuffmanCodec<>(symbols, bits);
    }

    /** Huffman coding tree */
    private Tree<Symbol> tree;

    /** Set of symbols encoded by this codec */
    private Symbols<Symbol> symbols;

    /** Map from symbol value to Huffman {@link Code} */
    private final Map<Symbol, Code> valueToCode = new HashMap<>();

    /** Map from coded symbol to Huffman {@link Code} */
    private Map<CodedSymbol<Symbol>, Code> symbolToCode;

    /** Table-driven, byte-wise huffman decoder */
    private FastHuffmanDecoder<Symbol> fastDecoder;

    protected HuffmanCodec()
    {
    }

    /**
     * @param symbols A set of symbols to encode, including any escapes
     * @param bits The maximum number of bits desired in a code
     */
    private HuffmanCodec(final Symbols<Symbol> symbols, final Maximum bits)
    {
        // To have a valid Huffman tree, there must be at least two symbols to encode because a Huffman tree with only
        // one symbol has no nodes and therefore no coding paths through the tree
        ensure(symbols.size() > 1, "$ is not enough symbols to create a codec", symbols.size());

        // Keep the symbols
        this.symbols = symbols;

        // build the coding tree
        tree = symbols.tree(bits);

        // assign Huffman codes to symbols
        symbolToCode = tree.encode();
        final var codedSymbols = tree.codedSymbols();

        // make sure all symbols were coded
        ensure(codedSymbols.size() == symbolToCode.size(), "Only $ of $ symbols were coded:\n$",
                symbolToCode.size(), symbols.size(), symbols);

        // create a map from symbol value to code
        for (final var symbol : symbolToCode.keySet())
        {
            final var code = symbolToCode.get(symbol);
            valueToCode.put(symbol.value(), code);
        }

        // then build the fast decoder tables
        fastDecoder = new FastHuffmanDecoder<>(this);
    }

    /**
     * @return This codec's symbols as a property map
     */
    public PropertyMap asProperties(final StringConverter<Symbol> converter)
    {
        return symbols.asProperties(converter, value -> null);
    }

    @Override
    public boolean canEncode(final Symbol symbol)
    {
        return code(symbol) != null;
    }

    public Code code(final Symbol value)
    {
        return valueToCode.get(value);
    }

    public Set<CodedSymbol<Symbol>> codedSymbols()
    {
        return tree.codedSymbols();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void decode(final ByteList input, final SymbolConsumer<Symbol> consumer)
    {
        fastDecoder.decode(input, consumer);
    }

    public CodedSymbol<Symbol> decode(final BitReader reader)
    {
        return tree.decode(reader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ByteList encode(final ByteList output, final SymbolProducer<Symbol> producer)
    {
        // Create a bit writer that can write to the output byte list
        final var bits = new BitArray("bits", output);
        bits.initialize();
        final var writer = bits.writer();

        // Loop through
        var ordinal = 0;
        while (true)
        {
            // getting the next symbol
            final var symbol = producer.get(ordinal++);
            if (symbol == null)
            {
                // unless there is none
                break;
            }

            // then get the Huffman code for the symbol
            final var code = code(symbol);
            if (code != null)
            {
                // and write it to the output
                code.write(writer);
            }
            else
            {
                // unless there is no code (but there is an escape symbol)
                final var escape = escape();
                if (escape != null)
                {
                    // in which case we write out the escape symbol
                    escape.code().write(writer);

                    // flush the rest of the current byte to the output byte list
                    writer.flush();

                    // and ask the producer to write out the escaped symbol.
                    producer.onEscape(output, symbol);
                }
            }
        }

        writer.close();
        return output;
    }

    public CodedSymbol<Symbol> escape()
    {
        return symbols.escape();
    }

    @Override
    public String toString()
    {
        final var lines = new StringList();
        lines.add("[HuffmanCodec size = " + size() + ", bits = " + bits() + "]:");
        final var keys = new ArrayList<>(symbolToCode.keySet());
        keys.sort(Comparator.comparingLong(CodedSymbol::frequency));
        var ordinal = 1;
        for (final var key : keys)
        {
            final var value = key.value();
            var string = value.toString();
            if (string.length() == 1 && Character.isISOControl(string.charAt(0)))
            {
                string = "0x" + Ints.toHex(string.charAt(0), 2);
            }
            else
            {
                string = "'" + value.toString() + "'";
            }
            lines.add(ordinal++ + ". " + symbolToCode.get(key) + " -> " + string + " (" + Count.count(key.frequency()) + ")");
        }
        return lines.join("\n    ");
    }

    /**
     * @return The number of symbols in this codec
     */
    int size()
    {
        return symbols.size();
    }

    private int bits()
    {
        var maximum = -1;
        for (final var code : valueToCode.values())
        {
            maximum = Math.max(maximum, code.lengthInBits());
        }
        return maximum;
    }
}
