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

package com.telenav.kivakit.resource;

import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.io.IO;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.progress.reporters.ProgressiveOutputStream;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import java.io.OutputStream;

/**
 * Interface to something that can potentially be opened for writing. An output stream can be obtained with {@link
 * #openForWriting()} or {@link #openForWriting(ProgressReporter)}. The latter method will report progress to the given
 * reporter as bytes are written to the output.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public interface Writable
{
    /**
     * @return True if this write-openable object can be written to, false if it cannot and null if it cannot be
     * determined if this object can be written to.
     */
    Boolean isWritable();

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
        return openForWriting(ProgressReporter.none());
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
    default OutputStream openForWriting(ProgressReporter reporter)
    {
        return new ProgressiveOutputStream(IO.buffer(onOpenForWriting()), reporter);
    }
}
