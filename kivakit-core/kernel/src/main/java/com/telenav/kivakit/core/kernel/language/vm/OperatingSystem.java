////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.vm;

import com.telenav.kivakit.core.kernel.interfaces.naming.Named;
import com.telenav.kivakit.core.kernel.language.collections.map.string.VariableMap;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageJavaVirtualMachine;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.File;
import java.io.IOException;

/**
 * An abstraction of features of the underlying OS through Java interfaces.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageJavaVirtualMachine.class)
public class OperatingSystem implements Named
{
    private static final OperatingSystem os = new OperatingSystem();

    private static final Logger LOGGER = LoggerFactory.newLogger();

    public static OperatingSystem get()
    {
        return os;
    }

    private VariableMap<String> variables;

    public VariableMap<String> environmentVariables()
    {
        if (variables == null)
        {
            variables = new VariableMap<>();
            for (final var key : System.getenv().keySet())
            {
                variables.put(key, System.getenv(key));
            }
        }
        return variables;
    }

    public String exec(final File folder, final String... command)
    {
        try
        {
            final var builder = new ProcessBuilder();
            builder.command(command);
            builder.directory(folder);
            builder.redirectErrorStream(true);
            final var process = builder.start();
            final var output = Processes.captureOutput(process);
            Processes.waitFor(process);
            return output;
        }
        catch (final IOException e)
        {
            LOGGER.warning(e, "OperationFailed reading output of child process");
        }
        return null;
    }

    public boolean isMac()
    {
        return name().toLowerCase().contains("mac");
    }

    public boolean isUnix()
    {
        return name().toLowerCase().contains("unix") || name().toLowerCase().contains("linux");
    }

    public boolean isWindows()
    {
        return name().toLowerCase().contains("win");
    }

    public String java()
    {
        return javaHome() + "/bin/java";
    }

    public String javaHome()
    {
        var home = property("KIVAKIT_JAVA_HOME");
        if (home == null)
        {
            home = property("JAVA_HOME");
        }
        return home;
    }

    @Override
    public String name()
    {
        return System.getProperty("os.name");
    }

    public int processIdentifier()
    {
        return (int) ProcessHandle.current().pid();
    }

    public String processor()
    {
        return System.getProperty("os.arch");
    }

    public String property(final String variable)
    {
        var value = System.getProperty(variable);
        if (value == null)
        {
            value = System.getenv(variable);
        }
        return value;
    }
}
