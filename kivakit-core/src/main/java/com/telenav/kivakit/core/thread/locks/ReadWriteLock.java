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

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramThread;
import com.telenav.kivakit.interfaces.value.Source;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A lock subclass that adds convenient features to {@link ReentrantLock}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramThread.class)
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class ReadWriteLock extends ReentrantReadWriteLock
{
    public ReadWriteLock()
    {
        super(true);
    }

    /**
     * Runs the provided code inside a lock / unlock pair, allowing the codes to have a return value.
     */
    public <T> T read(Source<T> code)
    {
        readLock().lock();
        try
        {
            return code.get();
        }
        finally
        {
            readLock().unlock();
        }
    }

    /**
     * Runs the provided code inside a lock / unlock pair.
     */
    public void read(Runnable code)
    {
        readLock().lock();
        try
        {
            code.run();
        }
        finally
        {
            readLock().unlock();
        }
    }

    /**
     * Runs the provided code inside a lock / unlock pair, allowing the code to have a return value.
     */
    public <T> T write(Source<T> code)
    {
        writeLock().lock();
        try
        {
            return code.get();
        }
        finally
        {
            writeLock().unlock();
        }
    }

    /**
     * Runs the provided code inside a lock / unlock pair.
     */
    public void write(Runnable code)
    {
        writeLock().lock();
        try
        {
            code.run();
        }
        finally
        {
            writeLock().unlock();
        }
    }
}
