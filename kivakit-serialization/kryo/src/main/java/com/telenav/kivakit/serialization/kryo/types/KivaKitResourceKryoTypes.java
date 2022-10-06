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

package com.telenav.kivakit.serialization.kryo.types;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.properties.PropertyMap;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * The types to register with Kryo for this project
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class KivaKitResourceKryoTypes extends KryoTypes
{
    public KivaKitResourceKryoTypes()
    {
        //----------------------------------------------------------------------------------------------
        // NOTE: To maintain backward compatibility of serialization, registration groups and the types
        // in each registration group must remain in the same order.
        //----------------------------------------------------------------------------------------------

        group("properties", () -> register(PropertyMap.class));
    }
}
