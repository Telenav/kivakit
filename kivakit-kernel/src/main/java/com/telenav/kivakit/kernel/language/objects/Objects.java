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

package com.telenav.kivakit.kernel.language.objects;

import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.kernel.messaging.Debug;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageObject;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

@UmlClassDiagram(diagram = DiagramLanguageObject.class)
public class Objects
{
    private static Debug DEBUG;

    private static final Map<Class<?>, ClassIdentityMap> classToIdentityMap = new HashMap<>();

    public static boolean equal(final Object a, final Object b)
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
    public static boolean equalIgnoringCase(final String a, final String b)
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

    public static boolean equalPairs(final Object... objects)
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

    /**
     * Helps track object identities by maintaining a reference identity map.
     * <p>
     * IMPORTANT NOTE: Since this method holds references to the objects it identifiers, it prevents garbage collection
     * of those objects, which can cause big memory leaks, but only when {@link Debug} is enabled for this class with
     * -DKIVAKIT_DEBUG=Objects or another similar pattern. Be sure you don't have {@link Debug} enabled for this class
     * when looking at memory leaks.
     *
     * @return A string representing the identity of the object. The ≡ symbol in math means "identity". Each class has a
     * separate sequence of objects that have been allocated, starting at identity 1. For example, StringList ≡1,
     * ObjectList ≡1, StringList ≡2, etc... This makes is easier to track objects when debug tracing.
     */
    public static synchronized String identityOf(final Object object)
    {
        // This can only be used under debug mode because the identity map will keep every object in
        // it from being garbage collected, requiring large amounts of RAM.
        if (DEBUG().isDebugOn())
        {
            if (!object.getClass().isPrimitive() && !object.getClass().isArray())
            {
                return "≡" + classToIdentityMap.computeIfAbsent(object.getClass(), (key) -> new ClassIdentityMap())
                        .identityOf(object);
            }
        }

        return "";
    }

    public static boolean isAnyNull(final Object... objects)
    {
        for (final var object : objects)
        {
            if (object == null)
            {
                return true;
            }
        }
        return false;
    }

    public static boolean isPrimitiveWrapper(final Object object)
    {
        return object instanceof Long || object instanceof Integer || object instanceof Short
                || object instanceof Character || object instanceof Byte || object instanceof Boolean
                || object instanceof Double || object instanceof Float;
    }

    /**
     * @return The rough size of the primitive object or array in bytes
     */
    public static Bytes primitiveSize(final Object object)
    {
        if (object == null)
        {
            return Bytes.bytes(8);
        }
        if (object.getClass().isArray())
        {
            final var elementType = object.getClass().getComponentType();
            final long length = Array.getLength(object);
            if (elementType.isPrimitive())
            {
                if (elementType.equals(Byte.TYPE))
                {
                    return Bytes.bytes(length);
                }
                if (elementType.equals(Short.TYPE))
                {
                    return Bytes.bytes(length * (Short.SIZE / 8));
                }
                if (elementType.equals(Character.TYPE))
                {
                    return Bytes.bytes(length * (Character.SIZE / 8));
                }
                if (elementType.equals(Integer.TYPE))
                {
                    return Bytes.bytes(length * (Integer.SIZE / 8));
                }
                if (elementType.equals(Long.TYPE))
                {
                    return Bytes.bytes(length * (Long.SIZE / 8));
                }
                if (elementType.equals(Float.TYPE))
                {
                    return Bytes.bytes(length * (Float.SIZE / 8));
                }
                if (elementType.equals(Double.TYPE))
                {
                    return Bytes.bytes(length * (Double.SIZE / 8));
                }
            }
        }
        return null;
    }

    private static class ClassIdentityMap
    {
        /**
         * An easy to understand identifier for an object (used during debugging only)
         */
        private final Map<Object, Integer> identityForObject = new IdentityHashMap<>();

        /**
         * The next identity to use in the identityForObject map
         */
        private int nextIdentity = 1;

        public int identityOf(final Object object)
        {
            return identityForObject.computeIfAbsent(object, (o) -> nextIdentity++);
        }
    }

    private static Debug DEBUG()
    {
        if (DEBUG == null)
        {
            DEBUG = new Debug(Listener.none());
        }
        return DEBUG;
    }
}
