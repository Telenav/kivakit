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
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_STATIC_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.FULLY_TESTED;

/**
 * Utility methods for working with <i>double</i> values
 *
 * <ul>
 *     <li>{@link #fastParse(String, double)} - Fast parsing given a fixed number of decimal places</li>
 *     <li>{@link #format(double)} - The value formatted with only one decimal place</li>
 *     <li>{@link #format(double, int)} - The value formatted with the given number of decimal places</li>
 *     <li>{@link #inRange(double, double, double)} - Returns the given value constrained to the given range</li>
 *     <li>{@link #isBetweenInclusive(double, double, double)} - True if the value is in the given range</li>
 *     <li>{@link #rounded(double)} - Returns the given value rounded to the nearest <i>int</i></li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramPrimitive.class)
@ApiQuality(stability = STABLE_STATIC_EXTENSIBLE,
            testing = FULLY_TESTED,
            documentation = FULLY_DOCUMENTED)
public class Doubles
{
    /** Default invalid value */
    public static final double INVALID = Double.MIN_VALUE;

    /**
     * A fast way to parse a simple double of the format xxx.yyyy where the number of digits in y is <i>fixed</i>. For
     * example:
     * <pre>
     * var result = fastParse("3.1415", 1_0000);
     * assert result == 3.1415;
     * </pre>
     *
     * @param value The value to parse
     * @param denominator The value to divide y by to get the digits to the right of the decimal place
     * @return The double value
     */
    public static double fastParse(String value, double denominator)
    {
        var index = value.indexOf('.');
        if (index > 0)
        {
            var invalid = Longs.INVALID;
            var major = Longs.parseFast(value.substring(0, index), invalid);
            if (major != invalid)
            {
                var minor = Longs.parseFast(value.substring(index + 1), invalid);
                if (minor != invalid)
                {
                    return major + (minor / denominator);
                }
            }
        }
        return INVALID;
    }

    /**
     * @return The double value formatted with only one decimal place
     */
    public static String format(double value)
    {
        return String.format("%.1f", value);
    }

    /**
     * @return The double value formatted with the given number of decimal places
     */
    public static String format(double value, int decimals)
    {
        return String.format("%." + decimals + "f", value);
    }

    /**
     * Returns the value constrained to the given range, inclusive.
     *
     * @param value The value
     * @param minimum The minimum value (inclusive)
     * @param maximum The maximum value (inclusive)
     * @return The value constrained to the given range
     */
    public static double inRange(double value, double minimum, double maximum)
    {
        if (value < minimum)
        {
            return minimum;
        }
        return Math.min(value, maximum);
    }

    /**
     * Returns true if the value is in the given range, inclusive.
     *
     * @param value The value
     * @param minimum The minimum value (inclusive)
     * @param maximum The maximum value (inclusive)
     * @return True if the value is between the minimum and maximum, inclusive.
     */
    public static boolean isBetweenInclusive(double value, double minimum, double maximum)
    {
        return value >= minimum && value <= maximum;
    }

    /**
     * Returns the given value rounded to the nearest <i>int</i>
     *
     * @param value The value to round
     * @return The rounded value
     */
    public static int rounded(double value)
    {
        return (int) (value + 0.5);
    }
}
