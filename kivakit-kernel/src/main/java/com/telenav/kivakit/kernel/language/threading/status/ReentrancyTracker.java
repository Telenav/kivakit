////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.kernel.language.threading.status;

import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageThread;
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
