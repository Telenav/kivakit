package com.telenav.kivakit.core.kernel.language.threading.status;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageThreadSynchronization;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageThreadSynchronization.class)
public enum WakeState
{
    /** Waiting was interrupted */
    INTERRUPTED,

    /** Waiting timed out */
    TIMED_OUT,

    /** The awaited condition was completed */
    COMPLETED
}
