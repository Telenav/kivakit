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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramString;
import com.telenav.kivakit.interfaces.string.StringFormattable;
import com.telenav.kivakit.interfaces.value.Source;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.string.CaseFormat.capitalizeOnlyFirstLetter;
import static com.telenav.kivakit.core.string.Join.join;
import static com.telenav.kivakit.core.string.Split.split;
import static java.lang.String.*;

/**
 * String conversion utilities
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramString.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class StringConversions
{
    /**
     * Returns the lowest bits of the given value as a binary string
     */
    public static String toBinaryString(int value, int bits)
    {
        var builder = new StringBuilder();
        var mask = 1 << (bits - 1);
        while (bits-- > 0)
        {
            builder.append((value & mask) == 0 ? "0" : "1");
            mask >>>= 1;
        }
        return builder.toString();
    }

    /**
     * Converts the given object to a debug string. If the object supports the AsString interface, the
     * {@link StringFormattable#asString(StringFormattable.Format)} method is called with
     * {@link StringFormattable.Format#DEBUG}. If it does not, the toString() method is called.
     *
     * @param object The object
     * @return A debug string for the object
     */
    public static String toDebugString(Object object)
    {
        if (object instanceof StringFormattable)
        {
            return ((StringFormattable) object).asString(StringFormattable.Format.DEBUG);
        }
        return toHumanizedString(object);
    }

    /**
     * Returns the given enum value as a displayable string
     */
    public static String toDisplayString(Enum<?> enumValue)
    {
        var words = split(enumValue.name(), "_");
        var display = new ArrayList<String>();
        for (var word : words)
        {
            display.add(capitalizeOnlyFirstLetter(word.toLowerCase()));
        }
        return join(display, " ");
    }

    /**
     * Returns the given text trivially converted to HTML
     */
    public static String toHtmlString(String text)
    {
        return text.replaceAll("\n", "<br/>").replaceAll(" ", "&nbsp;");
    }

    /**
     * Returns the value as a string or an empty string if it is null
     */
    public static String toNonNullString(Object value)
    {
        return value == null ? "" : toHumanizedString(value);
    }

    /**
     * Returns the given object as a string or the given value if it is null
     */
    public static String toHumanizedString(Object object, String defaultValue)
    {
        if (object == null)
        {
            return defaultValue;
        }
        if (object instanceof Source)
        {
            return toHumanizedString(((Source<?>) object).get());
        }
        if (object instanceof Long)
        {
            var value = (long) object;
            return format("%,d", value);
        }
        if (object instanceof Integer)
        {
            var value = (int) object;
            return format("%,d", value);
        }
        return object.toString();
    }

    /**
     * Returns the given object as a string or "null" if it is null
     */
    public static String toHumanizedString(Object object)
    {
        return toHumanizedString(object, "null");
    }
}
