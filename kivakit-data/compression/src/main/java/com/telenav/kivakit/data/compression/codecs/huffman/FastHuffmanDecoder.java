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
import com.telenav.kivakit.core.collections.primitive.array.scalars.ByteArray;
import com.telenav.kivakit.core.collections.primitive.list.ByteList;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;
import com.telenav.kivakit.core.kernel.language.strings.StringTo;
import com.telenav.kivakit.core.kernel.language.strings.conversion.AsString;
import com.telenav.kivakit.core.kernel.language.strings.conversion.StringFormat;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.Debug;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.data.compression.SymbolConsumer;
import com.telenav.kivakit.data.compression.SymbolConsumer.Directive;
import com.telenav.kivakit.data.compression.codecs.huffman.tree.CodedSymbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.telenav.kivakit.data.compression.SymbolConsumer.Directive.STOP;

/**
 * The method {@link #decode(ByteList, SymbolConsumer)} decodes a string a byte at a time from the given {@link
 * ByteArray} using a set of lookup tables built from a {@link HuffmanCodec}.
 *
 * @author jonathanl (shibo)
 * @see <a href="http://people.ucalgary.ca/~dfeder/449/Huffman.pdf">Fast Huffman Decoding</a>
 * @see HuffmanCodec
 * @see Consumer
 * @see ByteArray
 */
