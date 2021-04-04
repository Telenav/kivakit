////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.security.authentication.passwords;

import com.telenav.kivakit.core.test.UnitTest;
import org.junit.Test;

@SuppressWarnings("SpellCheckingInspection")
public class DigestPasswordTest extends UnitTest
{
    @Test
    public void test()
    {
        final var password = new DigestPassword("passw0rd");
        ensure(password.matches(new DigestPassword("passw0rd")));
        ensureFalse(password.matches(new DigestPassword("password")));
    }
}
