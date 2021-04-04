////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.security.digest.digesters;

import com.telenav.kivakit.core.test.UnitTest;
import org.junit.Test;

public class Sha1DigesterTest extends UnitTest
{
    @Test
    public void test()
    {
        final var digester = new Sha1Digester();
        final byte[] digest = digester.digest("this is a test of the emergency broadcasting system".getBytes());
        ensureEqual(20, digest.length);
    }
}
