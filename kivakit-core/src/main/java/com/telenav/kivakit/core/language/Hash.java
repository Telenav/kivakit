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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramObject;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Arrays;
import java.util.Iterator;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_STATIC_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Various convenience methods for computing different kinds of hash codes.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramObject.class)
@ApiQuality(stability = STABLE_STATIC_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class Hash
{
    /**
     * A large prime number suitable for seeding hashCode() implementations
     */
    public static final int SEED = 536870909;

    /** Seed for Knuth hashing */
    public static final long KNUTH_SEED = 2654435761L;

    public static int hashCode(byte[] a)
    {
        return hashCode(a, 0, a.length);
    }

    /**
     * Returns a hash code for the given range of the given array
     */
    public static int hashCode(byte[] a, int start, int end)
    {
        if (a == null)
        {
            return 0;
        }

        var result = 1;
        for (var i = start; i < end; i++)
        {
            var element = a[i];
            result = 31 * result + Byte.hashCode(element);
        }

        return result;
    }

    /**
     * Returns a hash code for the given range of the given array
     */
    public static int hashCode(char[] a, int start, int end)
    {
        if (a == null)
        {
            return 0;
        }

        var result = 1;
        for (var i = start; i < end; i++)
        {
            var element = a[i];
            result = 31 * result + Character.hashCode(element);
        }

        return result;
    }

    /**
     * Returns a hash code for the given int array
     */
    public static int hashCode(int[] a)
    {
        return hashCode(a, 0, a.length);
    }

    /**
     * Returns a hash code for the given range of the given array
     */
    public static int hashCode(int[] a, int start, int end)
    {
        if (a == null)
        {
            return 0;
        }

        var result = 1;
        for (var i = start; i < end; i++)
        {
            var element = a[i];
            result = 31 * result + Integer.hashCode(element);
        }

        return result;
    }

    /**
     * Returns a hash code for the given object, allowing support for array types
     */
    public static int hashCode(Object object)
    {
        if (object instanceof boolean[])
        {
            return Arrays.hashCode((boolean[]) object);
        }
        if (object instanceof byte[])
        {
            return Arrays.hashCode((byte[]) object);
        }
        if (object instanceof char[])
        {
            return Arrays.hashCode((char[]) object);
        }
        if (object instanceof short[])
        {
            return Arrays.hashCode((short[]) object);
        }
        if (object instanceof int[])
        {
            return Arrays.hashCode((int[]) object);
        }
        if (object instanceof long[])
        {
            return Arrays.hashCode((long[]) object);
        }
        if (object instanceof float[])
        {
            return Arrays.hashCode((float[]) object);
        }
        if (object instanceof double[])
        {
            return Arrays.hashCode((double[]) object);
        }
        if (object instanceof Object[])
        {
            return Arrays.hashCode((Object[]) object);
        }
        return object != null ? object.hashCode() : 0;
    }

    /**
     * Returns a hash code for the given iterator
     */
    public static int hashCode(Iterator<Object> objects)
    {
        var hashCode = 1;
        while (objects.hasNext())
        {
            var object = objects.next();
            if (object != null)
            {
                hashCode = hashCode * SEED + hashCode(object);
            }
        }
        return hashCode;
    }

    /**
     * Returns a hash code for the given long array
     */
    public static int hashCode(long[] a)
    {
        return hashCode(a, 0, a.length);
    }

    /**
     * Returns a hash code for the given long value
     */
    public static int hashCode(long value)
    {
        return (int) (value ^ (value >>> 32));
    }

    /**
     * Returns a hash code for the given range of the given array
     */
    public static int hashCode(long[] a, int start, int end)
    {
        if (a == null)
        {
            return 0;
        }

        var result = 1;
        for (var i = start; i < end; i++)
        {
            var element = a[i];
            result = 31 * result + Long.hashCode(element);
        }

        return result;
    }

    /**
     * Returns a hash code for the given variable number of objects
     */
    public static int hashMany(Object... objects)
    {
        return Arrays.hashCode(objects);
    }

    /**
     * Returns the identity hash of the given object
     */
    public static int identityHash(Object object)
    {
        return System.identityHashCode(object);
    }

    /**
     * Returns a Knuth hash for the given value
     */
    public static int knuthHash(int value)
    {
        return (int) Math.abs(value * KNUTH_SEED);
    }

    /**
     * Returns a Knuth hash for the given value
     */
    public static int knuthHash(long value)
    {
        return Math.abs(knuthHash((int) value) ^ knuthHash((int) (value >>> 32)));
    }
}
