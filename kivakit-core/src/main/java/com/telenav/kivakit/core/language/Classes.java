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

import com.telenav.kivakit.core.string.Paths;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;

/**
 * Class utility methods
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unchecked")
public class Classes
{
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

    public static <T> Class<T> forName(ClassLoader loader, String name)
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

    public static <T> Class<T> forName(String name)
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

    public static String simpleName(Class<?> type)
    {
        if (type != null)
        {
            if (type.isArray() || type.isPrimitive())
            {
                return type.getSimpleName();
            }
            return Paths.optionalSuffix(type.getName(), '.').replace('$', '.');
        }
        return "Unknown";
    }

    public static String simpleTopLevelClass(Class<?> type)
    {
        var name = Paths.optionalSuffix(type.getName(), '.');
        if (name.contains("$"))
        {
            return Paths.optionalHead(name, '$');
        }
        return name;
    }
}
