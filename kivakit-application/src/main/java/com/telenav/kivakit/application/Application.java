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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.application.internal.lexakai.DiagramApplication;
import com.telenav.kivakit.commandline.ApplicationMetadataTrait;
import com.telenav.kivakit.commandline.ArgumentParser;
import com.telenav.kivakit.commandline.ArgumentValueList;
import com.telenav.kivakit.commandline.CommandLine;
import com.telenav.kivakit.commandline.CommandLineParser;
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.component.Component;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.list.Stack;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.collections.set.IdentitySet;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.function.Result;
import com.telenav.kivakit.core.language.trait.TryTrait;
import com.telenav.kivakit.core.locale.Locale;
import com.telenav.kivakit.core.locale.LocaleLanguage;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.logs.BaseLog;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.messaging.filters.AllMessages;
import com.telenav.kivakit.core.messaging.filters.MessagesWithSeverityOf;
import com.telenav.kivakit.core.messaging.messages.status.Announcement;
import com.telenav.kivakit.core.messaging.messages.status.Glitch;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.project.Build;
import com.telenav.kivakit.core.project.Project;
import com.telenav.kivakit.core.project.ProjectTrait;
import com.telenav.kivakit.core.project.StartUpOptions;
import com.telenav.kivakit.core.project.StartUpOptions.StartupOption;
import com.telenav.kivakit.core.registry.Register;
import com.telenav.kivakit.core.registry.Registry;
import com.telenav.kivakit.core.registry.RegistryTrait;
import com.telenav.kivakit.core.string.Formatter;
import com.telenav.kivakit.core.thread.StateMachine;
import com.telenav.kivakit.core.value.identifier.StringIdentifier;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.core.vm.ShutdownHook;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.interfaces.code.Code;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.properties.PropertyMap;
import com.telenav.kivakit.resource.packages.PackageTrait;
import com.telenav.kivakit.resource.serialization.ObjectSerializerRegistry;
import com.telenav.kivakit.serialization.gson.GsonObjectSerializer;
import com.telenav.kivakit.serialization.gson.KivaKitCoreGsonFactory;
import com.telenav.kivakit.serialization.properties.PropertiesObjectSerializer;
import com.telenav.kivakit.settings.Deployment;
import com.telenav.kivakit.settings.DeploymentSet;
import com.telenav.kivakit.settings.SettingsTrait;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.application.Application.InvocationScope.INTERNAL_SCOPE;
import static com.telenav.kivakit.application.Application.InvocationScope.ON_ADD_PROJECTS;
import static com.telenav.kivakit.application.Application.InvocationScope.ON_COMMAND_LINE_PARSED;
import static com.telenav.kivakit.application.Application.InvocationScope.ON_INITIALIZE;
import static com.telenav.kivakit.application.Application.InvocationScope.ON_INITIALIZED;
import static com.telenav.kivakit.application.Application.InvocationScope.ON_INITIALIZING;
import static com.telenav.kivakit.application.Application.InvocationScope.ON_PROJECTS_INITIALIZE;
import static com.telenav.kivakit.application.Application.InvocationScope.ON_PROJECTS_INITIALIZED;
import static com.telenav.kivakit.application.Application.InvocationScope.ON_PROJECTS_INITIALIZING;
import static com.telenav.kivakit.application.Application.InvocationScope.ON_RAN;
import static com.telenav.kivakit.application.Application.InvocationScope.ON_RUN;
import static com.telenav.kivakit.application.Application.InvocationScope.ON_RUNNING;
import static com.telenav.kivakit.application.Application.InvocationScope.ON_SERIALIZATION_INITIALIZE;
import static com.telenav.kivakit.application.ApplicationExit.EXIT_SUCCESS;
import static com.telenav.kivakit.application.ExitCode.FAILED;
import static com.telenav.kivakit.application.ExitCode.SUCCEEDED;
import static com.telenav.kivakit.application.LifecyclePhase.CONSTRUCTING;
import static com.telenav.kivakit.application.LifecyclePhase.INITIALIZING;
import static com.telenav.kivakit.application.LifecyclePhase.READY;
import static com.telenav.kivakit.application.LifecyclePhase.RUNNING;
import static com.telenav.kivakit.application.LifecyclePhase.STOPPED;
import static com.telenav.kivakit.application.LifecyclePhase.STOPPING;
import static com.telenav.kivakit.commandline.Quantifier.OPTIONAL;
import static com.telenav.kivakit.commandline.Quantifier.REQUIRED;
import static com.telenav.kivakit.commandline.SwitchParsers.booleanSwitchParser;
import static com.telenav.kivakit.core.KivaKit.globalListener;
import static com.telenav.kivakit.core.KivaKit.globalLogger;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.set.ObjectSet.set;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.function.Result.failure;
import static com.telenav.kivakit.core.function.Result.result;
import static com.telenav.kivakit.core.language.Classes.newInstance;
import static com.telenav.kivakit.core.language.Classes.simpleName;
import static com.telenav.kivakit.core.logging.LoggerFactory.newLogger;
import static com.telenav.kivakit.core.logging.logs.BaseLog.logs;
import static com.telenav.kivakit.core.project.Project.resolveProject;
import static com.telenav.kivakit.core.project.StartUpOptions.isStartupOptionEnabled;
import static com.telenav.kivakit.core.string.Align.rightAlign;
import static com.telenav.kivakit.core.string.AsciiArt.TextBoxStyle.OPEN;
import static com.telenav.kivakit.core.string.AsciiArt.repeat;
import static com.telenav.kivakit.core.string.AsciiArt.textBox;
import static com.telenav.kivakit.core.string.Strip.stripLeading;
import static com.telenav.kivakit.core.vm.Properties.allProperties;
import static com.telenav.kivakit.properties.PropertyMap.loadLocalizedPropertyMap;
import static com.telenav.kivakit.properties.PropertyMap.loadPropertyMap;
import static com.telenav.kivakit.properties.PropertyMap.propertyMap;
import static com.telenav.kivakit.resource.Extension.JSON;
import static com.telenav.kivakit.resource.Extension.PROPERTIES;
import static com.telenav.kivakit.resource.Resource.resolveResource;
import static com.telenav.kivakit.settings.DeploymentSet.loadDeploymentSet;
import static java.util.Comparator.comparing;

