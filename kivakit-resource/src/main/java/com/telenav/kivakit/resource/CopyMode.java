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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * The mode for copying resources. Resources can be updated (copied if they don't exist yet), overwritten (copied over
 * top of any existing resource) or not overwritten (not copied if the resource already exists).
 *
 * <p><b>Values</b></p>
 *
 * <ul>
 *     <li>{@link #OVERWRITE}</li>
 *     <li>{@link #UPDATE}</li>
 *     <li>{@link #DO_NOT_OVERWRITE}</li>
 * </ul>
 *
 * <p><b>Copying</b></p>
 *
 * <ul>
 *     <li>{@link #canCopy(Resource, Resource)} - Returns true if this copy mode allows the given copy operation</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public enum CopyMode
{
    /** Copy to a stream */
    STREAM,

    /** Copy to the destination even if it already exists */
    OVERWRITE,

    /** Overwrite the destination if the source has a different size or last modification time */
    UPDATE,

    /** Do not overwrite the destination */
    DO_NOT_OVERWRITE;

    /**
     * Returns true if the given source can be copied to the given destination
     */
    public boolean canCopy(@NotNull Resource source,
                           @NotNull Resource destination)
    {
        switch (this)
        {
            case STREAM, OVERWRITE ->
            {
                return true;
            }

            case DO_NOT_OVERWRITE ->
            {
                return !destination.exists() || destination.isEmpty();
            }

            case UPDATE ->
            {
                return !destination.exists() || destination.isEmpty() || !source.isSame(destination);
            }

            default ->
            {
                unsupported("Unsupported copy mode: ", this);
                return false;
            }
        }
    }
}
