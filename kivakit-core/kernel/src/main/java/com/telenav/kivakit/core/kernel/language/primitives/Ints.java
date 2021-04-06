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

package com.telenav.kivakit.core.kernel.language.primitives;

import com.telenav.kivakit.core.kernel.language.strings.Align;
import com.telenav.kivakit.core.kernel.language.values.count.BitCount;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguagePrimitive;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramLanguagePrimitive.class)
public class Ints
{
    private static final int[] POWERS_OF_10 = { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000,
            1000000000 };

    /** Default invalid value */
    public static final int INVALID = Integer.MIN_VALUE;

    public static BitCount bitsToRepresent(final int value)
    {
        return BitCount.bitCount(Integer.SIZE - Integer.numberOfLeadingZeros(value));
    }

    public static int digits(final int value)
    {
        return (int) (Math.log10(value) + 1);
    }

    public static int forHighLow(final int high, final int low)
    {
        return high << 16 | low;
    }

    public static int high(final int value)
    {
        return value >> 16;
    }

    public static int inRange(final int value, final int min, final int max)
    {
        return Math.min(Math.max(value, min), max);
    }

    public static boolean isBetween(final int value, final int low, final int high)
    {
        return value >= low && value <= high;
    }

    public static boolean isPrime(final long n)
    {
        if (n % 2 == 1)
        {
            for (var a = 3L; a * a <= n; a += 2)
            {
                if (n % a == 0)
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static int low(final int value)
    {
        return value & 0xffff;
    }

    /**
     * @return An integer value or {@link #INVALID} if the string is not a valid integer
     */
    public static int parse(final String string)
    {
        return parse(string, INVALID);
    }

    /**
     * Yes, it turns out that {@link Integer#parseInt(String)} is a major hotspot due to the fact that it throws a
     * {@link NumberFormatException} instead of returning a signal value.
     *
     * @return An integer value or the specified invalid value if the string is not a valid integer
     */
    public static int parse(final String string, final int invalidValue)
    {
        return (int) Longs.parse(string, invalidValue);
    }

    /**
     * Yes, it turns out that {@link Integer#parseInt(String)} is a major hotspot due to the fact that it throws a
     * {@link NumberFormatException} instead of returning a signal value.
     *
     * @return A natural number for the string or -1 if the string is not a natural number
     */
    public static int parseNaturalNumber(final String string)
    {
        final var number = Longs.parseNaturalNumber(string);
        return number < 0 ? INVALID : (int) number;
    }

    /**
     * @return 10^power for values of power up to 32
     */
    public static int powerOfTen(final int power)
    {
        assert power >= 0;
        return POWERS_OF_10[power];
    }

    public static int rounded(final double value)
    {
        return (int) (value + 0.5);
    }

    public static int signExtend(final int value, final int bits)
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
    public static String toHex(final int value)
    {
        return Integer.toHexString(value);
    }

    /**
     * Converts the given value to a hexadecimal string usable as a web color.
     */
    public static String toHex(final int value, final int minimumLength)
    {
        return Align.right(toHex(value), minimumLength, '0');
    }
}
