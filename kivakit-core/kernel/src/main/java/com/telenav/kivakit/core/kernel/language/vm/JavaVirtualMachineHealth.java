package com.telenav.kivakit.core.kernel.language.vm;

import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.core.kernel.language.threading.status.ThreadSnapshot;
import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.logging.LogEntry;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageJavaVirtualMachine;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter.Format.SINGLE_LINE;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageJavaVirtualMachine.class)
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

    public Count count(final String messageType)
    {
        final var count = this.messageType.get(messageType);
        return count == null ? Count._0 : Count.count(count);
    }

    @KivaKitIncludeProperty
    public double cpuUse()
    {
        if (lastSnapshot != null)
        {
            final var elapsed = elapsed();
            final var elapsedCpuTime = elapsedCpuTime();
            return 100.0 * elapsedCpuTime.asMilliseconds() / elapsed.asMilliseconds();
        }
        return 0.0;
    }

    @KivaKitIncludeProperty
    public Duration elapsed()
    {
        return lastSnapshot == null ? Duration.NONE : snapshot.time().subtract(lastSnapshot.time());
    }

    @KivaKitIncludeProperty
    public Duration elapsedCpuTime()
    {
        return lastSnapshot == null ? Duration.NONE : snapshot.cpuTime().subtract(lastSnapshot.cpuTime());
    }

    @KivaKitIncludeProperty
    public Bytes freeMemory()
    {
        return freeMemory;
    }

    public void logEntry(final LogEntry entry)
    {
        final var messageType = entry.messageType();
        final var count = this.messageType.getOrDefault(messageType, 0);
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

        final var vm = JavaVirtualMachine.local();
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
        return totalMemory().subtract(freeMemory());
    }
}
