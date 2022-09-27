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

package com.telenav.kivakit.core.language.primitive;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramPrimitive;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.text.NumberFormat;
import java.util.Locale;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_STATIC_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.MORE_TESTING_NEEDED;

/**
 * Utility methods for <i>int</i> values.
 *
 * <hr>
 *
 * <p><b>Parsing</b></p>
 *
 * <ul>
 *     <li>{@link #parseFast(String)} - Parses the given string, returning {@link #INVALID} instead of throwing an exception. This can make a significant performance improvement when dealing with dirty data, because exceptions are expensive</li>
 *     <li>{@link #parseFast(String, int)} - Parses the given string returning the given flag value if parsing fails</li>
 *     <li>{@link #parseFastNaturalNumber(String)} - Parses a natural number, returning {@link #INVALID} if parsing fails</li>
 * </ul>
 *
 * <hr>
 *
 * <p><b>Words</b></p>
 *
 * <ul>
 *     <li>{@link #high(int)} - Returns the high 16 bit word of the given <i>int</i></li>
 *     <li>{@link #low(int)} - Returns the low 16 bit word of the given <i>int</i></li>
 *     <li>{@link #forHighLow(int, int)} - Returns the two 16 bit words combined into an <i>int</i></li>
 * </ul>
 *
 * <hr>
 *
 * <p><b>Ranges</b></p>
 *
 * <ul>
 *     <li>{@link #inRangeInclusive(int, int, int)} - Returns true if the given value is within the given range, inclusive</li>
 *     <li>{@link #isBetweenInclusive(int, int, int)} - Returns true if the given value is in the given range, inclusive</li>
 *     <li>{@link #isBetweenExclusive(int, int, int)} - Returns true if the given value is in the given range, exclusive</li>
 * </ul>
 *
 * <hr>
 *
 * <p><b>Math</b></p>
 *
 * <ul>
 *     <li>{@link #digits(int)} - Returns the number of digits required to represent the given value</li>
 *     <li>{@link #quantized(int, int)} - Returns the given value quantized to the nearest quantum</li>
 *     <li>{@link #signExtend(int, int)} - Sign extends the given value by the given number of bits</li>
 *     <li>{@link #powerOfTen(int)} - Returns the power of ten for the given exponent</li>
 *     <li>{@link #rounded(double)} - Returns the given value rounded to the nearest <i>int</i></li>
 * </ul>
 *
 * <hr>
 *
 * <p><b>String Conversion</b></p>
 *
 * <ul>
 *     <li>{@link #commaSeparated(int)} - Converts the given value to a comma-separated string</li>
 *     <li>{@link #toHex(int)} - Converts the given <i>int</i> to a hexadecimal string</li>
 *     <li>{@link #toHex(int, int)} - Converts the given value to a hexadecimal string of the given length</li>
 * </ul>
 *
 * <hr>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused") @UmlClassDiagram(diagram = DiagramPrimitive.class)
@ApiQuality(stability = STABLE_STATIC_EXPANDABLE,
            testing = MORE_TESTING_NEEDED,
            documentation = FULLY_DOCUMENTED)
public class Ints
{
    private static final int[] POWERS_OF_10 = { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000,
            1000000000 };

    /** Default invalid value */
    public static final int INVALID = Integer.MIN_VALUE;

    /**
     * Returns the given value formatted with comma separators for the US locale.
     */
    public static String commaSeparated(int value)
    {
        return NumberFormat.getNumberInstance(Locale.US).format(value);
    }

    /**
     * Returns the number of digits in the given value
     */
    public static int digits(int value)
    {
        return (int) (Math.log10(value) + 1);
    }

    /**
     * Returns the given high and low words as a single <i>int</i>
     */
    public static int forHighLow(int highWord, int lowWord)
    {
        return highWord << 16 | lowWord;
    }

    /**
     * Returns the high word of the given <i>int</i>
     */
    public static int high(int value)
    {
        return value >> 16;
    }

    /**
     * Returns the given value, constrained to the given range
     *
     * @param value The value to constrain
     * @param minimum The minimum value, inclusive
     * @param maximum The maximum value, inclusive
     * @return The constrained value4
     */
    public static int inRangeInclusive(int value, int minimum, int maximum)
    {
        return Math.min(Math.max(value, minimum), maximum);
    }

    /**
     * Returns true if the given value is in the given range, exclusive of the high value
     */
    public static boolean isBetweenExclusive(int value, int low, int high)
    {
        return value >= low && value < high;
    }

    /**
     * Returns true if the given value is in the given range, inclusive of the high value
     */
    public static boolean isBetweenInclusive(int value, int low, int high)
    {
        return value >= low && value <= high;
    }

    /**
     * Returns the low word of the given <i>int</i>
     */
    public static int low(int value)
    {
        return value & 0xffff;
    }

    /**
     * @return An integer value or {@link #INVALID} if the string is not a valid integer
     */
    public static int parseFast(String text)
    {
        return parseFast(text, INVALID);
    }

    /**
     * Yes, it turns out that {@link Integer#parseInt(String)} is a major hotspot due to the fact that it throws a
     * {@link NumberFormatException} instead of returning a signal value.
     *
     * @return An integer value or the specified invalid value if the string is not a valid integer
     */
    public static int parseFast(String text, int invalidValue)
    {
        return (int) Longs.parseFast(text, invalidValue);
    }

    /**
     * Yes, it turns out that {@link Integer#parseInt(String)} is a major hotspot due to the fact that it throws a
     * {@link NumberFormatException} instead of returning a signal value.
     *
     * @return A natural number for the string or -1 if the string is not a natural number
     */
    public static int parseFastNaturalNumber(String string)
    {
        var number = Longs.parseFastNaturalNumber(string);
        return number < 0 ? INVALID : (int) number;
    }

    /**
     * @return An integer value or throws an exception if the string is not a valid integer
     */
    public static int parseInt(Listener listener, String text)
    {
        try
        {
            return Integer.parseInt(text);
        }
        catch (Exception e)
        {
            throw listener.problem(e, "Invalid integer: $", text).asException();
        }
    }

    /**
     * @return 10^power for values of power up to 32
     */
    public static int powerOfTen(int power)
    {
        assert power >= 0;
        return POWERS_OF_10[power];
    }

    /**
     * The given value quantized to the nearest quantum
     *
     * @param value The value
     * @param quantum The quantum unit
     * @return The quantized value
     */
    public static int quantized(int value, int quantum)
    {
        var quanta = (value + quantum / 2) / quantum;
        return quantum * quanta;
    }

    /**
     * Returns the given value rounded to the nearest <i>int</i>
     */
    public static int rounded(double value)
    {
        return (int) (value + 0.5);
    }

    /**
     * Sign extends the given value by the given number of bits
     *
     * @param value The value to extend
     * @param bits The number of bits to extend it
     * @return The extended value
     */
    public static int signExtend(int value, int bits)
    {
        if (bits == 32)
        {
            return value;
        }
        else
        {
            return (value << (32 - bits)) >> (32 - bits);
        }
    }

    /**
     * Converts the given value to a hexadecimal string usable as a web color.
     */
    public static String toHex(int value)
    {
        return Integer.toHexString(value);
    }

    /**
     * Converts the given value to a hexadecimal string usable as a web color.
     */
    public static String toHex(int value, int length)
    {
        return String.format("%0" + length + "x", value);
    }
}
