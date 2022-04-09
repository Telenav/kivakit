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

import com.telenav.kivakit.core.lexakai.DiagramPrimitive;
import com.telenav.kivakit.interfaces.numeric.Quantizable;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * Utility methods for working with arrays.
 *
 * <ul>
 *     <li>{@link #reverse(int[])}</li>
 *     <li>{@link #reverse(long[])}</li>
 *     <li>{@link #reverseRange(long[], int, int)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused") @UmlClassDiagram(diagram = DiagramPrimitive.class)
@LexakaiJavadoc(complete = true)
public class Arrays
{
    /**
     * Converts the given byte array to a hex string
     */
    public static String asHexadecimalString(byte[] bytes)
    {
        var builder = new StringBuilder();
        for (var at : bytes)
        {
            builder.append(String.format("%02d", at & 0xff));
        }
        return builder.toString();
    }

    public static <T> T[] concatenate(T[] a, T[] b)
    {
        @SuppressWarnings("unchecked")
        T[] concatenated = (T[]) Array.newInstance(a.getClass().getComponentType(), a.length + b.length);
        System.arraycopy(a, 0, concatenated, 0, a.length);
        System.arraycopy(b, 0, concatenated, a.length, b.length);
        return concatenated;
    }

    public static <T> boolean contains(T[] array, T object)
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

    static int[] intArray(Collection<? extends Quantizable> values)
    {
        var array = new int[values.size()];
        var index = 0;
        for (Quantizable value : values)
        {
            array[index++] = (int) value.quantum();
        }
        return array;
    }

    static long[] longArray(Collection<? extends Quantizable> values)
    {
        var array = new long[values.size()];
        var index = 0;
        for (Quantizable value : values)
        {
            array[index++] = value.quantum();
        }
        return array;
    }

    /**
     * Reverses the elements in the given array
     */
    public static int[] reverse(int[] elements)
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
    public static long[] reverse(long[] elements)
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
    public static void reverseRange(long[] array, int fromIndex, int toIndex)
    {
        for (int i = fromIndex, j = toIndex - 1; i < j; i++, j--)
        {
            long temporary = array[i];
            array[i] = array[j];
            array[j] = temporary;
        }
    }
}
