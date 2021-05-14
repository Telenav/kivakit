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

package com.telenav.kivakit.kernel.language.values.count;

import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.language.primitives.Longs;
import com.telenav.kivakit.kernel.language.strings.Strings;
import com.telenav.kivakit.kernel.language.values.level.Percent;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Collection;
import java.util.Iterator;

/**
 * A count value that is a minimum. This class mainly exists to clarify APIs by giving a meaning to the count value.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramLanguageValue.class)
public class Minimum extends Count
{
    public static final Minimum _0 = new Minimum(0);

    public static final Minimum _1 = new Minimum(1);

    public static final Minimum _2 = new Minimum(2);

    public static final Minimum _3 = new Minimum(3);

    public static final Minimum _4 = new Minimum(4);

    public static final Minimum _5 = new Minimum(5);

    public static final Minimum _6 = new Minimum(6);

    public static final Minimum _7 = new Minimum(7);

    public static final Minimum _8 = new Minimum(8);

    public static final Minimum _9 = new Minimum(9);

    public static final Minimum _10 = new Minimum(10);

    public static final Minimum _11 = new Minimum(11);

    public static final Minimum _12 = new Minimum(12);

    public static final Minimum _13 = new Minimum(13);

    public static final Minimum _14 = new Minimum(14);

    public static final Minimum _15 = new Minimum(15);

    public static final Minimum _16 = new Minimum(16);

    public static final Minimum _17 = new Minimum(17);

    public static final Minimum _18 = new Minimum(18);

    public static final Minimum _19 = new Minimum(19);

    public static final Minimum _20 = new Minimum(20);

    public static final Minimum _21 = new Minimum(21);

    public static final Minimum _22 = new Minimum(22);

    public static final Minimum _23 = new Minimum(23);

    public static final Minimum _24 = new Minimum(24);

    public static final Minimum _25 = new Minimum(25);

    public static final Minimum _26 = new Minimum(26);

    public static final Minimum _27 = new Minimum(27);

    public static final Minimum _28 = new Minimum(28);

    public static final Minimum _29 = new Minimum(29);

    public static final Minimum _30 = new Minimum(30);

    public static final Minimum _31 = new Minimum(31);

    public static final Minimum _32 = new Minimum(32);

    public static final Minimum _64 = new Minimum(64);

    public static final Minimum _128 = new Minimum(128);

    public static final Minimum _256 = new Minimum(256);

    public static final Minimum _512 = new Minimum(512);

    public static final Minimum _1024 = new Minimum(1024);

    public static final Minimum _2048 = new Minimum(2048);

    public static final Minimum _4096 = new Minimum(4096);

    public static final Minimum _8192 = new Minimum(8192);

    public static final Minimum _16384 = new Minimum(16384);

    public static final Minimum _32768 = new Minimum(32678);

    public static final Minimum _65536 = new Minimum(65536);

    public static final Minimum _131_072 = new Minimum(131_072);

    public static final Minimum _262_144 = new Minimum(262_144);

    public static final Minimum _524_288 = new Minimum(524_288);

    public static final Minimum _1_048_576 = new Minimum(1048576);

    public static final Minimum _100 = new Minimum(100);

    public static final Minimum _1_000 = new Minimum(1_000);

    public static final Minimum _10_000 = new Minimum(10_000);

    public static final Minimum _100_000 = new Minimum(100_000);

    public static final Minimum _1_000_000 = new Minimum(1_000_000);

    public static final Minimum _10_000_000 = new Minimum(10_000_000);

    public static final Minimum _100_000_000 = new Minimum(100_000_000);

    public static final Minimum _1_000_000_000 = new Minimum(1_000_000_000);

    public static final Minimum MAXIMUM = new Minimum(Long.MAX_VALUE);

    private static final int CACHE_SIZE = 8_192;

    private static final Minimum[] CACHED;

    private static final Minimum[] CACHED_POWERS_OF_TWO;

    private static final Logger LOGGER = LoggerFactory.newLogger();

    static
    {
        CACHED = new Minimum[CACHE_SIZE];
        for (var i = 0; i < CACHED.length; i++)
        {
            CACHED[i] = new Minimum(i);
        }

        CACHED_POWERS_OF_TWO = new Minimum[63];
        for (var i = 0; i < CACHED_POWERS_OF_TWO.length; i++)
        {
            CACHED_POWERS_OF_TWO[i] = new Minimum(1L << i);
        }
    }

    public static Minimum minimum(final Collection<?> collection)
    {
        return minimum(collection.size());
    }

    public static Minimum minimum(final Iterable<?> iterable)
    {
        return minimum(iterable.iterator());
    }

    public static Minimum minimum(final Iterator<?> iterator)
    {
        var count = 0;
        while (iterator.hasNext())
        {
            iterator.next();
            count++;
        }
        return minimum(count);
    }

    public static Minimum minimum(final long value)
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
        return new Minimum(value);
    }

    public static <T> Minimum minimum(final T[] values)
    {
        return minimum(values.length);
    }

    public static Minimum parse(String value)
    {
        if (value.indexOf(',') > 0)
        {
            value = Strings.removeAll(value, ',');
        }
        return minimum(Integer.parseInt(value));
    }

    /**
     * Converts to and from {@link Minimum}
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public static class Converter extends BaseStringConverter<Minimum>
    {
        public Converter(final Listener listener)
        {
            super(listener);
        }

        @Override
        protected Minimum onConvertToObject(final String value)
        {
            return parse(value);
        }
    }

    protected Minimum(final long count)
    {
        super(count);
    }

    protected Minimum()
    {
        super(0);
    }

    @Override
    public BitCount asBitCount()
    {
        return super.asBitCount();
    }

    @Override
    public Minimum ceiling(final int digits)
    {
        return (Minimum) super.ceiling(digits);
    }

    @Override
    public Minimum decremented()
    {
        return (Minimum) super.decremented();
    }

    @Override
    public Minimum dividedBy(final Count divisor)
    {
        return dividedBy(divisor.get());
    }

    @Override
    public Minimum dividedBy(final long divisor)
    {
        return minimum(Longs.inRange(get() / divisor, 0, Long.MAX_VALUE));
    }

    @Override
    public Minimum floor(final int digits)
    {
        return (Minimum) super.floor(digits);
    }

    @Override
    public Minimum incremented()
    {
        return (Minimum) super.incremented();
    }

    @Override
    public Minimum minus(final Count count)
    {
        return (Minimum) super.minus(count);
    }

    @Override
    public Minimum minus(final long count)
    {
        return (Minimum) super.minus(count);
    }

    @Override
    public Minimum minusOne()
    {
        return (Minimum) super.minusOne();
    }

    @Override
    public Minimum nextPrime()
    {
        return (Minimum) super.nextPrime();
    }

    @Override
    public Minimum percent(final Percent percentage)
    {
        return (Minimum) super.percent(percentage);
    }

    @Override
    public Minimum plus(final Count count)
    {
        return plus(count.get());
    }

    @Override
    public Minimum plus(final long count)
    {
        return minimum(get() + count);
    }

    @Override
    public Minimum plusOne()
    {
        return (Minimum) super.plusOne();
    }

    @Override
    public Minimum roundUpToPowerOfTwo()
    {
        return (Minimum) super.roundUpToPowerOfTwo();
    }

    @Override
    public Minimum times(final Count count)
    {
        if (count.isMaximum() || isMaximum())
        {
            return MAXIMUM;
        }
        return minimum(Longs.inRange(get() * count.get(), 0, Long.MAX_VALUE));
    }

    @Override
    public Minimum times(final double multiplier)
    {
        return (Minimum) super.times(multiplier);
    }

    @Override
    public Minimum times(final long count)
    {
        return (Minimum) super.times(count);
    }

    @Override
    public Minimum times(final Percent percentage)
    {
        return (Minimum) super.times(percentage);
    }

    @Override
    protected Minimum onNewInstance(final long value)
    {
        return minimum(value);
    }
}
