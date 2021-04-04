////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.core;

import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.test.UnitTest;
import org.junit.Test;

public class HostTest extends UnitTest
{
    @Test
    public void test()
    {
        ensureEqual(Host.local(), Host.local());
    }

    @Test
    public void testConverter()
    {
        ensureEqual(new Host("google.com"), new Host.Converter(Listener.none()).convert("google.com"));
    }
}
