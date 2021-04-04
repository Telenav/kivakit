////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.core;

import com.telenav.kivakit.core.test.UnitTest;
import org.junit.Test;

public class ProtocolTest extends UnitTest
{
    @Test
    public void test()
    {
        ensureEqual("http", Protocol.HTTP.name().toLowerCase());
        ensureEqual(Protocol.HTTP, Protocol.HTTP);
        ensureNotEqual(Protocol.HTTP, Protocol.FTP);
    }
}
