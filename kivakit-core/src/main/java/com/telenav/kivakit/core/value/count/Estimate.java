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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramCount;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Collection;
import java.util.Iterator;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * A count value that is an estimate. This class mainly exists to clarify APIs by giving a meaning to the count value.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramCount.class)
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class Estimate extends BaseCount<Estimate>
{
    public static final Estimate _0 = new Estimate(0);

    public static final Estimate _1 = new Estimate(1);

    public static final Estimate _2 = new Estimate(2);

    public static final Estimate _3 = new Estimate(3);

    public static final Estimate _4 = new Estimate(4);

    public static final Estimate _5 = new Estimate(5);

    public static final Estimate _6 = new Estimate(6);

    public static final Estimate _7 = new Estimate(7);

    public static final Estimate _8 = new Estimate(8);

    public static final Estimate _9 = new Estimate(9);

    public static final Estimate _10 = new Estimate(10);

    public static final Estimate _11 = new Estimate(11);

    public static final Estimate _12 = new Estimate(12);

    public static final Estimate _13 = new Estimate(13);

    public static final Estimate _14 = new Estimate(14);

    public static final Estimate _15 = new Estimate(15);

    public static final Estimate _16 = new Estimate(16);

    public static final Estimate _17 = new Estimate(17);

    public static final Estimate _18 = new Estimate(18);

    public static final Estimate _19 = new Estimate(19);

    public static final Estimate _20 = new Estimate(20);

    public static final Estimate _21 = new Estimate(21);

    public static final Estimate _22 = new Estimate(22);

    public static final Estimate _23 = new Estimate(23);

    public static final Estimate _24 = new Estimate(24);

    public static final Estimate _25 = new Estimate(25);

    public static final Estimate _26 = new Estimate(26);

    public static final Estimate _27 = new Estimate(27);

    public static final Estimate _28 = new Estimate(28);

    public static final Estimate _29 = new Estimate(29);

    public static final Estimate _30 = new Estimate(30);

    public static final Estimate _31 = new Estimate(31);

    public static final Estimate _32 = new Estimate(32);

    public static final Estimate _64 = new Estimate(64);

    public static final Estimate _128 = new Estimate(128);

    public static final Estimate _256 = new Estimate(256);

    public static final Estimate _512 = new Estimate(512);

    public static final Estimate _1024 = new Estimate(1024);

    public static final Estimate _2048 = new Estimate(2048);

    public static final Estimate _4096 = new Estimate(4096);

    public static final Estimate _8192 = new Estimate(8192);

    public static final Estimate _16384 = new Estimate(16384);

    public static final Estimate _32768 = new Estimate(32678);

    public static final Estimate _65536 = new Estimate(65536);

    public static final Estimate _131_072 = new Estimate(131_072);

    public static final Estimate _262_144 = new Estimate(262_144);

    public static final Estimate _524_288 = new Estimate(524_288);

    public static final Estimate _1_048_576 = new Estimate(1_048_576);

    public static final Estimate _100 = new Estimate(100);

    public static final Estimate _1_000 = new Estimate(1_000);

    public static final Estimate _10_000 = new Estimate(10_000);

    public static final Estimate _100_000 = new Estimate(100_000);

    public static final Estimate _1_000_000 = new Estimate(1_000_000);

    public static final Estimate _10_000_000 = new Estimate(10_000_000);

    public static final Estimate _100_000_000 = new Estimate(100_000_000);

    public static final Estimate _1_000_000_000 = new Estimate(1_000_000_000);

    public static final Estimate MAXIMUM = new Estimate(Long.MAX_VALUE);

    private static final int CACHE_SIZE = 8_192;

    private static final Estimate[] CACHED;

    private static final Estimate[] CACHED_POWERS_OF_TWO;

    static
    {
        CACHED = new Estimate[CACHE_SIZE];
        for (var i = 0; i < CACHED.length; i++)
        {
            CACHED[i] = new Estimate(i);
        }

        CACHED_POWERS_OF_TWO = new Estimate[63];
        for (var i = 0; i < CACHED_POWERS_OF_TWO.length; i++)
        {
            CACHED_POWERS_OF_TWO[i] = new Estimate(1L << i);
        }
    }

    /**
     * Returns the number of values as an {@link Estimate}
     */
    public static Estimate estimate(Collection<?> collection)
    {
        return estimate(collection.size());
    }

    /**
     * Returns the number of values as an {@link Estimate}
     */
    public static Estimate estimate(Iterable<?> iterable)
    {
        return estimate(iterable.iterator());
    }

    /**
     * Returns the number of values as an {@link Estimate}
     */
    public static Estimate estimate(Iterator<?> iterator)
    {
        var count = 0L;
        while (iterator.hasNext())
        {
            iterator.next();
            count++;
        }
        return estimate(count);
    }

    /**
     * Returns the value as an {@link Estimate}
     */
    public static Estimate estimate(long value)
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
        return new Estimate(value);
    }

    /**
     * Returns the number of array elements as an {@link Estimate}
     */
    public static <T> Estimate estimate(T[] values)
    {
        return estimate(values.length);
    }

    /**
     * Parses the given text into an {@link Estimate}
     *
     * @param listener The listener to call with any problems
     * @param text The text to parse
     * @return The estimate
     */
    public static Estimate parseEstimate(Listener listener, String text)
    {
        if (text.indexOf(',') > 0)
        {
            text = Strings.removeAll(text, ',');
        }
        return Estimate.estimate(Long.parseLong(text));
    }

    protected Estimate(long estimate)
    {
        super(estimate);
    }

    protected Estimate()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String asString()
    {
        return super.toString() + " (estimate)";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Estimate onNewInstance(long count)
    {
        return estimate(count);
    }
}
