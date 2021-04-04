////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.threading.status;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageThread;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Checks code for reentrancy. Calling {@link #enter()} and {@link #exit()} (usually in a try / finally block to ensure
 * consistency) marks a section of code as potentially reentrant. Code within this section can call {@link
 * #hasReentered()} to determine if the code is being executed a second time through direct or indirect recursion. This
 * checking mechanism is implemented with a {@link ThreadLocal}, so there are no thread synchronization issues to be
 * concerned about.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageThread.class)
public class ReentrancyTracker
{
    private static final ThreadLocal<Integer> level = ThreadLocal.withInitial(() -> 0);

    /**
     * Enters a block of potentially reentrant code
     *
     * @return True if the following code has already been entered. Reentrancy can also be checked with {@link
     * #hasReentered()}.
     */
    public boolean enter()
    {
        try
        {
            // Return true if we've reentered
            return level.get() > 0;
        }
        finally
        {
            // Enter the code block
            level.set(level.get() + 1);
        }
    }

    /**
     * Exits a block of potentially re-entrant code
     */
    public void exit()
    {
        // Exit the code block
        level.set(level.get() - 1);
    }

    /**
     * @return True if the current thread has reentered the code between the {@link #enter()} call and the {@link
     * #exit()} call
     */
    public boolean hasReentered()
    {
        return level.get() > 0;
    }
}
