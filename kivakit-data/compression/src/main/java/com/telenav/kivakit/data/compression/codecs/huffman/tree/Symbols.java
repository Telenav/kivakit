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

import com.telenav.kivakit.core.kernel.data.conversion.string.StringConverter;
import com.telenav.kivakit.core.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.core.kernel.language.collections.map.count.CountMap;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.kernel.language.values.count.Minimum;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.resource.Resource;
import com.telenav.kivakit.core.resource.WritableResource;
import com.telenav.kivakit.core.resource.resources.other.PropertyMap;
import com.telenav.kivakit.data.compression.codecs.huffman.HuffmanCodec;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Function;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;

/**
 * A set of {@link CodedSymbol}s, optionally including an escape symbol, that can be used to construct a {@link
 * HuffmanCodec}.
 * <p>
 * The constructor {@link #Symbols(CountMap, Object, Minimum)} takes a map of symbol frequencies, an escape symbol
 * (which must have a frequency entry in the map) and a minimum number of times that a symbol must appear to be
 * encoded.
 * <p>
 * Once a set of symbols has been constructed, it can be converted to a {@link PropertyMap} object with {@link
 * #asProperties(StringConverter, Function)}  and saved to a file with {@link PropertyMap#save(WritableResource)}. The
 * properties can later be loaded with {@link PropertyMap#load(Resource)} and passed to {@link #load(PropertyMap,
 * Object, StringConverter)} along with the escape symbol and a converter that can convert property keys into symbols.
 * <p>
 * PropertyMap files for creating tag codecs can easily be constructed by running the <i>CodecGeneratorApplication</i>
 * application which will sample the tags in a PBF file and write them to a set of properties files:
 * <p>
 * <b>Tag Codec Symbol Sets</b>
 * <ul>
 *     <li>key-character.codec</li>
 *     <li>value-character.codec</li>
 *     <li>key-string.codec</li>
 *     <li>value-string.codec</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see HuffmanCodec
 * @see PropertyMap
 * @see CountMap
 * @see StringConverter
 * @see Resource
 * @see WritableResource
 * @see CodedSymbol
 */
@SuppressWarnings("ConstantConditions")
public class Symbols<Symbol>
{
    /**
     * Loads a set of symbols created through statistical analysis of a source file.
     *
     * @param properties The symbols and their frequencies
     * @param converter A converter that converts key values in the properties object into symbols
     * @return A set of symbols
     */
    public static <Symbol> Symbols<Symbol> load(final PropertyMap properties, final StringConverter<Symbol> converter)
    {
        return load(properties, null, converter);
    }

    /**
     * Loads a set of symbols created through statistical analysis of a source file.
     *
     * @param properties The symbols and their frequencies
     * @param escape The symbol that is the escape value, or null if there is no escape value
     * @param converter A converter that converts key values in the properties object into symbols
     * @return A set of symbols
     */
    public static <Symbol> Symbols<Symbol> load(final PropertyMap properties, final Symbol escape,
                                                final StringConverter<Symbol> converter)
    {
        final var counts = new CountMap<Symbol>();
        for (final var key : properties.keySet())
        {
            ensure(!key.contains("="));
            final var symbol = converter.convert(key);
            final var count = properties.count(key);
            counts.add(symbol, count);
        }
        return new Symbols<>(counts, escape, Minimum._0);
    }

    /** Set of encoded symbols */
    private Set<CodedSymbol<Symbol>> encoded = new HashSet<>();

    /** The symbol that is used to escape symbols that are not assigned codes */
    private CodedSymbol<Symbol> escape;

    /**
     * @param frequencies A frequency map containing the number of times each symbol appears
     */
    public Symbols(final CountMap<Symbol> frequencies)
    {
        this(frequencies, null, Minimum._0);
    }

    /**
     * @param frequencies A frequency map containing the number of times each symbol appears
     * @param occurrences The minimum number of times that a symbol must appear to be assigned a Huffman code
     */
    public Symbols(final CountMap<Symbol> frequencies, final Minimum occurrences)
    {
        this(frequencies, null, occurrences);
    }

