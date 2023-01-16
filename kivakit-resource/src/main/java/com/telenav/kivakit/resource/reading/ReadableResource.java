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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.resource.CloseMode;
import com.telenav.kivakit.resource.WriteMode;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.internal.lexakai.DiagramFileSystemFile;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResource;
import com.telenav.kivakit.resource.resources.StringResource;
import com.telenav.kivakit.resource.writing.WritableResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.progress.ProgressReporter.nullProgressReporter;
import static com.telenav.kivakit.resource.CloseMode.CLOSE;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Interface to an object that is {@link Readable} and can be read with a {@link ResourceReader}.
 *
 * <p><b>Reading</b></p>
 *
 * <ul>
 *     <li>{@link #charset()}</li>
 *     <li>{@link #reader()}</li>
 *     <li>{@link #reader(ProgressReporter)}</li>
 *     <li>{@link #reader(ProgressReporter, Charset)}</li>
 * </ul>
 *
 * <p><b>Copying</b></p>
 *
 * <ul>
 *     <li>{@link #copyTo(WritableResource, WriteMode, ProgressReporter)}</li>
 * </ul>
 *
 * <p><b>Implementation</b></p>
 *
 * <p>
 * The {@link #resource()} method must be defined by the implementer, as well as the method
 * {@link #copyTo(WritableResource, WriteMode, ProgressReporter)}.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramFileSystemFile.class)
@UmlClassDiagram(diagram = DiagramResource.class)
@UmlRelation(label = "provides", referent = ResourceReader.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public interface ReadableResource extends
        Readable,
        Repeater
{
    /**
     * Returns a string resource for the entire contents of this resource
     */
    default StringResource asStringResource()
    {
        return new StringResource(reader().asString());
    }

    /**
     * Returns the charset used by this resource
     */
    default Charset charset()
    {
        return UTF_8;
    }

    /**
     * Copies this resource to the given destination
     *
     * @param target The destination to write to
     * @throws IllegalStateException Thrown if the copy operation fails
     */
    void copyTo(@NotNull WritableResource target,
                @NotNull WriteMode writeMode,
                @NotNull CloseMode closeMode,
                @NotNull ProgressReporter reporter);

    /**
     * Copies the data in this resource to the destination.
     *
     * @throws IllegalStateException Thrown if the copy operation fails
     */
    default void copyTo(@NotNull WritableResource target,
                        @NotNull WriteMode writeMode,
                        @NotNull ProgressReporter reporter)
    {
        copyTo(target, writeMode, CLOSE, reporter);
    }

    /**
     * Copies the data in this resource to the destination.
     *
     * @throws IllegalStateException Thrown if the copy operation fails
     */
    default void copyTo(@NotNull WritableResource target,
                        @NotNull WriteMode writeMode)
    {
        copyTo(target, writeMode, CLOSE, nullProgressReporter());
    }

    /**
     * Returns the content of this resource as a string
     *
     * @return The text
     */
    default String readText()
    {
        return reader().readText();
    }

    /**
     * Returns a reader with convenient methods for reading from the resource
     */
    default ResourceReader reader(@NotNull ProgressReporter reporter)
    {
        return new ResourceReader(reporter, resource(), charset());
    }

    /**
     * Returns a reader with convenient methods for reading from the resource
     */
    default ResourceReader reader()
    {
        return reader(nullProgressReporter());
    }

    /**
     * Returns a reader with convenient methods for reading from the resource
     */
    default ResourceReader reader(@NotNull ProgressReporter reporter,
                                  @NotNull Charset charset)
    {
        return new ResourceReader(reporter, resource(), charset);
    }

    /**
     * Returns the resource being read
     */
    Resource resource();
}
