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

import com.telenav.kivakit.commandline.parsing.ArgumentListValidator;
import com.telenav.kivakit.commandline.parsing.ArgumentParserList;
import com.telenav.kivakit.commandline.parsing.SwitchList;
import com.telenav.kivakit.commandline.parsing.SwitchListValidator;
import com.telenav.kivakit.commandline.parsing.SwitchParserList;
import com.telenav.kivakit.commandline.lexakai.DiagramArgument;
import com.telenav.kivakit.commandline.lexakai.DiagramCommandLine;
import com.telenav.kivakit.core.KivaKit;
import com.telenav.kivakit.core.messaging.listeners.MessageList;
import com.telenav.kivakit.core.messaging.messages.OperationMessage;
import com.telenav.kivakit.core.string.AsciiArt;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.kivakit.core.string.Wrap;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.validation.BaseValidator;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.util.Collection;

import static com.telenav.kivakit.core.project.Project.resolveProject;

/**
 * Parses an array of command line arguments into a list of {@link SwitchList} of the form "-switch=value" and a list of
 * remaining non-switch arguments in an {@link ArgumentList}. Normally, this class does not need to be used directly, as
 * the Application base class provides easier access to the same functionality.
 *
 * <p><b>Parsing a Command Line</b></p>
 *
 * <p>
 * Parsers for individual switches and arguments can be added with {@link #add(SwitchParser)} and {@link
 * #add(ArgumentParser)}, respectively. The command line may then be parsed with {@link #parse(String[])}, taking the
 * array of arguments passed to the program's main(String[] arguments) method and returning a {@link CommandLine} object
 * representing the parsed command line.
 * </p>
 *
 * <p><b>Switch Conventions</b></p>
 *
 * <p>
 * Please note that the common UNIX convention of having switches with no value, "-verbose" for example, is not
 * supported. All switches must be specified with a value, such as "-verbose=true". Also note that the UNIX convention
 * of binding switches to the next argument (without using "=") is not supported either (so, "-output my-file.txt" must
 * be specified as "-output=my-file.txt").
 * </p>
 *
 * <p><b>Example</b></p>
 *
 * <p>
 * There are many classes in KivaKit that provide switch and argument parsers and it is easy to build new ones. Parsers
 * for some commonly used types are available as static methods in {@link ArgumentParser} and {@link SwitchParser}. Here
 * they are used to create a small example. This application takes one or more integer arguments. If the switch
 * -multiplier=[number] is passed in, each argument is multiplied by the multiplier before the arguments are summed to
 * produce a result:
 * </p>
 *
 * <pre>
 *     private static final ArgumentParser&lt;Integer&gt; NUMBER = ArgumentParser
 *         .integerArgument("numbers to add")
 *         .oneOrMore()
 *         .build();
 *
 *     private static final SwitchParser&lt;Integer&gt; MULTIPLIER = SwitchParser
 *         .integerSwitch("multiplier", "multiplier for each number")
 *         .optional()
 *         .defaultValue(1)
 *         .build();
 *
 *     public static void main( String[] arguments)
 *     {
 *         new Example(new CommandLineParser()
 *             .add(NUMBER)
 *             .add(MULTIPLIER)
 *             .parse(arguments);
 *     }
 *
 *     public Example( CommandLine commandLine)
 *     {
 *         final int multiplier = commandLine.get(MULTIPLIER);
 *         var result = 0;
 *         for ( Argument argument : commandLine)
 *         {
 *             result += argument.get(NUMBER) * multiplier;
 *         }
 *         LOGGER.information("result = $", result);
 *     }
 * </pre>
 *
 * @author jonathanl (shibo)
 * @see SwitchParser
 * @see ArgumentParser
 * @see CommandLine
 */
