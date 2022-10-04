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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.commandline.internal.lexakai.DiagramCommandLine;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.core.string.AsciiArt;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.interfaces.string.StringFormattable;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.UmlNote;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.regex.Pattern;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.string.Strings.format;
import static com.telenav.lexakai.annotations.UmlNote.Align.TOP;

/**
 * A parsed command line of switches and (non-switch) arguments.
 *
 * <p><b>Retrieving Switches and Arguments</b></p>
 *
 * <p>
 * The list of arguments can be retrieved with {@link #argumentValues()} or by using the command line object in an
 * advanced for loop. Switch values can be retrieved by passing a {@link SwitchParser} to {@link #get(SwitchParser)} or
 * {@link #get(SwitchParser, Object)}. The method {@link #has(SwitchParser)} can be used to determine if a switch is
 * present.
 * </p>
 *
 * <p><b>Error Handling</b></p>
 * <p>
 * If an application encounters a logical problem with the command line input (like mutually exclusive switches or
 * semantic problems), it can call {@link #exit(String, Object...)} to terminate the application with appropriate usage
 * help. An application might want to do this, for example, if two switches conflict or if a file is missing.
 * </p>
 *
 * <p><b>Arguments</b></p>
 *
 * <ul>
 *     <li>{@link #argument(ArgumentParser)}</li>
 *     <li>{@link #argument(int, ArgumentParser)}</li>
 *     <li>{@link #arguments(ArgumentParser)}</li>
 *     <li>{@link #argumentValues()}</li>
 *     <li>{@link #iterator()}</li>
 * </ul>
 *
 * <p><b>Switches</b></p>
 *
 * <ul>
 *     <li>{@link #addSwitch(String, String)}</li>
 *     <li>{@link #get(SwitchParser)}</li>
 *     <li>{@link #get(SwitchParser, Object)}</li>
 *     <li>{@link #has(SwitchParser)}</li>
 * </ul>
 *
 * <p><b>Operations</b></p>
 *
 * <ul>
 *     <li>{@link #exit(String, Object...)}</li>
 * </ul>
 *
 * <p><b>Conversion</b></p>
 *
 * <ul>
 *     <li>{@link #asArgumentArray()}</li>
 *     <li>{@link #asString(Format)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see CommandLineParser
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramCommandLine.class)
@UmlExcludeSuperTypes({ StringFormattable.class })
@UmlNote(text = "See Application for easy access to switches and arguments", align = TOP)
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public class CommandLine implements StringFormattable, Iterable<ArgumentValue>
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
    private final ArgumentValueList arguments;

    @UmlRelation(label = "parses with")
    private final CommandLineParser parser;

    /**
     * The switch arguments
     */
    @KivaKitIncludeProperty
    @UmlAggregation
    private final SwitchValueList switches;

    /**
     * @param parser The command line parser
     * @param arguments The argument list as passed to the CommandLineApplication constructor (and originating from the
     * standard Java main() entry point for command-line applications).
     */
    public CommandLine(@NotNull CommandLineParser parser, @NotNull String[] arguments)
    {
        // Save command line parser
        this.parser = parser;

        // Create argument list with parsers
        this.arguments = new ArgumentValueList();

        // Create switch list with parsers
        switches = new SwitchValueList();

        // Loop through arguments, assigning switches to the switch list and arguments to the
        // argument list
        for (var argument : arguments)
        {
            // If the argument starts with a dash, it must be a switch
            if (argument.startsWith("-"))
            {
                // Ensure the form of the switch argument
                var matcher = switchPattern.matcher(argument);

                // If it's a valid switch,
                if (matcher.matches())
                {
                    // add it to the switch list,
                    switches.add(new SwitchValue(matcher.group(1), matcher.group(2)));
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
                this.arguments.add(new ArgumentValue(argument));
            }
        }

        // Validation switches and arguments
        this.parser.validate(switches, this.arguments);
    }

    /**
     * A programmatic way to add a switch
     */
    @UmlExcludeMember
    public void addSwitch(@NotNull String name, @NotNull String value)
    {
        switches.add(new SwitchValue(name, value));
    }

    /**
     * Returns the non-switch argument at the given index parsed using the given argument parser
     */
    @UmlRelation(label = "gets arguments")
    public <T> T argument(int index, @NotNull ArgumentParser<T> parser)
    {
        return argumentValues().argumentValue(index).get(parser);
    }

    /**
     * Returns the first non-switch argument parsed using the given argument parser
     */
    public <T> T argument(@NotNull ArgumentParser<T> parser)
    {
        return argument(0, parser);
    }

    /**
     * Returns the list of command-line (non-switch) arguments
     */
    public ArgumentValueList argumentValues()
    {
        return arguments;
    }

    /**
     * Returns the list of all arguments retrieved using the given parser. Note that this implies that all arguments are
     * of the same type.
     */
    public <T> ObjectList<T> arguments(@NotNull ArgumentParser<T> parser)
    {
        var arguments = new ObjectList<T>();
        for (var argument : argumentValues())
        {
            arguments.add(argument.get(parser));
        }
        return arguments;
    }

    /**
     * Returns this command line converted back into a string array. Note that switches will always appear first in the
     * returned array.
     */
    @UmlExcludeMember
    public String[] asArgumentArray()
    {
        var strings = new StringList();
        for (var _switch : switches)
        {
            strings.add(_switch.toString());
        }
        for (var argument : arguments)
        {
            strings.add(argument.toString());
        }
        return strings.asStringArray();
    }

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    @Override
    @UmlExcludeMember
    public String asString(@NotNull Format format)
    {
        switch (format)
        {
            case DEBUG:
                return new ObjectFormatter(this).toString();

            default:
                return toString();
        }
    }

    /**
     * Exits the application with the given error message and help for the user.
     */
    public void exit(@NotNull String error, Object... arguments)
    {
        parser.exit(format(AsciiArt.spaces(4) + AsciiArt.bullet() + " " + error, arguments));
    }

    /**
     * Returns the value of any switch on this command line for the given switch parser
     */
    @UmlRelation(label = "gets switches")
    public <T> T get(@NotNull SwitchParser<T> parser)
    {
        ensureNotNull(parser);

        // Get the value for the given parser,
        var value = switches.get(parser);

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

        // otherwise, if there is no value,
        if (value == null)
        {
            // choose the parser's default value, if any.
            return parser.defaultValue();
        }

        return value;
    }

    /**
     * Returns the value for the given switch parser, or if the switch does not exist on this command line, the default
     * value instead.
     */
    public <T> T get(@NotNull SwitchParser<T> parser,
                     @NotNull T defaultValue)
    {
        ensureNotNull(parser);

        var value = switches.get(parser);
        if (value == null)
        {
            return defaultValue;
        }
        return get(parser);
    }

    /**
     * Returns true if this command line has a value for the given switch parser
     */
    public <T> boolean has(@NotNull SwitchParser<T> parser)
    {
        ensureNotNull(parser);

        return get(parser) != null;
    }

    @Override
    public Iterator<ArgumentValue> iterator()
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
