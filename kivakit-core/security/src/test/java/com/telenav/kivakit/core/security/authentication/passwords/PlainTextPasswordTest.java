////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.security.authentication.passwords;

import com.telenav.kivakit.core.test.UnitTest;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("SpellCheckingInspection")
public class PlainTextPasswordTest extends UnitTest
{
    @Test
    public void test()
    {
        final var password = new PlainTextPassword("passw0rd");
        ensure(password.matches(new PlainTextPassword("passw0rd")));
        ensureFalse(password.matches(new PlainTextPassword("password")));
    }

    @Test
    public void testToString()
    {
        final var password = new PlainTextPassword("This*is8hi");
        Assert.assertEquals(password.toString(), "This*is8hi");
        Assert.assertEquals(password.asString(), "**********");
    }
}