public final class FastHuffmanDecoder<Symbol>
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final Debug DEBUG = new Debug(LOGGER);

    /**
     * A table with entries for a given prefix (or no prefix in the case of the root table). For a very nice discussion
     * of this, including an example set of decoding tables, see page 69 of <a href="http://people.ucalgary.ca/~dfeder/449/Huffman.pdf">Fast
     * Huffman Decoding</a>
     */
    public static class Table<Symbol> implements AsString
    {
        /** An entry in this fast decoder table */
        public static class Entry<Symbol> implements AsString
        {
            /** The list of values for this table entry, if any */
            final List<Symbol> values = new ArrayList<>();

            /** The next table to look in, possibly the root table if there are no bits left */
            Table<Symbol> next;

            /** The table that owns this entry */
            private Table<Symbol> table;

            protected Entry()
            {
            }

            private Entry(final Table<Symbol> table)
            {
                this.table = table;
            }

            @Override
            public String asString(final StringFormat format)
            {
                return Message.format("[Entry next = '$', values = $]", next.prefix,
                        new StringList(values).join(", "));
            }

            @Override
            public String toString()
            {
                return "[" + new StringList(values).join(", ") + "]";
            }

            /**
             * Decodes values from the input byte and adds them to this entry's list. This may lead to the creation of
             * further tables when the input doesn't fit evenly.
             */
            void decode(final BitArray data)
            {
                final var reader = data.reader();

                // then loop
                CodedSymbol<Symbol> symbol;
                var position = 0;
                do
                {
                    // decoding the next symbol
                    symbol = table.decoder.codec.decode(reader);

                    // and adding it to the list of values
                    if (symbol != null)
                    {
                        values.add(symbol.value());
                        position = (int) reader.cursor();
                    }
                }
                // until we're out of symbols,
                while (symbol != null);

                // determine how many bits are left (either the rest of the byte or the rest of the input)
                final var bitsLeft = data.size() - position;

                // read those bits
                var remainder = 0;
                if (bitsLeft > 0)
                {
                    reader.cursor(position);
                    remainder = reader.read(bitsLeft);
                }

                // and use those bits as a prefix to get the next table (which might lead us back to the
                // root table if there are no bits left)
                next = table.prefixToTable(remainder, bitsLeft);
            }
        }

        /** The list of entries in this table */
        @SuppressWarnings("unchecked")
        final
        Entry<Symbol>[] byteToEntry = new Entry[256];

        /** The outer decoder */
        private FastHuffmanDecoder<Symbol> decoder;

        /** Prefix for this table */
        private String prefix;

        Table(final FastHuffmanDecoder<Symbol> decoder, final String prefix)
        {
            this.decoder = decoder;
            this.prefix = prefix;

            decoder.prefixToTable.put(prefix, this);
        }

        protected Table()
        {
        }

        @Override
        public String asString(final StringFormat format)
        {
            final var entries = new StringList();
            for (var index = 0; index < byteToEntry.length; index++)
            {
                final var entry = byteToEntry[index];
                entries.append(StringTo.binary(index, 8) + " = " + entry.asString());
            }
            return Message.format("[Table prefix = '$']\n    ", prefix) + entries.join("\n    ");
        }

        @Override
        public String toString()
        {
            return prefix;
        }

        /**
         * @param prefix The prefix value
         * @param bits The number of bits for the prefix value
         */
        void compute(final int prefix, final int bits)
        {
            DEBUG.trace("Computing table for prefix '$'", this.prefix);

            // Go through each possible byte
            for (var index = 0; index < 256; index++)
            {
                // create a new entry
                final var entry = new Entry<>(this);

                // and a bit array to decode from
                final var data = new BitArray("bits");
                data.initialize();

                // then write the prefix and the index into the bit array
                final var writer = data.writer();
                writer.write(prefix, bits);
                writer.write(index, 8);
                writer.close();

                // and decode it
                entry.decode(data);

                // then add the entry to the table for the given byte value (index)
                byteToEntry[index] = entry;
            }
        }

        /**
         * @return The table for the given prefix
         */
        Table<Symbol> prefixToTable(final int prefix, final int bits)
        {
            // If we don't have a table for the prefix already,
            final var key = StringTo.binary(prefix, bits);
            var table = decoder.prefixToTable.get(key);
            if (table == null)
            {
                // construct one and compute the table
                table = new Table<>(decoder, key);
                table.compute(prefix, bits);
            }

            return table;
        }
    }

    /** A map from prefixes to tables */
    final Map<String, Table<Symbol>> prefixToTable = new HashMap<>();

    /** Root lookup table for fast Huffman decoding */
    private Table<Symbol> root;

    /** The codec */
    private HuffmanCodec<Symbol> codec;

    /** The escape symbol used by the codec */
    private Symbol escape;

    /**
     * Creates a table-driven decoder for the given codec
     */
    public FastHuffmanDecoder(final HuffmanCodec<Symbol> codec)
    {
        // Save the codec and its escape symbol,
        this.codec = codec;
        if (codec.escape() != null)
        {
            escape = this.codec.escape().value();
        }

        // create the root table,
        root = new Table<>(this, "");
        root.compute(0, 0);

        if (DEBUG.isDebugOn())
        {
            DEBUG.trace("Codec:\n" + codec);
            for (final var table : prefixToTable.values())
            {
                DEBUG.trace(table.asString());
            }
        }
    }

    protected FastHuffmanDecoder()
    {
    }

    /**
     * Decodes input bytes calling the consumer until the input is exhausted or the sequence consumer returns {@link
     * Directive#STOP}
     */
    @SuppressWarnings("ForLoopReplaceableByForEach")
    public void decode(final ByteList input, final SymbolConsumer<Symbol> consumer)
    {
        // Start at the root table
        var table = root;
        var ordinal = 0;

        next:
        while (input.hasNext())
        {
            // and get the table entry for the next byte,
            final var next = input.next() & 0xff;
            final var entry = table.byteToEntry[next];

            // then for each symbol value in the entry,
            final var values = entry.values;
            final var size = values.size();
            for (var i = 0; i < size; i++)
            {
                // if the symbol is escape,
                final var symbol = values.get(i);
                if (symbol.equals(escape))
                {
                    // then let the consumer handle reading the escaped symbol from the input
                    consumer.onEscape(input);
                    table = root;
                    continue next;
                }
                else
                {
                    // otherwise call the consumer
                    if (consumer.next(ordinal++, symbol) == STOP)
                    {
                        // unless they want to stop
                        return;
                    }
                }
            }

            // before moving to the next table.
            table = entry.next;
        }
    }
}
