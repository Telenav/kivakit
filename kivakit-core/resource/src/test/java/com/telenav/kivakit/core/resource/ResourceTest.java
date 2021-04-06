////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource;

import com.telenav.kivakit.core.resource.resources.other.PropertyMap;
import com.telenav.kivakit.core.test.UnitTest;
import org.junit.Test;

public class ResourceTest extends UnitTest
{
    @Test
    public void testResolution()
    {
        final var properties = Resource.resolve("classpath:com/telenav/kivakit/core/resource/ResourceTest.properties");
        ensureEqual("b", PropertyMap.load(properties).get("a"));
    }
}
