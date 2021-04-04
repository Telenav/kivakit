////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.core;

import com.telenav.kivakit.core.test.UnitTest;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class NetworkLocationTest extends UnitTest
{
    @Test
    public void test() throws MalformedURLException
    {
        final var location = new NetworkLocation(Host.loopback().http().path("/foo"));
        ensureEqual(Host.loopback(), location.host());
        ensureEqual(80, location.port().number());
        ensureEqual(location.toString(), "http://localhost/foo");
        ensureEqual(new URL("http://127.0.0.1/foo"), location.asUrl());
        ensureEqual(Protocol.HTTP, location.protocol());
        final var parameters = new QueryParameters("x=9");
        location.queryParameters(parameters);
        ensureEqual("9", location.queryParameters().asMap().get("x"));
    }
}
