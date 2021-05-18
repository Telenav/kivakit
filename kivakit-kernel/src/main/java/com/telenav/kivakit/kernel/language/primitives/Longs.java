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

package com.telenav.kivakit.kernel.language.primitives;

import com.telenav.kivakit.kernel.language.strings.Strip;
import com.telenav.kivakit.kernel.language.values.count.BitCount;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguagePrimitive;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Utility methods for <i>long</i> values
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguagePrimitive.class)
@LexakaiJavadoc(complete = true)
public class Longs
{
    /** Default invalid value */
    public static final long INVALID = Long.MIN_VALUE;

    public static BitCount bitsToRepresent(final long value)
    {
        return BitCount.bitCount(Long.SIZE - Long.numberOfLeadingZeros(value)).maximum(BitCount._1);
    }

    public static long forHighLow(final int high, final int low)
    {
        return (((long) high) << 32) | (low & 0xffff_ffffL);
    }

    public static int high(final long value)
    {
        return (int) (value >> 32);
    }

    public static long inRange(final long value, final long min, final long max)
    {
        return Math.min(Math.max(value, min), max);
    }

    public static int low(final long value)
    {
        return (int) value;
    }

    public static long parse(final String string)
    {
        return parse(string, INVALID);
    }

    /**
     * Yes, it turns out that {@link Long#parseLong(String)} is a major hotspot due to the fact that it throws a {@link
     * NumberFormatException} instead of returning a signal value.
     *
     * @return An integer value or the specified invalid value if the string is not a valid integer
     */
    public static long parse(final String string, final long invalid)
    {
        if (string != null)
        {
            final var length = string.length();
            if (length != 0)
            {
                var i = 0;
                var sign = 1;

                final var first = string.charAt(0);
                if (first == '-')
                {
                    i++;
                    sign = -1;
                }

                long value = 0;
                while (i < length)
                {
                    final var character = string.charAt(i++);
                    if (character == ',')
                    {
                        continue;
                    }
                    final var digit = character - '0';
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
     * @return The given hexadecimal value in text as a long
     */
    public static long parseHex(final String text)
    {
        return Long.parseLong(Strip.leading(text, "0x"), 16);
    }

    /**
     * Yes, it turns out that {@link Integer#parseInt(String)} is a major hotspot due to the fact that it throws a
     * {@link NumberFormatException} instead of returning a signal value.
     *
     * @return A natural number for the string or -1 if the string is not a natural number
     */
    public static long parseNaturalNumber(final String string)
    {
        if (string != null)
        {
            final var length = string.length();
            if (length > 0)
            {
                long value = 0;
                for (var i = 0; i < length; i++)
                {
                    final var digit = string.charAt(i) - '0';
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
    public static boolean searchWords(long value, final int bits, final int searchFor)
    {
        for (var remaining = 64; remaining > 0; remaining -= bits)
        {
            final var word = (int) (value & 0xffffL);
            if (word == searchFor)
            {
                return true;
            }
            value >>>= bits;
        }
        return false;
    }
}
