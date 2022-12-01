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

package com.telenav.kivakit.core.thread.locks;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramThread;
import com.telenav.kivakit.interfaces.code.Code;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.concurrent.locks.ReentrantLock;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A lock subclass that adds convenient features to {@link ReentrantLock}. {@link #whileLocked(Code)} and
 * {@link #whileLocked(Runnable)} run the given code while the lock is held.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramThread.class)
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class Lock extends ReentrantLock
{
    /**
     * Runs the provided code inside a lock / unlock pair.
     */
    public void whileLocked(Runnable code)
    {
        lock();
        try
        {
            code.run();
        }
        finally
        {
            unlock();
        }
    }

    /**
     * Runs the provided code inside a lock / unlock pair.
     */
    public <T> T whileLocked(Code<T> code)
    {
        lock();
        try
        {
            return code.run();
        }
        finally
        {
            unlock();
        }
    }
}
