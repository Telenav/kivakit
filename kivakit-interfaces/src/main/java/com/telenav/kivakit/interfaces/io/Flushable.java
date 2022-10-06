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

package com.telenav.kivakit.interfaces.io;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.interfaces.internal.lexakai.DiagramIo;
import com.telenav.kivakit.interfaces.time.LengthOfTime;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * An object that can be flushed, like a queue or output stream.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramIo.class)
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public interface Flushable<Duration extends LengthOfTime<Duration>>
{
    /**
     * Flushes the object waiting not more than the give duration for this to occur.
     *
     * @param maximumFlushTime The amount of time to wait before giving up on flushing. To achieve a blocking flush,
     * simply pass in a maximal length of time as the wait time, or call {@link #flush()}.
     */
    void flush(Duration maximumFlushTime);

    /**
     * Flush the object, waiting until finished doing so.
     */
    default void flush()
    {
        flush(maximumFlushTime());
    }

    /**
     * @return The maximum time to wait when {@link #flush()} is called. Normally this is the largest duration possible,
     * which for all practical purposes is "forever".
     */
    Duration maximumFlushTime();
}