@SuppressWarnings("UseOfSystemOutOrSystemErr")
@UmlClassDiagram(diagram = DiagramArgument.class)
@UmlClassDiagram(diagram = DiagramCommandLine.class)
@UmlRelation(label = "validates", referent = ArgumentList.class)
@UmlRelation(diagram = DiagramCommandLine.class, label = "validates", referent = SwitchList.class)
public class CommandLineParser
{
    /** Parsers for arguments */
    @UmlAggregation
    private final ArgumentParserList argumentParsers = new ArgumentParserList();

    /** Parsers for switches */
    @UmlAggregation
    private final SwitchParserList switchParsers = new SwitchParserList();

    /** The application that created this command line parser */
    private final ApplicationMetadata application;

    /**
     * @param application The application's class
     */
    public CommandLineParser(ApplicationMetadata application)
    {
        this.application = application;
    }

    /**
     * Adds the given argument parser to this command line parser
     */
    public <T> CommandLineParser add(ArgumentParser<T> parser)
    {
        argumentParsers.add(parser);
        parser.parent(this);
        return this;
    }

    /**
     * Adds the given switch parser to this command line parser
     */
    public <T> CommandLineParser add(SwitchParser<T> parser)
    {
        switchParsers.add(parser);
        parser.parent(this);
        return this;
    }

    /**
     * Adds the given argument parsers to this command line parser
     */
    @UmlExcludeMember
    public CommandLineParser addArgumentParsers(Collection<ArgumentParser<?>> parsers)
    {
        for (var parser : parsers)
        {
            add(parser);
        }
        return this;
    }

    /**
     * Adds the given switch parsers to this command line parser
     */
    @UmlExcludeMember
    public CommandLineParser addSwitchParsers(Collection<SwitchParser<?>> parsers)
    {
        for (var parser : parsers)
        {
            add(parser);
        }
        return this;
    }

    /**
     * Parses the given arguments into a {@link CommandLine}. If an error occurs, the application will exit.
     *
     * @param arguments The command line arguments passed to main(String[] arguments)
     * @return The parsed command line
     */
    @UmlRelation(label = "creates")
    public CommandLine parse(String[] arguments)
    {
        return new CommandLine(this, arguments);
    }

    /**
     * Exits the program displaying the given message and command line help for the user
     */
    protected void exit(String message, Object... arguments)
    {
        var formatted = Strings.format(message, arguments);
        System.err.println("\n" + AsciiArt.textBox("COMMAND LINE ERROR(S)", formatted));
        System.err.flush();
        Duration.seconds(0.25).sleep();
        System.out.println(help() + "\n");
        System.out.flush();

        // If the application allows calling System.exit()
        if (application.callSystemExitOnUnrecoverableError())
        {
            // then quit the whole VM
            System.exit(1);
        }
        else
        {
            throw new IllegalArgumentException("Illegal command line arguments.");
        }
    }

    /**
     * Validates the given list of switches and the given list of arguments, using the available switch parsers and
     * argument parsers.
     */
    void validate(SwitchList switches, ArgumentList arguments)
    {
        var messages = new MessageList(Matcher.matchAll());

        var validator = new BaseValidator()
        {
            @Override
            protected void onValidate()
            {
                validate(new SwitchListValidator(switchParsers, switches));
                validate(new ArgumentListValidator(argumentParsers, arguments));
            }
        };

        if (!validator.validate(message -> messages.add(((OperationMessage) message).cause(null))))
        {
            exit(messages.bulleted(4));
        }
    }

    /**
     * @return A help message for this command line, giving help for all switches and arguments
     */
    private String help()
    {
        var kivakitVersion = "\nKivaKit " + resolveProject(KivaKit.class).projectVersion() + " (" + resolveProject(KivaKit.class).build().name() + ")";
        var usage = "\n\nUsage: " + application.getClass().getSimpleName() + " " + application.version() + " <switches> <arguments>\n\n";
        var description = Wrap.wrapRegion(application.description(), 100).trim() + "\n\n";
        var arguments = "Arguments:\n\n" + argumentParsers.help();
        var switches = "\n\nSwitches:\n\n" + switchParsers.help();

        return kivakitVersion
                + usage
                + description
                + arguments
                + switches;
    }
}
