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

import com.telenav.kivakit.core.internal.lexakai.DiagramProgress;
import com.telenav.kivakit.core.progress.reporters.BroadcastingProgressReporter;
import com.telenav.kivakit.core.progress.reporters.ProgressiveInputStream;
import com.telenav.kivakit.core.progress.reporters.ProgressiveOutputStream;
import com.telenav.kivakit.core.value.count.BaseCount;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.interfaces.lifecycle.Resettable;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Reports the progress of some operation to an end-user in some manner. The operation begins when {@link #start()} is
 * called and ends when {@link #end()} is called. During the operation, each increment of progress can be reported with
 * {@link #next()} or {@link #next(Count)}. As the operation progresses, any {@link ProgressListener} that is registered
 * via {@link #listener(ProgressListener)} is called with the percent-complete.
 *
 * @author jonathanl (shibo)
 * @see BroadcastingProgressReporter
 */
@UmlClassDiagram(diagram = DiagramProgress.class)
@UmlRelation(label = "reports progress to", referent = ProgressListener.class)
public interface ProgressReporter extends Resettable
{
    /**
     * A progress reporter that does nothing
     */
    static ProgressReporter none()
    {
        return () ->
        {
        };
    }

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
     * @return True if progress is indefinite because the number of steps in not known
     */
    default boolean isIndefinite()
    {
        return steps() == null;
    }

    /**
     * Calls a listener with the percent of progress each time it changes. This method is only called if {@link
     * #steps()} is larger than 0.
     */
    default ProgressReporter listener(ProgressListener listener)
    {
        return this;
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
     * @param phase The phase of processing the same items multiple times, like "reading", "sorting", "saving"
     */
    default ProgressReporter phase(String phase)
    {
        return this;
    }

    default ProgressiveInputStream progressiveInput(InputStream input)
    {
        if (input instanceof ProgressiveInputStream)
        {
            return (ProgressiveInputStream) input;
        }
        return new ProgressiveInputStream(input, this);
    }

    default ProgressiveOutputStream progressiveOutput(OutputStream output)
    {
        if (output instanceof ProgressiveOutputStream)
        {
            return (ProgressiveOutputStream) output;
        }
        return new ProgressiveOutputStream(output, this);
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
    default ProgressReporter start(String label)
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
