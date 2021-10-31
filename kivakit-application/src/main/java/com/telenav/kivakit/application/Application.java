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
import com.telenav.kivakit.commandline.Quantifier;
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.configuration.settings.deployment.Deployment;
import com.telenav.kivakit.configuration.settings.deployment.DeploymentSet;
import com.telenav.kivakit.kernel.interfaces.naming.Named;
import com.telenav.kivakit.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.collections.set.ObjectSet;
import com.telenav.kivakit.kernel.language.locales.Locale;
import com.telenav.kivakit.kernel.language.strings.Align;
import com.telenav.kivakit.kernel.language.strings.AsciiArt;
import com.telenav.kivakit.kernel.language.strings.Strip;
import com.telenav.kivakit.kernel.language.threading.conditions.StateMachine;
import com.telenav.kivakit.kernel.language.types.Classes;
import com.telenav.kivakit.kernel.language.values.identifier.StringIdentifier;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.kernel.language.vm.KivaKitShutdownHook;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.logging.logs.BaseLog;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.Repeater;
import com.telenav.kivakit.kernel.messaging.filters.AllMessages;
import com.telenav.kivakit.kernel.messaging.filters.SeverityGreaterThanOrEqualTo;
import com.telenav.kivakit.kernel.messaging.messages.status.Glitch;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.kernel.project.Project;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.resources.other.PropertyMap;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.telenav.kivakit.application.Application.State.CREATED;
import static com.telenav.kivakit.application.Application.State.INITIALIZING;
import static com.telenav.kivakit.application.Application.State.READY;
import static com.telenav.kivakit.application.Application.State.RUNNING;
import static com.telenav.kivakit.application.Application.State.STOPPED;
import static com.telenav.kivakit.application.Application.State.STOPPING;
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
 *     public static void main( String[] arguments)
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
 *     <li>{@link #argumentList()} - Gets command line arguments (excluding switches)</li>
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
@LexakaiJavadoc(complete = true)
public abstract class Application extends BaseComponent implements Named, ApplicationMetadata
{
    /** The one and only application running in this process */
    private static Application instance;

    /** The default final destination for messages that bubble up to the application level */
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /**
     * @return The currently running application
     */
    public static Application get()
    {
        return instance;
    }

    public enum State
    {
        CREATED,
        INITIALIZING,
        RUNNING,
        READY,
        STOPPING,
        STOPPED
    }

    /**
     * A unique string identifier for a KivaKit {@link Application}.
     *
     * @author jonathanl (shibo)
     */
    @UmlClassDiagram(diagram = DiagramApplication.class)
    @UmlExcludeSuperTypes
    @LexakaiJavadoc(complete = true)
    public static class Identifier extends StringIdentifier
    {
        public Identifier(String identifier)
        {
            super(identifier);
        }

        protected Identifier()
        {
        }
    }

    /** The project that this application uses */
    @UmlAggregation(label = "initializes and uses")
    private Project project;

    /** The parsed command line for this application */
    @UmlAggregation
    private CommandLine commandLine;

    /** Switch parser to specify deployment settings */
    private SwitchParser<Deployment> DEPLOYMENT;

    @UmlExcludeMember
    protected final SwitchParser<Boolean> QUIET =
            booleanSwitchParser(this, "quiet", "Minimize output")
                    .optional()
                    .defaultValue(false)
                    .build();

    /** Set of deployments for the application, if any */
    private DeploymentSet deployments;

    private final StateMachine<State> state = new StateMachine<>(CREATED);

    /**
     * @param projects One or more projects to initialize
     */
    protected Application(Project... projects)
    {
        register(this);

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
                public ObjectSet<Project> dependencies()
                {
                    return ObjectSet.objectSet(projects);
                }
            };
        }
    }

    /**
     * @return The non-switch argument at the given index parsed using the given argument parser
     */
    public <T> T argument(int index, ArgumentParser<T> parser)
    {
        return commandLine.argument(index, parser);
    }

    /**
     * @return The first non-switch argument parsed using the given argument parser
     */
    public <T> T argument(ArgumentParser<T> parser)
    {
        return commandLine.argument(parser);
    }

    /**
     * @return All non-switch command line arguments
     */
    public ArgumentList argumentList()
    {
        return commandLine.arguments();
    }

    /**
     * @return A list of parsed arguments
     */
    public <T> ObjectList<T> arguments(ArgumentParser<T> parser)
    {
        var arguments = new ObjectList<T>();
        for (int i = 0; i < argumentList().size(); i++)
        {
            if (parser.canParse(argumentList().get(i).value()))
            {
                arguments.add(argument(i, parser));
            }
        }
        return arguments;
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
    public String commandLineDescription(String title)
    {
        var box = new StringList();
        int number = 1;

        if (!argumentList().isEmpty())
        {
            box.add("");
            box.add("Arguments:");
            box.add("");
            for (var argument : argumentList())
            {
                box.add(AsciiArt.repeat(4, ' ') + "$. $", number++, argument.value());
            }
        }
        if (!internalSwitchParsers().isEmpty())
        {
            box.add("");
            box.add("Switches:");
            box.add("");
            var sorted = new ArrayList<>(internalSwitchParsers());
            sorted.sort(Comparator.comparing(SwitchParser::name));
            var width = new StringList(sorted).longest().asInt();
            for (var switchParser : sorted)
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
        return "No description available";
    }

    /**
     * Exits the application with the given message formatted by {@link Message#format(String, Object...)}
     *
     * @param message The message
     * @param arguments Arguments to interpolate into the message
     */
    public void exit(String message, Object... arguments)
    {
        commandLine.exit(message, arguments);
    }

    /**
     * @return The value for the command line switch parsed by the given switch parser, if any
     */

    public <T> T get(SwitchParser<T> parser)
    {
        return commandLine.get(parser);
    }

    /**
     * @return The value for the command line switch parsed by given switch parser or the default value if the switch
     * does not exist
     */
    public <T> T get(SwitchParser<T> parser, T defaultValue)
    {
        return commandLine.get(parser, defaultValue);
    }

    /**
     * @return True if this application has a value for the command line switch parsed by the given parser
     */
    public <T> boolean has(SwitchParser<T> parser)
    {
        return commandLine.has(parser);
    }

    @UmlRelation(label = "identified by")
    public Identifier identifier()
    {
        return new Identifier(Classes.simpleName(getClass()));
    }

    public PropertyMap localizedProperties(Locale locale)
    {
        return PropertyMap.localized(this, packagePath(), locale);
    }

    public Project project()
    {
        if (project == null)
        {
            project = ensureNotNull(newProject());
        }
        return project;
    }

    public PropertyMap properties()
    {
        return PropertyMap.propertyMap(project().properties());
    }

    public void ready()
    {
        state.transitionTo(READY);
    }

    /**
     * Runs the application by calling {@link #onRun()} given the arguments from the Java main(String[]) application
     * entrypoint. Operations occur in this order:
     * <ol>
     *     <li>{@link #onRunning()} is called to indicate that running is about to start</li>
     *     <li>Command line arguments are validated and parsed into a {@link CommandLine}</li>
     *     <li>{@link #onConfigureListeners()} is called to allow redirection of output</li>
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
    public final void run(String[] arguments)
    {
        state.transitionTo(INITIALIZING);

        // Signal that we're about to start running,
        onRunning();

        // set up temporary listener,
        LOGGER.listenTo(this);

        // load deployments,
        deployments = DeploymentSet.load(this, getClass());

        // then through arguments
        var argumentList = new StringList();
        for (var argument : arguments)
        {
            // and if the argument is -switches=[resource]
            if (argument.startsWith("-switches="))
            {
                // then load properties from the resource
                var resourceIdentifier = Strip.leading(argument, "-switches=");
                var resource = Resource.resolve(this, resourceIdentifier);
                var properties = PropertyMap.load(this, resource);

                // and add those properties to the argument list
                for (var key : properties.keySet())
                {
                    var value = properties.get(key);
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
                .addSwitchParsers(internalSwitchParsers())
                .addArgumentParsers(argumentParsers())
                .parse(argumentList.asStringArray());

        // Remove temporary logger and allow subclass to configure output streams,
        clearListeners();
        onConfigureListeners();

        // and if a deployment was specified,
        if (!ignoreDeployments() && !deployments.isEmpty() && has(DEPLOYMENT))
        {
            // install it in the global settings registry.
            get(DEPLOYMENT).install();
        }

        // Initialize this application's project
        onProjectInitializing();
        project.initialize();
        onProjectInitialized();

        announce("Project: $", project.name());
        announce("Application: " + name());

        try
        {
            // Run the application's code
            state.transitionTo(RUNNING);
            onRun();
            state.transitionTo(STOPPING);
        }
        catch (Exception e)
        {
            problem(e, "Application $ failed with exception", name());
        }

        BaseLog.logs().forEach(BaseLog::flush);

        for (var log : BaseLog.logs())
        {
            if (log.messageCounts().size() > 0)
            {
                information(AsciiArt.textBox(log.name() + " Log Messages", "$", log.messageCounts().toString("\n")));
            }
        }

        onRan();
        state.transitionTo(STOPPED);
    }

    @UmlExcludeMember
    public void showCommandLine()
    {
        announce(commandLineDescription(name()));
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
     * Waits until the {@link #ready()} method is called in the {@link #onRun()} implementation to indicate that the
     * application is fully ready. If {@link #ready()} is not called, this method will never return.
     */
    public void waitForReady()
    {
        state.waitFor(READY);
    }

    /**
     * @return The argument parsers for this application
     */
    protected List<ArgumentParser<?>> argumentParsers()
    {
        return Collections.emptyList();
    }

    protected boolean ignoreDeployments()
    {
        return false;
    }

    /**
     * If a project is <i>not</i> passed to the constructor, then this method can be overridden to provide a {@link
     * Project} dynamically.
     *
     * @return The {@link Project} for this application
     */
    protected Project newProject()
    {
        return null;
    }

    /**
     * Configures output of the application
     */
    @UmlExcludeMember
    protected void onConfigureListeners()
    {
        configureLogging();
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
     * @return The switch parsers for this application
     */
    protected ObjectSet<SwitchParser<?>> switchParsers()
    {
        return ObjectSet.objectSet();
    }

    /**
     * Sets output for the application to go to the application logger. This is the same in an application as
     * LOGGER.listenTo(this), but this method adds a filter which reduces output to a minimum if the -quiet=true switch
     * is set (the switch must be added to the return value of {@link #switchParsers()}).
     */
    @UmlExcludeMember
    private void configureLogging()
    {
        var filter = get(QUIET)
                ? new SeverityGreaterThanOrEqualTo(new Glitch().severity())
                : new AllMessages();

        LOGGER.listenTo(this, filter);
    }

    private Set<SwitchParser<?>> internalSwitchParsers()
    {
        var parsers = new HashSet<SwitchParser<?>>();

        if (!ignoreDeployments() && !deployments.isEmpty() && DEPLOYMENT == null)
        {
            DEPLOYMENT = deployments
                    .switchParser("deployment")
                    .quantifier(deployments.isEmpty() ? Quantifier.OPTIONAL : Quantifier.REQUIRED)
                    .build();

            parsers.add(DEPLOYMENT);
        }

        parsers.add(QUIET);
        parsers.addAll(switchParsers());

        return parsers;
    }
}
