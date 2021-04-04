////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.set.logical.operations;

import com.telenav.kivakit.core.collections.set.SetOperationTest;
import org.junit.Test;

public class WithoutTest extends SetOperationTest
{
    @Test
    public void test()
    {
        final var a = set(1, 2, 3);
        final var b = set(2, 3, 4);
        final var without = new Without<>(a, b);
        ensureEqual(1, without.size());
        ensureFalse(without.isEmpty());
        ensure(without.contains(1));
        ensureFalse(without.contains(4));
        ensure(set(1).equals(without));
    }
}
