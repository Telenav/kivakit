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

package com.telenav.kivakit.core.resource.resources.jar.launcher;

import com.telenav.kivakit.core.filesystem.File;
import com.telenav.kivakit.core.filesystem.Folder;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.kernel.language.threading.KivaKitThread;
import com.telenav.kivakit.core.kernel.language.vm.OperatingSystem;
import com.telenav.kivakit.core.kernel.language.vm.Processes;
import com.telenav.kivakit.core.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.resource.Resource;
import com.telenav.kivakit.core.resource.Resourced;
import com.telenav.kivakit.core.resource.path.Extension;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramJarLauncher;
import com.telenav.kivakit.core.resource.resources.other.PropertyMap;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.resource.resources.jar.launcher.JarLauncher.ProcessType.CHILD;
import static com.telenav.kivakit.core.resource.resources.jar.launcher.JarLauncher.ProcessType.DETACHED;

/**
 * Launches an executable Java program from a jar {@link Resource}, in a child or detached process. The process type and
 * program arguments are passed as parameters to the constructor. Any number of {@link Resourced} objects can be added
 * with {@link #source(Resourced)}. The method {@link #run()} then launches the application, attempting to use each
 * {@link Resource} that was added with {@link #source(Resourced)} in order.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramJarLauncher.class)
public class JarLauncher extends BaseRepeater
{
    @UmlClassDiagram(diagram = DiagramJarLauncher.class)
    public enum ProcessType
    {
        DETACHED,
        CHILD
    }

    public enum RedirectTo
    {
        FILE,
        CONSOLE
    }

    @UmlAggregation(label = "launches process of type")
    private ProcessType processType = CHILD;

    @UmlAggregation(label = "retrieves jar from one of")
    private final List<Resourced> sources = new ArrayList<>();

    private StringList programArguments = new StringList();

    @UmlAggregation(label = "determines output redirection")
    private RedirectTo redirectTo = RedirectTo.FILE;

    private int debugPort = -1;

    private boolean headless = true;

    public JarLauncher arguments(final StringList arguments)
    {
        programArguments = arguments;
        return this;
    }

    public JarLauncher arguments(final String... arguments)
    {
        programArguments = StringList.stringList(Arrays.asList(arguments));
        return this;
    }

    public JarLauncher enableDebuggerOnPort(final int debugPort)
    {
        this.debugPort = debugPort;
        return this;
    }

    public JarLauncher headless(final boolean headless)
    {
        this.headless = headless;
        return this;
    }

    public JarLauncher processType(final ProcessType type)
    {
        processType = type;
        return this;
    }

    public JarLauncher redirectTo(final RedirectTo redirectTo)
    {
        this.redirectTo = redirectTo;
        return this;
    }

    public Process run()
    {
        // Go through each possible jar source,
        for (final var source : sources)
        {
            // get the resource and materialize it to the local host,
            final var resource = source.resource().materialized(ProgressReporter.NULL);
            try
            {
                // get the resource basename,
                final var base = resource.fileName().withoutExtension(Extension.JAR);

                // and create the argument list.
                final var java = OperatingSystem.get().java();
                final var arguments = new StringList();
                arguments.add(java);
                if (headless)
                {
                    arguments.add("-Djava.awt.headless=true");
                }
                if (debugPort > 0)
                {
                    arguments.add("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=" + debugPort);
                }
                arguments.add("-jar");
                arguments.add(resource.path().toString());
                arguments.addAll(programArguments);

                // If the process should be detached,
                final var script = folder().file(base + ".sh");
                if (processType == DETACHED)
                {
                    // write the arguments to a shell script,
                    try (final var out = script.printWriter())
                    {
                        out.println("#!/bin/bash");
                        out.println("");
                        out.println(arguments.join(" ") + " &");
                    }

                    // and mark it as executable.
                    script.chmod(File.ACCESS_775);
                }

                // Create a process builder for the script or arguments,
                final var builder = new ProcessBuilder();
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
                final var pid = OperatingSystem.get().processIdentifier();

                // and launch the jar, redirecting output to
                final var announcement = PropertyMap.create();
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
                        final var process = builder.start();
                        KivaKitThread.run(this, "RedirectOutputToConsole", () -> Processes.copyStandardOutToConsole(process));
                        KivaKitThread.run(this, "RedirectErrorToConsole", () -> Processes.copyStandardErrorToConsole(process));
                        return process;
                    }

                    // or to a file.
                    case FILE:
                    {
                        final var stderr = folder().file(base + "-" + pid + "-stderr.txt");
                        final var stdout = folder().file(base + "-" + pid + "-stdout.txt");
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
            catch (final IOException e)
            {
                problem(e, "Unable to launch $", resource);
            }
        }

        warning("Unable to launch jar from any provided resource");
        return null;
    }

    /**
     * Adds the given resourced object to this launcher
     */
    public JarLauncher source(final Resourced resourced)
    {
        sources.add(resourced);
        return this;
    }

    private Folder folder()
    {
        return Folder.kivakitTemporaryFolder().folder("launcher").mkdirs();
    }
}
