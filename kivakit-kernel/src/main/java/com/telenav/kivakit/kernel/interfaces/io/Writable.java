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

package com.telenav.kivakit.kernel.interfaces.io;

import com.telenav.kivakit.kernel.language.io.ProgressiveOutput;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramInterfaceIo;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.OutputStream;

/**
 * Interface to something that can potentially be opened for writing. An output stream can be obtained with {@link
 * #openForWriting()} or {@link #openForWriting(ProgressReporter)}. The latter method will report progress to the given
 * reporter as bytes are written to the output.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceIo.class)
@LexakaiJavadoc(complete = true)
public interface Writable
{
    /**
     * @return True if this write-openable object can be written to
     */
    boolean isWritable();

    /**
     * Opens the output stream to this writable
     */
    OutputStream onOpenForWriting();

    /**
     * Opens something that can be written to, returning an output stream.
     *
     * @return The output stream to write to
     */
    default OutputStream openForWriting()
    {
        return onOpenForWriting();
    }

    /**
     * Opens something that can be written to, returning an output stream. The given progress reporter is called for
     * each byte that is written to the output stream. The caller may wish to initialize the progress reporter with the
     * number of bytes it plans to write via {@link ProgressReporter#steps(Count)}  to allow the reporter to report the
     * percent complete as the stream is written.
     *
     * @param reporter A progress reporter that is called for each byte that is written
     * @return The output stream to write to
     */
    default OutputStream openForWriting(final ProgressReporter reporter)
    {
        return new ProgressiveOutput(onOpenForWriting(), reporter);
    }
}