/**
 * Base class for KivaKit applications.
 *
 * <p>
 * This class provides:
 * </p>
 *
 * <ul>
 *     <li>Project Initialization - Initializes {@link Project}s used by the application. Project initialization
 *        allows other KivaKit projects to perform tasks that are required for their use, such as loading resources,
 *        or registering objects like serializers.</li>
 *     <li>Application Metadata - Application metadata provides information about the application and its environment</li>
 *     <li>Application Environment - Provides information about the execution environment</li>
 *     <li>Application Execution - Runs the application</li>
 *     <li>Command Line Parsing - Parses command line switches and arguments</li>
 *     <li>Messaging and Logging - Captures and logs {@link Message}s broadcast by child components</li>
 * </ul>
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
 *         run(MyApplication.class, arguments);
 *     }
 *
 *     [...]
 * }</pre>
 *
 * <p><b>Inherited Traits</b></p>
 *
 * <p>
 * {@link Application} inherits several traits, some from of which are inherited indirectly from {@link Component}:
 * </p>
 *
 * <ul>
 *     <li>{@link ApplicationMetadataTrait} - Provides a description of the application</li>
 *     <li>{@link Named} - Provides the application name</li>
 *     <li>{@link PackageTrait} - Provides access to packages and packaged resources</li>
 *     <li>{@link ProjectTrait} - Provides project lookup and KivaKit's version</li>
 *     <li>{@link RegistryTrait} - Provides convenient access to the object {@link Registry}</li>
 *     <li>{@link Repeater} - Provides message broadcasting, listening and repeating</li>
 *     <li>{@link SettingsTrait} - Loads settings objects and deployment configurations</li>
 *     <li>{@link TryTrait} - Provides convenient methods for implementing try/catch/finally blocks</li>
 * </ul>
 *
 * <p><b>Project Initialization</b></p>
 *
 * <p>
 * Applications can register one or more {@link Project}s in {@link #onProjectsInitialize()} by calling {@link #addProject(Class)}.
 * KivaKit will ensure that all of the application's transitively dependent project(s) are initialized. See {@link Project} for details.
 * </p>
 *
 * <p><b>Application Metadata</b></p>
 *
 * <p>
 * Applications provide {@link ApplicationMetadataTrait} about themselves:
 * </p>
 *
 * <ul>
 *     <li>{@link #version()} - The version of this application</li>
 *     <li>{@link #description()} - A description of this application</li>
 *     <li>{@link #identifier()} - A unique identifier for the application class</li>
 * </ul>
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
 * <p><b>Execution</b></p>
 *
 * <p><i>Lifecycle</i></p>
 *
 * <p>
 * The KivaKit application lifecycle is described in this diagram and the text below:
 * </p>
 *
 * <img src="https://telenav.github.io/telenav-assets/images/diagrams/application-lifecycle@2x.png"/>
 *
 * <p><b>CONSTRUCTING</p></b>
 *
 * <ol>
 *     <li>The method <b>main(String[] arguments)</b> is called</li>
 *     <li>The application enters the <b>CONSTRUCTING</b> phase</li>
 *     <li>The application is constructed</li>
 *     <li>The <b>run(String[])</b> method is called to execute the application</li>
 * </ol>
 *
 * <p><b>INITIALIZING</p></b>
 *
 * <ol>
 *     <li>The application enters the <b>INITIALIZING</b> phase</li>
 *     <li><b>onInitializing()</b> is called</li>
 *     <li><b>startupOptions()</b> is called and the set of <b>StartupOptions</b> returned is enabled</li>
 *     <li><b>onSerializationInitialize()</b> is called to allow registration of serializers</li>
 *     <li>Any <b>Deployment</b> configurations are loaded from the <b>deployments</b> package</li>
 *     <li>The command line arguments passed to <b>main()</b> are parsed into a <b>CommandLine</b> using
 * switch and argument parsers returned by <b>switchParsers()</b> and <b>argumentParsers()</b></li>
 *     <li>If a <b>-deployment</b> was specified, it is loaded and registered with the global settings registry</li>
 *     <li><b>onInitialize()</b> is called to initialize the application</li>
 *     <li><b>onProjectsInitializing()</b> is called to notify that projects will be initializing</li>
 *     <li><b>onProjectsInitialize</b> is called</li>
 *     <li>The default implementation of <b>onProjectsInitialize()</b> initializes projects</li>
 *     <li><b>onProjectsInitialized()</b> is called</li>
 *     <li><b>onInitialized()</b> is called to notify that initialization is complete</li>
 * </ol>
 *
 * <p><b>RUNNING</p></b>
 *
 * <ol>
 *     <li>The application enters the <b>RUNNING</b> phase</li>
 *     <li><b>onRunning()</b> is called to notify that the application is about to run</li>
 * </ol>
 *
 * <p><b>READY</p></b>
 *
 * <ol>
 *     <li>The application enters the <b>READY</b> phase</li>
 *     <li><b>onRun()</b> is called to run the application</li>
 * </ol>
 *
 * <p><b>STOPPING</p></b>
 *
 * <ol>
 *     <li>The application enters the <b>STOPPING</b> phase</li>
 *     <li>All logs are flushed</li>
 *     <li>Message statistics are displayed (how many glitches, warnings, problems, etc.)</li>
 *     <li><b>onRan()</b> is called</li>
 * </ol>
 *
 * <p><b>STOPPED</p></b>
 *
 * <ol>
 *     <li>The application transitions to the <b>STOPPED</b> state</li>
 *     <li>The application exits. If no exception was thrown and exit wasn't called, it exits with status code 0 (no error), otherwise it exits with a non-zero status code.</li>
 * </ol>
 *
 * <p><b>Abnormal Termination</b></p>
 *
 * <p>
 * If an application wishes to terminate its execution abnormally, it can call {@link #exit(String, Object...)}. This will
 * display the given message and show command line usage before exiting the application.
 * </p>
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
 *     <li>{@link #startupInformation(String)} - Returns a text box describing the command line with the given title</li>
 *     <li>{@link #showStartupInformation()} - Broadcasts the command line description as an {@link Announcement} message</li>
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
 * <ul>
 *     <li>{@link #argumentList()} - Gets command line arguments (excluding switches)</li>
 *     <li>{@link #argument(ArgumentParser)} - Gets the first command line argument (excluding switches)</li>
 *     <li>{@link #argument(int, ArgumentParser)} - Gets the nth argument using the given argument parser</li>
 * </ul>
 *
 * <p><b>Messaging and Logging</b></p>
 *
 * <p>
 * This class extends {@link BaseRepeater} and has a {@link Logger} that listens for messages and logs them.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see BaseRepeater
 * @see CommandLine
 * @see SwitchParser
 * @see ArgumentParser
 */
