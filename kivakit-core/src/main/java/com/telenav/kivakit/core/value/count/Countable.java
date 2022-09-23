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

import com.telenav.kivakit.interfaces.numeric.Zeroable;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * Interface to something that has a {@link Count} value.
 *
 * @author jonathanl (shibo)
 * @see Count
 */
@LexakaiJavadoc(complete = true)
public interface Countable extends Zeroable
{
    /**
     * @return The count for this object
     */
    Count count();

    @Override
    default boolean isZero()
    {
        return count().isZero();
    }
}
