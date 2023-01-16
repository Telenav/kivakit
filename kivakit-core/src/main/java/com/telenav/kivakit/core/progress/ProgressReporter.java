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

package com.telenav.kivakit.core.progress;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramProgress;
import com.telenav.kivakit.core.progress.reporters.BroadcastingProgressReporter;
import com.telenav.kivakit.core.value.count.BaseCount;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.interfaces.lifecycle.Resettable;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.value.count.Count._1;

/**
 * Reports the progress of some operation to an end-user in some manner. The operation begins when {@link #start()} is
 * called and ends when {@link #end()} is called. During the operation, each increment of progress can be reported with
 * {@link #next()} or {@link #next(Count)}. As the operation progresses, any {@link ProgressListener} that is registered
 * via {@link #progressReporter(ProgressListener)} is called with the percent-complete.
 *
 * @author jonathanl (shibo)
 * @see BroadcastingProgressReporter
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramProgress.class)
@UmlRelation(label = "reports progress to", referent = ProgressListener.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public interface ProgressReporter extends Resettable
{
    /**
     * A progress reporter that does nothing
     */
    static ProgressReporter nullProgressReporter()
    {
        return new NullProgressReporter();
    }

    /**
     * Returns the step this reporter is at
     */
    Count at();

    /**
     * Report that the operation has ended
     */
    default void end(String message, Object... arguments)
    {
    }

    /**
     * Report that the operation has ended
     */
    default void end()
    {
        end("Processed");
    }

    /**
     * Returns true if progress is indefinite because the number of steps in not known
     */
    default boolean isIndefinite()
    {
        return steps() == null;
    }

    /**
     * Report the next step in the operation. This is often fine-grained such as processing a single node or way in a
     * data file. The implementation in {@link BroadcastingProgressReporter} of this interface is designed to be
     * efficient in this way.
     */
    void next();

    /**
     * Report progress of the given number of steps towards completion
     */
    default void next(Count steps)
    {
        next(steps.asInt());
    }

    /**
     * Report progress of the given number of steps towards completion
     */
    default void next(int steps)
    {
        for (var i = 0L; i < steps; i++)
        {
            next();
        }
    }

    /**
     * Returns the number of steps that were okay
     */
    default Count okay()
    {
        return at().minus(problems());
    }

    /**
     * @param phase The phase of processing the same items multiple times, like "reading", "sorting", "saving"
     */
    default ProgressReporter phase(String phase)
    {
        return this;
    }

    /**
     * Report a problem
     */
    default void problem()
    {
        problems(_1);
    }

    /**
     * Report several problems
     */
    void problems(long problems);

    /**
     * Report several problems
     */
    default void problems(Count problems)
    {
        problems(problems.asLong());
    }

    /**
     * Returns The number of problems encountered
     */
    Count problems();

    /**
     * Calls a listener with the percent of progress each time it changes. This method is only called if
     * {@link #steps()} is larger than 0.
     */
    default ProgressReporter progressReporter(ProgressListener listener)
    {
        return this;
    }

    /**
     * Report that the operation has begun
     */
    default ProgressReporter start()
    {
        start("Processing");
        return this;
    }

    /**
     * Report that the operation has begun
     */
    default ProgressReporter start(String label, Object... arguments)
    {
        return this;
    }

    /**
     * Sets the number of steps that can be executed, allowing a calculation of absolute progress
     */
    default ProgressReporter steps(BaseCount<?> steps)
    {
        return this;
    }

    /** The number of steps in the operation before it completes or null if it is indefinite */
    default Count steps()
    {
        return null;
    }
}