@SuppressWarnings({ "unused", "BooleanMethodIsAlwaysInverted", "SameParameterValue" })
@UmlClassDiagram(diagram = DiagramApplication.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
@Register
public abstract class Application extends BaseComponent implements
    Named,
    PackageTrait,
    ProjectTrait,
    SettingsTrait,
    ApplicationMetadataTrait,
    TryTrait
{
    /** The one and only application running in this process */
    private static Application application;

    /**
     * Returns the currently running application
     */
    public static Application application()
    {
        return application;
    }

    /**
     * Executes the given application with the given arguments
     *
     * @param applicationType The type of the application
     * @param arguments The arguments to pass to the application
     * @param <T> The application type
     * @return The result of running the application
     */
    public static <T extends Application> Result<ApplicationExit> run(Class<T> applicationType, String[] arguments)
    {
        globalLogger(newLogger());
        return run(globalListener(), applicationType, arguments);
    }

    /**
     * Constructs the given application instance, attaches the given listener to it, and runs it with the given
     * arguments.
     *
     * @param listener The application listener
     * @param applicationType The class of the application
     * @param arguments The application arguments
     * @param <T> The application type
     * @return The result of running the application
     */
    public static <T extends Application> Result<ApplicationExit> run(Listener listener,
                                                                      Class<T> applicationType,
                                                                      String[] arguments)
    {
        ensureNotNull(listener);
        ensureNotNull(applicationType);
        ensureNotNull(arguments);

        try
        {
            var application = newInstance(applicationType);
            listener.listenTo(application);
            return application.run(arguments);
        }
        catch (Exception e)
        {
            return failure(new ApplicationExit(FAILED, e), "Application failed");
        }
    }

    /**
     * These enum values are used to restrict user calls to a particular code scope. For example, the
     * {@link #addProject(Class)} method can only be called from {@link #onAddProjects()}.
     */
    protected enum InvocationScope
    {
        INTERNAL_SCOPE,
        ON_ADD_PROJECTS,
        ON_COMMAND_LINE_PARSED,
        ON_CONFIGURE_LISTENERS,
        ON_INITIALIZE,
        ON_INITIALIZED,
        ON_INITIALIZING,
        ON_PROJECTS_INITIALIZE,
        ON_PROJECTS_INITIALIZED,
        ON_PROJECTS_INITIALIZING,
        ON_RAN,
        ON_REGISTER_OBJECT_SERIALIZERS,
        ON_RUN,
        ON_RUNNING,
        ON_SERIALIZATION_INITIALIZE
    }

    /**
     * A unique string identifier for a KivaKit {@link Application}.
     *
     * @author jonathanl (shibo)
     */
    @UmlClassDiagram(diagram = DiagramApplication.class)
    @UmlExcludeSuperTypes
    @TypeQuality(stability = STABLE,
                 testing = UNTESTED,
                 documentation = DOCUMENTED)
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
    private final Set<Project> projects = new IdentitySet<>();

    /** CONSTRUCTING - 2. State machine for application lifecycle */
    private final StateMachine<LifecyclePhase> phase = new StateMachine<>(CONSTRUCTING);

    @UmlExcludeMember
    protected final SwitchParser<Boolean> QUIET =
        booleanSwitchParser(this, "quiet", "Minimize output")
            .optional()
            .defaultValue(false)
            .build();

    /** Stack of invocation scopes. The top of stack is the current code scope. */
    private final Stack<InvocationScope> scopeStack = new Stack<>();

    /** The set of scopes that have been invoked */
    private final ObjectSet<InvocationScope> invokedScopes = set();

    protected Application()
    {
        scopeStack.push(INTERNAL_SCOPE);

        // CONSTRUCTING - 3. Create application
        register(this);
        register(globalLogger());

        application = this;
    }

    /**
     * Adds the given {@link Project} based on its class. Projects should be added in the application constructor. This
     * ensures that project objects are singletons.
     *
     * @param project The {@link Project} class
     */
    public Application addProject(Class<? extends Project> project)
    {
        ensureInvoked(set(ON_INITIALIZE, INTERNAL_SCOPE), "addProject(project)");

        projects.add(resolveProject(project));
        return this;
    }

    /**
     * Returns the non-switch argument at the given index parsed using the given argument parser
     */
    public <T> T argument(int index, ArgumentParser<T> parser)
    {
        ensureInvokedAfter(ON_COMMAND_LINE_PARSED, "argument(index, parser)");

        return commandLine().argument(index, parser);
    }

    /**
     * Returns the first non-switch argument parsed using the given argument parser
     */
    public <T> T argument(ArgumentParser<T> parser)
    {
        ensureInvokedAfter(ON_COMMAND_LINE_PARSED, "argument(parser)");

        return commandLine().argument(parser);
    }

    /**
     * Returns all non-switch command line arguments
     */
    public ArgumentValueList argumentList()
    {
        ensureInvokedAfter(ON_COMMAND_LINE_PARSED, "argumentList()");

        return commandLine().argumentValues();
    }

    /**
     * Returns a list of parsed arguments
     */
    public <T> ObjectList<T> arguments(ArgumentParser<T> parser)
    {
        ensureInvokedAfter(ON_COMMAND_LINE_PARSED, "arguments(parser)");

        var arguments = new ObjectList<T>();
        for (int i = 0; i < argumentList().size(); i++)
        {
            if (parser.canParse(argumentList().argumentValue(i).value()))
            {
                arguments.add(argument(i, parser));
            }
        }
        return arguments;
    }

    /**
     * Returns the application version as specified in the resource "/project.properties"
     */
    public Build build()
    {
        return Build.build(getClass());
    }

    /**
     * Returns the parsed command line
     */
    @UmlRelation(label = "parses arguments into")
    public CommandLine commandLine()
    {
        ensureInvokedAfter(ON_COMMAND_LINE_PARSED, "commandLine()");

        ensureNotNull(commandLine, "Cannot access command line during initialization");
        return commandLine;
    }

    /**
     * Returns a description of the application for use in help
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
        ensureInvokedAfter(ON_COMMAND_LINE_PARSED, "exit(message, arguments)");

        commandLine().exit(message, arguments);
    }

    /**
     * Returns the value for the command line switch parsed by the given switch parser, if any
     */

    public <T> T get(SwitchParser<T> parser)
    {
        ensureInvokedAfter(ON_COMMAND_LINE_PARSED, "get(parser)");

        return commandLine().get(ensureNotNull(parser));
    }

    /**
     * Returns the value for the command line switch parsed by given switch parser or the default value if the switch
     * does not exist
     */
    public <T> T get(SwitchParser<T> parser, T defaultValue)
    {
        ensureInvokedAfter(ON_COMMAND_LINE_PARSED, "get(parser, defaultValue)");

        return commandLine().get(ensureNotNull(parser), defaultValue);
    }

    /**
     * Returns true if this application has a value for the command line switch parsed by the given parser
     */
    public <T> boolean has(SwitchParser<T> parser)
    {
        ensureInvokedAfter(ON_COMMAND_LINE_PARSED, "has(parser)");

        return commandLine().has(ensureNotNull(parser));
    }

    @UmlRelation(label = "identified by")
    public Identifier identifier()
    {
        return new Identifier(simpleName(getClass()));
    }

    public PropertyMap localizedProperties(Locale locale)
    {
        return localizedProperties(locale, locale.primaryLanguage());
    }

    public PropertyMap localizedProperties(Locale locale, LocaleLanguage language)
    {
        return loadLocalizedPropertyMap(this, packageForThis().path(), locale, language);
    }

    /**
     * Parses the given command line arguments. Normally, this method does not need to be called, but it may be
     * necessary in certain tests to parse a command line without running the application.
     *
     * @param arguments The arguments to parse
     */
    public void parseCommandLine(String[] arguments)
    {
        var argumentList = new StringList();
        for (var argument : arguments)
        {
            // and if the argument is -switches=[resource]
            if (argument.startsWith("-switches="))
            {
                // then load properties from the resource
                var resourceIdentifier = stripLeading(argument, "-switches=");
                var resource = resolveResource(this, resourceIdentifier);
                var properties = loadPropertyMap(this, resource);

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

        // then parse the command line arguments,
        commandLine = new CommandLineParser(this)
            .addSwitchParsers(internalSwitchParsers())
            .addArgumentParsers(argumentParsers())
            .parse(argumentList.asStringArray());

        // and notify that the command line has been parsed.
        invoke(ON_COMMAND_LINE_PARSED, this::onCommandLineParsed);
    }

    /**
     * Returns the state
     *
     * @return The application lifecycle state
     */
    public LifecyclePhase phase()
    {
        return phase.at();
    }

    /**
     * Returns the set of projects on which this application depends
     *
     * @return The project dependencies
     */
    public Set<Project> projects()
    {
        ensureInvokedDuringOrAfter(ON_PROJECTS_INITIALIZE, "projects()");

        return projects;
    }

    /**
     * Returns the properties for this application's project
     */
    public PropertyMap properties()
    {
        return propertyMap(allProperties(getClass()));
    }

    /**
     * Transitions this application to the {@link LifecyclePhase#READY} state
     */
    public void ready()
    {
        phase.transitionTo(READY);
    }

    /**
     * Runs the application by calling {@link #onRun()} given the arguments from the Java main(String[]) application
     * entrypoint.
     *
     * @param arguments Command line arguments to parse
     */
    public final Result<ApplicationExit> run(String[] arguments)
    {
        // CONSTRUCTING - 4. Run

        var exitCode = FAILED;
        Exception exception = null;

        try
        {
            // If there is no listener,
            if (listeners().isEmpty())
            {
                // add a default listener
                globalLogger().listenTo(this);
            }

            // INITIALIZING - 1. Begin initializing
            phase.transitionTo(INITIALIZING);

            // INITIALIZING - 2. Notify that we are initializing
            invoke(ON_ADD_PROJECTS, this::onAddProjects);
            invoke(ON_INITIALIZING, this::onInitializing);

            // INITIALIZING - 3. Enable start-up options
            startupOptions().forEach(StartUpOptions::enableStartupOption);

            // INITIALIZING - 4. Register any object serializers,
            invoke(ON_SERIALIZATION_INITIALIZE, this::onSerializationInitialize);

            // INITIALIZING - 5. Load deployments,
            deployments = loadDeploymentSet(this, getClass());

            // INITIALIZING - 6. Parse command line
            parseCommandLine(arguments);

            // INITIALIZING - 7. If a deployment was specified, register its settings
            if (deploymentSpecified())
            {
                invoke(INTERNAL_SCOPE, () -> registerSettingsIn(get(DEPLOYMENT)));
            }

            // INITIALIZING - 8. Initialize the application,
            invoke(ON_INITIALIZE, this::onInitialize);

            // INITIALIZING - 9. Projects will be initialized
            invoke(ON_PROJECTS_INITIALIZING, this::onProjectsInitializing);

            // INITIALIZING - 10. Initialize projects
            invoke(ON_PROJECTS_INITIALIZE, this::onProjectsInitialize);

            // INITIALIZING - 12. Projects have been initialized
            invoke(ON_PROJECTS_INITIALIZED, this::onProjectsInitialized);

            // INITIALIZING - 13. Notify that we are done initializing
            invoke(ON_INITIALIZED, this::onInitialized);

            // RUNNING - 1. Transition to running phase
            phase.transitionTo(RUNNING);

            // RUNNING - 2. Notify that we are starting to run
            invoke(ON_RUNNING, this::onRunning);

            // RUNNING - 3. Remove temporary logger and allow subclass to configure output streams
            clearListeners();
            onConfigureListeners();

            // RUNNING - 4. Show startup information
            if (!isStartupOptionEnabled(StartupOption.QUIET))
            {
                invoke(INTERNAL_SCOPE, this::showStartupInformation);
            }

            // READY - 1. Transition to READY phase
            ready();

            try
            {
                // READY - 2. Run the application's code
                invoke(ON_RUN, this::onRun);
            }
            catch (Exception e)
            {
                problem(e, "Application.onRun() $ failed with exception", name());
                exception = e;
            }
            finally
            {
                // STOPPING - 1. Transition to stopping phase
                phase.transitionTo(STOPPING);
            }

            // STOPPING - 2. Flush logs
            logs().forEach(BaseLog::flush);

            // STOPPING - 3. Show message statistics
            for (var log : logs())
            {
                if (log.messageCounts().size() > 0)
                {
                    information(textBox(log.name() + " Log MessageTransceiver", "$", log.messageCounts().join("\n")));
                }
            }

            // STOPPING - 4. Notify that the application has run
            invoke(ON_RAN, this::onRan);

            // STOPPED - 1. Transition to stopped phase
            phase.transitionTo(STOPPED);
            exitCode = SUCCEEDED;
        }
        catch (Exception e)
        {
            problem(e, "Application $ failed with exception", name());
            exception = e;
        }

        // STOPPED - 2. Return the application exit code
        return exitCode == SUCCEEDED
            ? result(EXIT_SUCCESS)
            : failure(new ApplicationExit(exitCode, exception), "Application failed");
    }

    @UmlExcludeMember
    public void showStartupInformation()
    {
        ensureInvokedAfter(ON_RUNNING, "showStartupInformation()");

        announce(startupInformation(name()));
    }

    /**
     * Returns this command line in a text box intended for user feedback when starting an application
     */
    public String startupInformation(String title)
    {
        ensureInvokedAfter(ON_RUNNING, "startupInformation(title)");

        var box = new StringList();
        int number = 1;

        var deployment = deploymentSpecified() ? invoke(INTERNAL_SCOPE, () -> get(DEPLOYMENT)) : null;

        box.add(" ");
        box.add("    Version: $", version());
        box.add("      Build: $", build());
        if (deployment != null)
        {
            box.add(" Deployment: $ ($)", deployment.name(), deployment.description());
        }

        var arguments = invoke(INTERNAL_SCOPE, this::argumentList);
        if (!arguments.isEmpty())
        {
            box.add("");
            box.add("Arguments:");
            box.add("");
            for (var argument : arguments)
            {
                box.add(repeat(4, ' ') + "$. $", number++, argument.value());
            }
        }

        if (!internalSwitchParsers().isEmpty())
        {
            box.add("");
            box.add("Switches:");
            box.add("");
            var sorted = new ArrayList<>(internalSwitchParsers());
            sorted.sort(comparing(SwitchParser::name));
            var width = new StringList(sorted).longest().asInt();
            for (var switchParser : sorted)
            {
                var value = invoke(INTERNAL_SCOPE, () -> get(switchParser));
                if (value instanceof Folder)
                {
                    value = ((Folder) value).path().asContraction(80);
                }
                box.add("   $ = $", rightAlign(switchParser.name(), width, ' '),
                    value == null ? "N/A" : value);
            }
        }

        if (deploymentSpecified() && invoke(INTERNAL_SCOPE, () -> has(DEPLOYMENT)))
        {
            box.add(" ");
            box.add("Deployment Settings:");
            box.add(" ");
            box.addAll(invoke(INTERNAL_SCOPE, () -> get(DEPLOYMENT))
                .asStringList()
                .trim()
                .indented(4));
        }

        box.add(" ");
        box.addAll(registry().asStringList());

        return box.titledBox(OPEN, title);
    }

    /**
     * Returns the application version as specified in the resource "/project.properties"
     */
    @Override
    public Version version()
    {
        return properties().asVersion("project-version");
    }

    /**
     * Waits until the {@link #ready()} method is called in the {@link #onRun()} implementation to indicate that the
     * application is fully ready. If {@link #ready()} is not called, this method will never return.
     */
    public void waitForReady()
    {
        phase.waitFor(READY);
    }

    /**
     * Returns the argument parsers for this application
     */
    protected ObjectList<ArgumentParser<?>> argumentParsers()
    {
        return list();
    }

    /**
     * If this method returns true, the deployments switch is not included
     */
    protected boolean ignoreDeploymentSwitch()
    {
        return false;
    }

    /**
     * <b>Not public API</b>
     *
     * <p>
     * Invokes the given scoped code (typically an onXXX() method), pushing the given invocation scope onto the scope
     * stack before the call and popping the scope off the stack after the call.
     * </p>
     *
     * @param scope The identity of the scope being invoked
     * @param code The scoped code to invoke
     */
    protected <T> T invoke(InvocationScope scope, Code<T> code)
    {
        this.scopeStack.push(scope);
        try
        {
            return code.run();
        }
        finally
        {
            invokedScopes.add(scope);
            scopeStack.pop();
        }
    }

    /**
     * <b>Not public API</b>
     *
     * <p>
     * Invokes the given scoped code (typically an onXXX() method), pushing the given invocation scope onto the scope
     * stack before the call and popping the scope off the stack after the call.
     * </p>
     *
     * @param scope The identity of the scope being invoked
     * @param code The scoped code to invoke
     */
    protected void invoke(InvocationScope scope, Runnable code)
    {
        this.scopeStack.push(scope);
        try
        {
            code.run();
        }
        finally
        {
            invokedScopes.add(scope);
            scopeStack.pop();
        }
    }

    /**
     * This method can be overridden to provide a {@link Project} instance dynamically, instead of calling
     * {@link #addProject(Class)}.
     *
     * @return The {@link Project} for this application
     */
    protected Project newProject()
    {
        return null;
    }

    /**
     * Called when projects should be added
     */
    protected void onAddProjects()
    {
    }

    /**
     * Called when the command line has been parsed
     */
    protected void onCommandLineParsed()
    {
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
     * Called to initialize the application before running
     */
    protected void onInitialize()
    {
    }

    /**
     * Called when the application is initialized
     */
    protected void onInitialized()
    {
    }

    /**
     * Called before initializing the application
     */
    protected void onInitializing()
    {
    }

    /**
     * Initializes project dependencies
     */
    protected void onProjectsInitialize()
    {
        // INITIALIZING - 11. Initialize projects
        initializeProjects();
    }

    /**
     * Called after this application's project has been initialized
     */
    @UmlExcludeMember
    protected void onProjectsInitialized()
    {
    }

    /**
     * Called before this application's project is initialized
     */
    @UmlExcludeMember
    protected void onProjectsInitializing()
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
     * This method is called when {@link #onSerializationInitialize()} is called to register object serializers
     */
    protected void onRegisterObjectSerializers()
    {
        register(new KivaKitCoreGsonFactory());

        var serializers = new ObjectSerializerRegistry();
        tryCatch(() -> serializers.add(JSON, listenTo(new GsonObjectSerializer())), "Unable to register JSON serializer");
        tryCatch(() -> serializers.add(PROPERTIES, listenTo(new PropertiesObjectSerializer())), "Unable to register .properties file serializer");
        register(serializers);
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
     * Called to register object serializers
     */
    @MustBeInvokedByOverriders
    protected void onSerializationInitialize()
    {
        onRegisterObjectSerializers();
    }

    /**
     * Returns the set of startup options for this application
     */
    protected ObjectSet<StartupOption> startupOptions()
    {
        return set();
    }

    /**
     * Returns the switch parsers for this application
     */
    protected ObjectSet<SwitchParser<?>> switchParsers()
    {
        return set();
    }

    /**
     * Sets output for the application to go to the application logger. This is the same in an application as
     * LOGGER.listenTo(this), but this method adds a filter which reduces output to a minimum if the -quiet=true switch
     * is set (the switch must be added to the return value of {@link #switchParsers()}).
     */
    @UmlExcludeMember
    private void configureLogging()
    {
        var filter = invoke(INTERNAL_SCOPE, () -> get(QUIET))
            ? new MessagesWithSeverityOf(new Glitch().severity())
            : new AllMessages();

        globalLogger().listenTo(this, filter);
    }

    /**
     * Returns true if a deployment was specified on the command line
     */
    private boolean deploymentSpecified()
    {
        return invoke(INTERNAL_SCOPE, () -> !ignoreDeploymentSwitch() && has(DEPLOYMENT));
    }

    /**
     * Ensures that the caller is in one of the given scopes, or the internal scope
     *
     * @param scopes The allowed scopes
     * @param methodName The calling method
     */
    private void ensureInvoked(ObjectSet<InvocationScope> scopes, String methodName)
    {
        ensure(scopes.contains(scopeStack.top()) || isInInternalScope(), "Can only invoke $ in one of the following methods: $",
            methodName, scopes.without(INTERNAL_SCOPE));
    }

    /**
     * Ensures that the given scope has already been invoked
     *
     * @param scope The scope that must already have been invoked
     * @param methodName The calling method
     */
    private void ensureInvokedAfter(InvocationScope scope, String methodName)
    {
        ensure(isAfterScope(scope) || isInInternalScope(), "Can only invoke $ after $ has been invoked", methodName, scope);
    }

    /**
     * Ensures that the caller has already been through the given scope, or is still in it.
     *
     * @param scope The scope that must already have been passed through or which the caller is still in
     * @param methodName The calling method
     */
    private void ensureInvokedDuringOrAfter(InvocationScope scope, String methodName)
    {
        ensure(isAfterScope(scope) || (isInScope(scope) || isInInternalScope()),
            "Can only invoke $ during or after $ has been invoked", methodName, scope);
    }

    private void initializeProject(IdentitySet<Project> uninitialized, Project project)
    {
        // For each dependent project,
        for (var at : project.dependencies())
        {
            // initialize it,
            initializeProject(uninitialized, project(at));
        }

        // then initialize this project,
        listenTo(project).initialize();

        // and remove it from the uninitialized set.
        uninitialized.remove(project);
    }

    /**
     * Initialize all projects and their dependencies in depth-first order
     */
    private void initializeProjects()
    {
        // Start with all projects uninitialized,
        var uninitialized = new IdentitySet<Project>();
        var projects = invoke(INTERNAL_SCOPE, this::projects);
        uninitialized.addAll(projects);

        // then for each project,
        for (var project : projects)
        {
            // if it is uninitialized,
            if (uninitialized.contains(project))
            {
                // initialize it and its dependencies.
                initializeProject(uninitialized, project);
            }
        }
    }

    private Set<SwitchParser<?>> internalSwitchParsers()
    {
        var parsers = new HashSet<SwitchParser<?>>();

        if (!ignoreDeploymentSwitch() && DEPLOYMENT == null)
        {
            DEPLOYMENT = deployments
                .switchParser("deployment")
                .quantifier(deployments.isEmpty() ? OPTIONAL : REQUIRED)
                .build();

            parsers.add(DEPLOYMENT);
        }

        parsers.add(QUIET);
        parsers.addAll(switchParsers());

        return parsers;
    }

    /**
     * True if the given scope has already been invoked
     *
     * @param scope The scope
     * @return True if the scope has already been invoked
     */
    private boolean isAfterScope(InvocationScope scope)
    {
        return invokedScopes.contains(scope);
    }

    /**
     * Returns true if we are in the internal scope
     *
     * @return True if the current scope is internal
     */
    private boolean isInInternalScope()
    {
        return isInScope(INTERNAL_SCOPE);
    }

    /**
     * Returns true if the given scope is the current scope
     *
     * @param scope The scope
     * @return True if the caller is in the given scope
     */
    private boolean isInScope(InvocationScope scope)
    {
        return scope() == scope;
    }

    /**
     * Returns the current scope
     */
    private InvocationScope scope()
    {
        return this.scopeStack.top();
    }
}
