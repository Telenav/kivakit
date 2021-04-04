package com.telenav.kivakit.core.kernel.language.threading.status;

import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageThread;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageThread.class)
public class ThreadStatus
{
    Duration cpuTime;

    String name;

    boolean isDaemon;

    Thread.State state;

    public Duration cpuTime()
    {
        return cpuTime;
    }

    public boolean isDaemon()
    {
        return isDaemon;
    }

    public boolean isKivaKit()
    {
        return name.startsWith("KivaKit-");
    }

    public String name()
    {
        return name;
    }

    public Thread.State state()
    {
        return state;
    }
}
