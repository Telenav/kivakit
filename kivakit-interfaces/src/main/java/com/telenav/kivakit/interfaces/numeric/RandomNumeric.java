package com.telenav.kivakit.interfaces.numeric;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.interfaces.comparison.Matcher;

import java.util.Objects;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.interfaces.comparison.Matcher.matchAll;

/**
 * Provides convenient random number methods given an implementation of {@link #randomLongExclusive(long, long)}, and
 * {@link #randomDoubleZeroToOne()}. Methods that end in "exclusive" do not include the given maximum value. Methods
 * that end in "inclusive" do include the given maximum value.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public interface RandomNumeric extends CastTrait
{
    /**
     * Returns a random {@link Number} of the given type
     */
    default <T extends Number> T random(Class<T> type)
    {
        return random(type, matchAll());
    }

    /**
     * Returns a random {@link Number} of the given type, matching the given matcher
     */
    default <T extends Number> T random(Class<T> type, Matcher<T> matcher)
    {
        return randomInclusive(minimum(type), maximum(type), type, matcher);
    }

    /**
     * Returns either true or false, randomly
     */
    default boolean randomBoolean()
    {
        return randomIntInclusive(0, 1) == 1;
    }

    /**
     * Returns a random byte value
     */
    default byte randomByte()
    {
        return randomByteInclusive(Byte.MIN_VALUE, Byte.MAX_VALUE);
    }

    /**
     * Returns a random byte value in the given range, exclusive
     */
    default byte randomByteExclusive(byte minimum, byte maximum)
    {
        return (byte) randomIntExclusive(minimum, maximum);
    }

    /**
     * Returns a random byte value in the given range, inclusive
     */
    default byte randomByteInclusive(byte minimum, byte maximum)
    {
        return (byte) randomIntInclusive(minimum, maximum);
    }

    /**
     * Returns a random char value
     */
    default char randomChar()
    {
        return randomCharInclusive(Character.MIN_VALUE, Character.MAX_VALUE);
    }

    /**
     * Returns a random char value in the given range, exclusive
     */
    default char randomCharExclusive(char minimum, char maximum)
    {
        return (char) randomIntExclusive(minimum, maximum);
    }

    /**
     * Returns a random char value in the given range, inclusive
     */
    default char randomCharInclusive(char minimum, char maximum)
    {
        return (char) randomIntInclusive(minimum, maximum);
    }

    /**
     * Returns a random double value
     */
    default double randomDouble()
    {
        return randomDouble(Double.MIN_VALUE, Double.MAX_VALUE);
    }

    /**
     * Returns a random double value in the given range, inclusive
     */
    default double randomDouble(double minimum, double maximum)
    {
        var difference = maximum - minimum;
        return minimum + randomDoubleZeroToOne() * difference;
    }

    /**
     * Returns a random value between zero and one
     */
    double randomDoubleZeroToOne();

    /**
     * @param minimum The minimum value
     * @param maximum The maximum value, exclusive
     * @param type The type of {@link Number}
     * @param matcher The matcher that the result must satisfy
     * @return A random {@link Number} in the given range, exclusive, matching the given matcher
     */
    default <T extends Number> T randomExclusive(long minimum,
                                                 long maximum,
                                                 Class<T> type,
                                                 Matcher<T> matcher)
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

    /**
     * @param minimum The minimum value
     * @param maximum The maximum value, exclusive
     * @param type The type of {@link Number}
     * @return A random {@link Number} in the given range, exclusive, matching the given matcher
     */
    default <T extends Number> T randomExclusive(long minimum, long maximum, Class<T> type)
    {
        return randomExclusive(minimum, maximum, type, matchAll());
    }

    /**
     * Returns a random float value
     */
    default float randomFloat()
    {
        return (float) randomDouble(Float.MIN_VALUE, Float.MAX_VALUE);
    }

    /**
     * @param minimum The minimum value
     * @param maximum The maximum value, inclusive
     * @param type The type of {@link Number}
     * @param matcher The matcher that the result must satisfy
     * @return A random {@link Number} in the given range, exclusive, matching the given matcher
     */
    default <T extends Number> T randomInclusive(long minimum,
                                                 long maximum,
                                                 Class<T> type,
                                                 Matcher<T> matcher)
    {
        if (maximum + 1 < 0)
        {
            maximum = Long.MAX_VALUE - 1;
        }
        return randomExclusive(minimum, maximum + 1, type, matcher);
    }

    /**
     * @param minimum The minimum value
     * @param maximum The maximum value, inclusive
     * @param type The type of {@link Number}
     * @return A random {@link Number} in the given range, exclusive
     */
    default <T extends Number> T randomInclusive(long minimum, long maximum, Class<T> type)
    {
        return randomInclusive(minimum, maximum, type, matchAll());
    }

    /**
     * Returns a random index, between 0 and maximum - 1.
     */
    default int randomIndex(int maximum)
    {
        return randomIntExclusive(0, maximum);
    }

    /**
     * Returns a random int value
     */
    default int randomInt()
    {
        return randomIntInclusive(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Returns a random int value in the given range, exclusive
     */
    default int randomIntExclusive(int minimum, int maximum)
    {
        return randomExclusive(minimum, maximum, Integer.class);
    }

    /**
     * Returns a random int value in the given range, inclusive
     */
    default int randomIntInclusive(int minimum, int maximum)
    {
        return randomInclusive(minimum, maximum, Integer.class);
    }

    /**
     * Returns a random long value
     */
    default long randomLong()
    {
        return randomInclusive(Long.MIN_VALUE, Long.MAX_VALUE, Long.class);
    }

    /**
     * Returns a random long value in the given range, exclusive
     */
    long randomLongExclusive(long minimum, long maximum);

    /**
     * Returns a random long value in the given range, inclusive
     */
    default long randomLongInclusive(long minimum, long maximum)
    {
        return randomLongExclusive(minimum, maximum + 1);
    }

    /**
     * Returns a random short value
     */
    default short randomShort()
    {
        return randomInclusive(Short.MIN_VALUE, Short.MAX_VALUE, Short.class);
    }

    /**
     * Returns a random short value in the given range, exclusive
     */
    default short randomShortExclusive(short minimum, short maximum)
    {
        return randomExclusive(minimum, maximum, Short.class);
    }

    /**
     * Returns a random short value in the given range, inclusive
     */
    default short randomShortInclusive(short minimum, short maximum)
    {
        return randomInclusive(minimum, maximum, Short.class);
    }

    /**
     * Returns a random unsigned double value
     */
    default double randomUnsignedDouble()
    {
        return randomUnsignedDouble(Double.MAX_VALUE);
    }

    /**
     * Returns a random unsigned double value inclusive of the maximum
     */
    default double randomUnsignedDouble(double maximum)
    {
        return randomDouble(0, maximum);
    }

    /**
     * Returns a random positive int value
     */
    default int randomUnsignedInt()
    {
        return randomIndex(Integer.MAX_VALUE);
    }

    /**
     * Returns a random positive long value
     */
    default long randomUnsignedLong()
    {
        return randomLongInclusive(0, Long.MAX_VALUE);
    }
}
