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

package com.telenav.kivakit.interfaces.naming;

import com.telenav.kivakit.annotations.code.ApiQuality;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.SUFFICIENT;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNNECESSARY;

/**
 * An object that can be assigned a name.
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = STABLE,
            testing = UNNECESSARY,
            documentation = SUFFICIENT)
public interface Nameable
{
    /**
     * @param name The name to assign. The default implementation ignores calls.
     */
    default void assignName(String name)
    {
    }
}
