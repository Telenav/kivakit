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

package com.telenav.kivakit.resource.reading;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.io.IO;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.progress.reporters.ProgressiveInputStream;
import com.telenav.kivakit.core.value.count.ByteSized;
import com.telenav.kivakit.core.value.count.Bytes;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Interface to something which can be opened for reading. The input stream can be obtained with
 * {@link #openForReading()} or {@link #openForReading(ProgressReporter)}. The latter method will report progress to the
 * given reporter as bytes are read from the input.
 *
 * <p><b>Opening to Read</b></p>
 *
 * <ul>
 *     <li>{@link #openForReading()}</li>
 *     <li>{@link #openForReading(ProgressReporter)}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #isReadable()}</li>
 *     <li>{@link #sizeInBytes()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public interface Readable extends ByteSized
{
    /**
     * Returns true if reading is possible
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
    default InputStream openForReading(@NotNull ProgressReporter reporter)
    {
        // Get the size of this resource
        var size = sizeInBytes();
        if (size != null)
        {
            // and set the number of steps so the progress reporter can report
            // absolute progress as a percent complete
            reporter.steps(size.asMaximum());
        }

        return new ProgressiveInputStream(openForReading(), reporter);
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
     * Returns the number of bytes that can be read
     */
    @Override
    default Bytes sizeInBytes()
    {
        return null;
    }
}
