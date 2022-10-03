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

package com.telenav.kivakit.conversion;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversion;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.messaging.Transceiver;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Collection;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A converter converts from one type to another. Converters are message {@link Repeater}s, relaying information about
 * any conversion issues to listeners.
 *
 * @param <From> Source type
 * @param <To> Destination type
 * @author jonathanl (shibo)
 * @see Repeater
 * @see Transceiver
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramConversion.class)
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public interface Converter<From, To> extends Repeater
{
    /**
     * Convert from type &lt;From&gt; to type &lt;To&gt;.
     *
     * @param from The value to convert
     * @return The converted value
     */
    To convert(From from);

    /**
     * Convert from type &lt;From&gt; to type &lt;To&gt;.
     *
     * @param from The value to convert
     * @return The converted value
     */
    default To convertOrDefault(From from, To defaultValue)
    {
        var to = convert(from);
        return to != null ? to : defaultValue;
    }

    /**
     * Joins a collection of values, each converted by this converter, with the given separator
     *
     * @return The given collection of values converted using the given converter and separated by the given separator
     */
    default String join(Collection<From> values, String separator)
    {
        var builder = new StringBuilder();
        var first = true;
        for (var value : values)
        {
            if (!first)
            {
                builder.append(separator);
            }
            builder.append(convert(value));
            first = false;
        }
        return builder.toString();
    }

    /**
     * Joins a collection of values, each converted by this converter, with commas
     *
     * @return The given collection of values as comma separated text
     */
    default String join(Collection<From> values)
    {
        return join(values, ", ");
    }
}
