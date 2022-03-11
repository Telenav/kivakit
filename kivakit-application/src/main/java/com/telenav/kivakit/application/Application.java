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

import com.telenav.kivakit.application.project.lexakai.DiagramApplication;
import com.telenav.kivakit.commandline.ApplicationMetadata;
import com.telenav.kivakit.commandline.ArgumentList;
import com.telenav.kivakit.commandline.ArgumentParser;
import com.telenav.kivakit.commandline.CommandLine;
import com.telenav.kivakit.commandline.CommandLineParser;
import com.telenav.kivakit.commandline.Quantifier;
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.component.Component;
import com.telenav.kivakit.core.KivaKit;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.language.Classes;
import com.telenav.kivakit.core.language.trait.LanguageTrait;
import com.telenav.kivakit.core.locale.Locale;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.LoggerFactory;
import com.telenav.kivakit.core.logging.logs.BaseLog;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.messaging.filters.AllMessages;
import com.telenav.kivakit.core.messaging.filters.SeverityGreaterThanOrEqualTo;
import com.telenav.kivakit.core.messaging.messages.status.Announcement;
import com.telenav.kivakit.core.messaging.messages.status.Glitch;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.project.Project;
import com.telenav.kivakit.core.registry.Registry;
import com.telenav.kivakit.core.registry.RegistryTrait;
import com.telenav.kivakit.core.string.Align;
import com.telenav.kivakit.core.string.AsciiArt;
import com.telenav.kivakit.core.string.Formatter;
import com.telenav.kivakit.core.string.Strip;
import com.telenav.kivakit.core.thread.StateMachine;
import com.telenav.kivakit.core.value.identifier.StringIdentifier;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.core.vm.ShutdownHook;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.interfaces.naming.NamedObject;
import com.telenav.kivakit.resource.PackageTrait;
import com.telenav.kivakit.resource.PropertyMap;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.settings.Deployment;
import com.telenav.kivakit.settings.DeploymentSet;
import com.telenav.kivakit.settings.SettingsTrait;
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
import static com.telenav.kivakit.commandline.SwitchParsers.booleanSwitchParser;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;

/**
 * Base class for KivaKit applications.
 *
 * <p>
 * This class provides:
 * </p>
 *
 * <ul>
 *     <li>Project Initialization - Initializes {@link Project}s used by the application</li>
 *     <li>Application Metadata - Provides information about the application and its environment</li>
 *     <li>Application Environment - Provides information about the execution environment</li>
 *     <li>Application Execution - Runs the application</li>
 *     <li>Command Line Parsing - Parses command line switches and arguments</li>
 *     <li>Messaging and Logging - Captures and logs {@link Message}s broadcast by child components</li>
 * </ul>
 *
 * <p>
 * {@link Application} is also a {@link Component} and so it inherits:
 * </p>
 *
 * <ul>
 *     <li>{@link PackageTrait} - Provides access to packages and packaged resources</li>
 *     <li>{@link SettingsTrait} - Loads settings objects and deployment configurations</li>
 *     <li>{@link RegistryTrait} - Service {@link Registry} access</li>
 *     <li>{@link LanguageTrait} - Enhancements that reduce language verbosity</li>
 *     <li>{@link Repeater} - Message broadcasting, listening and repeating</li>
 *     <li>{@link NamedObject} - Provides component name</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Creating an Application</b></p>
 *
 * <p>
 * An {@link Application} subclass should be constructed in the <i>main(String[])</i> Java application
 * entrypoint and the {@link #run(String[])} method should be called on it. This can be done in one step:
 *
 * <pre>
 * public class MyApplication extends Application
 * {
 *     public static void main(String[] arguments)
 *     {
 *         new MyApplication().run(arguments);
 *     }
 *
 *     [...]
 * }</pre>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Project Initialization</b></p>
 *
 * <p>
 * Application constructors should pass one or more {@link Project}s to the {@link Application} constructor to
 * ensure that all of the application's transitively dependent project(s) are initialized. See {@link Project} for details.
 * </p>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Application Metadata</b></p>
 *
 * <p>
 * Applications provide {@link ApplicationMetadata} about themselves:
 * </p>
 *
 * <ul>
 *     <li>{@link #version()} - The version of this application</li>
 *     <li>{@link #description()} - A description of this application</li>
 *     <li>{@link #identifier()} - A unique identifier for the application class</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Application Environment</b></p>
 *
 * <p>
 * Applications can access information about their execution environment:
 * </p>
 *
 * <ul>
 *     <li>{@link #properties()} - System and environment properties</li>
 *     <li>{@link #localizedProperties(Locale)} - Properties that are specific to the {@link Locale}</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Execution</b></p>
 *
 * <p><i>Lifecycle</i></p>
 *
 * <p>
 * The {@link #run(String[])} method performs the following steps:
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
 *     <li>Call {@link #onRunning()}</li>
 *     <li>Call {@link #onRun()}, executing the application</li>
 *     <li>Call {@link #onRan()}</li>
 *     <li>Exit</li>
 * </ol>
 *
 * <p><i>Abnormal Termination</i></p>
 *
 * <p>
 * If an application wishes to terminate its execution abnormally, it can call {@link #exit(String, Object...)}. This will
 * display the given message and show command line usage before exiting the application.
 * </p>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Command Line Parsing</b></p>
 *
 * <p>
 * Within the {@link #onRun()} method, the {@link Application} class provides convenient access to a model of the command line:
 * </p>
 *
 * <p><i>Command Line</i></p>
 *
 * <ul>
 *     <li>{@link #commandLine()} - Gets the parsed command line</li>
 *     <li>{@link #commandLineDescription(String)} - Returns a text box describing the command line with the given title</li>
 *     <li>{@link #showCommandLine()} - Broadcasts the command line description as an {@link Announcement} message</li>
 * </ul>
 *
 * <p><i>Switches</i></p>
 *
 * <ul>
 *     <li>{@link #get(SwitchParser)} - Gets the switch value for the given switch parser</li>
 *     <li>{@link #has(SwitchParser)} - Determines if there is a switch value for the given switch parser</li>
 * </ul>
 *
 * <p><i>Arguments</i></p>
 *
 * </ul>
 *     <li>{@link #argumentList()} - Gets command line arguments (excluding switches)</li>
 *     <li>{@link #argument(ArgumentParser)} - Gets the first command line argument (excluding switches)</li>
 *     <li>{@link #argument(int, ArgumentParser)} - Gets the nth argument using the given argument parser</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Messaging and Logging</b></p>
 *
 * <p>
 * This class extends {@link BaseRepeater} and has a {@link Logger} that listens for messages and logs them.
 * </p>
 *
 * <p><br/><hr/><br/></p>
 *
 * @author jonathanl (shibo)
 * @see BaseRepeater
 * @see CommandLine
 * @see SwitchParser
 * @see ArgumentParser
 */
