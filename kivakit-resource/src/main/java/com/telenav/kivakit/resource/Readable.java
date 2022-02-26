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

import com.telenav.kivakit.kernel.language.io.IO;
import com.telenav.kivakit.kernel.language.io.ProgressiveInput;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.kernel.language.values.count.ByteSized;
import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import java.io.InputStream;

/**
 * Interface to something which can be opened for reading. The input stream can be obtained with {@link
 * #openForReading()} or {@link #openForReading(ProgressReporter)}. The latter method will report progress to the given
 * reporter as bytes are read from the input.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public interface Readable extends ByteSized
{
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
     * that is read from the input stream. If the input size is known (meaning that {@link #sizeInBytes()} returns a
     * non-null value), the reporter will be initialized with the number of bytes to allow percent completion to be
     * computed as the stream is read.
     *
     * @param reporter A progress reporter that is called for each byte that is read
     * @return The input stream to read from
     */
    default InputStream openForReading(ProgressReporter reporter)
    {
        // Get the size of this resource
        var size = sizeInBytes();
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
        return IO.buffer(onOpenForReading());
    }

    /**
     * @return The number of bytes that can be read
     */
    @Override
    default Bytes sizeInBytes()
    {
        return null;
    }
}
