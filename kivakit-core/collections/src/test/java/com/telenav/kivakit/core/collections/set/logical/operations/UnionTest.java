////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.set.logical.operations;

import com.telenav.kivakit.core.collections.set.SetOperationTest;
import org.junit.Test;

import java.util.Set;

public class UnionTest extends SetOperationTest
{
    @Test
    public void test()
    {
        final Set<Integer> a = set(1, 2, 3);
        final Set<Integer> b = set(2, 3, 4);
        final Union<Integer> union = new Union<>(a, b);
        ensureEqual(4, union.size());
        ensureFalse(union.isEmpty());
        ensure(union.contains(4));
        ensureFalse(union.contains(5));
        ensure(set(1, 2, 3, 4).equals(union));
    }
}
