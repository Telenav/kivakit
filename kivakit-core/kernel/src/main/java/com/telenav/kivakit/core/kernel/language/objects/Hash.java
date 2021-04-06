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

package com.telenav.kivakit.core.kernel.language.objects;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageObject;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Hashing utility methods
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageObject.class)
public class Hash
{
    /**
     * A large prime number suitable for seeding hashCode() implementations
     */
    public static final int SEED = 536870909;

    public static final long KNUTH_SEED = 2654435761L;

    public static int code(final byte[] a)
    {
        return code(a, 0, a.length);
    }

    public static int code(final byte[] a, final int start, final int end)
    {
        if (a == null)
        {
            return 0;
        }

        var result = 1;
        for (var i = start; i < end; i++)
        {
            final var element = a[i];
            result = 31 * result + Byte.hashCode(element);
        }

        return result;
    }

    public static int code(final char[] a, final int start, final int end)
    {
        if (a == null)
        {
            return 0;
        }

        var result = 1;
        for (var i = start; i < end; i++)
        {
            final var element = a[i];
            result = 31 * result + Character.hashCode(element);
        }

        return result;
    }

    public static int code(final int[] a)
    {
        return code(a, 0, a.length);
    }

    public static int code(final int[] a, final int start, final int end)
    {
        if (a == null)
        {
            return 0;
        }

        var result = 1;
        for (var i = start; i < end; i++)
        {
            final var element = a[i];
            result = 31 * result + Integer.hashCode(element);
        }

        return result;
    }

    public static int code(final Object object)
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

    public static int code(final Iterator<Object> objects)
    {
        var hashCode = 1;
        while (objects.hasNext())
        {
            final var object = objects.next();
            if (object != null)
            {
                hashCode = hashCode * SEED + code(object);
            }
        }
        return hashCode;
    }

    public static int code(final long[] a)
    {
        return code(a, 0, a.length);
    }

    public static int code(final long value)
    {
        return (int) (value ^ (value >>> 32));
    }

    public static int code(final long[] a, final int start, final int end)
    {
        if (a == null)
        {
            return 0;
        }

        var result = 1;
        for (var i = start; i < end; i++)
        {
            final var element = a[i];
            result = 31 * result + Long.hashCode(element);
        }

        return result;
    }

    public static int knuth(final int value)
    {
        return (int) Math.abs(value * KNUTH_SEED);
    }

    public static int knuth(final long value)
    {
        return Math.abs(knuth((int) value) ^ knuth((int) (value >>> 32)));
    }

    /**
     * Warning...
     */
    public static int many(final Object... objects)
    {
        return Arrays.hashCode(objects);
    }
}
