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

import com.sun.tools.attach.VirtualMachine;
import com.telenav.kivakit.core.KivaKit;
import com.telenav.kivakit.core.collections.Sets;
import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.internal.lexakai.DiagramLanguage;
import com.telenav.kivakit.core.language.primitive.Primitives;
import com.telenav.kivakit.core.language.reflection.Field;
import com.telenav.kivakit.core.language.reflection.Type;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.messages.status.Warning;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.object.Lazy;
import com.telenav.kivakit.core.os.OperatingSystem;
import com.telenav.kivakit.core.string.AsciiArt;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.name.Name;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.Set;

import static com.telenav.kivakit.core.project.Project.resolveProject;
import static com.telenav.kivakit.core.time.Frequency.ONCE;

/**
 * An object for working with a Java virtual machine, including getting properties, instrumentation and determining the
 * rough size of objects.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused") @UmlClassDiagram(diagram = DiagramLanguage.class)
public class JavaVirtualMachine extends BaseRepeater
{
    private static final boolean DETAILED_DEBUG = true;

    private static boolean ASSERTIONS_ENABLED;

    private static final Lazy<JavaVirtualMachine> LOCAL = Lazy.of(JavaVirtualMachine::new);

    static
    {
        // Slightly tricky way to determine if assertions are enabled via asserted side effect
        //noinspection AssertWithSideEffects
        assert ASSERTIONS_ENABLED = true;
    }

    public static void agentmain(String arguments, Instrumentation instrumentation)
    {
        local().instrumentation(instrumentation);
    }

    public static boolean assertionsEnabled()
    {
        return ASSERTIONS_ENABLED;
    }

    public static JavaVirtualMachine local()
    {
        return LOCAL.get();
    }

    @SuppressWarnings("EmptyMethod")
    public static void main(String[] args)
    {
    }

    public static void premain(String arguments, Instrumentation instrumentation)
    {
        local().instrumentation(instrumentation);
    }

    /**
     * Exclude a field or class from sizeOfObjectGraph() calculations
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD, ElementType.TYPE })
    public @interface KivaKitExcludeFromSizeOf
    {
    }

    /**
     * Exclude a field or class from sizeOfObjectGraph() debug tracing
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD, ElementType.TYPE })
    public @interface KivaKitExcludeFromSizeOfDebugTracing
    {
    }

    /**
     * To mark objects passed to sizeOfObjectGraph() as not having cycles (for efficiency).
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE })
    public @interface KivaKitNonCyclicObjectGraph
    {
    }

    @UmlAggregation
    private Instrumentation instrumentation;

    private boolean triedToInstrument;

    private VariableMap<String> variables;

    @UmlAggregation
    private JavaVirtualMachineHealth health;

    private JavaVirtualMachine()
    {
    }

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

    public Bytes freeMemory()
    {
        return Bytes.bytes(Runtime.getRuntime().freeMemory());
    }

    public JavaVirtualMachineHealth health()
    {
        if (health == null)
        {
            health = new JavaVirtualMachineHealth();
            health.update();
        }
        return health;
    }

    public boolean instrument()
    {
        // If we're not instrumented yet
        if (!isInstrumented())
        {
            // and we already tried it once,
            if (triedToInstrument)
            {
                // then we can't instrument
                return false;
            }

            // Don't try this again
            triedToInstrument = true;

            // Show virtual machines available if we're debugging
            if (isDebugOn())
            {
                for (var descriptor : VirtualMachine.list())
                {
                    information(descriptor.toString());
                }
            }

            // Get path to kivakit agent jar
            var agentJar = System.getenv("KIVAKIT_AGENT_JAR");
            if (agentJar == null)
            {
                agentJar = System.getProperty("KIVAKIT_AGENT_JAR");
            }
            if (agentJar == null)
            {
                var kivakitHome = resolveProject(KivaKit.class).homeFolderPath();
                if (kivakitHome != null)
                {
                    agentJar = kivakitHome + "/tools/agent/kivakit-agent.jar";
                }
            }
            information("Instrumentation agent: $", agentJar);
            if (agentJar == null || !new java.io.File(agentJar).exists())
            {
                transmit(new Warning("Unable to instrument JavaVirtualMachine: Must set KIVAKIT_AGENT_JAR to point to kivakit-agent.jar\n"
                        + "or KIVAKIT_HOME must be set and kivakit-agent.jar must be in kivakit/tools/agent\n"
                        + "Consider adding this command to your .profile: launchctl setenv KIVAKIT_HOME $$KIVAKIT_HOME")
                        .maximumFrequency(ONCE));
                return false;
            }
            try
            {
                var name = ManagementFactory.getRuntimeMXBean().getName();
                var pid = name.substring(0, name.indexOf('@'));
                information("Attaching agent to process $", pid);
                var vm = VirtualMachine.attach(pid);
                vm.loadAgent(agentJar);
                vm.detach();
                trace("Successfully instrumented virtual machine");
                return true;
            }
            catch (Exception e)
            {
                //noinspection SpellCheckingInspection
                warning(e, "Unable to instrument VM. Ensure that you defined this system property: -Djdk.attach.allowAttachSelf=true");
            }
            warning("Unable to instrument VM");
            return false;
        }
        return true;
    }

    public void instrumentation(Instrumentation instrumentation)
    {
        this.instrumentation = instrumentation;
    }

    public void invalidateProperties()
    {
        variables = null;
    }

    public boolean isInstrumented()
    {
        return instrumentation != null;
    }

    public Bytes maximumMemory()
    {
        return Bytes.bytes(Runtime.getRuntime().maxMemory());
    }

    public Count processors()
    {
        return Count.count(Runtime.getRuntime().availableProcessors());
    }

    public VariableMap<String> properties()
    {
        if (variables == null)
        {
            variables = new VariableMap<>();
            var properties = System.getProperties();
            for (var key : properties.keySet())
            {
                variables.put(key.toString(), properties.getProperty(key.toString()));
            }
        }
        return variables;
    }

    public Bytes sizeOf(Object object)
    {
        instrument();
        if (isInstrumented())
        {
            return Bytes.bytes(instrumentation.getObjectSize(object));
        }
        return null;
    }

    public Bytes sizeOfObjectGraph(Object object)
    {
        return sizeOfObjectGraph(object, "size", Bytes.megabytes(1));
    }

    /**
     * Computes the size of an object graph. To use this method, you must call instrument() or pass
     * -javaagent:&lt;project.jar&gt; to the JVM. This will call the {@link #premain(String, Instrumentation)} method
     * above, which will enable the size measuring functionality in the JVM.
     *
     * @param prefix A prefix to name the object for display purposes
     * @param minimumSizeToDebugTrace The minimum size of an object to display it with trace. To show nothing when
     * computing object size, call {@link #sizeOfObjectGraph(Object)} instead of this method.
     * @return The recursive size of the object using JVM instrumentation
     */
    public Bytes sizeOfObjectGraph(Object object, String prefix, Bytes minimumSizeToDebugTrace)
    {
        Type<?> type = Type.type(object);
        var isTree = type.type().isAnnotationPresent(KivaKitNonCyclicObjectGraph.class);
        return sizeOfObject(object, isTree ? null : Sets.identitySet(), prefix, minimumSizeToDebugTrace, false, 0);
    }

    public Bytes sizeOfPrimitive(Object value)
    {
        if (!Primitives.isPrimitiveWrapper(value))
        {
            throw new IllegalArgumentException("Not a primitive wrapper type: " + value.getClass());
        }
        var size = 0;
        if (value instanceof Long)
        {
            size = 8;
        }
        else if (value instanceof Integer)
        {
            size = 4;
        }
        else if (value instanceof Short)
        {
            size = 2;
        }
        else if (value instanceof Character)
        {
            size = 2;
        }
        else if (value instanceof Byte)
        {
            size = 1;
        }
        else if (value instanceof Boolean)
        {
            size = 4;
        }
        else if (value instanceof Double)
        {
            size = 8;
        }
        else if (value instanceof Float)
        {
            size = 4;
        }
        return Bytes.bytes(size);
    }

    public Bytes sizeOfPrimitiveType(Class<?> type)
    {
        var bytes = 0;
        if (type == Long.TYPE)
        {
            bytes = Long.BYTES;
        }
        else if (type == Integer.TYPE)
        {
            bytes = Integer.BYTES;
        }
        else if (type == Short.TYPE)
        {
            bytes = Short.BYTES;
        }
        else if (type == Character.TYPE)
        {
            bytes = Character.BYTES;
        }
        else if (type == Byte.TYPE)
        {
            bytes = Byte.BYTES;
        }
        else if (type == Boolean.TYPE)
        {
            bytes = 4;
        }
        else if (type == Double.TYPE)
        {
            bytes = Double.BYTES;
        }
        else if (type == Float.TYPE)
        {
            bytes = Float.BYTES;
        }
        else
        {
            if (type != null)
            {
                // This isn't always true in reality, but it's good enough
                bytes = Long.BYTES;
            }
        }
        return Bytes.bytes(bytes);
    }

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

    public Bytes totalMemory()
    {
        return Bytes.bytes(Runtime.getRuntime().totalMemory());
    }

    public Bytes traceSizeChange(Listener listener, String operation, Object root,
                                 Bytes minimumDebugTraceSize, Runnable runnable)
    {
        if (JavaVirtualMachine.local().isInstrumented())
        {
            var start = Time.now();
            listener.information("Running '$' on '$'", operation, Name.of(root));
            var before = JavaVirtualMachine.local().sizeOfObjectGraph(root, operation + ".before", minimumDebugTraceSize);
            runnable.run();
            var after = JavaVirtualMachine.local().sizeOfObjectGraph(root, operation + ".after", minimumDebugTraceSize);
            if (before != null && after != null)
            {
                listener.information("Operation '$' changed size of $ $ from $ to $", operation, root.getClass().getSimpleName(),
                        after.percentOf(before).inverse(), before, after);
            }
            listener.information("Completed '$' of '$' in $", operation, Name.of(root), start.elapsedSince());
            return after;
        }
        else
        {
            runnable.run();
            return null;
        }
    }

    public Bytes usedMemory()
    {
        return Bytes.bytes(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
    }

    public VariableMap<String> variables()
    {
        return properties().addAll(OperatingSystem.get().environmentVariables());
    }

    private Bytes sizeOfField(Object object, java.lang.reflect.Field field, Set<Object> visited,
                              String prefix,
                              Bytes minimumSizeToDebugTrace, boolean excludeFromTracing, int level)
    {
        if (!isInstrumented() || object == null)
        {
            return null;
        }
        var size = Bytes._0;
        if (Field.accessible(field))
        {
            if (!field.getName().startsWith("this$"))
            {
                if (!Modifier.isStatic(field.getModifiers()))
                {
                    try
                    {
                        var value = field.get(object);
                        if (value != null && !field.isAnnotationPresent(KivaKitExcludeFromSizeOf.class))
                        {
                            if (visited == null || !visited.contains(value))
                            {
                                if (visited != null)
                                {
                                    visited.add(value);
                                }
                                var child = prefix + "." + field.getName();
                                if (value.getClass().isArray())
                                {
                                    if (value.getClass().getComponentType().isPrimitive())
                                    {
                                        size = size.plus(sizeOf(value));
                                    }
                                    else
                                    {
                                        var length = Array.getLength(value);
                                        for (var i = 0; i < length; i++)
                                        {
                                            var element = Array.get(value, i);
                                            if (element != null)
                                            {
                                                var sizeOf = sizeOfObject(element, visited, child + "[" + i + "]",
                                                        minimumSizeToDebugTrace, excludeFromTracing, level + 1);
                                                if (sizeOf != null)
                                                {
                                                    size = size.plus(sizeOf);
                                                }
                                            }
                                        }
                                    }
                                }
                                else
                                {
                                    if (Primitives.isPrimitive(field.getType()))
                                    {
                                        size = size.plus(sizeOfPrimitive(value));
                                    }
                                    else
                                    {
                                        var sizeOf = sizeOfObject(value, visited, child, minimumSizeToDebugTrace,
                                                excludeFromTracing, level + 1);
                                        if (sizeOf != null)
                                        {
                                            size = size.plus(sizeOf);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    catch (IllegalArgumentException | IllegalAccessException e)
                    {
                        warning(e, "Unable to determine size of field $", field);
                    }
                }
            }
        }
        return size;
    }

    private Bytes sizeOfObject(Object object, Set<Object> visited, String prefix,
                               Bytes minimumSizeToDebugTrace, boolean excludeFromTracing, int level)
    {
        instrument();
        if (isInstrumented() && object != null)
        {
            var size = sizeOf(object);
            if (!Primitives.isPrimitiveWrapper(object))
            {
                if (visited != null)
                {
                    visited.add(object);
                }
                Type<?> type = Type.type(object);
                excludeFromTracing = excludeFromTracing
                        || type.type().isAnnotationPresent(KivaKitExcludeFromSizeOfDebugTracing.class);
                if (!type.type().isAnnotationPresent(KivaKitExcludeFromSizeOf.class))
                {
                    for (var field : type.allFields())
                    {
                        excludeFromTracing = excludeFromTracing
                                || field.isAnnotationPresent(KivaKitExcludeFromSizeOfDebugTracing.class);
                        var sizeOf = sizeOfField(object, field, visited, prefix, minimumSizeToDebugTrace,
                                excludeFromTracing, level);
                        if (sizeOf != null)
                        {
                            size = size.plus(sizeOf);
                        }
                    }
                }
            }
            if (size.isGreaterThan(minimumSizeToDebugTrace) && !excludeFromTracing)
            {
                if (DETAILED_DEBUG)
                {
                    trace(AsciiArt.repeat(level * 2, ' ') + "$ => $", prefix, size);
                }
                else
                {
                    trace("$ => $", prefix, size);
                }
            }
            return size;
        }
        return null;
    }
}
