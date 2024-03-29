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

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.string.Paths.pathOptionalHead;
import static com.telenav.kivakit.core.string.Paths.pathOptionalSuffix;

/**
 * Class utility methods
 *
 * <p><b>Classes</b></p>
 *
 * <ul>
 *     <li>{@link #loadClass(ClassLoader, String)}</li>
 *     <li>{@link #classForName(String)}</li>
 *     <li>{@link #constructor(Class, Class[])}</li>
 *     <li>{@link #newInstance(Class, Object...)}</li>
 *     <li>{@link #simpleName(Class)}</li>
 * </ul>>
 *
 * <p><b>Resources</b></p>
 *
 * <ul>
 *      <li>{@link #openResource(Class, String)}</li>
 *      <li>{@link #resourceUri(Class, String)}</li>
 *      <li>{@link #resourceUrl(Class, String)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "unchecked", "unused" })
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class Classes
{
    /**
     * Returns true if the given class can be assigned to the given class, taking into account primitive types
     *
     * @param from The class to assign from
     * @param to The class to assign to
     */
    @SuppressWarnings("DuplicatedCode")
    public static boolean canAssign(Class<?> from, Class<?> to)
    {
        if (to.isAssignableFrom(from))
        {
            return true;
        }
        if (to.isPrimitive())
        {
            if (to == Integer.TYPE && from == Integer.class)
            {
                return true;
            }
            if (to == Long.TYPE && from == Long.class)
            {
                return true;
            }
            if (to == Character.TYPE && from == Character.class)
            {
                return true;
            }
            if (to == Boolean.TYPE && from == Boolean.class)
            {
                return true;
            }
            if (to == Short.TYPE && from == Short.class)
            {
                return true;
            }
            if (to == Byte.TYPE && from == Byte.class)
            {
                return true;
            }
            if (to == Double.TYPE && from == Double.class)
            {
                return true;
            }
            if (to == Float.TYPE && from == Float.class)
            {
                return true;
            }
        }
        if (from.isPrimitive())
        {
            if (to == Integer.class && from == Integer.TYPE)
            {
                return true;
            }
            if (to == Long.class && from == Long.TYPE)
            {
                return true;
            }
            if (to == Character.class && from == Character.TYPE)
            {
                return true;
            }
            if (to == Boolean.class && from == Boolean.TYPE)
            {
                return true;
            }
            if (to == Short.class && from == Short.TYPE)
            {
                return true;
            }
            if (to == Byte.class && from == Byte.TYPE)
            {
                return true;
            }
            return to == Float.class && from == Float.TYPE;
        }
        return false;
    }

    /**
     * Gets the named class by loading it.
     */
    public static <T> Class<T> classForName(String name)
    {
        try
        {
            return (Class<T>) Class.forName(name);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Gets the constructor for the given type with the given arguments
     */
    public static <T> Constructor<T> constructor(Class<T> type, Class<?>... arguments)
    {
        try
        {
            return type.getConstructor(arguments);
        }
        catch (Exception e)
        {
            throw new IllegalStateException("Unable to create instance: " + type, e);
        }
    }

    /**
     * Gets the named class by loading it.
     */
    public static <T> Class<T> loadClass(ClassLoader loader, String name)
    {
        try
        {
            return (Class<T>) loader.loadClass(name);
        }
        catch (ClassNotFoundException e)
        {
            return null;
        }
    }

    /**
     * Constructs an object from a type and a list of arguments. The list of arguments must alternate between type and
     * value. For example:
     *
     * <pre>
     * Listener listener = [...]
     * var converter = newInstance(DurationConverter.class, Listener.class, listener);
     * </pre>
     *
     * @param type The type to instantiate
     * @param arguments The arguments, alternating between argument type and argument value
     * @return The created object
     */
    public static <T> T newInstance(Class<T> type, Object... arguments)
    {
        try
        {
            ensure(arguments.length % 2 == 0);

            int count = arguments.length / 2;

            var types = new Class<?>[count];
            var values = new Object[count];

            for (var index = 0; index < count; index++)
            {
                var argumentIndex = index * 2;

                ensure(arguments[argumentIndex] instanceof Class);

                types[index] = (Class<?>) arguments[argumentIndex];
                values[index] = arguments[argumentIndex + 1];
            }

            var constructor = type.getConstructor(types);
            constructor.setAccessible(true);
            return constructor.newInstance(values);
        }
        catch (Exception e)
        {
            throw new IllegalStateException("Unable to instantiate: " + type);
        }
    }

    /**
     * Returns an open input stream to the resource at the given path relative to the base class
     */
    public static InputStream openResource(Class<?> base, String path)
    {
        var in = base.getResourceAsStream(path);
        if (in == null)
        {
            var loader = base.getClassLoader();
            if (loader != null)
            {
                in = loader.getResourceAsStream(path);
            }
        }
        if (in == null)
        {
            in = ClassLoader.getSystemClassLoader().getResourceAsStream(path);
        }
        if (in == null)
        {
            in = Classes.class.getResourceAsStream(path);
        }
        if (in == null)
        {
            throw new IllegalStateException("Unable to open resource: " + base.getSimpleName() + ":" + path);
        }
        return in;
    }

    /**
     * Returns The URI to the resource at the given path relative to the base class
     */
    public static URI resourceUri(Class<?> base, String path)
    {
        try
        {
            return resourceUrl(base, path).toURI();
        }
        catch (URISyntaxException e)
        {
            throw new IllegalStateException("Unable to get URI: " + base + ":" + path);
        }
    }

    /**
     * Returns The URL to the resource at the given path relative to the base class
     */
    public static URL resourceUrl(Class<?> base, String path)
    {
        var resource = base.getResource(path);
        if (resource == null)
        {
            resource = base.getClassLoader().getResource(path);
        }
        if (resource == null)
        {
            resource = ClassLoader.getSystemClassLoader().getResource(path);
        }
        if (resource == null)
        {
            throw new IllegalStateException("Unable to find resource: " + base + ":" + path);
        }
        return resource;
    }

    /**
     * Returns the simple name for the given type
     */
    public static String simpleName(Class<?> type)
    {
        if (type != null)
        {
            if (type.isArray() || type.isPrimitive())
            {
                return type.getSimpleName();
            }
            return pathOptionalSuffix(type.getName(), '.').replace('$', '.');
        }
        return "Unknown";
    }

    /**
     * Returns the simple name of the top-level class (without any anonymous / nested classes)
     */
    public static String simpleTopLevelClass(Class<?> type)
    {
        var name = pathOptionalSuffix(type.getName(), '.');
        if (name.contains("$"))
        {
            return pathOptionalHead(name, '$');
        }
        return name;
    }
}
