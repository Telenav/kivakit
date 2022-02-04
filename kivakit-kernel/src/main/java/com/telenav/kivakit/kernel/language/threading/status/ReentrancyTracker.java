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

package com.telenav.kivakit.kernel.language.threading.status;

import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageThread;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.kernel.language.threading.status.ReentrancyTracker.Reentrancy.ENTERED;
import static com.telenav.kivakit.kernel.language.threading.status.ReentrancyTracker.Reentrancy.EXITED;
import static com.telenav.kivakit.kernel.language.threading.status.ReentrancyTracker.Reentrancy.EXITED_REENTRY;
import static com.telenav.kivakit.kernel.language.threading.status.ReentrancyTracker.Reentrancy.REENTERED;

/**
 * Checks a section of code for reentrancy.
 *
 * <p>
 * Calling {@link #enter()} and {@link #exit()} (usually in a try / finally block to ensure consistency) marks a section
 * of code as potentially reentrant. If {@link #enter()} returns {@link Reentrancy#ENTERED}, the code section is being
 * entered for the first time. If it returns {@link Reentrancy#REENTERED}, then the code section is being reentered
 * recursively. In the same way, {@link #exit()} returns {@link Reentrancy#EXITED} if the code section is being exited
 * for the last time, and {@link Reentrancy#EXITED_REENTRY} otherwise. Within the code section, {@link #hasReentered()}
 * will return true if the code is being executed a second time through direct or indirect recursion.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageThread.class)
public class ReentrancyTracker
{
    public enum Reentrancy
    {
        ENTERED,
        REENTERED,
        EXITED_REENTRY,
        EXITED;
    }

    private final ThreadLocal<Integer> level = ThreadLocal.withInitial(() -> 0);

    /**
     * Enters a block of reentrant code.
     *
     * @return True if the code section can be entered because it has not yet been entered. Reentrancy can also be
     * checked with {@link #hasReentered()}.
     */
    public Reentrancy enter()
    {
        try
        {
            // Return true if we have not reentered,
            return level.get() == 0 ? ENTERED : REENTERED;
        }
        finally
        {
            // and enter the code block.
            level.set(level.get() + 1);
        }
    }

    /**
     * Exits a block of re-entrant code.
     *
     * @return True if the code is exiting for the last time.
     */
    public Reentrancy exit()
    {
        // Exit the code block,
        level.set(level.get() - 1);

        // and return true if we are exiting the code for the last time.
        return level.get() == 0 ? EXITED : EXITED_REENTRY;
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
