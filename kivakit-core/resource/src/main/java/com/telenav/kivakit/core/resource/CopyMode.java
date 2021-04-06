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

package com.telenav.kivakit.core.resource;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * @author jonathanl (shibo)
 */
public enum CopyMode
{
    /** Overwrite the destination if it exists */
    OVERWRITE,

    /** Overwrite the destination if the source has a different size or last modification time */
    UPDATE,

    /** Do not overwrite the destination */
    DO_NOT_OVERWRITE;

    /**
     * @return True if the given source can be copied to the given destination
     */
    public boolean canCopy(final Resource source, final Resource destination)
    {
        switch (this)
        {
            case OVERWRITE:
                return true;

            case DO_NOT_OVERWRITE:
                return !destination.exists() || destination.isEmpty();

            case UPDATE:
                return !destination.exists() || destination.isEmpty() || !source.isSame(destination);

            default:
                unsupported("Unsupported copy mode: ", this);
                return false;
        }
    }
}
