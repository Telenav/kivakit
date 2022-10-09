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

package com.telenav.kivakit.core.value.count;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

/**
 * An interface to any object whose size can be measured in bytes.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public interface ByteSized
{
    /**
     * Returns true if this byte sized object is larger than the given one
     */
    default boolean isLargerThan(ByteSized that)
    {
        return sizeInBytes().isGreaterThan(that.sizeInBytes());
    }

    /**
     * Returns true if this byte sized object is the same size as the given one
     */
    default boolean isSameSizeAs(ByteSized that)
    {
        return sizeInBytes().equals(that.sizeInBytes());
    }

    /**
     * Returns true if this byte sized object is smaller than the given one
     */
    default boolean isSmallerThan(ByteSized that)
    {
        return sizeInBytes().isLessThan(that.sizeInBytes());
    }

    /**
     * @return The number of bytes in this object
     */
    Bytes sizeInBytes();
}
