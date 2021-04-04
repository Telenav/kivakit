////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.threading.locks;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageThreadSynchronization;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * An object to use instead of {@link Object} when a simple mutex lock is required.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageThreadSynchronization.class)
public class Monitor
{
    public void await()
    {
        try
        {
            wait();
        }
        catch (final InterruptedException ignored)
        {
        }
    }

    public void done()
    {
        notifyAll();
    }
}
