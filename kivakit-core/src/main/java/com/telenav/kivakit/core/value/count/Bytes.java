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

package com.telenav.kivakit.core.value.count;

import com.telenav.kivakit.core.language.primitive.Doubles;
import com.telenav.kivakit.core.lexakai.DiagramCount;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.reflect.Array;
import java.util.regex.Pattern;

/**
 * Represents an immutable byte count. These static factory methods allow easy construction of value objects using
 * either long values like bytes(2034) or megabytes(3):
 * <ul>
 * <li>Bytes.bytes(long)
 * <li>Bytes.kilobytes(long)
 * <li>Bytes.megabytes(long)
 * <li>Bytes.gigabytes(long)
 * <li>Bytes.terabytes(long)
 * </ul>
 * <p>
 * or double precision floating point values like megabytes(3.2):
 * <ul>
 * <li>Bytes.bytes(double)
 * <li>Bytes.kilobytes(double)
 * <li>Bytes.megabytes(double)
 * <li>Bytes.gigabytes(double)
 * <li>Bytes.terabytes(double)
 * </ul>
 * <p>
 * The precise number of bytes in a Bytes object can be retrieved by calling bytes(). Approximate
 * values for different units can be retrieved as double precision values using these methods:
 * <ul>
 * <li>kilobytes()
 * <li>megabytes()
 * <li>gigabytes()
 * <li>terabytes()
 * </ul>
 * Also, value objects can be constructed from strings, optionally using a Locale with
 * valueOf(String) and valueOf(String,Locale). The string may contain a decimal or floating point
 * number followed by optional whitespace followed by a unit (nothing for bytes, K for kilobyte, M
 * for megabytes, G for gigabytes or T for terabytes) optionally followed by a B (for bytes). Any of
 * these letters can be any case. So, examples of permissible string values are:
 * <ul>
 * <li>37 (37 bytes)
 * <li>2.3K (2.3 kilobytes)
 * <li>2.5 kb (2.5 kilobytes)
 * <li>4k (4 kilobytes)
 * <li>35.2GB (35.2 gigabytes)
 * <li>1024M (1024 megabytes)
 * </ul>
 * <p>
 * The toString() method is smart enough to convert a given value object to the most appropriate
 * units for the given value.
 *
 * @author Jonathan Locke
 */
@UmlClassDiagram(diagram = DiagramCount.class)
public final class Bytes extends BaseCount<Bytes> implements ByteSized
{
    /** Pattern for string parsing. */
    private static final Pattern PATTERN = Pattern.compile("([0-9]+([.,][0-9]+)?)\\s*(|K|M|G|T)B?",
            Pattern.CASE_INSENSITIVE);

    /** No bytes */
    public static final Bytes _0 = bytes(0);

    /** Maximum bytes value */
    public static final Bytes MAXIMUM = bytes(Long.MAX_VALUE);

    public static Bytes bytes(double bytes)
    {
        return new Bytes((long) bytes);
    }

    public static Bytes bytes(long bytes)
    {
        return new Bytes(bytes);
    }

    public static Bytes bytes(long[] array)
    {
        return new Bytes(array.length * 8L);
    }

    public static Bytes bytes(int[] array)
    {
        return new Bytes(array.length * 4L);
    }

    public static Bytes bytes(byte[] array)
    {
        return new Bytes(array.length);
    }

    public static Bytes bytes(Count count)
    {
        return bytes(count.get());
    }

    public static Bytes gigabytes(double gigabytes)
    {
        return megabytes(gigabytes * 1024.0);
    }

    public static Bytes gigabytes(long gigabytes)
    {
        return megabytes(gigabytes * 1024);
    }

    public static Bytes kilobytes(double kilobytes)
    {
        return bytes(kilobytes * 1024.0);
    }

    public static Bytes kilobytes(long kilobytes)
    {
        return bytes(kilobytes * 1024);
    }

    public static Bytes megabytes(double megabytes)
    {
        return kilobytes(megabytes * 1024.0);
    }

    public static Bytes megabytes(long megabytes)
    {
        return kilobytes(megabytes * 1024);
    }

