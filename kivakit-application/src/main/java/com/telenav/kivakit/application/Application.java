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

package com.telenav.kivakit.application;

import com.telenav.kivakit.application.project.lexakai.diagrams.DiagramApplication;
import com.telenav.kivakit.commandline.ApplicationMetadata;
import com.telenav.kivakit.commandline.ArgumentList;
import com.telenav.kivakit.commandline.ArgumentParser;
import com.telenav.kivakit.commandline.CommandLine;
import com.telenav.kivakit.commandline.CommandLineParser;
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.configuration.BaseComponent;
import com.telenav.kivakit.kernel.interfaces.naming.Named;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.collections.map.string.VariableMap;
import com.telenav.kivakit.kernel.language.collections.set.Sets;
import com.telenav.kivakit.kernel.language.locales.Locale;
import com.telenav.kivakit.kernel.language.strings.Align;
import com.telenav.kivakit.kernel.language.strings.AsciiArt;
import com.telenav.kivakit.kernel.language.strings.Strip;
import com.telenav.kivakit.kernel.language.types.Classes;
import com.telenav.kivakit.kernel.language.values.identifier.StringIdentifier;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.kernel.language.vm.KivaKitShutdownHook;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.logging.logs.BaseLog;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.Repeater;
import com.telenav.kivakit.kernel.messaging.filters.AllMessages;
import com.telenav.kivakit.kernel.messaging.filters.SeverityGreaterThanOrEqualTo;
import com.telenav.kivakit.kernel.messaging.messages.status.Quibble;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.kernel.project.Project;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.resources.other.PropertyMap;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.UmlNote;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static com.telenav.kivakit.commandline.SwitchParser.booleanSwitchParser;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * Base class for KivaKit applications. Handles command line parsing, project initialization and configuration.
 *
 * <p><b>Messaging</b></p>
 *
 * <p>
 * Because this class extends {@link BaseRepeater} and has a {@link Logger} that listens to the application class, all
 * {@link Application}s automatically inherit logging functionality via the convenience methods in {@link Repeater}.
 * </p>
 *
 * <p><b>Startup</b></p>
 *
 * <p>
 * The {@link Application} object should be constructed in the main(String[]) Java entrypoint and the {@link
 * #run(String[])} method should be called. This can be done in one step:
 * </p>
 *
 * <pre>
 * public static class MyApplication extends Application
 * {
 *     public static void main(final String[] arguments)
 *     {
 *         new MyApplication().run(arguments);
 *     }
 *
 *     [...]
 * }
 * </pre>
 *
 * <p><b>Command Line Parsing</b></p>
 *
 * <p>
 * For within the application implementation in the {@link #onRun()} method, the {@link Application} class provides
 * convenient access to the parsed command line:
 * </p>
 *
 * <ul>
 *     <li>{@link #commandLine()} - Gets the parsed command line</li>
 *     <li>{@link #arguments()} - Gets command line arguments (excluding switches)</li>
 *     <li>{@link #argument(ArgumentParser)} - Gets the first command line argument (excluding switches)</li>
 *     <li>{@link #argument(int, ArgumentParser)} - Gets the nth argument using the given argument parser</li>
 *     <li>{@link #get(SwitchParser)} - Gets the switch value for the given switch parser</li>
 *     <li>{@link #has(SwitchParser)} - Determines if there is a switch value for the given switch parser</li>
 *     <li>{@link #exit(String, Object...)} - Exits the application displaying the given message and command line usage</li>
 * </ul>
 *
 * <p><b>Important Note: Project Initialization</b></p>
 *
 * <p>
 * All applications must pass one or more {@link Project}s to the {@link Application} constructor to ensure that all of
 * the application's dependent project(s) are correctly initialized. See {@link Project} for details.
 * </p>
 *
 * <p><b>Running</b></p>
 *
 * <p>
 * The {@link #run(String[])} method will perform the following steps:
 * </p>
 *
 * <ol start=1>
 *     <li>Call {@link #onProjectInitializing()}</li>
 *     <li>Initialize and install the {@link Project} passed to the constructor</li>
 *     <li>Call {@link #onProjectInitialized()}</li>
 *     <li>Parse command line arguments using:
 *     <ul>
 *         <li>The {@link ArgumentParser}s returned by {@link #argumentParsers()}</li>
 *         <li>The {@link SwitchParser}s returned by {@link #switchParsers()}</li>
 *     </ul>
 *     </li>
 *     <li>Call the application implementation in {@link #onRun()}</li>
 * </ol>
 *
 * @author jonathanl (shibo)
 * @see BaseRepeater
 * @see CommandLine
 * @see SwitchParser
 * @see ArgumentParser
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramApplication.class)
@UmlNote(text = "See kivakit-core-configuration for details on application configuration")
@LexakaiJavadoc(complete = true)
public abstract class Application extends BaseComponent implements Named, ApplicationMetadata
{
    /** The one and only application running in this process */
    private static Application instance;

    private static final Logger LOGGER = LoggerFactory.newLogger();

    /**
     * @return The currently running application
     */
    public static Application get()
    {
        return instance;
    }

    /**
     * A unique string identifier for a KivaKit {@link Application}.
     *
     * @author jonathanl (shibo)
     */
    @Schema(description = "A unique identifier for a KivaKit application, provided by the Application base class",
            example = "ServiceRegistryServer")
    @UmlClassDiagram(diagram = DiagramApplication.class)
    @UmlExcludeSuperTypes
    @LexakaiJavadoc(complete = true)
    public static class Identifier extends StringIdentifier
    {
        public Identifier(final String identifier)
        {
            super(identifier);
        }

        protected Identifier()
        {
        }
    }

    @UmlExcludeMember
    protected final SwitchParser<Boolean> QUIET =
            booleanSwitchParser("quiet", "Minimize output")
                    .optional()
                    .defaultValue(false)
                    .build();

    /** The project that this application uses */
    @UmlAggregation(label = "initializes and uses")
    private final Project project;

    /** The parsed command line for this application */
    @UmlAggregation
    private CommandLine commandLine;

    /**
     * @param projects One or more projects to initialize
     */
    protected Application(final Project... projects)
    {
        registerObject(this);

        instance = this;
        if (projects.length == 1)
        {
            project = ensureNotNull(projects[0]);
        }
        else
        {
            project = new Project()
            {
                @Override
                public Set<Project> dependencies()
                {
                    return Sets.of(projects);
                }
            };
        }
    }

    @UmlExcludeMember
    public void announce()
    {
        announce(commandLineDescription(name()));
    }

    /**
     * @return The non-switch argument at the given index parsed using the given argument parser
     */
    public <T> T argument(final int index, final ArgumentParser<T> parser)
    {
        return commandLine.argument(index, parser);
    }

    /**
     * @return The first non-switch argument parsed using the given argument parser
     */
    public <T> T argument(final ArgumentParser<T> parser)
    {
        return commandLine.argument(parser);
    }

    /**
     * @return All non-switch command line arguments
     */
    public ArgumentList arguments()
    {
        return commandLine.arguments();
    }

    /**
     * @return The parsed command line
     */
    @UmlRelation(label = "parses arguments into")
    public CommandLine commandLine()
    {
        return commandLine;
    }

    /**
     * @return This command line in a text box intended for user feedback when starting an application
     */
    public String commandLineDescription(final String title)
    {
        final var box = new StringList();
        int number = 1;

        if (!arguments().isEmpty())
        {
            box.add("");
            box.add("Arguments:");
            box.add("");
            for (final var argument : arguments())
            {
                box.add(AsciiArt.repeat(4, ' ') + "$. $", number++, argument.value());
            }
        }
        if (!switchParsers().isEmpty())
        {
            box.add("");
            box.add("Switches:");
            box.add("");
            final var sorted = new ArrayList<>(switchParsers());
            sorted.sort(Comparator.comparing(SwitchParser::name));
            final var width = new StringList(sorted).longest().asInt();
            for (final var switchParser : sorted)
            {
                box.add("   $ = $", Align.right(switchParser.name(), width, ' '),
                        get(switchParser) == null ? "N/A" : get(switchParser));
            }
        }
        box.add("");
        return box.titledBox(title);
    }

    /**
     * @return A description of the application for use in help
     */
    @Override
    public String description()
    {
        return "";
    }

    /**
     * Exits the application with the given message formatted by {@link Message#format(String, Object...)}
     *
     * @param message The message
     * @param arguments Arguments to interpolate into the message
     */
    public void exit(final String message, final Object... arguments)
    {
        commandLine.exit(message, arguments);
    }

    /**
     * @return The value for the command line switch parsed by the given switch parser, if any
     */

    public <T> T get(final SwitchParser<T> parser)
    {
        return commandLine.get(parser);
    }

    /**
     * @return The value for the command line switch parsed by given switch parser or the default value if the switch
     * does not exist
     */
    public <T> T get(final SwitchParser<T> parser, final T defaultValue)
    {
        return commandLine.get(parser, defaultValue);
    }

    /**
     * @return True if this application has a value for the command line switch parsed by the given parser
     */
    public <T> boolean has(final SwitchParser<T> parser)
    {
        return commandLine.has(parser);
    }

    @UmlRelation(label = "identified by")
    public Identifier identifier()
    {
        return new Identifier(Classes.simpleName(getClass()));
    }

    public PropertyMap localizedProperties(final Locale locale)
    {
        return PropertyMap.localized(packagePath(), locale);
    }

    public Project project()
    {
        return project;
    }

    public VariableMap<String> properties()
    {
        return project.properties();
    }

    /**
     * Runs the application by calling {@link #onRun()} given the arguments from the Java main(String[]) application
     * entrypoint. Operations occur in this order:
     * <ol>
     *     <li>{@link #onRunning()} is called to indicate that running is about to start</li>
     *     <li>Command line arguments are validated and parsed into a {@link CommandLine}</li>
     *     <li>{@link #onConfigureOutput()} is called to allow redirection of output</li>
     *     <li>{@link #onProjectInitializing()} is called before the {@link Project} for this application is initialized</li>
     *     <li>{@link Project#initialize()} is called</li>
     *     <li>{@link #onProjectInitialized()} is called</li>
     *     <li>{@link #onRun()} is called</li>
     *     <li>{@link #onRan()} is called</li>
     *     <li>The application exits</li>
     * </ol>
     *
     * @param arguments Command line arguments to parse
     */
    public final void run(final String[] arguments)
    {
        onRunning();

        // Go through arguments
        final var argumentList = new StringList();
        for (final var argument : arguments)
        {
            // and if the argument is -switches=[resource]
            if (argument.startsWith("-switches="))
            {
                // then load properties from the resource
                final var resourceIdentifier = Strip.leading(argument, "-switches=");
                final var resource = Resource.resolve(resourceIdentifier);
                final var properties = PropertyMap.load(resource);

                // and add those properties to the argument list
                for (final var key : properties.keySet())
                {
                    final var value = properties.get(key);
                    argumentList.add(key + "=" + value);
                }
            }
            else
            {
                // otherwise, add the argument
                argumentList.add(argument);
            }
        }

        // then parse the command line arguments.
        commandLine = new CommandLineParser(this)
                .addSwitchParsers(switchParsers())
                .addArgumentParsers(argumentParsers())
                .parse(argumentList.asStringArray());

        onConfigureOutput();

        onProjectInitializing();
        project.initialize();
        onProjectInitialized();

        announce("Project: $", project.name());
        announce("Application: " + name());

        try
        {
            onRun();
        }
        catch (final Exception e)
        {
            problem(e, "Application $ failed with exception", name());
        }

        BaseLog.logs().forEach(BaseLog::flush);

        for (final var log : BaseLog.logs())
        {
            if (log.messageCounts().size() > 0)
            {
                information(AsciiArt.textBox(log.name() + " Log Messages", "$", log.messageCounts().toString("\n")));
            }
        }

        onRan();
    }

    /**
     * @return The application version as specified in the resource "/project.properties"
     */
    @Override
    public Version version()
    {
        return project().projectVersion();
    }

    /**
     * @return The argument parsers for this application
     */
    protected List<ArgumentParser<?>> argumentParsers()
    {
        return Collections.emptyList();
    }

    /**
     * Configures output of the application
     */
    @UmlExcludeMember
    protected void onConfigureOutput()
    {
        output(LOGGER);
    }

    /**
     * Called after this application's project has been initialized
     */
    @UmlExcludeMember
    protected void onProjectInitialized()
    {
    }

    /**
     * Called before this application's project is initialized
     */
    @UmlExcludeMember
    protected void onProjectInitializing()
    {
    }

    /**
     * Called after the application has run and just before shutdown. For system hooks, see {@link
     * KivaKitShutdownHook}.
     */
    @UmlExcludeMember
    protected void onRan()
    {
    }

    /**
     * Application implementation
     */
    protected abstract void onRun();

    /**
     * Called before the application begins to parse the command line to run
     */
    @UmlExcludeMember
    protected void onRunning()
    {
    }

    /**
     * Sets output for the application to go to the given listener. This is the same in an application as
     * LOGGER.listenTo(this), but this method adds a filter which reduces output to a minimum if the -quiet=true switch
     * is set (the switch must be added to the return value of {@link #switchParsers()}).
     */
    @UmlExcludeMember
    protected void output(final Listener listener)
    {
        final var filter = get(QUIET)
                ? new SeverityGreaterThanOrEqualTo(new Quibble().severity())
                : new AllMessages();

        listener.listenTo(this, filter);
    }

    /**
     * @return The switch parsers for this application
     */
    protected Set<SwitchParser<?>> switchParsers()
    {
        return Sets.empty();
    }
}
