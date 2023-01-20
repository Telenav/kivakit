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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramObject;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Arrays;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_INSUFFICIENT;
import static com.telenav.kivakit.core.ensure.Ensure.fail;

/**
 * Object convenience methods.
 *
 * <p><b>Equality</b></p>
 *
 * <ul>
 *     <li>{@link #isEqual(Object, Object)}</li>
 *     <li>{@link #areEqualPairs(Object...)}</li>
 * </ul>
 *
 * <p><b>Nullity</b></p>
 *
 * <ul>
 *     <li>{@link #areAnyNull(Object...)}</li>
 *     <li>{@link #isNotNull(Object)}</li>
 *     <li>{@link #isNull(Object)}</li>
 *     <li>{@link #nonNullOr(Object, Object)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramObject.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_INSUFFICIENT,
             documentation = DOCUMENTED)
public class Objects
{
    /**
     * Returns true if any of the given objects is null.
     */
    public static boolean areAnyNull(Object... objects)
    {
        for (var object : objects)
        {
            if (object == null)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the given variable number of values contains a series of equal pairs
     */
    public static boolean areEqualPairs(Object... values)
    {
        if (values.length % 2 != 0)
        {
            fail("Must supply an even number of objects");
            return false;
        }
        for (var i = 0; i < values.length; i += 2)
        {
            if (!isEqual(values[i], values[i + 1]))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if all the given objects are null
     */
    public static boolean areNull(Object... objects)
    {
        for (var object : objects)
        {
            if (object != null)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if the given objects are equal, taking into account primitive arrays.
     */
    public static boolean isEqual(Object a, Object b)
    {
        if (a == b)
        {
            return true;
        }
        if (a != null && b != null)
        {
            if (a instanceof boolean[] && b instanceof boolean[])
            {
                return Arrays.equals((boolean[]) a, (boolean[]) b);
            }
            if (a instanceof byte[] && b instanceof byte[])
            {
                return Arrays.equals((byte[]) a, (byte[]) b);
            }
            if (a instanceof char[] && b instanceof char[])
            {
                return Arrays.equals((char[]) a, (char[]) b);
            }
            if (a instanceof short[] && b instanceof short[])
            {
                return Arrays.equals((short[]) a, (short[]) b);
            }
            if (a instanceof int[] && b instanceof int[])
            {
                return Arrays.equals((int[]) a, (int[]) b);
            }
            if (a instanceof long[] && b instanceof long[])
            {
                return Arrays.equals((long[]) a, (long[]) b);
            }
            if (a instanceof float[] && b instanceof float[])
            {
                return Arrays.equals((float[]) a, (float[]) b);
            }
            if (a instanceof double[] && b instanceof double[])
            {
                return Arrays.equals((double[]) a, (double[]) b);
            }
            if (a instanceof Object[] && b instanceof Object[])
            {
                return Arrays.deepEquals((Object[]) a, (Object[]) b);
            }
            return a.equals(b);
        }
        return false;
    }

    /**
     * Returns true if the given object is not null
     */
    public static boolean isNotNull(Object object)
    {
        return object != null;
    }

    /**
     * Returns true if the given object is null
     */
    public static boolean isNull(Object object)
    {
        return object == null;
    }

    /**
     * Returns the value, or defaultValue if the value is null
     */
    public static <Value> Value nonNullOr(Value value, Value defaultValue)
    {
        return (value != null) ? value : defaultValue;
    }
}
