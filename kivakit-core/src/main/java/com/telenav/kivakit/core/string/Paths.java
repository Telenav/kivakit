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

package com.telenav.kivakit.core.string;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramString;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Utility methods for working with string paths. In general, prefer {@link StringPath} and subtypes.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramString.class)
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class Paths
{
    /**
     * Concatenates a series of paths, ensuring that only a single slash exists between the two. If any tail path is
     * empty (null or ""), it is simply ignored.
     *
     * @param head The start of the path
     * @param tails The tails to concatenate to the path
     * @return The concatenated path
     */
    public static String pathConcatenate(String head, String... tails)
    {
        var concatenated = new StringBuilder(Strip.trailing(head, "/"));
        for (var tail : tails)
        {
            var next = Strip.trailing(Strip.leading(tail, "/"), "/");
            if (!Strings.isEmpty(next))
            {
                concatenated.append("/").append(next);
            }
        }
        return concatenated.toString();
    }

    /**
     * @return The first element in the given path up to the separator or null if no separator is found
     */
    public static String pathHead(String path, char separator)
    {
        if (path != null)
        {
            var index = path.indexOf(separator);
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
    public static String pathHead(String path, String separator)
    {
        if (path != null)
        {
            var index = path.indexOf(separator);
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
    public static String pathOptionalHead(String path, String separator)
    {
        return StringConversions.toString(pathHead(path, separator), path);
    }

    /**
     * @return The first element in the given path up to the separator or the path itself if no separator is found
     */
    public static String pathOptionalHead(String path, char separator)
    {
        return StringConversions.toString(pathHead(path, separator), path);
    }

    /**
     * @return The last element in the given path up to the separator or the path itself if no separator is found
     */
    public static String pathOptionalSuffix(String path, char separator)
    {
        if (path != null)
        {
            var index = path.lastIndexOf(separator);
            return index == -1 ? path : path.substring(index + 1);
        }
        return null;
    }

    /**
     * @return All but the first element in the path or null if the separator does not occur
     */
    public static String pathTail(String string, String separator)
    {
        if (string != null)
        {
            var index = string.indexOf(separator);
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
    public static String pathTail(String text, char separator)
    {
        if (text != null)
        {
            var index = text.indexOf(separator);
            if (index >= 0)
            {
                return text.substring(index + 1);
            }
        }
        return null;
    }

    /**
     * Returns the given path as a package path
     */
    public static String pathToPackage(String path)
    {
        return path.replaceAll("/", ".");
    }

    /**
     * @return The last element in the given path up to the separator or the path itself if no separator is found
     */
    public static String pathWithoutOptionalSuffix(String path, char separator)
    {
        return StringConversions.toString(pathWithoutSuffix(path, separator), path);
    }

    /**
     * @return The last element in the given path up to the separator or null if no separator is found
     */
    public static String pathWithoutSuffix(String path, char separator)
    {
        if (path != null)
        {
            var index = path.lastIndexOf(separator);
            if (index >= 0)
            {
                return path.substring(0, index);
            }
        }
        return null;
    }
}
