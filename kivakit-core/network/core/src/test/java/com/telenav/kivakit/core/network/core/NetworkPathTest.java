////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.core;

import com.telenav.kivakit.core.test.UnitTest;
import org.junit.Test;

public class NetworkPathTest extends UnitTest
{
    @Test
    public void test()
    {
        final var port = Host.local().http(8080);
        final var path = port.path("/foo/bar");
        ensureEqual("/", path.separator());
        ensure(path.isAbsolute());
    }
}
