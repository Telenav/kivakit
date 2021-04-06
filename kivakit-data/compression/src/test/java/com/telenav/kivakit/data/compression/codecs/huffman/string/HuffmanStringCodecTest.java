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
