////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.strings;

import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageString;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.data.conversion.BaseConverter;
import com.telenav.kivakit.core.kernel.data.conversion.Converter;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageString.class)
public class Join
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /**
     * @return The given collection of values as comma separated text
     */
    public static <T> String join(final Collection<T> values)
    {
        return join(values, ", ");
    }

    /**
     * @return The given collection of values as text converted by the given function and separated by the given
     * separator
     */
    public static <T> String join(final Collection<T> values, final Function<T, String> toString,
                                  final String separator)
    {
        return values.stream().map(toString).collect(Collectors.joining(separator));
    }

    /**
     * @return The given collection of values separated by the given separator
     */
    public static <T> String join(final Collection<T> values, final String separator)
    {
        return join(values, separator, new BaseConverter<>(LOGGER)
        {
            @Override
            protected String onConvert(final T value)
            {
                return StringTo.string(value);
            }
        });
    }

    /**
     * @return The given collection of values converted using the given converter and separated by the given separator
     */
    public static <T> String join(final Collection<T> values, final String separator,
                                  final Converter<T, String> converter)
    {
        final var builder = new StringBuilder();
        var first = true;
        for (final var value : values)
        {
            if (!first)
            {
                builder.append(separator);
            }
            builder.append(converter.convert(value));
            first = false;
        }
        return builder.toString();
    }
}
