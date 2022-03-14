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

import com.telenav.kivakit.core.lexakai.DiagramString;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Collection;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Joins collections together with separator characters
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramString.class)
@LexakaiJavadoc(complete = true)
public class Join
{
    /**
     * Joins the given values on the given separator
     *
     * @param values The values to join
     * @param separator The separator to use
     * @return The joined string
     */
    public static <T> String join(Iterable<T> values, String separator)
    {
        var joiner = new StringJoiner(separator);
        values.forEach(at -> joiner.add(at.toString()));
        return joiner.toString();
    }

    /**
     * @return The given collection of values as text converted by the given function and separated by the given
     * separator
     */
    public static <T> String join(Collection<T> values,
                                  String separator,
                                  Function<T, String> toString)
    {
        return values.stream()
                .map(toString)
                .collect(Collectors.joining(separator));
    }
}
