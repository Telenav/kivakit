////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.core;

import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.test.UnitTest;
import org.junit.Test;

public class NetworkAccessConstraintsTest extends UnitTest
{
    @Test
    public void testEquals()
    {
        final NetworkAccessConstraints constraints = new NetworkAccessConstraints();
        constraints.timeout(Duration.seconds(7));
        ensureEqual(Duration.seconds(7), constraints.timeout());
    }
}
