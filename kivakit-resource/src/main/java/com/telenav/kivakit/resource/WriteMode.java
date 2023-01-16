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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.resource.writing.WritableResource;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * The mode for writing to resources. Resources can be appended to (created if non-existent), updated (created if
 * non-existent), overwritten (writing over top of any existing resource) or not overwritten (not copied if the resource
 * already exists).
 *
 * <p><b>Values</b></p>
 *
 * <ul>
 *     <li>{@link #APPEND}</li>
 *     <li>{@link #DO_NOT_OVERWRITE}</li>
 *     <li>{@link #OVERWRITE}</li>
 *     <li>{@link #STREAM}</li>
 *     <li>{@link #UPDATE}</li>
 * </ul>
 *
 * <p><b>Copying</b></p>
 *
 * <ul>
 *     <li>{@link #ensureAllowed(Resource, WritableResource)} - Returns true if this copy mode allows the given copy operation</li>
 *     <li>{@link #ensureAllowed(File)}</li>
 *     <li>{@link #ensureAllowed(Path)}</li>
 *     <li>{@link #ensureAllowed(OutputStream)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public enum WriteMode
{

    /** Append to the target */
    APPEND,

    /** Do not overwrite the target if it is non-empty */
    DO_NOT_OVERWRITE,

    /** Copy to the target even if it already exists */
    OVERWRITE,

    /** Copy to a stream */
    STREAM,

    /** Overwrite the target if the source has a different size or last modification time */
    UPDATE;

    /**
     * Ensures that the given source can be copied to the given destination  under this copy mode
     *
     * @param source The source resource
     * @param target The writable target resource
     * @throws IllegalStateException Thrown if copying isn't allowed
     */
    public void ensureAllowed(@NotNull Resource source,
                              @NotNull WritableResource target)
    {
        switch (this)
        {
            case APPEND, STREAM, OVERWRITE ->
            {
            }
            case DO_NOT_OVERWRITE -> ensure(!target.exists() || target.isEmpty());
            case UPDATE -> ensure(!target.exists() || !source.isSame(target));
            default -> unsupported("Unsupported copy mode: ", this);
        }
    }

    /**
     * Ensures that the given source can be copied to the given destination  under this copy mode
     *
     * @param target The target file to write to
     * @throws IllegalStateException Thrown if copying isn't allowed
     */
    public void ensureAllowed(@NotNull java.io.File target)
    {
        switch (this)
        {
            case APPEND, STREAM, OVERWRITE ->
            {
            }
            case DO_NOT_OVERWRITE -> ensure(!target.exists());
            default -> unsupported("Unsupported copy mode: ", this);
        }
    }

    public void ensureAllowed(Path javaPath)
    {
        switch (this)
        {
            case APPEND, STREAM, OVERWRITE ->
            {
            }
            case DO_NOT_OVERWRITE -> ensure(!Files.exists(javaPath));
            default -> unsupported("Unsupported copy mode: ", this);
        }
    }

    public void ensureAllowed(OutputStream out)
    {
        switch (this)
        {
            case STREAM, OVERWRITE ->
            {
            }
            case APPEND, DO_NOT_OVERWRITE -> fail("Unsupported copy mode: ", this);
            default -> unsupported("Unsupported copy mode: ", this);
        }
    }
}