    /**
     * @param frequencies A frequency map containing the number of times each symbol appears
     * @param escape The symbol used to escape symbols that are not assigned a Huffman code
     * @param occurrences The minimum number of times that a symbol must appear to be assigned a Huffman code
     */
    public Symbols(final CountMap<Symbol> frequencies, final Symbol escape, final Minimum occurrences)
    {
        ensure(frequencies != null);
        ensure(frequencies.size() > 0);
        ensure(occurrences.isGreaterThanOrEqualTo(Count._0));

        // Go through the given symbols
        for (final var symbol : frequencies.keySet())
        {
            // and if the symbol frequency is greater than the minimum number of occurrences,
            final var count = frequencies.count(symbol);
            if (count.isGreaterThanOrEqualTo(occurrences))
            {
                // add it to the symbol set
                final var encodedSymbol = new CodedSymbol<>(symbol, count);
                encoded.add(encodedSymbol);

                // and if the symbol is the escape symbol,
                if (symbol.equals(escape))
                {
                    // then point to it
                    this.escape = encodedSymbol;
                }
            }
        }
    }

    private Symbols()
    {
    }

    /**
     * @param converter A converter to convert symbols into strings to be used as keys in the properties object
     * @return This set of symbols as property map where the keys are symbols and values are frequencies
     */
    public PropertyMap asProperties(final StringConverter<Symbol> converter, final Function<Symbol, String> commenter)
    {
        final var properties = PropertyMap.create();
        for (final var symbol : sortedByFrequency())
        {
            final var key = converter.toString(symbol.value());
            final var value = Count.count(symbol.frequency()).toCommaSeparatedString();
            ensure(!key.contains("="));
            final var comment = commenter.apply(symbol.value());
            if (comment != null)
            {
                properties.comment(key, comment);
            }
            properties.put(key, value);
        }
        return properties;
    }

    /**
     * @return The set of encoded symbols
     */
    public Set<CodedSymbol<Symbol>> encoded()
    {
        return encoded;
    }

    /**
     * @return The escape symbol, if any
     */
    public CodedSymbol<Symbol> escape()
    {
        return escape;
    }

    /**
     * @return The number of symbols in this set
     */
    public int size()
    {
        return encoded.size();
    }

    /**
     * @return List of symbol values
     */
    public ObjectList<Symbol> symbols()
    {
        final var list = new ObjectList<Symbol>();
        for (final var symbol : encoded)
        {
            list.add(symbol.value());
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return Message.format("[Symbols escape = $, size = $]\n    $", escape(), size(),
                ObjectList.objectList(encoded).join("\n    "));
    }

    /**
     * A brute-force algorithm for building a Huffman coding tree where no code exceeds the given maximum bit length.
     * Simply builds a tree, checks its height and if it's too tall, removes the least frequent symbol and tries again.
     */
    public Tree<Symbol> tree(final Maximum bits)
    {
        ensure(!encoded.isEmpty());

        // Make a copy of our symbols
        final var symbols = copy(this);

        // and get a list of them sorted by frequency
        final var sortedByFrequency = sortedByFrequency();

        // then loop
        while (true)
        {
            // and build a Huffman tree from the symbols
            final var tree = Tree.tree(symbols);

            // check the height of the tree,
            assert tree != null;
            if (tree.height() <= bits.asInt())
            {
                // and return the tree if it's short enough to not exceed the maximum number of bits
                return tree;
            }

            // otherwise, remove the least frequent symbol (but not the escape symbol)
            final var leastFrequent = removeLeastFrequent(sortedByFrequency);
            symbols.encoded.remove(leastFrequent);

            // and increase the frequency of any escape symbol by this amount since the symbol will have to
            // be escaped now
            if (escape != null)
            {
                escape.increaseFrequency(leastFrequent.frequency());
            }
        }
    }

    /**
     * @return A copy of the given symbols
     */
    private Symbols<Symbol> copy(final Symbols<Symbol> that)
    {
        final var copy = new Symbols<Symbol>();
        copy.escape = that.escape;
        copy.encoded = new HashSet<>(that.encoded);
        return copy;
    }

    /**
     * @return The least frequently occurring symbol removed from the given queue, but never the escape symbol
     */
    private CodedSymbol<Symbol> removeLeastFrequent(final PriorityQueue<CodedSymbol<Symbol>> queue)
    {
        // Remove the least frequent symbol
        var leastFrequent = queue.remove();

        // and if it's the escape symbol
        if (leastFrequent.equals(escape))
        {
            // then remove the next least frequent symbol
            leastFrequent = queue.remove();

            // and add back the escape symbol
            queue.add(escape);
        }

        return leastFrequent;
    }

    private PriorityQueue<CodedSymbol<Symbol>> sortedByFrequency()
    {
        final var queue = new PriorityQueue<CodedSymbol<Symbol>>(Comparator.comparingLong(CodedSymbol::frequency));
        queue.addAll(encoded);
        return queue;
    }
}
