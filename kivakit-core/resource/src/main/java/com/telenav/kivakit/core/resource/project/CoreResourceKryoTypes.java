////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
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
