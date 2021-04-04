////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.web.jetty;

import com.telenav.kivakit.core.network.core.Host;
import com.telenav.kivakit.core.network.http.HttpNetworkLocation;
import org.junit.Test;

public class HttpNetworkLocationTest extends WebUnitTest
{
    @Test
    public void testReadString()
    {
        final var port = 8910;
        if (startWebServer(port))
        {
            final HttpNetworkLocation location = new HttpNetworkLocation(Host.loopback().http(port).path("test.txt"));
            final String text = location.readString();
            ensureEqual("This is a test.", text);
        }
    }
}
