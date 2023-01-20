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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.internal.lexakai.DiagramOs;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.File;
import java.io.IOException;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.os.OperatingSystem.OperatingSystemType.MACOS;
import static com.telenav.kivakit.core.os.OperatingSystem.OperatingSystemType.OTHER_OS;
import static com.telenav.kivakit.core.os.OperatingSystem.OperatingSystemType.UNIX;
import static com.telenav.kivakit.core.os.OperatingSystem.OperatingSystemType.WINDOWS;
import static com.telenav.kivakit.core.os.OperatingSystem.ProcessorArchitecture.APPLE;
import static com.telenav.kivakit.core.os.OperatingSystem.ProcessorArchitecture.INTEL;
import static com.telenav.kivakit.core.os.OperatingSystem.ProcessorArchitecture.OTHER_PROCESSOR;
import static com.telenav.kivakit.core.os.Processes.captureOutput;
import static com.telenav.kivakit.core.os.Processes.waitForTermination;

/**
 * An abstraction of features of the underlying OS through Java interfaces. This object is a singleton, which can be
 * retrieved with {@link #operatingSystem()}.
 *
 * <p><b>Identity</b></p>
 *
 * <ul>
 *     <li>{@link #operatingSystemType()}</li>
 *     <li>{@link #processorArchitecture()}</li>
 *     <li>{@link #isMac()}</li>
 *     <li>{@link #isWindows()}</li>
 *     <li>{@link #isWindows()}</li>
 *     <li>{@link #name()}</li>
 * </ul>
 *
 * <p><b>Execution</b></p>
 *
 * <ul>
 *     <li>{@link #execute(Listener, File, String...)}</li>
 *     <li>{@link #javaExecutable()}</li>
 *     <li>{@link #processIdentifier()}</li>
 * </ul>
 *
 * <p><b>Variables</b></p>
 *
 * <ul>
 *     <li>{@link #environmentVariables()}</li>
 *     <li>{@link #systemPropertyOrEnvironmentVariable(String)}</li>
 *     <li>{@link #javaHome()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramOs.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
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

    /**
     * The type of operating system
     */
    public enum OperatingSystemType
    {
        WINDOWS,
        MACOS,
        UNIX,
        OTHER_OS
    }

    /**
     * The type of processor
     */
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
     * @return The output of the command
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
            var output = captureOutput(listener, process);
            waitForTermination(process);
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
     * Returns true if this is UNIX (but not macOS)
     */
    public boolean isUnix()
    {
        return name().toLowerCase().contains("unix")
                || name().toLowerCase().contains("linux");
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
    public String javaExecutable()
    {
        return ensureNotNull(javaHome(), "JAVA_HOME must be set") + "/bin/java";
    }

    /**
     * Returns KIVAKIT_JAVA_HOME, or if that's not defined, returns JAVA_HOME
     */
    public String javaHome()
    {
        var home = systemPropertyOrEnvironmentVariable("KIVAKIT_JAVA_HOME");
        if (home == null)
        {
            home = systemPropertyOrEnvironmentVariable("JAVA_HOME");
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
        return switch (System.getProperty("os.arch"))
                {
                    case "x86" -> INTEL;
                    case "aarch64" -> APPLE;
                    default -> OTHER_PROCESSOR;
                };
    }

    /**
     * Returns the value of the given property, or if that doesn't exist the value of the given environment variable. If
     * that also does not exist, returns the given default value.
     *
     * @param variable The name of the property or environment variable to get.
     * @param defaultValue A default value to use if there is no system property or environment variable defined for the
     * given key
     */
    public String systemPropertyOrEnvironmentVariable(String variable, String defaultValue)
    {
        var value = systemPropertyOrEnvironmentVariable(variable);
        return value == null ? defaultValue : value;
    }

    /**
     * Returns the value of the given property, or if that doesn't exist the value of the given environment variable
     *
     * @param variable The name of the property or environment variable to get.
     */
    public String systemPropertyOrEnvironmentVariable(String variable)
    {
        // First check for a system property
        var value = System.getProperty(variable);

        // then if that's not available,
        if (value == null)
        {
            // check for an environment variable
            value = System.getenv(variable);
        }

        return value;
    }
}
