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

import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.project.lexakai.DiagramObject;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Arrays;

@UmlClassDiagram(diagram = DiagramObject.class)
public class Objects
{
    public static boolean equal(Object a, Object b)
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

    @SuppressWarnings("StringEquality")
    public static boolean equalIgnoringCase(String a, String b)
    {
        if (a == b)
        {
            return true;
        }
        if (a != null && b != null)
        {
            return a.equalsIgnoreCase(b);
        }
        return false;
    }

    public static boolean equalPairs(Object... objects)
    {
        if (objects.length % 2 != 0)
        {
            Ensure.fail("Must supply an even number of objects");
            return false;
        }
        for (var i = 0; i < objects.length; i += 2)
        {
            if (!equal(objects[i], objects[i + 1]))
            {
                return false;
            }
        }
        return true;
    }

    public static boolean isAnyNull(Object... objects)
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

    public static boolean isNotNull(Object object)
    {
        return object != null;
    }

    public static boolean isNull(Object object)
    {
        return object == null;
    }

    public static <Value> Value nonNullOr(Value value, Value defaultValue)
    {
        return (value != null) ? value : defaultValue;
    }
}
