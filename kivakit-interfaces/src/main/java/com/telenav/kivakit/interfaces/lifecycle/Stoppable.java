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

package com.telenav.kivakit.interfaces.lifecycle;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.interfaces.internal.lexakai.DiagramLifeCycle;
import com.telenav.kivakit.interfaces.time.LengthOfTime;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * An operation that can be stopped
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLifeCycle.class)
@TypeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public interface Stoppable<Duration extends LengthOfTime<Duration>> extends Operation
{
    /**
     * Returns the maximum time to wait when stopping
     */
    Duration maximumStopTime();

    /**
     * Stops this task, blocking until the operation is completed
     */
    default void stop()
    {
        stop(maximumStopTime());
    }

    /**
     * Stops this task, waiting no more than the given wait time before giving up.
     */
    void stop(Duration wait);
}
