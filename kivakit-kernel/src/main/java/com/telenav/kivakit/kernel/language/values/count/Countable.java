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

package com.telenav.kivakit.kernel.language.values.count;

import com.telenav.kivakit.interfaces.collection.Sized;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * Interface to something that has a {@link Count} value.
 *
 * @author jonathanl (shibo)
 * @see Count
 */
@LexakaiJavadoc(complete = true)
public interface Countable extends Sized
{
    /**
     * The size of this object as a {@link Count}
     *
     * @return This object's size as a {@link Count}
     */
    default Count count()
    {
        return Count.count(size());
    }
}