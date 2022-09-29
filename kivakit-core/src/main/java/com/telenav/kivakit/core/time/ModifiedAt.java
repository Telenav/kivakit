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

import com.telenav.kivakit.annotations.code.ApiQuality;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Interface to an object that has a last modification time
 *
 * @author jonathanl (shibo)
 * @see Modifiable
 */
@SuppressWarnings("unused")
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public interface ModifiedAt
{
    /**
     * Returns the time of last modification
     */
    default Time modifiedAt()
    {
        throw new UnsupportedOperationException("Cannot retrieve last modified time from: " + getClass());
    }

    /**
     * Returns true if this object was changed after the given object
     */
    default boolean wasChangedAfter(ModifiedAt that)
    {
        return modifiedAt().isAfter(that.modifiedAt());
    }

    /**
     * Returns true if this object was changed before the given object
     */
    default boolean wasChangedBefore(ModifiedAt that)
    {
        return modifiedAt().isBefore(that.modifiedAt());
    }
}
