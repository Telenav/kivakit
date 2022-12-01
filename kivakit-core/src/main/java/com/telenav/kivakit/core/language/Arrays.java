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

package com.telenav.kivakit.core.language;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramPrimitive;
import com.telenav.kivakit.interfaces.value.LongValued;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.reflect.Array;
import java.util.Collection;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static java.lang.String.format;
import static java.lang.System.arraycopy;

/**
 * Utility methods for working with arrays.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #intArray(Collection)}</li>
 *     <li>{@link #longArray(Collection)}</li>
 * </ul>
 *
 * <p><b>Ordering</b></p>
 *
 * <ul>
 *     <li>{@link #reverseArray(int[])}</li>
 *     <li>{@link #reverseArray(long[])}</li>
 *     <li>{@link #reverseArrayRange(long[], int, int)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused") @UmlClassDiagram(diagram = DiagramPrimitive.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class Arrays
{
    /**
     * Returns true if the given array contains the given object
     */
    public static <Value> boolean arrayContains(Value[] array, Value object)
    {
        for (var at : array)
        {
            if (at.equals(object))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Converts the given byte array to a hex string
     */
    public static String asHexadecimalString(byte[] bytes)
    {
        var builder = new StringBuilder();
        for (var at : bytes)
        {
            builder.append(format("%02d", at & 0xff));
        }
        return builder.toString();
    }

    /**
     * Returns the concatenation of the two given arrays, as an array.
     */
    public static <Value> Value[] concatenateArrays(Value[] a, Value[] b)
    {
        @SuppressWarnings("unchecked")
        Value[] concatenated = (Value[]) Array.newInstance(a.getClass().getComponentType(), a.length + b.length);
        arraycopy(a, 0, concatenated, 0, a.length);
        arraycopy(b, 0, concatenated, a.length, b.length);
        return concatenated;
    }

    /**
     * Creates an int[] for the given {@link LongValued} collections
     */
    static int[] intArray(Collection<? extends LongValued> values)
    {
        var array = new int[values.size()];
        var index = 0;
        for (var value : values)
        {
            array[index++] = (int) value.longValue();
        }
        return array;
    }

    /**
     * Creates a long[] for the given {@link LongValued} collections
     */
    static long[] longArray(Collection<? extends LongValued> values)
    {
        var array = new long[values.size()];
        var index = 0;
        for (var value : values)
        {
            array[index++] = value.longValue();
        }
        return array;
    }

    /**
     * Reverses the elements in the given array
     */
    public static void reverseArray(byte[] array)
    {
        reverseArray(array, 0, array.length);
    }

    /**
     * Reverses the region of elements in the given array
     */
    public static void reverseArray(byte[] array, int fromIndex, int toIndex)
    {
        for (int i = fromIndex, j = toIndex - 1; i < j; i++, j--)
        {
            byte temporary = array[i];
            array[i] = array[j];
            array[j] = temporary;
        }
    }

    /**
     * Reverses the elements in the given array
     */
    public static int[] reverseArray(int[] elements)
    {
        for (var i = 0; i < elements.length / 2; i++)
        {
            var temp = elements[i];
            elements[i] = elements[elements.length - i - 1];
            elements[elements.length - i - 1] = temp;
        }
        return elements;
    }

    /**
     * Reverses the elements in the given array
     */
    public static long[] reverseArray(long[] elements)
    {
        for (var i = 0; i < elements.length / 2; i++)
        {
            var opposite = elements.length - 1 - i;
            var temp = elements[i];
            elements[i] = elements[opposite];
            elements[opposite] = temp;
        }
        return elements;
    }

    /**
     * Reverses the values in the given range of indexes
     */
    public static void reverseArrayRange(long[] array, int fromIndex, int toIndex)
    {
        for (int i = fromIndex, j = toIndex - 1; i < j; i++, j--)
        {
            long temporary = array[i];
            array[i] = array[j];
            array[j] = temporary;
        }
    }
}
