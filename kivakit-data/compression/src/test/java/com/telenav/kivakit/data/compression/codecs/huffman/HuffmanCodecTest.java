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

import com.telenav.kivakit.core.kernel.language.collections.map.count.CountMap;
import com.telenav.kivakit.core.kernel.language.progress.reporters.Progress;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.data.compression.SymbolConsumer;
import com.telenav.kivakit.data.compression.codecs.huffman.tree.Symbols;
import com.telenav.kivakit.data.compression.project.DataCompressionUnitTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class HuffmanCodecTest extends DataCompressionUnitTest
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /**
     * 123,869,689 per second
     */
    // @Test
    public void testBenchmark()
    {
        final var symbols = new Symbols<>(new CountMap<String>()
                .add("a", Count._1)
                .add("b", Count._10)
                .add("c", Count._1_000)
                .add("d", Count._100)
                .add("last", Count._10_000));

        final var codec = HuffmanCodec.from(symbols, Maximum._8);

        //        [HuffmanCodec size = 4, bits = 3]:
        //        1. 000 -> a (1)
        //        2. 001 -> b (10)
        //        3. 01 -> d (100)
        //        4. 1 -> c (1,000)

        // Message.println(codec.toString());

        final var encoded = encode(codec, List.of("a", "b", "a", "c", "a", "a", "a", "a", "last"));

        final var progress = Progress.create(LOGGER);
        for (int i = 0; i < 1_000_000_000; i++)
        {
            codec.decode(encoded, (ordinal, next) -> "last".equals(next) ? SymbolConsumer.Directive.STOP : SymbolConsumer.Directive.CONTINUE);
            progress.next();
        }
    }

    @Test
    public void testDecode()
    {
        final var symbols = fixedSymbolSet();
        final var codec = HuffmanCodec.from(symbols, Maximum._8);

        //        [HuffmanCodec size = 4, bits = 3]:
        //        1. 000 -> jkl (1)
        //        2. 001 -> ghi (10)
        //        3. 01 -> def (100)
        //        4. 1 -> abc (1,000)

        // Message.println(codec.toString());

        test(codec, List.of("abc", "abc", "abc"));
        test(codec, List.of("def", "def", "def"));
        test(codec, List.of("ghi", "ghi", "ghi"));
        test(codec, List.of("jkl", "jkl", "jkl", "jkl", "jkl"));
    }

    @Test
    public void testEncode()
    {
        final var symbols = fixedSymbolSet();

        final var codec = HuffmanCodec.from(symbols, Maximum._8);

        //        [HuffmanCodec size = 4, bits = 3]:
        //        1. 000 -> jkl (1)
        //        2. 001 -> ghi (10)
        //        3. 01 -> def (100)
        //        4. 1 -> abc (1,000)

        // Message.println(codec.toString());

        ensureEqual(4, codec.size());
        ensureEqual(1, encode(codec, List.of("abc", "abc", "abc")).size());
        ensureEqual(1, encode(codec, List.of("def", "def", "def")).size());
        ensureEqual(2, encode(codec, List.of("ghi", "ghi", "ghi")).size());
        ensureEqual(2, encode(codec, List.of("jkl", "jkl", "jkl", "jkl", "jkl")).size());
    }

    @Test
    public void testFailure()
    {
        final var symbols = new Symbols<>(new CountMap<String>()
                .add("db", Count._1)
                .add("qts", Count._1)
                .add("vkl", Count._1)
                .add("oonpv", Count._1));

        final var codec = HuffmanCodec.from(symbols, Maximum._8);

        test(codec, List.of("db", "qts", "vkl", "qts", "oonpv", "db", "db", "db"));
        test(codec, List.of("db", "oonpv", "vkl", "qts", "oonpv", "db"));
        test(codec, List.of("db", "db", "vkl", "qts", "vkl", "db", "db", "db"));
        test(codec, List.of("db", "qts", "db", "qts", "oonpv", "db"));
    }

    @Test
    public void testFailure2()
    {
        final var symbols = new Symbols<>(new CountMap<String>()
                .add("stxq", Count.count(803))
                .add("sshtp", Count.count(1_366))
                .add("i", Count.count(7_088))
                .add("zvgupm", Count.count(7_486)));

        final var codec = HuffmanCodec.from(symbols, Maximum._8);

        //        [HuffmanCodec size = 4, bits = 3]:
        //        1. 100 -> stxq (803)
        //        2. 101 -> sshtp (1,366)
        //        3. 11 -> i (7,088)
        //        4. 0 -> zvgupm (7,486)

        // Message.println(codec.toString());

        test(codec, List.of("stxq", "sshtp", "sshtp", "i", "zvgupm", "zvgupm", "zvgupm", "stxq"));
    }

    @Test
    public void testFailure3()
    {
        final var symbols = new Symbols<>(new CountMap<String>()
                .add("a", Count._1)
                .add("b", Count._1)
                .add("c", Count._1)
                .add("d", Count._1)
                .add("end", Count._1));

        final var codec = HuffmanCodec.from(symbols, Maximum._8);

        //        [HuffmanCodec size = 5, bits = 3]:
        //        1. 00 -> a (1)
        //        2. 111 -> b (1)
        //        3. 10 -> c (1)
        //        4. 110 -> d (1)
        //        5. 01 -> end (1)

        // Message.println(codec.toString());

        final var values = List.of("a", "c", "a", "d", "a", "a", "a", "a", "end");
        final var data = encode(codec, values);

        // Message.println("data:\n$", data.toBinaryString());

        // a  c  a  d      a  a  a  a     end
        // 00 10 00 11 | 0 00 00 00 0 | 0 01

        testDecode(codec, data, values);
    }

    @Test
    public void testRandom()
    {
        final var progress = Progress.create();

        // For each random codec
        loop(10, codecNumber ->
        {
            final var symbols = randomStringSymbols(2, 200, 1, 8);
            final var codec = HuffmanCodec.from(symbols, Maximum._8);

            // test it a few times
            loop(100, testNumber ->
            {
                // by creating a random list of values to encode from the coded symbols in the codec
                final var values = new ArrayList<String>();
                final var choices = new ArrayList<>(codec.codedSymbols());
                loop(2, 100, ordinal -> values.add(choices.get(randomInt(0, choices.size() - 1)).value()));

                // and trying to encode and decode those values
                test(codec, values);

                progress.next();
            });
        });
    }
}
