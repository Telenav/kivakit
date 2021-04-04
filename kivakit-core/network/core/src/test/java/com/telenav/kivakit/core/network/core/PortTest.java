////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.core;

import com.telenav.kivakit.core.test.UnitTest;
import org.junit.Test;

public class PortTest extends UnitTest
{
    @Test
    public void testPort()
    {
        final var host = new Host("192.168.0.4");
        final var port = host.http(8081);
        ensureEqual("192.168.0.4:8081", port.toString());
    }

    @Test
    public void testProtocol()
    {
        final var host = new Host("192.168.0.4");
        final var port = host.http(8081);
        port.protocol(Protocol.HTTP);
        ensure(port.isHttp());
    }
}
