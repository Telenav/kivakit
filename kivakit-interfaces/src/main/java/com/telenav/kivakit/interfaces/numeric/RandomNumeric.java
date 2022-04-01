package com.telenav.kivakit.interfaces.numeric;

import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.interfaces.comparison.Matcher;

import java.util.Objects;

/**
 * Provides convenient random number methods given an implementation of {@link #randomLongExclusive(long, long)}, and
 * {@link #randomDoubleZeroToOne()}. Methods that end in "exclusive" do not include the given maximum value. Methods
 * that end in "inclusive" do include the given maximum value.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused") public interface RandomNumeric extends CastTrait
{
    default <T extends Number> T random(Class<T> type)
    {
        return random(type, Filter.all());
    }

    default <T extends Number> T random(Class<T> type, Matcher<T> filter)
    {
        return randomInclusive(minimum(type), maximum(type), type, filter);
    }

    default boolean randomBoolean()
    {
        return randomIntInclusive(0, 1) == 1;
    }

    default byte randomByte()
    {
        return randomByteInclusive(Byte.MIN_VALUE, Byte.MAX_VALUE);
    }

    default byte randomByteExclusive(byte minimum, byte maximum)
    {
        return (byte) randomIntExclusive(minimum, maximum);
    }

    default byte randomByteInclusive(byte minimum, byte maximum)
    {
        return (byte) randomIntInclusive(minimum, maximum);
    }

    default char randomChar()
    {
        return randomCharInclusive(Character.MIN_VALUE, Character.MAX_VALUE);
    }

    default char randomCharExclusive(char minimum, char maximum)
    {
        return (char) randomIntExclusive(minimum, maximum);
    }

    default char randomCharInclusive(char minimum, char maximum)
    {
        return (char) randomIntInclusive(minimum, maximum);
    }

    default double randomDouble()
    {
        return randomDouble(Double.MIN_VALUE, Double.MAX_VALUE);
    }

    default double randomDouble(double minimum, double maximum)
    {
        var difference = maximum - minimum;
        return minimum + randomDoubleZeroToOne() * difference;
    }

    /**
     * Returns a random value between zero and one
     */
    double randomDoubleZeroToOne();

    default <T extends Number> T randomExclusive(long minimum, long maximum, Class<T> type, Matcher<T> matcher)
    {
        Objects.requireNonNull(matcher);
        T value;
        do
        {
            value = cast(randomLongExclusive(minimum, maximum), type);
        }
        while (!matcher.matches(value));
        return value;
    }

    default <T extends Number> T randomExclusive(long minimum, long maximum, Class<T> type)
    {
        return randomExclusive(minimum, maximum, type, Filter.all());
    }

    default float randomFloat()
    {
        return (float) randomDouble(Float.MIN_VALUE, Float.MAX_VALUE);
    }

    default <T extends Number> T randomInclusive(long minimum, long maximum, Class<T> type, Matcher<T> filter)
    {
        if (maximum + 1 < 0)
        {
            maximum = Long.MAX_VALUE - 1;
        }
        return randomExclusive(minimum, maximum + 1, type, filter);
    }

    default <T extends Number> T randomInclusive(long minimum, long maximum, Class<T> type)
    {
        return randomInclusive(minimum, maximum, type, Filter.all());
    }

    default int randomIndex(int maximum)
    {
        return randomIntExclusive(0, maximum);
    }

    default int randomInt()
    {
        return randomInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    default int randomInt(int minimum, int maximum)
    {
        return randomInclusive(minimum, maximum, Integer.class);
    }

    default int randomIntExclusive(int minimum, int maximum)
    {
        return randomExclusive(minimum, maximum, Integer.class);
    }

    default int randomIntInclusive(int minimum, int maximum)
    {
        return randomInclusive(minimum, maximum, Integer.class);
    }

    default long randomLong()
    {
        return randomInclusive(Long.MIN_VALUE, Long.MAX_VALUE, Long.class);
    }

    long randomLongExclusive(long minimum, long maximum);

    default long randomLongInclusive(long minimum, long maximum)
    {
        return randomLongExclusive(minimum, maximum + 1);
    }

    default short randomShort()
    {
        return randomInclusive(Short.MIN_VALUE, Short.MAX_VALUE, Short.class);
    }

    default short randomShortExclusive(short minimum, short maximum)
    {
        return randomExclusive(minimum, maximum, Short.class);
    }

    default short randomShortInclusive(short minimum, short maximum)
    {
        return randomInclusive(minimum, maximum, Short.class);
    }

    default double randomUnsignedDouble()
    {
        return randomUnsignedDouble(Double.MAX_VALUE);
    }

    default double randomUnsignedDouble(double maximum)
    {
        return randomDouble(0, maximum);
    }

    default int randomUnsignedInt()
    {
        return randomIndex(Integer.MAX_VALUE);
    }

    default long randomUnsignedLong()
    {
        return randomLongInclusive(0, Long.MAX_VALUE);
    }
}
