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

package com.telenav.kivakit.core.kernel.language.threading.status;

import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageThread;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Information about the state of a thread from Java's management API.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageThread.class)
@LexakaiJavadoc(complete = true)
public class ThreadStatus
{
    Duration cpuTime;

    String name;

    boolean isDaemon;

    Thread.State state;

    /**
     * @return The CPU time consumed by this thread
     */
    public Duration cpuTime()
    {
        return cpuTime;
    }

    /**
     * @return True if this thread is a daemon
     */
    public boolean isDaemon()
    {
        return isDaemon;
    }

    /**
     * @return True if this is a KivaKit thread
     */
    public boolean isKivaKit()
    {
        return name.startsWith("KivaKit-");
    }

    /**
     * @return The name of this thread
     */
    public String name()
    {
        return name;
    }

    /**
     * @return This thread's state according to Java
     */
    public Thread.State state()
    {
        return state;
    }
}
