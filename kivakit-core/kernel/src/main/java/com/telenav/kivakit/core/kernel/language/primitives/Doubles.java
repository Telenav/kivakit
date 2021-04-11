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

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguagePrimitive;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Utility methods for working with <i>double</i> values
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguagePrimitive.class)
@LexakaiJavadoc(complete = true)
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
    public static double fastParse(final String value, final double denominator)
    {
        final var index = value.indexOf('.');
        if (index > 0)
        {
            final var invalid = Longs.INVALID;
            final var major = Longs.parse(value.substring(0, index), invalid);
            if (major != invalid)
            {
                final var minor = Longs.parse(value.substring(index + 1), invalid);
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
    public static String format(final double value)
    {
        return String.format("%.1f", value);
    }

    /**
     * @return The double value formatted with the given number of decimal places
     */
    public static String format(final double value, final int decimals)
    {
        return String.format("%." + decimals + "f", value);
    }

    public static double inRange(final double value, final double min, final double max)
    {
        if (value < min)
        {
            return min;
        }
        return Math.min(value, max);
    }

    public static boolean isBetween(final double value, final double min, final double max)
    {
        return value >= min && value <= max;
    }

    public static int rounded(final double value)
    {
        return (int) (value + 0.5);
    }
}
