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

package com.telenav.kivakit.launcher;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.os.OperatingSystem;
import com.telenav.kivakit.core.os.Processes;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.thread.KivaKitThread;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.properties.PropertyMap;
import com.telenav.kivakit.resource.Extension;
import com.telenav.kivakit.resource.Resourceful;
import com.telenav.kivakit.resource.internal.lexakai.DiagramJarLauncher;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.launcher.JarLauncher.ProcessType.CHILD;
import static com.telenav.kivakit.launcher.JarLauncher.ProcessType.DETACHED;

/**
 * Launches an executable Java program from some, possibly remote, resource when {@link #run()}  is called.
 *
 * <p><b>Options</b></p>
 *
 * <ul>
 *     <li>{@link #addJarSource(Resourceful)} - Adds a place where a JAR can be accessed, either locally or remotely</li>
 *     <li>{@link #arguments(String...)} - Adds the given arguments to be passed to the new process</li>
 *     <li>{@link #arguments(StringList)} - Adds the given arguments to be passed to the new process</li>
 *     <li>{@link #enableDebuggerOnPort(int)} - Enables Java debugging on the given port</li>
 *     <li>{@link #headless(boolean)} - Launches the process in AWT headless mode, with no tray icon (even temporarily)</li>
 *     <li>{@link #processType(ProcessType)} - Specifies if the process should be a child process or detached</li>
 *     <li>{@link #redirectTo(RedirectTo)} - Specifies how output should be redirected</li>
 * </ul>
 *
 * <p><b>Example</b></p>
 * <p>
 * This example launches the executable JAR for PlantUML from a package resource as a child process with the given
 * arguments. It then waits for the process to complete.
 *
 * <pre>
 * var process = listenTo(new JarLauncher()
 *     .processType(CHILD)
 *     .arguments(arguments))
 *     .addJarSource(PackageResource.packageResource(getClass(), "plantuml.jar"))
 *     .redirectTo(CONSOLE)
 *     .run();
 *
 * Processes.waitFor(process);
 * </pre>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramJarLauncher.class)
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class JarLauncher extends BaseRepeater
{
    /**
     * The type of process to create
     *
     * @author jonathanl (shibo)
     */
    @UmlClassDiagram(diagram = DiagramJarLauncher.class)
    @CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
                 testing = TESTING_NOT_NEEDED,
                 documentation = DOCUMENTATION_COMPLETE)
    public enum ProcessType
    {
        DETACHED,
        CHILD
    }

    /**
     * The type of redirection for process output
     *
     * @author jonathanl (shibo)
     */
    @CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
                 testing = TESTING_NOT_NEEDED,
                 documentation = DOCUMENTATION_COMPLETE)
    public enum RedirectTo
    {
        FILE,
        CONSOLE
    }

    @UmlAggregation(label = "launches process of type")
    private ProcessType processType = CHILD;

    @UmlAggregation(label = "retrieves jar from one of")
    private final List<Resourceful> jarSources = new ArrayList<>();

    private StringList programArguments = new StringList();

    @UmlAggregation(label = "determines output redirection")
    private RedirectTo redirectTo = RedirectTo.FILE;

    private int debugPort = -1;

    private boolean headless = true;

    /**
     * Adds the given resourced object to this launcher as a potential place to load the JAR file from
     */
    public JarLauncher addJarSource(@NotNull Resourceful resourced)
    {
        jarSources.add(resourced);
        return this;
    }

    /**
     * @param arguments The arguments to pass to the launched Java program
     */
    public JarLauncher arguments(@NotNull String... arguments)
    {
        programArguments = StringList.stringList(Arrays.asList(arguments));
        return this;
    }

    /**
     * @param arguments The arguments to pass to the launched Java program
     */
    public JarLauncher arguments(@NotNull StringList arguments)
    {
        programArguments = arguments;
        return this;
    }

    /**
     * @param debugPort Enables Java debugging on this given port
     */
    public JarLauncher enableDebuggerOnPort(int debugPort)
    {
        this.debugPort = debugPort;
        return this;
    }

    /**
     * @param headless True if the process should run in AWT headless mode, meaning that AWT is not initialized and
     * there is no tray icon shown (even temporarily) for the process
     */
    public JarLauncher headless(boolean headless)
    {
        this.headless = headless;
        return this;
    }

    /**
     * @param type The type of process to launch
     */
    public JarLauncher processType(@NotNull ProcessType type)
    {
        processType = type;
        return this;
    }

    /**
     * @param redirectTo The type of output redirection to perform
     */
    public JarLauncher redirectTo(@NotNull RedirectTo redirectTo)
    {
        this.redirectTo = redirectTo;
        return this;
    }

    /**
     * Launches the JAR file as specified by the properties of this launcher
     *
     * @return The launched process
     */
    public Process run()
    {
        // Go through each possible jar source,
        for (var source : jarSources)
        {
            // get the resource and materialize it to the local host,
            var resource = source.resource().materialized(ProgressReporter.nullProgressReporter());
            try
            {
                // get the resource basename,
                var base = resource.fileName().withoutExtension(Extension.JAR);

                // and create the argument list.
                var java = OperatingSystem.operatingSystem().javaExecutable();
                var arguments = new StringList();
                arguments.add(java);
                if (headless)
                {
                    //noinspection SpellCheckingInspection
                    arguments.add("-Djava.awt.headless=true");
                }
                if (debugPort > 0)
                {
                    arguments.add("-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=" + debugPort);
                }
                arguments.add("-jar");
                arguments.add(resource.path().toString());
                arguments.addAll(programArguments);

                // If the process should be detached,
                var script = folder().file(base + ".sh");
                if (processType == DETACHED)
                {
                    // write the arguments to a shell script,
                    try (var out = script.printWriter())
                    {
                        out.println("#!/bin/bash");
                        out.println("");
                        out.println(arguments.join(" ") + " &");
                    }

                    // and mark it as executable.
                    script.chmod(File.ACCESS_775);
                }

                // Create a process builder for the script or arguments,
                var builder = new ProcessBuilder();
                if (processType == CHILD)
                {
                    trace("Executing $", arguments.join(" "));
                    builder.command(arguments);
                }
                if (processType == DETACHED)
                {
                    builder.command(script.toString());
                }

                // get this process' identifier
                var pid = OperatingSystem.operatingSystem().processIdentifier();

                // and launch the jar, redirecting output to
                var announcement = PropertyMap.propertyMap();
                announcement.put("jar", resource.path().toString());
                announcement.put("arguments", arguments.join(" "));
                switch (redirectTo)
                {
                    // the console
                    case CONSOLE:
                    {
                        announcement.put("stdout", "console");
                        announcement.put("stderr", "console");
                        announcement.asStringList().titledBox("Launching Jar");
                        var process = builder.start();
                        KivaKitThread.run(this, "RedirectOutputToConsole", () -> Processes.redirectStandardOutToConsole(this, process));
                        KivaKitThread.run(this, "RedirectErrorToConsole", () -> Processes.redirectStandardErrorToConsole(this, process));
                        return process;
                    }

                    // or to a file.
                    case FILE:
                    {
                        var stderr = folder().file(base + "-" + pid + "-stderr.txt");
                        var stdout = folder().file(base + "-" + pid + "-stdout.txt");
                        announcement.put("stdout", stdout.path().toString());
                        announcement.put("stderr", stderr.path().toString());
                        announcement.asStringList().titledBox("Launching Jar");
                        return builder
                                .redirectError(stderr.asJavaFile())
                                .redirectOutput(stdout.asJavaFile())
                                .start();
                    }

                    default:
                        unsupported("Unsupported redirection");
                }
            }
            catch (IOException e)
            {
                problem(e, "Unable to launch $", resource);
            }
        }

        warning("Unable to launch jar from any provided resource");
        return null;
    }

    private Folder folder()
    {
        return Folder.kivakitTemporary().folder("launcher").mkdirs();
    }
}
