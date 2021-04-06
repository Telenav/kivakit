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

package com.telenav.kivakit.core.kernel.interfaces.io;

import com.telenav.kivakit.core.kernel.language.io.ProgressiveInput;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceIo;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.InputStream;

/**
 * Interface to something which can be opened for reading.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceIo.class)
public interface Readable extends ByteSized
{
    /**
     * @return The number of bytes that can be read
     */
    @Override
    default Bytes bytes()
    {
        return null;
    }

    /**
     * @return True if reading is possible
     */
    default boolean isReadable()
    {
        return true;
    }

    /**
     * Opens the input stream to this readable
     */
    InputStream onOpenForReading();

    /**
     * Opens something that can be read, returning an input stream. The given progress reporter is called for each byte
     * that is read from the input stream. If the input size is known (meaning that {@link #bytes()} returns a non-null
     * value), the reporter will be initialized with the number of bytes to allow percent completion to be computed as
     * the stream is read.
     *
     * @param reporter A progress reporter that is called for each byte that is read
     * @return The input stream to read from
     */
    default InputStream openForReading(final ProgressReporter reporter)
    {
        // Get the size of this resource
        final var size = bytes();
        if (size != null)
        {
            // and set the number of steps so the progress reporter can report
            // absolute progress as a percent complete
            reporter.steps(size.asMaximum());
        }

        return new ProgressiveInput(openForReading(), reporter);
    }

    /**
     * Opens something that can be read, returning an input stream.
     *
     * @return The input stream to read from
     */
    default InputStream openForReading()
    {
        return onOpenForReading();
    }
}
