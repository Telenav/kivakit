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

package com.telenav.kivakit.kernel.language.values.count;

import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.interfaces.io.ByteSized;
import com.telenav.kivakit.kernel.language.primitives.Doubles;
import com.telenav.kivakit.kernel.language.strings.Strings;
import com.telenav.kivakit.kernel.language.values.level.Percent;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

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
@UmlClassDiagram(diagram = DiagramLanguageValue.class)
public final class Bytes extends Count implements ByteSized
{
    /** No bytes */
    public static final Bytes _0 = bytes(0);

    /** Maximum bytes value */
    public static final Bytes MAXIMUM = bytes(Long.MAX_VALUE);

    private static final Logger LOGGER = LoggerFactory.newLogger();

    public static Bytes bytes(double bytes)
    {
        return new Bytes((long) bytes);
    }

    public static Bytes bytes(long bytes)
    {
        return new Bytes(bytes);
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

    public static Bytes parse(String bytes)
    {
        return parse(LOGGER, bytes);
    }

    public static Bytes parse(Listener listener, String bytes)
    {
        return new Converter(listener).convert(bytes);
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

    /**
     * Converts to and from {@link Bytes}. Valid suffixes are (case-insensitive):
     *
     * <ul>
     *     <li>b - bytes</li>
     *     <li>k - kilobytes</li>
     *     <li>m - megabytes</li>
     *     <li>g - gigabytes</li>
     *     <li>t - terabytes</li>
     * </ul>
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public static class Converter extends BaseStringConverter<Bytes>
    {
        /** Pattern for string parsing. */
        private static final Pattern PATTERN = Pattern.compile("([0-9]+([.,][0-9]+)?)\\s*(|K|M|G|T)B?",
                Pattern.CASE_INSENSITIVE);

        public Converter(Listener listener)
        {
            super(listener);
        }

        @Override
        protected Bytes onToValue(String value)
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
                    return bytes(scalar);
                }
                else if ("K".equalsIgnoreCase(units))
                {
                    return kilobytes(scalar);
                }
                else if ("M".equalsIgnoreCase(units))
                {
                    return megabytes(scalar);
                }
                else if ("G".equalsIgnoreCase(units))
                {
                    return gigabytes(scalar);
                }
                else if ("T".equalsIgnoreCase(units))
                {
                    return terabytes(scalar);
                }
                else
                {
                    problem("Unrecognized units: ${debug}", value);
                    return null;
                }
            }
            else
            {
                problem("Unable to parse: ${debug}", value);
                return null;
            }
        }
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

    public Bytes add(Bytes that)
    {
        return bytes(asBytes() + that.asBytes());
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
    public Bytes decremented()
    {
        return (Bytes) super.decremented();
    }

    @Override
    public Bytes dividedBy(Count divisor)
    {
        return (Bytes) super.dividedBy(divisor);
    }

    @Override
    public Bytes dividedBy(long divisor)
    {
        return (Bytes) super.dividedBy(divisor);
    }

    @Override
    public Bytes incremented()
    {
        return (Bytes) super.incremented();
    }

    /**
     * Compares this <code>Bytes</code> with another <code>Bytes</code> instance.
     *
     * @param that the <code>Bytes</code> instance to compare with
     * @return <code>true</code> if this <code>Bytes</code> is greater than the given
     * <code>Bytes</code> instance
     */
    public boolean isGreaterThan(Bytes that)
    {
        if (this == that || that == null)
        {
            return false;
        }
        return asBytes() > that.asBytes();
    }

    /**
     * Compares this <code>Bytes</code> with another <code>Bytes</code> instance.
     *
     * @param that the <code>Bytes</code> instance to compare with
     * @return <code>true</code> if this <code>Bytes</code> is greater than the given
     * <code>Bytes</code> instance
     */
    public boolean isGreaterThanOrEqualTo(Bytes that)
    {
        if (that == null)
        {
            return false;
        }
        return asBytes() >= that.asBytes();
    }

    /**
     * Compares this <code>Bytes</code> with another <code>Bytes</code> instance.
     *
     * @param that the <code>Bytes</code> instance to compare with
     * @return <code>true</code> if this <code>Bytes</code> is greater than the given
     * <code>Bytes</code> instance
     */
    public boolean isLessThan(Bytes that)
    {
        if (this == that || that == null)
        {
            return false;
        }
        return asBytes() < that.asBytes();
    }

    public boolean isLessThanOrEqualTo(Bytes that)
    {
        if (that == null)
        {
            return false;
        }
        return asBytes() <= that.asBytes();
    }

    @Override
    public boolean isZero()
    {
        return equals(_0);
    }

    @Override
    public Bytes maximum(Count that)
    {
        return (Bytes) super.maximum(that);
    }

    public Bytes maximum(Bytes that)
    {
        return isGreaterThan(that) ? this : that;
    }

    @Override
    public Bytes minimum(Count that)
    {
        return (Bytes) super.minimum(that);
    }

    public Bytes minimum(Bytes that)
    {
        return isLessThan(that) ? this : that;
    }

    @Override
    public Bytes minus(Count count)
    {
        return (Bytes) super.minus(count);
    }

    @Override
    public Bytes minus(long count)
    {
        return (Bytes) super.minus(count);
    }

    public Bytes minus(Bytes that)
    {
        return bytes(asBytes() - that.asBytes());
    }

    @Override
    public Bytes minusOne()
    {
        return (Bytes) super.minusOne();
    }

    @Override
    public Bytes percent(Percent percentage)
    {
        return bytes(asBytes() * percentage.asUnitValue());
    }

    public Percent percentOf(Bytes that)
    {
        return Percent.of((double) asBytes() / that.asBytes() * 100.0);
    }

    @Override
    public Bytes plus(long count)
    {
        return bytes(asBytes() + count);
    }

    @Override
    public Bytes plus(Count count)
    {
        return (Bytes) super.plus(count);
    }

    @Override
    public Bytes plusOne()
    {
        return (Bytes) super.plusOne();
    }

    @Override
    public Bytes roundUpToPowerOfTwo()
    {
        return (Bytes) super.roundUpToPowerOfTwo();
    }

    @Override
    public Bytes sizeInBytes()
    {
        return this;
    }

    @Override
    public Bytes times(Count count)
    {
        return times(count.get());
    }

    @Override
    public Bytes times(double value)
    {
        return bytes(asBytes() * value);
    }

    @Override
    public Bytes times(long bytes)
    {
        var product = get() * bytes;
        if (product < 0)
        {
            return MAXIMUM;
        }
        return bytes(product);
    }

    @Override
    public Bytes times(Percent percentage)
    {
        return (Bytes) super.times(percentage);
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
