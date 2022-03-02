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
import com.telenav.kivakit.core.project.lexakai.DiagramOs;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.File;
import java.io.IOException;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;

/**
 * An abstraction of features of the underlying OS through Java interfaces.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramOs.class)
public class OperatingSystem implements Named
{
    private static final OperatingSystem os = new OperatingSystem();

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
            for (var key : System.getenv().keySet())
            {
                variables.put(key, System.getenv(key));
            }
        }
        return variables;
    }

    public String exec(File folder, String... command)
    {
        try
        {
            var builder = new ProcessBuilder();
            builder.command(command);
            builder.directory(folder);
            builder.redirectErrorStream(true);
            var process = builder.start();
            var output = Processes.captureOutput(process);
            Processes.waitFor(process);
            return output;
        }
        catch (IOException e)
        {
            throw new IllegalStateException("OperationFailed reading output of child process", e);
        }
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
        return ensureNotNull(javaHome()) + "/bin/java";
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
