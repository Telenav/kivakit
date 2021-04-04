////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.set.logical.operations;

import com.telenav.kivakit.core.collections.set.SetOperationTest;
import org.junit.Test;

public class IntersectionTest extends SetOperationTest
{
    @Test
    public void test()
    {
        final var a = set(1, 2, 3);
        final var b = set(2, 3, 4);
        final var intersection = new Intersection<>(a, b);
        ensureEqual(2, intersection.size());
        ensureFalse(intersection.isEmpty());
        ensure(intersection.contains(3));
        ensureFalse(intersection.contains(1));
        ensure(set(2, 3).equals(intersection));
    }
}
