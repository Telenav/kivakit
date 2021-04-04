////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.compression.codecs.huffman.character;

import com.telenav.kivakit.core.kernel.language.progress.reporters.Progress;
import com.telenav.kivakit.data.compression.SymbolConsumer;
import com.telenav.kivakit.data.compression.project.DataCompressionUnitTest;
import org.junit.Test;

import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class HuffmanCharacterCodecTest extends DataCompressionUnitTest
{
    /**
     * 69,458,915 per second
     */
    // @Test
    public void testBenchmark()
    {
        final var codec = HuffmanCharacterCodec.from(properties("character.codec"), HuffmanCharacterCodec.ESCAPE);

        final var encoded = encode(codec,
                List.of("this is a test", "whatever", "banana", "cherry",
                        "apple", "coconut", "cookie", "whatever", "last"));

        final var progress = Progress.create();
        for (int i = 0; i < 1_000_000_000; i++)
        {
            codec.decode(encoded, (ordinal, next) -> "last".equals(next) ? SymbolConsumer.Directive.STOP : SymbolConsumer.Directive.CONTINUE);
            progress.next();
        }
    }

    @Test
    public void testDecode()
    {
        final var codec = HuffmanCharacterCodec.from(properties("character.codec"), HuffmanCharacterCodec.ESCAPE);

        test(codec, List.of("880號州際公路'"));
        test(codec, List.of("z"));
        test(codec, List.of("a", "b", "abc"));
        test(codec, List.of("alphabet", "zowie", "querty"));
    }

    @Test
    public void testRandom()
    {
        loop(10, codecNumber ->
        {
            final var symbols = randomCharacterSymbols(2, 26);
            final var codec = HuffmanCharacterCodec.from(symbols);

            final var progress = Progress.create();
            loop(100, () ->
            {
                final var random = randomStringSymbols(1, 64, 1, 32);
                test(codec, random.symbols());

                progress.next();
            });
        });
    }
}