@UmlClassDiagram(diagram = DiagramApplication.class)
@LexakaiJavadoc(complete = true)
public abstract class Application extends BaseComponent implements
        Named,
        ApplicationMetadata
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

    /** Switch parser to specify deployment settings */
    private SwitchParser<Deployment> DEPLOYMENT;

    /** The parsed command line for this application */
    @UmlAggregation
    private CommandLine commandLine;

    /** Set of deployments for the application, if any */
    private DeploymentSet deployments;

    /** The project that this application uses */
    @UmlAggregation(label = "initializes and uses")
    private Project project;

    private final StateMachine<State> state = new StateMachine<>(CREATED);

    @UmlExcludeMember
    protected final SwitchParser<Boolean> QUIET =
            booleanSwitchParser(this, "quiet", "Minimize output")
                    .optional()
                    .defaultValue(false)
                    .build();

    /**
     * @param projects One or more projects to initialize
     */
    protected Application(Project... projects)
    {
        register(this);
        register(LOGGER);

        instance = this;

        if (projects.length == 1)
        {
            project = listenTo(ensureNotNull(projects[0]));
        }
        else if (projects.length > 1)
        {
            project = listenTo(new Project()
            {
                @Override
                public ObjectSet<Project> dependencies()
                {
                    return ObjectSet.objectSet(projects);
                }
            });
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
                var value = get(switchParser);
                if (value instanceof Folder)
                {
                    value = ((Folder) value).path().asContraction(80);
                }
                box.add("   $ = $", Align.right(switchParser.name(), width, ' '),
                        value == null ? "N/A" : value);
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
     * Exits the application with the given message formatted by {@link Formatter#format(String, Object...)}
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
            registerSettingsIn(get(DEPLOYMENT));
        }

        // Initialize this application's project
        onProjectInitializing();
        project.initialize();
        onProjectInitialized();

        announce("Project: $", project.name());
        announce("Application: " + name() + " (" + KivaKit.get().projectVersion() + ")");

        try
        {
            // Run the application's code
            state.transitionTo(RUNNING);
            onRun();
        }
        catch (Exception e)
        {
            problem(e, "Application $ failed with exception", name());
        }
        finally
        {
            state.transitionTo(STOPPING);
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
     * Called after the application has run and just before shutdown. For system hooks, see {@link ShutdownHook}.
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
