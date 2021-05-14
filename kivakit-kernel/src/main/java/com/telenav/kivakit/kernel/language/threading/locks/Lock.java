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

package com.telenav.kivakit.kernel.language.threading.locks;

import com.telenav.kivakit.kernel.interfaces.code.Code;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageThreadSynchronization;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.concurrent.locks.ReentrantLock;

/**
 * A lock subclass that adds convenient features to {@link ReentrantLock}. {@link #whileLocked(Code)} and {@link
 * #whileLocked(Runnable)} run the given code while the lock is held.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageThreadSynchronization.class)
@LexakaiJavadoc(complete = true)
public class Lock extends ReentrantLock
{
    /**
     * Runs the provided code inside a lock / unlock pair.
     */
    public void whileLocked(final Runnable code)
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
    public <T> T whileLocked(final Code<T> code)
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
