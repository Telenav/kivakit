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

package com.telenav.kivakit.interfaces.lifecycle;

import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * Initializes an object
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public interface Initializable
{
    /**
     * Initializes this object.
     */
    default void initialize()
    {
        onInitialize();
    }

    /**
     * Initializes the given object by calling {@link #initialize()} on it
     *
     * @return The initialized object
     */
    default <T extends Initializable> T initialize(T object)
    {
        object.initialize();
        return object;
    }

    /**
     * Called by {@link #initialize()} to initialize this object
     */
    default void onInitialize()
    {
    }
}
