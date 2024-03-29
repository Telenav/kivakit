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

package com.telenav.kivakit.core.vm;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.internal.lexakai.DiagramLanguage;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.nio.file.Path;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.os.OperatingSystem.operatingSystem;
import static com.telenav.kivakit.core.value.count.Bytes.bytes;
import static com.telenav.kivakit.core.value.count.Count.count;

/**
 * An object for working with the Java virtual machine.
 *
 * <p><b>Access</b></p>
 *
 * <p>
 * An instance of the local Java virtual machine can be retrieved with {@link #javaVirtualMachine()}
 * </p>
 *
 * <p><b>Resource Availability</b></p>
 *
 * <ul>
 *     <li>{@link #freeMemory()}</li>
 *     <li>{@link #maximumMemory()}</li>
 *     <li>{@link #processors()}</li>
 *     <li>{@link #totalMemory()}</li>
 *     <li>{@link #usedMemory()}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #invalidateProperties()}</li>
 *     <li>{@link #systemProperties()}</li>
 *     <li>{@link #systemPropertiesAndEnvironmentVariables()}</li>
 * </ul>
 *
 * <p><b>Java Health</b></p>
 *
 * <ul>
 *     <li>{@link #health()}</li>
 * </ul>
 *
 * <p><b>Debugging</b></p>
 *
 * <ul>
 *     <li>{@link #assertionsEnabled()}</li>
 *     <li>{@link #dumpHeap(Path)}</li>
 *     <li>{@link #threadSnapshot()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramLanguage.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class JavaVirtualMachine extends BaseRepeater
{
    private static final boolean DETAILED_DEBUG = true;

    private static boolean ASSERTIONS_ENABLED;

    private static JavaVirtualMachine LOCAL;

    static
    {
        // Slightly tricky way to determine if assertions are enabled via asserted side effect
        //noinspection AssertWithSideEffects
        assert ASSERTIONS_ENABLED = true;
    }

    public static JavaVirtualMachine javaVirtualMachine()
    {
        if (LOCAL == null)
        {
            LOCAL = new JavaVirtualMachine();
        }
        return LOCAL;
    }

    private VariableMap<String> systemProperties;

    @UmlAggregation
    private JavaVirtualMachineHealth health;

    private JavaVirtualMachine()
    {
    }

    public boolean assertionsEnabled()
    {
        return ASSERTIONS_ENABLED;
    }

    /**
     * Dumps the heap to the given path
     */
    public void dumpHeap(Path path)
    {
        var processIdentifier = ProcessHandle.current().pid();
        try
        {
            @SuppressWarnings("SpellCheckingInspection")
            var process = Runtime.getRuntime().exec("jmap -dump:live,file=" + path + " " + processIdentifier);
            process.waitFor();
        }
        catch (Exception e)
        {
            problem(e, "Unable to dump heap");
        }
    }

    /**
     * Returns the amount of memory free for Java to use
     */
    public Bytes freeMemory()
    {
        return bytes(Runtime.getRuntime().freeMemory());
    }

    /**
     * Returns the current health status of Java
     */
    public JavaVirtualMachineHealth health()
    {
        if (health == null)
        {
            health = new JavaVirtualMachineHealth();
            health.update();
        }
        return health;
    }

    /**
     * Forces properties to reload when they are accessed again
     */
    public void invalidateProperties()
    {
        systemProperties = null;
    }

    /**
     * Returns the maximum amount of memory that Java will attempt to use
     */
    public Bytes maximumMemory()
    {
        return bytes(Runtime.getRuntime().maxMemory());
    }

    /**
     * Returns the number of processors available
     */
    public Count processors()
    {
        return count(Runtime.getRuntime().availableProcessors());
    }

    /**
     * Returns all system properties
     */
    public VariableMap<String> systemProperties()
    {
        if (systemProperties == null)
        {
            systemProperties = new VariableMap<>();
            var properties = System.getProperties();
            for (var key : properties.keySet())
            {
                systemProperties.put(key.toString(), properties.getProperty(key.toString()));
            }
        }
        return systemProperties;
    }

    /**
     * Returns all system properties and environment variables
     */
    public VariableMap<String> systemPropertiesAndEnvironmentVariables()
    {
        return systemProperties().addAll(operatingSystem().environmentVariables());
    }

    /**
     * Returns a snapshot of the current thread activity
     */
    public ThreadSnapshot threadSnapshot()
    {
        return new ThreadSnapshot().update();
    }

    @Override
    public String toString()
    {
        return "processors = " + processors() + ", free = " + freeMemory() + ", used = " + usedMemory()
                + ", allocated = " + totalMemory() + ", maximum = " + maximumMemory();
    }

    /**
     * Returns the total memory in the JVM
     */
    public Bytes totalMemory()
    {
        return bytes(Runtime.getRuntime().totalMemory());
    }

    /**
     * Returns the amount of memory being used by Java
     */
    public Bytes usedMemory()
    {
        return bytes(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
    }
}
