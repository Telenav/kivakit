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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramPrimitive;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static java.lang.Long.toHexString;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.String.format;

/**
 * Utility methods for <i>int</i> values.
 *
 * <p><b>Parsing</b></p>
 *
 * <ul>
 *     <li>{@link #parseFastLong(String)} - Parses the given string, returning {@link #INVALID_LONG} instead of throwing an exception. This can make a significant performance improvement when dealing with dirty data, because exceptions are expensive</li>
 *     <li>{@link #parseFastLong(String, long)} - Parses the given string returning the given flag value if parsing fails</li>
 *     <li>{@link #parseFastNaturalNumber(String)} - Parses a natural number, returning {@link #INVALID_LONG} if parsing fails</li>
 * </ul>
 *
 * <p><b>Words</b></p>
 *
 * <ul>
 *     <li>{@link #longHighWord(long)} - Returns the high 32 bit word of the given <i>long</i></li>
 *     <li>{@link #longLowWord(long)} - Returns the low 32 bit word of the given <i>int</i></li>
 *     <li>{@link #longForWords(int, int)} - Returns the two 32 bit words combined into a <i>long</i></li>
 * </ul>
 *
 * <p><b>Ranges</b></p>
 *
 * <ul>
 *     <li>{@link #longInRangeInclusive(long, long, long)} - Returns true if the given value is within the given range, inclusive</li>
 *     <li>{@link #longIsBetweenInclusive(long, long, long)} - Returns true if the given value is in the given range, inclusive</li>
 *     <li>{@link #longIsBetweenExclusive(long, long, long)} - Returns true if the given value is in the given range, exclusive</li>
 * </ul>
 *
 * <p><b>String Conversion</b></p>
 *
 * <ul>
 *     <li>{@link #longAsCommaSeparated(long)} - Converts the given value to a comma-separated string</li>
 *     <li>{@link #longToHex(long)} - Converts the given <i>int</i> to a hexadecimal string</li>
 *     <li>{@link #longToHex(long, long)} - Converts the given value to a hexadecimal string of the given length</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramPrimitive.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class Longs
{
    /** Default invalid value */
    public static final long INVALID_LONG = Long.MIN_VALUE;

    /**
     * Returns the given value formatted with comma separators for the US locale.
     */
    public static String longAsCommaSeparated(long value)
    {
        return NumberFormat.getNumberInstance(Locale.US).format(value);
    }

    public static long longForWords(int high, int low)
    {
        return (((long) high) << 32) | (low & 0xffff_ffffL);
    }

    public static int longHighWord(long value)
    {
        return (int) (value >> 32);
    }

    public static long longInRangeInclusive(long value, long min, long max)
    {
        return min(max(value, min), max);
    }

    /**
     * Returns true if the given value is in the given range (exclusive)
     */
    public static boolean longIsBetweenExclusive(long value, long low, long high)
    {
        return value >= low && value < high;
    }

    /**
     * Returns true if the given value is in the given range (inclusive)
     */
    public static boolean longIsBetweenInclusive(long value, long low, long high)
    {
        return value >= low && value <= high;
    }

    public static int longLowWord(long value)
    {
        return (int) value;
    }

    /**
     * NOTE: This (poorly documented) method is verbatim from java.util.Random.java. It is being used because the task
     * of finding a random long between two longs involves complex overflow issues and this code has been tested.
     * <p>
     * The form of nextLong used by LongStream Spliterators.  If origin is greater than bound, acts as unbounded form of
     * nextLong, else as bounded form.
     *
     * @param minimum the least value, unless greater than bound
     * @param maximumExclusive the upper bound (exclusive), must not equal origin
     * @return a pseudorandom value
     */
    @SuppressWarnings("StatementWithEmptyBody")
    public static long longRandom(Random random, long minimum, long maximumExclusive)
    {
        if (minimum == maximumExclusive)
        {
            return minimum;
        }
        var r = random.nextLong();
        if (minimum < maximumExclusive)
        {
            var n = maximumExclusive - minimum;
            var m = n - 1;
            if ((n & m) == 0L)  // power of two
            {
                r = (r & m) + minimum;
            }
            else if (n > 0L)
            {  // reject over-represented candidates
                for (var u = r >>> 1;            // ensure non-negative
                     u + m - (r = u % n) < 0L;    // rejection check
                     u = random.nextLong() >>> 1) // retry
                {
                }
                r += minimum;
            }
            else
            {              // range not representable as long
                while (r < minimum || r >= maximumExclusive)
                {
                    r = random.nextLong();
                }
            }
        }
        return r;
    }

    /**
     * @param value The value to search
     * @param bits The word length
     * @param searchFor The value to locate
     * @return True if the value contains the search term in the words
     */
    public static boolean longSearchWords(long value, int bits, int searchFor)
    {
        for (var remaining = 64; remaining > 0; remaining -= bits)
        {
            var word = (int) (value & 0xffffL);
            if (word == searchFor)
            {
                return true;
            }
            value >>>= bits;
        }
        return false;
    }

    /**
     * Converts the given value to a hexadecimal string usable as a web color.
     */
    public static String longToHex(long value)
    {
        return toHexString(value);
    }

    /**
     * Converts the given value to a hexadecimal string usable as a web color.
     */
    public static String longToHex(long value, long length)
    {
        return format("%0" + length + "x", value);
    }

    public static long parseFastLong(String string)
    {
        return parseFastLong(string, INVALID_LONG);
    }

    /**
     * Yes, it turns out that {@link Long#parseLong(String)} is a major hotspot due to the fact that it throws a
     * {@link NumberFormatException} instead of returning a signal value.
     *
     * @return An integer value or the specified invalid value if the string is not a valid integer
     */
    public static long parseFastLong(String string, long invalid)
    {
        if (string != null)
        {
            var length = string.length();
            if (length != 0)
            {
                var i = 0;
                var sign = 1;

                var first = string.charAt(0);
                if (first == '-')
                {
                    i++;
                    sign = -1;
                }

                long value = 0;
                while (i < length)
                {
                    var character = string.charAt(i++);
                    if (character == ',')
                    {
                        continue;
                    }
                    var digit = character - '0';
                    if (digit < 0 || digit > 9)
                    {
                        return invalid;
                    }
                    value = value * 10 + digit;
                }

                return sign * value;
            }
        }
        return invalid;
    }

    /**
     * Yes, it turns out that {@link Integer#parseInt(String)} is a major hotspot due to the fact that it throws a
     * {@link NumberFormatException} instead of returning a signal value.
     *
     * @return A natural number for the string or -1 if the string is not a natural number
     */
    public static long parseFastNaturalNumber(String string)
    {
        if (string != null)
        {
            var length = string.length();
            if (length > 0)
            {
                long value = 0;
                for (var i = 0; i < length; i++)
                {
                    var digit = string.charAt(i) - '0';
                    if (digit < 0 || digit > 9)
                    {
                        return -1;
                    }
                    value = value * 10 + digit;
                }
                return value;
            }
        }
        return INVALID_LONG;
    }
}
