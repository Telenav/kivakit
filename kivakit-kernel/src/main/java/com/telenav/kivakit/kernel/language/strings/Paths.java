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

package com.telenav.kivakit.kernel.language.strings;

import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageString;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageString.class)
public class Paths
{
    /**
     * @return The first element in the given path up to the separator or null if no separator is found
     */
    public static String head(final String path, final char separator)
    {
        if (path != null)
        {
            final var index = path.indexOf(separator);
            if (index >= 0)
            {
                return path.substring(0, index);
            }
            return path;
        }
        return null;
    }

    /**
     * @return The first element in the given path up to the separator or null if the separator is not found
     */
    public static String head(final String path, final String separator)
    {
        if (path != null)
        {
            final var index = path.indexOf(separator);
            if (index > 0 && index < path.length() - 1)
            {
                return path.substring(0, index);
            }
        }
        return null;
    }

    /**
     * @return The first element in the given path up to the separator or the path itself if the separator is not found
     */
    public static String optionalHead(final String path, final String separator)
    {
        return StringTo.string(head(path, separator), path);
    }

    /**
     * @return The first element in the given path up to the separator or the path itself if no separator is found
     */
    public static String optionalHead(final String path, final char separator)
    {
        return StringTo.string(head(path, separator), path);
    }

    /**
     * @return The last element in the given path up to the separator or the path itself if no separator is found
     */
    public static String optionalSuffix(final String path, final char separator)
    {
        if (path != null)
        {
            final var index = path.lastIndexOf(separator);
            return index == -1 ? path : path.substring(index + 1);
        }
        return null;
    }

    /**
     * @return All but the first element in the path or null if the separator does not occur
     */
    public static String tail(final String string, final String separator)
    {
        if (string != null)
        {
            final var index = string.indexOf(separator);
            if (index >= 0 && index < string.length() - 1)
            {
                return string.substring(index + separator.length());
            }
        }
        return null;
    }

    /**
     * @return All but the first element in the path or null if the separator does not occur
     */
    public static String tail(final String text, final char separator)
    {
        if (text != null)
        {
            final var index = text.indexOf(separator);
            if (index >= 0)
            {
                return text.substring(index + 1);
            }
        }
        return null;
    }

    public static String toPackage(final String path)
    {
        return path.replaceAll("/", ".");
    }

    /**
     * @return The last element in the given path up to the separator or the path itself if no separator is found
     */
    public static String withoutOptionalSuffix(final String path, final char separator)
    {
        return StringTo.string(withoutSuffix(path, separator), path);
    }

    /**
     * @return The last element in the given path up to the separator or null if no separator is found
     */
    public static String withoutSuffix(final String path, final char separator)
    {
        if (path != null)
        {
            final var index = path.lastIndexOf(separator);
            if (index >= 0)
            {
                return path.substring(0, index);
            }
        }
        return null;
    }
}
