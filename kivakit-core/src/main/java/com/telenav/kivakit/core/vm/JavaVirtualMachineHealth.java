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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramLanguage;
import com.telenav.kivakit.core.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.core.logging.LogEntry;
import com.telenav.kivakit.core.string.KivaKitFormat;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.string.ObjectFormatter.ObjectFormat.MULTILINE;

/**
 * Information about the Java virtual machine that relates to health and resources
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramLanguage.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class JavaVirtualMachineHealth
{
    /** The amount of free memory */
    private Bytes freeMemory;

    /** The amount of memory Java will attempt to use */
    private Bytes maximumMemory;

    /** The total amount of memory available to Java */
    private Bytes totalMemory;

    /** The number of processors */
    private Count processors;

    /** The last thread snapshot */
    private ThreadSnapshot lastSnapshot;

    /** The current thread snapshot */
    private ThreadSnapshot snapshot;

    /** The time that monitoring was started */
    private Time started;

    /**
     * Count of messages that have been received for each message type
     */
    private final Map<String, Integer> messageType = new ConcurrentHashMap<>();

    /**
     * Returns the percentage of CPU time consumed by the calling thread
     */
    @KivaKitFormat
    public double cpuUse()
    {
        if (lastSnapshot != null)
        {
            var elapsed = elapsedSinceLastSnapshot();
            var elapsedCpuTime = elapsedCpuTimeSinceLastSnapshot();
            return 100.0 * elapsedCpuTime.asMilliseconds() / elapsed.asMilliseconds();
        }
        return 0.0;
    }

    /**
     * The CPU time elapsed since the last snapshot
     */
    @KivaKitFormat
    public Duration elapsedCpuTimeSinceLastSnapshot()
    {
        return lastSnapshot == null ? Duration.ZERO_DURATION : snapshot.totalCpuTime().minus(lastSnapshot.totalCpuTime());
    }

    /**
     * The amount of time that has elapsed since the last snapshot
     */
    @KivaKitFormat
    public Duration elapsedSinceLastSnapshot()
    {
        return lastSnapshot == null ? Duration.ZERO_DURATION : snapshot.capturedAt().elapsedSince(lastSnapshot.capturedAt());
    }

    /**
     * Returns the amount of free memory
     */
    @KivaKitFormat
    public Bytes freeMemory()
    {
        return freeMemory;
    }

    /**
     * Called when messages are logged
     */
    public void logEntry(LogEntry entry)
    {
        var messageType = entry.messageType();
        var count = this.messageType.getOrDefault(messageType, 0);
        this.messageType.put(messageType, count + 1);
    }

    /**
     * The number of messages of the given type that have been logged
     */
    public Count loggedMessageCount(String messageType)
    {
        var count = this.messageType.get(messageType);
        return count == null ? Count._0 : Count.count(count);
    }

    /**
     * Returns the maximum memory that Java will attempt to use
     */
    @KivaKitFormat
    public Bytes maximumMemory()
    {
        return maximumMemory;
    }

    /**
     * Returns the percentage of available memory that is being used
     */
    @KivaKitFormat
    public double memoryUse()
    {
        return usedMemory().percentOf(maximumMemory).asZeroToOne();
    }

    @KivaKitFormat
    public Map<String, Integer> messageType()
    {
        return messageType;
    }

    /**
     * Returns the number of processors
     */
    @KivaKitFormat
    public Count processors()
    {
        return processors;
    }

    /**
     * Returns the current thread snapshot
     */
    public ThreadSnapshot threadSnapshot()
    {
        return snapshot;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).asString(MULTILINE);
    }

    /**
     * Returns the total amount of memory available to Java
     */
    @KivaKitFormat
    public Bytes totalMemory()
    {
        return totalMemory;
    }

    /**
     * Returns the amount of time since monitoring began
     */
    @KivaKitFormat
    public Duration upTime()
    {
        return started.elapsedSince();
    }

    /**
     * Updates health data
     */
    public JavaVirtualMachineHealth update()
    {
        if (started == null)
        {
            started = Time.now();
        }

        var vm = JavaVirtualMachine.javaVirtualMachine();
        freeMemory = vm.freeMemory();
        maximumMemory = vm.maximumMemory();
        totalMemory = vm.totalMemory();
        processors = vm.processors();
        lastSnapshot = snapshot;
        snapshot = vm.threadSnapshot();
        return this;
    }

    /**
     * Returns the amount of memory Java is using
     */
    @KivaKitIncludeProperty
    public Bytes usedMemory()
    {
        return totalMemory().minus(freeMemory());
    }
}
