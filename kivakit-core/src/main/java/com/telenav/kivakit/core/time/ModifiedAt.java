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

package com.telenav.kivakit.core.time;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Interface to an object that has a last modification time
 *
 * @author jonathanl (shibo)
 * @see Modifiable
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public interface ModifiedAt
{
    /**
     * Returns the time of last modification
     */
    default Time lastModified()
    {
        throw new UnsupportedOperationException("Cannot retrieve last modified time from: " + getClass());
    }

    /**
     * Returns true if this object was changed after the given object
     */
    default boolean wasChangedAfter(ModifiedAt that)
    {
        return lastModified().isAfter(that.lastModified());
    }

    /**
     * Returns true if this object was changed before the given object
     */
    default boolean wasChangedBefore(ModifiedAt that)
    {
        return lastModified().isBefore(that.lastModified());
    }
}
