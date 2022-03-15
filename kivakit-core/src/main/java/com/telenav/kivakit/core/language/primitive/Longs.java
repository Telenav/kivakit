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

import com.telenav.kivakit.core.lexakai.DiagramPrimitive;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Utility methods for <i>int</i> values.
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Parsing</b></p>
 *
 * <ul>
 *     <li>{@link #parseFast(String)} - Parses the given string, returning {@link #INVALID} instead of throwing an exception. This can make a significant performance improvement when dealing with dirty data, because exceptions are expensive</li>
 *     <li>{@link #parseFast(String, long)} - Parses the given string returning the given flag value if parsing fails</li>
 *     <li>{@link #parseFastNaturalNumber(String)} - Parses a natural number, returning {@link #INVALID} if parsing fails</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Words</b></p>
 *
 * <ul>
 *     <li>{@link #high(long)} - Returns the high 32 bit word of the given <i>long</i></li>
 *     <li>{@link #low(long)} - Returns the low 32 bit word of the given <i>int</i></li>
 *     <li>{@link #forHighLow(int, int)} - Returns the two 32 bit words combined into a <i>long</i></li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Ranges</b></p>
 *
 * <ul>
 *     <li>{@link #inRange(long, long, long)} - Returns true if the given value is within the given range, inclusive</li>
 *     <li>{@link #isBetweenInclusive(long, long, long)} - Returns true if the given value is in the given range, inclusive</li>
 *     <li>{@link #isBetweenExclusive(long, long, long)} - Returns true if the given value is in the given range, exclusive</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>String Conversion</b></p>
 *
 * <ul>
 *     <li>{@link #commaSeparated(long)} - Converts the given value to a comma-separated string</li>
 *     <li>{@link #toHex(long)} - Converts the given <i>int</i> to a hexadecimal string</li>
 *     <li>{@link #toHex(long, long)} - Converts the given value to a hexadecimal string of the given length</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramPrimitive.class)
@LexakaiJavadoc(complete = true)
public class Longs
{
    /** Default invalid value */
    public static final long INVALID = Long.MIN_VALUE;

    /**
     * Returns the given value formatted with comma separators for the US locale.
     */
    public static String commaSeparated(long value)
    {
        return NumberFormat.getNumberInstance(Locale.US).format(value);
    }

    public static long forHighLow(int high, int low)
    {
        return (((long) high) << 32) | (low & 0xffff_ffffL);
    }

    public static int high(long value)
    {
        return (int) (value >> 32);
    }

    public static long inRange(long value, long min, long max)
    {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Returns true if the given value is in the given range, exclusive
     */
    public static boolean isBetweenExclusive(long value, long low, long high)
    {
        return value > low && value < high;
    }

    /**
     * Returns true if the given value is in the given range, inclusive
     */
    public static boolean isBetweenInclusive(long value, long low, long high)
    {
        return value >= low && value <= high;
    }

    public static int low(long value)
    {
        return (int) value;
    }

    public static long parseFast(String string)
    {
        return parseFast(string, INVALID);
    }

    /**
     * Yes, it turns out that {@link Long#parseLong(String)} is a major hotspot due to the fact that it throws a {@link
     * NumberFormatException} instead of returning a signal value.
     *
     * @return An integer value or the specified invalid value if the string is not a valid integer
     */
    public static long parseFast(String string, long invalid)
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
        return INVALID;
    }

    /**
     * @param value The value to search
     * @param bits The word length
     * @param searchFor The value to locate
     * @return True if the value contains the search term in the words
     */
    public static boolean searchWords(long value, int bits, int searchFor)
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
    public static String toHex(long value)
    {
        return Long.toHexString(value);
    }

    /**
     * Converts the given value to a hexadecimal string usable as a web color.
     */
    public static String toHex(long value, long length)
    {
        return String.format("%0" + length + "x", value);
    }
}