    public static Bytes parseBytes(Listener listener, String value)
    {
        var matcher = PATTERN.matcher(value);

        // Valid input?
        if (matcher.matches())
        {
            // Get double precision value
            var scalar = Double.parseDouble(Strings.replaceAll(matcher.group(1), ",", ""));

            // Get units specified
            var units = matcher.group(3);

            if ("".equalsIgnoreCase(units))
            {
                return Bytes.bytes(scalar);
            }
            else if ("K".equalsIgnoreCase(units))
            {
                return Bytes.kilobytes(scalar);
            }
            else if ("M".equalsIgnoreCase(units))
            {
                return Bytes.megabytes(scalar);
            }
            else if ("G".equalsIgnoreCase(units))
            {
                return Bytes.gigabytes(scalar);
            }
            else if ("T".equalsIgnoreCase(units))
            {
                return Bytes.terabytes(scalar);
            }
            else
            {
                listener.problem("Unrecognized units: ${debug}", value);
                return null;
            }
        }
        else
        {
            listener.problem("Unable to parse: ${debug}", value);
            return null;
        }
    }

    /**
     * @return The rough size of the primitive object or array in bytes
     */
    public static Bytes primitiveSize(Object object)
    {
        if (object == null)
        {
            return bytes(8);
        }
        if (object.getClass().isArray())
        {
            var elementType = object.getClass().getComponentType();
            long length = Array.getLength(object);
            if (elementType.isPrimitive())
            {
                if (elementType.equals(Byte.TYPE))
                {
                    return bytes(length);
                }
                if (elementType.equals(Short.TYPE))
                {
                    return bytes(length * (Short.SIZE / 8));
                }
                if (elementType.equals(Character.TYPE))
                {
                    return bytes(length * (Character.SIZE / 8));
                }
                if (elementType.equals(Integer.TYPE))
                {
                    return bytes(length * (Integer.SIZE / 8));
                }
                if (elementType.equals(Long.TYPE))
                {
                    return bytes(length * (Long.SIZE / 8));
                }
                if (elementType.equals(Float.TYPE))
                {
                    return bytes(length * (Float.SIZE / 8));
                }
                if (elementType.equals(Double.TYPE))
                {
                    return bytes(length * (Double.SIZE / 8));
                }
            }
        }
        return null;
    }

    public static void reverse(byte[] array)
    {
        reverse(array, 0, array.length);
    }

    public static void reverse(byte[] array, int fromIndex, int toIndex)
    {
        for (int i = fromIndex, j = toIndex - 1; i < j; i++, j--)
        {
            byte temporary = array[i];
            array[i] = array[j];
            array[j] = temporary;
        }
    }

    public static Bytes terabytes(double terabytes)
    {
        return gigabytes(terabytes * 1024.0);
    }

    public static Bytes terabytes(long terabytes)
    {
        return gigabytes(terabytes * 1024);
    }

    private Bytes()
    {
    }

    /**
     * Private constructor forces use of static factory methods.
     *
     * @param bytes Number of bytes
     */
    private Bytes(long bytes)
    {
        super(bytes);
    }

    /**
     * Gets the byte count represented by this value object.
     *
     * @return Byte count
     */
    public long asBytes()
    {
        return super.get();
    }

    /**
     * Gets the byte count in gigabytes.
     *
     * @return The value in gigabytes
     */
    public double asGigabytes()
    {
        return asMegabytes() / 1024.0;
    }

    /**
     * Gets the byte count in kilobytes.
     *
     * @return The value in kilobytes
     */
    public double asKilobytes()
    {
        return asBytes() / 1024.0;
    }

    /**
     * Gets the byte count in megabytes.
     *
     * @return The value in megabytes
     */
    public double asMegabytes()
    {
        return asKilobytes() / 1024.0;
    }

    /**
     * Gets the byte count in terabytes.
     *
     * @return The value in terabytes
     */
    public double asTerabytes()
    {
        return asGigabytes() / 1024.0;
    }

    @Override
    public Bytes newInstance(long count)
    {
        return bytes(count);
    }

    @Override
    public Bytes sizeInBytes()
    {
        return this;
    }

    /**
     * Converts this byte count to a string
     *
     * @return The string for this byte count
     */
    @Override
    public String toString()
    {
        if (asGigabytes() >= 1000)
        {
            return unitString(asTerabytes(), "T");
        }
        if (asMegabytes() >= 1000)
        {
            return unitString(asGigabytes(), "G");
        }
        if (asKilobytes() >= 1000)
        {
            return unitString(asMegabytes(), "M");
        }
        if (asBytes() >= 1000)
        {
            return unitString(asKilobytes(), "K");
        }
        return asBytes() + " bytes";
    }

    /**
     * Convert value to formatted floating point number and units.
     *
     * @param value The value
     * @param units The units
     * @return The formatted string
     */
    private String unitString(double value, String units)
    {
        return Doubles.format(value, 1) + units;
    }
}
