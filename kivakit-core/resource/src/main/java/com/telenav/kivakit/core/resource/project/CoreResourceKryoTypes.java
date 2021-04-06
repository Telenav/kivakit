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

package com.telenav.kivakit.core.resource.project;

import com.telenav.kivakit.core.resource.resources.other.PropertyMap;
import com.telenav.kivakit.core.serialization.kryo.KryoTypes;

/**
 * Serializer for objects in the kivakit-core-collections module and in dependent projects.
 *
 * @author jonathanl (shibo)
 */
public class CoreResourceKryoTypes extends KryoTypes
{
    public CoreResourceKryoTypes()
    {
        //----------------------------------------------------------------------------------------------
        // NOTE: To maintain backward compatibility of serialization, registration groups and the types
        // in each registration group must remain in the same order.
        //----------------------------------------------------------------------------------------------

        group("other", () -> register(PropertyMap.class));
    }
}
