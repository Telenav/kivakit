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

package com.telenav.kivakit.core.os;

import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.internal.lexakai.DiagramOs;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.File;
import java.io.IOException;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.os.OperatingSystem.OperatingSystemType.MACOS;
import static com.telenav.kivakit.core.os.OperatingSystem.OperatingSystemType.OTHER_OS;
import static com.telenav.kivakit.core.os.OperatingSystem.OperatingSystemType.UNIX;
import static com.telenav.kivakit.core.os.OperatingSystem.OperatingSystemType.WINDOWS;
import static com.telenav.kivakit.core.os.OperatingSystem.ProcessorArchitecture.APPLE;
import static com.telenav.kivakit.core.os.OperatingSystem.ProcessorArchitecture.INTEL;
import static com.telenav.kivakit.core.os.OperatingSystem.ProcessorArchitecture.OTHER_PROCESSOR;

/**
 * An abstraction of features of the underlying OS through Java interfaces.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramOs.class)
public class OperatingSystem implements Named
{
    /** This operating system */
    private static final OperatingSystem os = new OperatingSystem();

    /**
     * Returns the operating system
     */
    public static OperatingSystem operatingSystem()
    {
        return os;
    }

    public enum OperatingSystemType
    {
        WINDOWS,
        MACOS,
        UNIX,
        OTHER_OS
    }

    public enum ProcessorArchitecture
    {
        INTEL,
        APPLE,
        OTHER_PROCESSOR
    }

    /** Map of environment variables */
    private VariableMap<String> environmentVariables;

    /**
     * Returns all OS environment variables
     */
    public VariableMap<String> environmentVariables()
    {
        if (environmentVariables == null)
        {
            environmentVariables = new VariableMap<>();
            for (var key : System.getenv().keySet())
            {
                environmentVariables.put(key, System.getenv(key));
            }
        }
        return environmentVariables;
    }

    /**
     * Executes the given command in the given folder
     *
     * @param listener The listener to call with any problems
     * @param folder The folder to run the command in
     * @param command The command to run
     */
    public String execute(Listener listener, File folder, String... command)
    {
        try
        {
            var builder = new ProcessBuilder();
            builder.command(command);
            builder.directory(folder);
            builder.redirectErrorStream(true);
            var process = builder.start();
            var output = Processes.captureOutput(listener, process);
            Processes.waitFor(process);
            return output;
        }
        catch (IOException e)
        {
            throw new IllegalStateException("OperationFailed reading output of child process", e);
        }
    }

    /**
     * Returns true if this is a Mac
     */
    public boolean isMac()
    {
        return name().toLowerCase().contains("mac");
    }

    /**
     * Returns true if this is UNIX
     */
    public boolean isUnix()
    {
        return name().toLowerCase().contains("unix") || name().toLowerCase().contains("linux");
    }

    /**
     * Returns true if this Windows
     */
    public boolean isWindows()
    {
        return name().toLowerCase().contains("win");
    }

    /**
     * Returns path to java executable
     */
    public String java()
    {
        return ensureNotNull(javaHome(), "JAVA_HOME must be set") + "/bin/java";
    }

    /**
     * Returns KIVAKIT_JAVA_HOME, or if that's not defined, returns JAVA_HOME
     */
    public String javaHome()
    {
        var home = property("KIVAKIT_JAVA_HOME");
        if (home == null)
        {
            home = property("JAVA_HOME");
        }
        return home;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String name()
    {
        return System.getProperty("os.name");
    }

    /**
     * Returns the type of operating system
     */
    public OperatingSystemType operatingSystemType()
    {
        if (name().contains("Mac"))
        {
            return MACOS;
        }

        if (name().contains("Windows"))
        {
            return WINDOWS;
        }

        if (name().contains("Linux")
                || name().contains("SunOS")
                || name().contains("BSD"))
        {
            return UNIX;
        }

        return OTHER_OS;
    }

    /**
     * Returns the PID for this process
     */
    public int processIdentifier()
    {
        return (int) ProcessHandle.current().pid();
    }

    /**
     * Returns the processor architecture
     */
    @SuppressWarnings("SpellCheckingInspection")
    public ProcessorArchitecture processorArchitecture()
    {
        switch (System.getProperty("os.arch"))
        {
            case "x86":
                return INTEL;

            case "aarch64":
                return APPLE;

            default:
                return OTHER_PROCESSOR;
        }
    }

    /**
     * Returns the value of the given property, or if that doesn't exist the value of the given environment variable
     *
     * @param variable The name of the property or environment variable to get.
     */
    public String property(String variable)
    {
        var value = System.getProperty(variable);
        if (value == null)
        {
            value = System.getenv(variable);
        }
        return value;
    }
}
