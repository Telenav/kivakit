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

package com.telenav.kivakit.core.kernel.messaging.messages;

import com.telenav.kivakit.core.kernel.interfaces.naming.Named;
import com.telenav.kivakit.core.kernel.language.collections.map.string.VariableMap;
import com.telenav.kivakit.core.kernel.language.primitives.Doubles;
import com.telenav.kivakit.core.kernel.language.strings.Align;
import com.telenav.kivakit.core.kernel.language.strings.StringTo;
import com.telenav.kivakit.core.kernel.language.strings.Strings;
import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.core.kernel.language.types.Classes;
import com.telenav.kivakit.core.kernel.logging.LogEntry;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.logs.text.BaseTextLog;
import com.telenav.kivakit.core.kernel.logging.logs.text.ConsoleLog;
import com.telenav.kivakit.core.kernel.logging.logs.text.LogEntryFormatter;
import com.telenav.kivakit.core.kernel.messaging.Broadcaster;
import com.telenav.kivakit.core.kernel.messaging.Debug;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.messaging.Repeater;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessaging;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Handles message formatting, given a message and an argument array. This class is used extensively throughout the
 * KivaKit to format messages (by {@link Debug}, {@link Logger}, {@link Broadcaster}, {@link Repeater} and others), so
 * it is useful to know the following special instructions to perform string substitutions (interpolations) of the
 * arguments to {@link #format(String, Object...)}:
 *
 * <p><b>Interpolations</b></p>
 *
 * <ul>
 *     <li><b>$</b> - converts the corresponding argument to a string using {@link StringTo#string(Object)}.
 *             Long and Integer arguments are converted to a more readable comma-separated format</li>
 *     <li><b>$$</b> - evaluates to a literal '$'</li>
 *     <li><b>${class}</b> - converts a {@link Class} argument to a simple (non-qualified) class name</li>
 *     <li><b>${hex}</b> - converts a long argument to a hexadecimal value</li>
 *     <li><b>${binary}</b> - converts a long argument to a binary string</li>
 *     <li><b>${integer}</b> - converts an integer argument to a non-comma-separated numeric string</li>
 *     <li><b>${long}</b> - converts a long argument to a non-comma-separated numeric string </li>
 *     <li><b>${float}</b> - converts a float argument to a string with one digit after the decimal</li>
 *     <li><b>${double}</b> - converts a double argument to a string with one digit after the decimal</li>
 *     <li><b>${debug}</b> - converts an argument to a string using toDebugString() if that interface is supported.
 *                    If it is not supported, toString() is called</li>
 *     <li><b>${object}</b> - uses the {@link ObjectFormatter} to format the argument by reflecting on it</li>
 *     <li><b>${flag}</b> - converts boolean arguments to 'enabled' or 'disabled'</li>
 *     <li><b>${name}</b> - converts the argument to the name returned by {@link Named#name()} if the argument is {@link Named}</li>
 *     <li><b>${nowrap}</b> - signals that the message should not be wrapped</li>
 * </ul>
 *
 * <p><b>Example</b></p>
 *
 * <p>
 * For example, {@link Broadcaster} implements {@link Broadcaster#problem(String, Object...)}, which can be used like this:
 * </p>
 *
 * <pre>
 * problem("The resource '$' was read in $", fileName, start.elapsedSince());
 * </pre>
 *
 * <p>
 * A {@link Problem} object is broadcast to any listeners. Listeners may do a variety of things with the message, but
 * when a listener like a {@link ConsoleLog} receives the message it will convert it to a {@link LogEntry} which it may
 * choose to format with {@link #format(String, Object...)}:
 * </p>
 *
 * <pre>
 * The resource 'bay_area.osm.pbf' was read in 0.3 seconds
 * </pre>
 *
 * @author jonathanl (shibo)
 * @see Listener
 * @see Broadcaster
 * @see Repeater
 * @see Logger
 * @see LogEntry
 * @see ConsoleLog
 * @see BaseTextLog
 * @see LogEntryFormatter
 * @see ConsoleLog.Format
 * @see Debug
 * @see ObjectFormatter
 * @see Named
 */
@UmlClassDiagram(diagram = DiagramMessaging.class)
public class MessageFormatter
{
    public enum Format
    {
        WITHOUT_EXCEPTION,
        WITH_EXCEPTION
    }

    /**
     * Formats the given message by interpolating the given arguments.
     *
     * @param message The message to format
     * @param arguments The arguments to use in formatting
     * @return The formatted message
     */
    public String format(final String message, final Object... arguments)
    {
        return formatArray(message, arguments);
    }

    /**
     * Formats the given message by interpolating the given arguments.
     *
     * @param message The message to format
     * @param arguments The arguments to use in formatting
     * @return The formatted message
     */
    public String formatArray(final String message, final Object[] arguments)
    {
        try
        {
            VariableMap<?> map = null;
            if (arguments.length >= 1)
            {
                if (arguments[0] instanceof VariableMap<?>)
                {
                    if (arguments.length > 1)
                    {
                        return "If first argument to '" + message + "' is a VariableMap, there can be no other arguments";
                    }
                    map = (VariableMap<?>) arguments[0];
                }
            }
            final var builder = new StringBuilder();
            var current = 0;
            var argumentIndex = 0;
            int start;
            do
            {
                // Find next interpolation
                start = message.indexOf("$", current);

                // If we found it and we can look at the next character,
                if (start >= 0)
                {
                    // get the next character, if we can
                    final char next;
                    if (start + 1 < message.length())
                    {
                        next = message.charAt(start + 1);
                    }
                    else
                    {
                        next = Character.MIN_VALUE;
                    }

                    if (next == '$')
                    {
                        builder.append('$');
                        current = start + 2;
                        continue;
                    }

                    final int close;
                    final String command;
                    if (next == '{')
                    {
                        // ${x} format (hopefully)
                        close = message.indexOf('}', start);
                        if (close < 0)
                        {
                            return "Unclosed interpolation expression. Maybe add a '}' somewhere: " + message;
                        }
                        command = message.substring(start + 2, close);
                    }
                    else
                    {
                        // $ shorthand
                        close = start;
                        command = "string";
                    }

                    // then add the text from current to interpolation start
                    builder.append(message, current, start);

                    // Ensure argument index
                    if (argumentIndex >= arguments.length)
                    {
                        return "Out of arguments: " + message;
                    }

                    // If we've got a string value map,
                    if (map != null)
                    {
                        // add the value from the map
                        final var value = map.get(command);
                        if (value == null)
                        {
                            return "No key '" + command + "' in: " + message;
                        }
                        builder.append(StringTo.string(value));
                    }
                    else
                    {
                        // Interpret the command
                        switch (command)
                        {
                            case "string":
                                builder.append(StringTo.string(arguments[argumentIndex++]));
                                break;

                            case "lower":
                                builder.append(StringTo.string(arguments[argumentIndex++]).toLowerCase());
                                break;

                            case "upper":
                                builder.append(StringTo.string(arguments[argumentIndex++]).toUpperCase());
                                break;

                            case "integer":
                            case "long":
                                builder.append(arguments[argumentIndex++]);
                                break;

                            case "float":
                                builder.append(Doubles.format((float) arguments[argumentIndex++], 1));
                                break;

                            case "double":
                                builder.append(Doubles.format((double) arguments[argumentIndex++], 1));
                                break;

                            case "right":
                                builder.append(Align.right(arguments[argumentIndex++].toString(), 16, ' '));
                                break;

                            case "left":
                                builder.append(Align.left(arguments[argumentIndex++].toString(), 16, ' '));
                                break;

                            case "hex":
                                builder.append(Long.toHexString(Long.parseLong(arguments[argumentIndex++].toString())));
                                break;

                            case "binary":
                                builder.append(Long.toBinaryString(Long.parseLong(arguments[argumentIndex++].toString())));
                                break;

                            case "debug":
                                builder.append(StringTo.debug(arguments[argumentIndex++]));
                                break;

                            case "object":
                                builder.append(new ObjectFormatter(arguments[argumentIndex++]));
                                break;

                            case "class":
                            {
                                final var cast = cast(arguments[argumentIndex++], Class.class);
                                if (cast == null)
                                {
                                    return "Expected parameter of type '" + Class.class + "' for 'class'";
                                }
                                builder.append(Classes.simpleName(cast));
                                break;
                            }

                            case "flag":
                            {
                                final var cast = cast(arguments[argumentIndex++], Boolean.class);
                                if (cast == null)
                                {
                                    return "Expected parameter of type '" + Boolean.class + "' for 'flag'";
                                }
                                builder.append(cast ? "enabled" : "disabled");
                                break;
                            }

                            case "name":
                            {
                                final var named = cast(arguments[argumentIndex++], Named.class);
                                if (named == null)
                                {
                                    return "Expected parameter of type '" + Named.class + "' for 'name'";
                                }
                                builder.append("'");
                                builder.append(named.name());
                                builder.append("'");
                                break;
                            }

                            case "nowrap":
                                builder.append("${nowrap}");
                                break;

                            default:
                                try
                                {
                                    final var position = Integer.parseInt(command);
                                    if (position >= 0 && position <= arguments.length - 1)
                                    {
                                        builder.append(StringTo.string(arguments[position]));
                                    }
                                    else
                                    {
                                        return "Cannot interpolate argument " + position + " into: " + message;
                                    }
                                }
                                catch (final NumberFormatException e)
                                {
                                    return "Unrecognized interpolation '" + command + "' in: " + message;
                                }
                                break;
                        }
                    }

                    // Move past the close marker if any
                    current = close + 1;
                }
            }
            while (start >= 0);

            // We must consume all arguments if the format is positional
            if (map == null && argumentIndex != arguments.length)
            {
                return "Extraneous arguments. " + arguments.length + " arguments provided to: " + message;
            }

            // Add the tail end
            builder.append(message.substring(current));

            // Return the final, interpolated string
            return builder.toString();
        }
        catch (final Throwable e)
        {
            // We can't use the logging facility here because we may be formatting a log message
            var cause = e.getMessage();
            if (cause != null)
            {
                cause = Strings.replaceAll(cause, "$", "$$");
            }
            else
            {
                cause = "Unknown cause";
            }
            return "Problem: Unable to format message '" + message + "' due to exception: " + cause;
        }
    }

    @SuppressWarnings({ "unchecked" })
    private <T> T cast(final Object object, final Class<T> type)
    {
        if (type.isAssignableFrom(object.getClass()))
        {
            return (T) object;
        }
        return null;
    }
}
