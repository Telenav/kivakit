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

import com.telenav.kivakit.core.language.object.ObjectFormatter;
import com.telenav.kivakit.core.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.core.lexakai.DiagramLanguage;
import com.telenav.kivakit.core.logging.LogEntry;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.telenav.kivakit.core.language.object.ObjectFormatter.Format.SINGLE_LINE;

/**
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramLanguage.class)
public class JavaVirtualMachineHealth
{
    private Bytes freeMemory;

    private Bytes maximumMemory;

    private Bytes totalMemory;

    private Count processors;

    private ThreadSnapshot lastSnapshot;

    private ThreadSnapshot snapshot;

    private Time started;

    private final Map<String, Integer> messageType = new ConcurrentHashMap<>();

    public Count count(String messageType)
    {
        var count = this.messageType.get(messageType);
        return count == null ? Count._0 : Count.count(count);
    }

    @KivaKitIncludeProperty
    public double cpuUse()
    {
        if (lastSnapshot != null)
        {
            var elapsed = elapsed();
            var elapsedCpuTime = elapsedCpuTime();
            return 100.0 * elapsedCpuTime.asMilliseconds() / elapsed.asMilliseconds();
        }
        return 0.0;
    }

    @KivaKitIncludeProperty
    public Duration elapsed()
    {
        return lastSnapshot == null ? Duration.ZERO_DURATION : snapshot.capturedAt().elapsedSince(lastSnapshot.capturedAt());
    }

    @KivaKitIncludeProperty
    public Duration elapsedCpuTime()
    {
        return lastSnapshot == null ? Duration.ZERO_DURATION : snapshot.totalCpuTime().minus(lastSnapshot.totalCpuTime());
    }

    @KivaKitIncludeProperty
    public Bytes freeMemory()
    {
        return freeMemory;
    }

    public void logEntry(LogEntry entry)
    {
        var messageType = entry.messageType();
        var count = this.messageType.getOrDefault(messageType, 0);
        this.messageType.put(messageType, count + 1);
    }

    @KivaKitIncludeProperty
    public Bytes maximumMemory()
    {
        return maximumMemory;
    }

    @KivaKitIncludeProperty
    public double memoryUse()
    {
        return usedMemory().percentOf(maximumMemory).asZeroToOne();
    }

    @KivaKitIncludeProperty
    public Map<String, Integer> messageType()
    {
        return messageType;
    }

    @KivaKitIncludeProperty
    public Count processors()
    {
        return processors;
    }

    public ThreadSnapshot threadSnapshot()
    {
        return snapshot;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString(SINGLE_LINE);
    }

    @KivaKitIncludeProperty
    public Bytes totalMemory()
    {
        return totalMemory;
    }

    @KivaKitIncludeProperty
    public Duration upTime()
    {
        return started.elapsedSince();
    }

    public JavaVirtualMachineHealth update()
    {
        if (started == null)
        {
            started = Time.now();
        }

        var vm = JavaVirtualMachine.local();
        freeMemory = vm.freeMemory();
        maximumMemory = vm.maximumMemory();
        totalMemory = vm.totalMemory();
        processors = vm.processors();
        lastSnapshot = snapshot;
        snapshot = vm.threadSnapshot();
        return this;
    }

    @KivaKitIncludeProperty
    public Bytes usedMemory()
    {
        return totalMemory().minus(freeMemory());
    }
}
