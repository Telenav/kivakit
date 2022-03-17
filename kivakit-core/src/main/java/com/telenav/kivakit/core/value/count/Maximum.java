////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.value.count;

import com.telenav.kivakit.core.lexakai.DiagramCount;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Collection;
import java.util.Iterator;

/**
 * A count value that is an maximum. This class mainly exists to clarify APIs by giving a meaning to the count value.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramCount.class)
public class Maximum extends AbstractCount<Maximum>
{
    public static final Maximum _0 = new Maximum(0);

    public static final Maximum _1 = new Maximum(1);

    public static final Maximum _2 = new Maximum(2);

    public static final Maximum _3 = new Maximum(3);

    public static final Maximum _4 = new Maximum(4);

    public static final Maximum _5 = new Maximum(5);

    public static final Maximum _6 = new Maximum(6);

    public static final Maximum _7 = new Maximum(7);

    public static final Maximum _8 = new Maximum(8);

    public static final Maximum _9 = new Maximum(9);

    public static final Maximum _10 = new Maximum(10);

    public static final Maximum _11 = new Maximum(11);

    public static final Maximum _12 = new Maximum(12);

    public static final Maximum _13 = new Maximum(13);

    public static final Maximum _14 = new Maximum(14);

    public static final Maximum _15 = new Maximum(15);

    public static final Maximum _16 = new Maximum(16);

    public static final Maximum _17 = new Maximum(17);

    public static final Maximum _18 = new Maximum(18);

    public static final Maximum _19 = new Maximum(19);

    public static final Maximum _20 = new Maximum(20);

    public static final Maximum _21 = new Maximum(21);

    public static final Maximum _22 = new Maximum(22);

    public static final Maximum _23 = new Maximum(23);

    public static final Maximum _24 = new Maximum(24);

    public static final Maximum _25 = new Maximum(25);

    public static final Maximum _26 = new Maximum(26);

    public static final Maximum _27 = new Maximum(27);

    public static final Maximum _28 = new Maximum(28);

    public static final Maximum _29 = new Maximum(29);

    public static final Maximum _30 = new Maximum(30);

    public static final Maximum _31 = new Maximum(31);

    public static final Maximum _32 = new Maximum(32);

    public static final Maximum _64 = new Maximum(64);

    public static final Maximum _128 = new Maximum(128);

    public static final Maximum _256 = new Maximum(256);

    public static final Maximum _512 = new Maximum(512);

    public static final Maximum _1024 = new Maximum(1024);

    public static final Maximum _2048 = new Maximum(2048);

    public static final Maximum _4096 = new Maximum(4096);

    public static final Maximum _8192 = new Maximum(8192);

    public static final Maximum _16384 = new Maximum(16384);

    public static final Maximum _32768 = new Maximum(32678);

    public static final Maximum _65536 = new Maximum(65536);

    public static final Maximum _131_072 = new Maximum(131_072);

    public static final Maximum _262_144 = new Maximum(262_144);

    public static final Maximum _524_288 = new Maximum(524_288);

    public static final Maximum _1_048_576 = new Maximum(1048576);

    public static final Maximum _100 = new Maximum(100);

    public static final Maximum _1_000 = new Maximum(1_000);

    public static final Maximum _10_000 = new Maximum(10_000);

    public static final Maximum _100_000 = new Maximum(100_000);

    public static final Maximum _1_000_000 = new Maximum(1_000_000);

    public static final Maximum _10_000_000 = new Maximum(10_000_000);

    public static final Maximum _100_000_000 = new Maximum(100_000_000);

    public static final Maximum _1_000_000_000 = new Maximum(1_000_000_000);

    public static final Maximum MAXIMUM = new Maximum(Long.MAX_VALUE);

    private static final int CACHE_SIZE = 8_192;

    private static final Maximum[] CACHED;

    private static final Maximum[] CACHED_POWERS_OF_TWO;

    static
    {
        CACHED = new Maximum[CACHE_SIZE];
        for (var i = 0; i < CACHED.length; i++)
        {
            CACHED[i] = new Maximum(i);
        }

        CACHED_POWERS_OF_TWO = new Maximum[63];
        for (var i = 0; i < CACHED_POWERS_OF_TWO.length; i++)
        {
            CACHED_POWERS_OF_TWO[i] = new Maximum(1L << i);
        }
    }

    public static Maximum maximum(Iterable<?> iterable)
    {
        return maximum(iterable.iterator());
    }

    public static Maximum maximum(Iterator<?> iterator)
    {
        var count = 0;
        while (iterator.hasNext())
        {
            iterator.next();
            count++;
        }
        return maximum(count);
    }

    public static Maximum maximum(long value)
    {
        // If we have a cached value,
        if (value < CACHE_SIZE)
        {
            // return it
            return CACHED[(int) value];
        }

        // If our value is a power of two,
        if ((value & value - 1) == 0)
        {
            // return the cached value
            return CACHED_POWERS_OF_TWO[Long.numberOfTrailingZeros(value)];
        }

        // If the value isn't < CACHE_SIZE (65,536) and it's not a power of two we have to allocate
        return new Maximum(value);
    }

    public static <T> Maximum maximum(T[] values)
    {
        return maximum(values.length);
    }

    public static Maximum maximum(Collection<?> collection)
    {
        return maximum(collection.size());
    }

    public static Maximum parseMaximum(Listener listener, String value)
    {
        if (value.indexOf(',') > 0)
        {
            value = Strings.removeAll(value, ',');
        }
        return maximum(Integer.parseInt(value));
    }

    protected Maximum(long count)
    {
        super(count);
    }

    protected Maximum()
    {
        super(0);
    }

    @Override
    protected Maximum newInstance(long count)
    {
        return maximum(count);
    }
}
