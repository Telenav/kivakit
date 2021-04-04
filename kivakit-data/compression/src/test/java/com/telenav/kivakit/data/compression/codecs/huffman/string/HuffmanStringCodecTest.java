////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.compression.codecs.huffman.string;

import com.telenav.kivakit.core.kernel.language.progress.reporters.Progress;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.data.compression.project.DataCompressionUnitTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class HuffmanStringCodecTest extends DataCompressionUnitTest
{
    @Test
    public void testDecode()
    {
        final var codec = HuffmanStringCodec.from(properties("string.codec"));

        test(codec, List.of("bicycle", "barrier", "highway"));
        test(codec, List.of("oneway", "foot", "access", "footway"));
        test(codec, List.of("amenity", "footway", "maxspeed", "footway"));
    }

    @Test
    public void testRandom()
    {
        final var progress = Progress.create(Listener.none(), "codec");
        loop(10, codecNumber ->
        {
            final var symbols = randomStringSymbols(2, 100, 1, 100);
            final var codec = HuffmanStringCodec.from(symbols);
            final var choices = symbols.symbols();

            final var test = Progress.create(Listener.none(), "test");
            loop(100, testNumber ->
            {
                final var input = new ArrayList<String>();
                loop(1, 200, () -> input.add(choices.get(randomInt(0, choices.size() - 1))));
                test(codec, input);
                test.next();
            });
            progress.next();
        });
    }
}
