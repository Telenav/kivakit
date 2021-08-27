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

package com.telenav.kivakit.kernel.data.conversion.string.language;

import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramDataConversionOther;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.List;

/**
 * Throwable converter for formatting exceptions.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataConversionOther.class)
public class ThrowableConverter extends BaseStringConverter<Throwable>
{
    /**
     * @param listener The listener to hear any conversion issues
     */
    public ThrowableConverter(final Listener listener)
    {
        super(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Throwable onToValue(final String value)
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String onToString(final Throwable throwable)
    {
        if (throwable != null)
        {
            final List<Throwable> causes = new ArrayList<>();
            var cause = throwable;
            causes.add(cause);
            while ((cause.getCause() != null) && (cause != cause.getCause()))
            {
                cause = cause.getCause();
                causes.add(cause);
            }
            final var builder = new StringBuilder(256);
            // First print the last cause
            final var length = causes.size() - 1;
            cause = causes.get(length);
            if (throwable instanceof RuntimeException)
            {
                builder.append("Message: ");
                builder.append(throwable.getMessage());
                builder.append("\n\n");
            }
            builder.append("Root cause:\n\n");
            outputThrowable(cause, builder);
            if (length > 0)
            {
                builder.append("\n\nComplete stack:\n\n");
                for (var i = 0; i < length; i++)
                {
                    outputThrowable(causes.get(i), builder);
                    builder.append("\n");
                }
            }
            return builder.toString();
        }
        else
        {
            return "<Null Throwable>";
        }
    }

    /**
     * Outputs the {@link Throwable} and its stack trace to the builder. To make output more readable, sun.reflect.
     * packages are filtered out.
     */
    private void outputThrowable(final Throwable cause, final StringBuilder builder)
    {
        builder.append(cause);
        builder.append("\n");
        final var trace = cause.getStackTrace();
        for (var i = 0; i < trace.length; i++)
        {
            final var traceString = trace[i].toString();
            if (!(traceString.startsWith("sun.reflect.") && (i > 1)))
            {
                builder.append("     at ");
                builder.append(traceString);
                builder.append("\n");
            }
        }
    }
}
