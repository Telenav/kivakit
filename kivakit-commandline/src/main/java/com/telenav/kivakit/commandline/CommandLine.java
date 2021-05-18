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

package com.telenav.kivakit.commandline;

import com.telenav.kivakit.commandline.parsing.SwitchList;
import com.telenav.kivakit.commandline.project.lexakai.diagrams.DiagramCommandLine;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.strings.AsciiArt;
import com.telenav.kivakit.kernel.language.strings.conversion.AsString;
import com.telenav.kivakit.kernel.language.strings.conversion.StringFormat;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.project.CoreKernelLimits;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.UmlNote;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import static com.telenav.kivakit.kernel.language.strings.conversion.StringFormat.DEBUGGER_IDENTIFIER;
import static com.telenav.lexakai.annotations.UmlNote.Align.TOP;

/**
 * A parsed command line of switches and (non-switch) arguments.
 *
 * <p><b>Retrieving Switches and Arguments</b></p>
 *
 * <p>
 * The list of arguments can be retrieved with {@link #arguments()} or by using the command line object in an advanced
 * for loop. Switch values can be retrieved by passing a {@link SwitchParser} to {@link #get(SwitchParser)} or {@link
 * #get(SwitchParser, Object)}. The method {@link #has(SwitchParser)} can be used to determine if a switch is present.
 * </p>
 *
 * <p><b>Error Handling</b></p>
 * <p>
 * If an application encounters a logical problem with the command line input (like mutually exclusive switches or
 * semantic problems), it can call {@link #exit(String, Object...)} to terminate the application with appropriate usage
 * help. An application might want to do this, for example, if two switches conflict or if a file is missing.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see CommandLineParser
 */
@UmlClassDiagram(diagram = DiagramCommandLine.class)
@UmlExcludeSuperTypes({ AsString.class })
@UmlNote(text = "See Application for easy access to switches and arguments", align = TOP)
public class CommandLine implements AsString, Iterable<Argument>
{
    /**
     * The pattern for switches, such as "-x=9" ( note that the equal sign is always required)
     */
    private static final Pattern switchPattern = Pattern.compile("-([\\-\\p{Alpha}:]+)=(.+)");

    /**
     * The proper arguments (without switches)
     */
    @KivaKitIncludeProperty
    @UmlAggregation
    private final ArgumentList arguments;

    /**
     * The switch arguments
     */
    @KivaKitIncludeProperty
    @UmlAggregation
    private final SwitchList switches;

    @UmlRelation(label = "parses with")
    private final CommandLineParser parser;

    /**
     * @param parser The command line parser
     * @param arguments The argument list as passed to the CommandLineApplication constructor (and originating from the
     * standard Java main() entry point for command-line applications).
     */
    public CommandLine(final CommandLineParser parser, final String[] arguments)
    {
        // Save command line parser
        this.parser = parser;

        // Create argument list with parsers
        this.arguments = new ArgumentList();

        // Create switch list with parsers
        switches = new SwitchList();

        // Loop through arguments, assigning switches to the switch list and arguments to the
        // argument list
        for (final var argument : arguments)
        {
            // If the argument starts with a dash, it must be a switch
            if (argument.startsWith("-"))
            {
                // Ensure the form of the switch argument
                final var matcher = switchPattern.matcher(argument);

                // If it's a valid switch,
                if (matcher.matches())
                {
                    // add it to the switch list,
                    switches.add(new Switch(matcher.group(1), matcher.group(2)));
                }
                else
                {
                    // and if it's not a valid switch, throw an exception.
                    exit("Invalid switch: " + argument);
                }
            }
            else
            {
                // otherwise, add the argument to the argument list
                this.arguments.add(new Argument(argument));
            }
        }

        // Validation switches and arguments
        this.parser.validate(switches, this.arguments);
    }

    /**
     * A programmatic way to add a switch
     */
    @UmlExcludeMember
    public void addSwitch(final String name, final String value)
    {
        switches.add(new Switch(name, value));
    }

    /**
     * @return The non-switch argument at the given index parsed using the given argument parser
     */
    @UmlRelation(label = "gets argument values")
    public <T> T argument(final int index, final ArgumentParser<T> parser)
    {
        return arguments().get(index).get(parser);
    }

    /**
     * @return The first non-switch argument parsed using the given argument parser
     */
    public <T> T argument(final ArgumentParser<T> parser)
    {
        return argument(0, parser);
    }

    /**
     * @return The list of all arguments retrieved using the given parser. Note that this implies that all arguments are
     * of the same type.
     */
    public <T> List<T> arguments(final ArgumentParser<T> parser)
    {
        final var arguments = new ArrayList<T>();
        for (final var argument : arguments())
        {
            arguments.add(argument.get(parser));
        }
        return arguments;
    }

    /**
     * @return The list of command-line (non-switch) arguments
     */
    public ArgumentList arguments()
    {
        return arguments;
    }

    /**
     * @return This command line converted back into a string array. Note that switches will always appear first in the
     * returned array.
     */
    @UmlExcludeMember
    public String[] asArgumentArray()
    {
        final var strings = new StringList(CoreKernelLimits.COMMAND_LINE_ARGUMENTS.plus(CoreKernelLimits.COMMAND_LINE_SWITCHES));
        for (final var _switch : switches)
        {
            strings.add(_switch.toString());
        }
        for (final var argument : arguments)
        {
            strings.add(argument.toString());
        }
        return strings.asStringArray();
    }

    @Override
    @UmlExcludeMember
    public String asString(final StringFormat format)
    {
        switch (format.identifier())
        {
            case DEBUGGER_IDENTIFIER:
                return new ObjectFormatter(this).toString();

            default:
                return toString();
        }
    }

    /**
     * Exits the application with the given error message and help for the user.
     */
    public void exit(final String error, final Object... arguments)
    {
        parser.exit(Message.format(AsciiArt.spaces(4) + AsciiArt.bullet() + " " + error, arguments));
    }

    /**
     * @return The value of any switch on this command line for the given switch parser
     */
    @UmlRelation(label = "gets switch values")
    public <T> T get(final SwitchParser<T> parser)
    {
        // Get the value for the given parser,
        final var value = switches.get(parser);

        // and if there is no value, but it's required,
        if (value == null && parser.isRequired())
        {
            // then fail with an error
            exit("No value for " + parser.name());
        }

        // or if there is a value, but it isn't a valid value,
        if (value != null && parser.validValues() != null && !parser.validValues().contains(value))
        {
            // then fail with an error,
            exit("Value for " + parser.name() + " must be one of: " + parser.validValues());
        }

        // otherwise if there is no value,
        if (value == null)
        {
            // choose the parser's default value, if any.
            return parser.defaultValue();
        }

        return value;
    }

    /**
     * @return The value for the given switch parser, or if the switch does not exist on this command line, the default
     * value instead.
     */
    public <T> T get(final SwitchParser<T> parser, final T defaultValue)
    {
        final var value = switches.get(parser);
        if (value == null)
        {
            return defaultValue;
        }
        return get(parser);
    }

    /**
     * @return True if this command line has a value for the given switch parser
     */
    public <T> boolean has(final SwitchParser<T> parser)
    {
        return get(parser) != null;
    }

    @Override
    public Iterator<Argument> iterator()
    {
        return arguments.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return switches + " " + arguments;
    }
}
