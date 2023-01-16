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

package com.telenav.kivakit.core.vm;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramThread;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Information about the state of a thread from Java's management API.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramThread.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class ThreadStatus
{
    /** The total amount of CPU time consumed so far */
    Duration cpuTime;

    /** The name of the thread */
    String name;

    /** True if the thread is a daemon */
    boolean isDaemon;

    /** The thread state */
    Thread.State state;

    /** The thread identifier */
    long identifier;

    /**
     * Returns the CPU time consumed by this thread
     */
    public Duration cpuTime()
    {
        return cpuTime;
    }

    /**
     * Returns the thread identifier
     */
    public long identifier()
    {
        return identifier;
    }

    /**
     * Returns true if this thread is a daemon
     */
    public boolean isDaemon()
    {
        return isDaemon;
    }

    /**
     * Returns true if this is a KivaKit thread
     */
    public boolean isKivaKit()
    {
        return name.startsWith("KivaKit-");
    }

    /**
     * Returns the name of this thread
     */
    public String name()
    {
        return name;
    }

    /**
     * Returns this thread's state according to Java
     */
    public Thread.State state()
    {
        return state;
    }
}
