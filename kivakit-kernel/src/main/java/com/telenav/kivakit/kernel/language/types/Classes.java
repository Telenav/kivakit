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

package com.telenav.kivakit.kernel.language.types;

import com.telenav.kivakit.kernel.language.strings.Paths;
import com.telenav.kivakit.kernel.messaging.Listener;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Class utility methods
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unchecked")
public class Classes
{
    public static <T> Constructor<T> constructor(Listener listener, Class<T> type, Class<?>... arguments)
    {
        try
        {
            return type.getConstructor(arguments);
        }
        catch (Exception e)
        {
            listener.problem(e, "Unable to create instance: $", type);
            return null;
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

    public static boolean isPrimitive(Class<?> type)
    {
        return Long.TYPE.equals(type) || Integer.TYPE.equals(type) || Short.TYPE.equals(type)
                || Character.TYPE.equals(type) || Byte.TYPE.equals(type) || Boolean.TYPE.equals(type)
                || Double.TYPE.equals(type) || Float.TYPE.equals(type);
    }

    public static InputStream openResource(Listener listener, Class<?> base, String path)
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
            listener.problem("Unable to open resource $:$", base.getSimpleName(), path);
        }
        return in;
    }

    public static URI resourceUri(Listener listener, Class<?> base, String path)
    {
        try
        {
            return resourceUrl(listener, base, path).toURI();
        }
        catch (URISyntaxException e)
        {
            throw listener.problem(e, "Unable to get URI for $:$", base, path).asException();
        }
    }

    public static URL resourceUrl(Listener listener, Class<?> base, String path)
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
            throw listener.problem("Unable to find resource: ${class}:$", base, path).asException();
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
