////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.test.random;

import com.telenav.kivakit.core.kernel.language.objects.Hash;
import com.telenav.kivakit.core.kernel.language.values.identifier.Identifier;
import com.telenav.kivakit.core.kernel.language.values.level.Confidence;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.Debug;
import com.telenav.kivakit.core.test.UnitTest;
import com.telenav.kivakit.core.test.project.lexakai.diagrams.DiagramTest;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;

/**
 * Utility class for tests used to create random values. {@link UnitTest} has a variety of methods for random testing
 * that use this class
 *
 * @author jonathanl (shibo)
 * @see UnitTest
 */
@UmlClassDiagram(diagram = DiagramTest.class)
public class RandomValueFactory
{
    private static volatile long seedUniquifier = 8682522807148012L;

    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final Debug DEBUG = new Debug(LOGGER);

    protected Random random;

    private long seed;

    public RandomValueFactory(final long seed)
    {
        seed(seed);
    }

    public RandomValueFactory()
    {
        seed(newSeed());
    }

    @SuppressWarnings("SpellCheckingInspection")
    public char newAsciiChar()
    {
        final var alphabet = "abcdefghijklmonpqrstuvwxyz";
        return alphabet.charAt(newInt(0, alphabet.length()));
    }

    public String newAsciiString()
    {
        return newAsciiString(1, 32);
    }

    public String newAsciiString(final int minLength, final int maxLength)
    {
        final var builder = new StringBuilder();
        for (var i = 0; i < newCount(minLength, maxLength); i++)
        {
            builder.append(newAsciiChar());
        }
        return builder.toString();
    }

    public boolean newBoolean()
    {
        return newInt(0, 2) == 0;
    }

    public final byte newByte(final byte minimum, final byte maximum)
    {
        return (byte) newInt(minimum, maximum);
    }

    public final byte newByte()
    {
        return (byte) newInt();
    }

    public final byte newByte(final byte minimum, final byte maximum, final Predicate<Byte> filter)
    {
        byte value;
        do
        {
            value = newByte(minimum, maximum);
        }
        while (filter != null && !filter.test(value));
        return value;
    }

    public char newChar()
    {
        return (char) newInt(0, 32768);
    }

    public Confidence newConfidence()
    {
        return new Confidence(newDoubleZeroToOne());
    }

    public int newCount(final int minimum, final int maximum)
    {
        return Math.abs(newInt(minimum, maximum));
    }

    public final double newDouble()
    {
        return newDouble(-1 * Double.MAX_VALUE, Double.MIN_VALUE);
    }

    public final double newDouble(final double minimum, final double maximum)
    {
        final var difference = maximum - minimum;
        return minimum + random.nextDouble() * difference;
    }

    public final double newDoubleZeroToOne()
    {
        return random.nextDouble();
    }

    public <T> T newFrom(final Collection<T> values)
    {
        if (values.isEmpty())
        {
            return null;
        }
        else
        {
            final List<T> list = new ArrayList<>(values);
            return list.get(newIndex(list.size()));
        }
    }

    public <T> T newFrom(final T[] values)
    {
        if (values.length == 0)
        {
            return null;
        }
        else
        {
            return values[newIndex(values.length)];
        }
    }

    public Identifier newIdentifier(final long maximum)
    {
        return new Identifier(newLong(1, maximum));
    }

    public int newIndex(final int maximum)
    {
        ensure(maximum > 0);
        return newInt(0, maximum);
    }

    public final int newInt(final int minimum, final int maximum, final Predicate<Integer> filter)
    {
        int value;
        do
        {
            value = newInt(minimum, maximum);
        }
        while (filter != null && !filter.test(value));
        return value;
    }

    public final int newInt(final int minimum, final int maximum)
    {
        return (int) newLong(minimum, maximum);
    }

    public final int newInt()
    {
        return random.nextInt();
    }

    public final long newLong(final long minimum, final long maximum, final Predicate<Long> filter)
    {
        long value;
        do
        {
            value = newLong(minimum, maximum);
        }
        while (filter != null && !filter.test(value));
        return value;
    }

    public final long newLong()
    {
        final var value = random.nextLong();
        DEBUG.trace("Next = $", value);
        return value;
    }

    public final long newLong(final long minimum, final long maximum)
    {
        final var value = internalNextLong(minimum, maximum);
        DEBUG.trace("Next long = $", value);
        return value;
    }

    public final short newShort(final short minimum, final short maximum, final Predicate<Short> filter)
    {
        short value;
        do
        {
            value = newShort(minimum, maximum);
        }
        while (filter != null && !filter.test(value));
        return value;
    }

    public final short newShort(final short minimum, final short maximum)
    {
        return (short) newInt(minimum, maximum);
    }

    public final short newShort()
    {
        return (short) newInt();
    }

    public String newString()
    {
        return newString(1, 1024);
    }

    public String newString(final int minLength, final int maxLength)
    {
        final var builder = new StringBuilder();
        for (var i = 0; i < newInt(minLength, maxLength); i++)
        {
            builder.append(newChar());
        }
        return builder.toString();
    }

    public final double newUnsignedDouble()
    {
        return newUnsignedDouble(Double.MAX_VALUE);
    }

    public final double newUnsignedDouble(final double maximum)
    {
        return newDouble(0, maximum);
    }

    public final int newUnsignedInt()
    {
        return newInt(0, Integer.MAX_VALUE);
    }

    public final long newUnsignedLong()
    {
        return newLong(0, Long.MAX_VALUE);
    }

    public long seed()
    {
        return seed;
    }

    public void seed(final long seed)
    {
        random = new Random(seed);
        this.seed = seed;
    }

    /**
     * NOTE: This (poorly documented) method is verbatim from java.util.Random.java. It is being used because the task
     * of finding a random long between two longs involves complex overflow issues and this code has been tested.
     * <p>
     * The form of nextLong used by LongStream Spliterators.  If origin is greater than bound, acts as unbounded form of
     * nextLong, else as bounded form.
     *
     * @param origin the least value, unless greater than bound
     * @param bound the upper bound (exclusive), must not equal origin
     * @return a pseudorandom value
     */
    @SuppressWarnings("StatementWithEmptyBody")
    private long internalNextLong(final long origin, final long bound)
    {
        if (origin == bound)
        {
            return origin;
        }
        var r = random.nextLong();
        if (origin < bound)
        {
            final var n = bound - origin;
            final var m = n - 1;
            if ((n & m) == 0L)  // power of two
            {
                r = (r & m) + origin;
            }
            else if (n > 0L)
            {  // reject over-represented candidates
                for (var u = r >>> 1;            // ensure non-negative
                     u + m - (r = u % n) < 0L;    // rejection check
                     u = random.nextLong() >>> 1) // retry
                {
                }
                r += origin;
            }
            else
            {              // range not representable as long
                while (r < origin || r >= bound)
                {
                    r = random.nextLong();
                }
            }
        }
        return r;
    }

    private synchronized long newSeed()
    {
        // NOTE: this code comes from java.util.Random, since it does not expose the way it creates
        // seed values
        return Hash.knuth(++seedUniquifier + System.nanoTime());
    }
}
