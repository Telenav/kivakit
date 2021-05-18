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

import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.language.strings.Paths;

import java.io.InputStream;
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
    public static <T> Class<T> forName(final ClassLoader loader, final String name)
    {
        try
        {
            return (Class<T>) loader.loadClass(name);
        }
        catch (final ClassNotFoundException e)
        {
            return null;
        }
    }

    public static <T> Class<T> forName(final String name)
    {
        try
        {
            return (Class<T>) Class.forName(name);
        }
        catch (final Exception e)
        {
            return null;
        }
    }

    public static boolean isPrimitive(final Class<?> type)
    {
        return Long.TYPE.equals(type) || Integer.TYPE.equals(type) || Short.TYPE.equals(type)
                || Character.TYPE.equals(type) || Byte.TYPE.equals(type) || Boolean.TYPE.equals(type)
                || Double.TYPE.equals(type) || Float.TYPE.equals(type);
    }

    public static InputStream openResource(final Class<?> base, final String path)
    {
        var in = base.getResourceAsStream(path);
        if (in == null)
        {
            final var loader = base.getClassLoader();
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
        return in;
    }

    public static URI resourceUri(final Class<?> base, final String path)
    {
        try
        {
            return resourceUrl(base, path).toURI();
        }
        catch (final URISyntaxException e)
        {
            return Ensure.fail(e, "Unable to get URI for $:$", base, path);
        }
    }

    public static URL resourceUrl(final Class<?> base, final String path)
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
        Ensure.ensure(resource != null);
        return resource;
    }

    public static String simpleName(final Class<?> type)
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

    public static String simpleTopLevelClass(final Class<?> type)
    {
        final var name = Paths.optionalSuffix(type.getName(), '.');
        if (name.contains("$"))
        {
            return Paths.optionalHead(name, '$');
        }
        return name;
    }
}
