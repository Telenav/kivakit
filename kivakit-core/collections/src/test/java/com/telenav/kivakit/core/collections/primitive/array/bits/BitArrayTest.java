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

package com.telenav.kivakit.core.collections.primitive.array.bits;

import com.telenav.kivakit.core.collections.primitive.array.bits.io.BitReader;
import com.telenav.kivakit.core.collections.primitive.array.bits.io.BitWriter;
import com.telenav.kivakit.core.collections.project.CoreCollectionsUnitTest;
import com.telenav.kivakit.core.kernel.language.bits.Bits;
import com.telenav.kivakit.core.kernel.language.io.IO;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import org.junit.Test;

public class BitArrayTest extends CoreCollectionsUnitTest
{
    @Test
    public void test()
    {
        final var bits = new BitArray("test");
        bits.initialize();
        final BitWriter writer = bits.writer();
        for (var i = 0; i < 8; i++)
        {
            writer.write(i, 5);
        }
        IO.close(writer);
        ensureEqual("00000 00001 00010 00011 00100 00101 00110 00111".replaceAll(" ", ""),
                bits.toBitString().replaceAll(" ", ""));
        final BitReader reader = bits.reader();
        for (var i = 0; i < 8; i++)
        {
            ensureEqual(i, reader.read(5));
        }
        IO.close(reader);
    }

    @Test
    public void test32Bit()
    {
        final var bits = new BitArray("test");
        bits.initialize();

        final BitWriter writer = bits.writer();
        writer.write(47677740, 32);
        writer.close();

        ensureEqual("00000010110101111000000100101100", bits.toBitString().replaceAll(" ", ""));

        final BitReader reader = bits.reader();
        final var read = reader.read(32);
        ensureEqual(47677740, read);
        reader.close();
    }

    @Test
    public void testExhaustive()
    {
        final var bits = new BitArray("test");
        bits.initialize();
        final BitWriter writer = bits.writer();
        for (var i = 0; i < 1_000; i++)
        {
            for (var j = 1; j <= 32; j++)
            {
                writer.write(i, j);
            }
        }
        IO.close(writer);

        final BitReader reader = bits.reader();
        for (var i = 0; i < 1_000; i++)
        {
            for (var j = 1; j <= 32; j++)
            {
                final int mask = (int) Bits.oneBits(Count.count(j));
                ensureEqual(i & mask, reader.read(j));
            }
        }
        IO.close(reader);
    }

    @Test
    public void testReadByBytes()
    {
        final var bits = new BitArray("test");
        bits.initialize();
        final BitWriter writer = bits.writer();
        writer.write(0xf0f0f0f0, 32);
        IO.close(writer);
        final BitReader reader = bits.reader();
        final var read = reader.read(32);
        ensureEqual(0xf0f0f0f0, read);
        IO.close(reader);
    }

    @Test
    public void testSeek()
    {
        final var bits = new BitArray("test");
        bits.initialize();
        final BitWriter writer = bits.writer();
        final long start = writer.cursor();
        ensureEqual(0L, start);
        writer.write(5, 5);
        final long middle = writer.cursor();
        ensureEqual(5L, middle);
        writer.write(10, 5);
        final long end = writer.cursor();
        ensureEqual(10L, end);
        IO.close(writer);
        final BitReader reader = bits.reader();
        ensureEqual(0L, reader.cursor());
        reader.cursor(middle);
        ensureEqual(middle, reader.cursor());
        final int ten = reader.read(5);
        ensureEqual(10, ten);
        reader.cursor(start);
        ensureEqual(start, reader.cursor());
        final int five = reader.read(5);
        ensureEqual(5, five);
    }
}
