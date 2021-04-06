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

package com.telenav.kivakit.data.compression.project;

import com.telenav.kivakit.core.collections.primitive.array.scalars.ByteArray;
import com.telenav.kivakit.core.collections.primitive.list.ByteList;
import com.telenav.kivakit.core.kernel.language.collections.map.count.CountMap;
import com.telenav.kivakit.core.kernel.language.paths.PackagePath;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.language.values.count.Minimum;
import com.telenav.kivakit.core.kernel.language.values.count.MutableCount;
import com.telenav.kivakit.core.kernel.language.values.mutable.MutableValue;
import com.telenav.kivakit.core.resource.resources.other.PropertyMap;
import com.telenav.kivakit.core.serialization.kryo.CoreKernelKryoTypes;
import com.telenav.kivakit.core.serialization.kryo.KryoTypes;
import com.telenav.kivakit.core.serialization.kryo.KryoUnitTest;
import com.telenav.kivakit.data.compression.Codec;
import com.telenav.kivakit.data.compression.SymbolConsumer;
import com.telenav.kivakit.data.compression.SymbolProducer;
import com.telenav.kivakit.data.compression.codecs.huffman.character.HuffmanCharacterCodec;
import com.telenav.kivakit.data.compression.codecs.huffman.tree.Symbols;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author jonathanl (shibo)
 */
public class DataCompressionUnitTest extends KryoUnitTest
{
    protected ByteList encode(final Codec<String> codec, final List<String> values)
    {
        final var data = new ByteArray("data");
        data.initialize();
        return codec.encode(data, SymbolProducer.fromList(values));
    }

    @NotNull
    protected Symbols<String> fixedSymbolSet()
    {
        return new Symbols<>(new CountMap<String>()
                .add("abc", Count._1_000)
                .add("def", Count._100)
                .add("ghi", Count._10)
                .add("jkl", Count._1));
    }

    @Override
    protected KryoTypes kryoTypes()
    {
        return new CoreKernelKryoTypes().mergedWith(new DataCompressionKryoTypes());
    }

    protected PropertyMap properties(final String name)
    {
        return PropertyMap.load(PackagePath.packagePath(getClass()), name);
    }

    @NotNull
    protected Symbols<Character> randomCharacterSymbols(final int minimum, final int maximum)
    {
        final var frequencies = new CountMap<Character>();
        loop(minimum, maximum, () -> frequencies.add(randomAsciiChar(), Count.count(randomInt(1, 10_000))));
        frequencies.add(HuffmanCharacterCodec.ESCAPE, Count._1024);
        frequencies.add(HuffmanCharacterCodec.END_OF_STRING, Count._1024);
        return new Symbols<>(frequencies, HuffmanCharacterCodec.ESCAPE, Minimum._1);
    }

    protected Symbols<String> randomStringSymbols(final int minimum, final int maximum, final int minimumLength,
                                                  final int maximumLength)
    {
        final var frequencies = new CountMap<String>();
        loop(minimum, maximum, () ->
        {
            while (true)
            {
                final var value = randomAsciiString(minimumLength, maximumLength);
                if (!frequencies.contains(value))
                {
                    frequencies.add(value, Count.count(randomInt(1, 10_000)));
                    break;
                }
            }
        });
        return new Symbols<>(frequencies);
    }

    protected void test(final Codec<String> codec, final List<String> symbols)
    {
        final var data = encode(codec, symbols);
        testDecode(codec, data, symbols);
    }

    protected void testDecode(final Codec<String> codec, final ByteList data, final List<String> expected)
    {
        data.reset();
        final var count = new MutableCount();
        final var indexValue = new MutableValue<Integer>();
        codec.decode(data, (index, next) ->
        {
            ensureEqual(index, (int) count.asLong());
            ensureEqual(next, expected.get(index));
            indexValue.set(index);
            count.increment();
            return index < expected.size() - 1 ? SymbolConsumer.Directive.CONTINUE : SymbolConsumer.Directive.STOP;
        });
        ensureEqual((int) count.asLong(), expected.size());
        ensureEqual(indexValue.get(), expected.size() - 1);
    }
}
