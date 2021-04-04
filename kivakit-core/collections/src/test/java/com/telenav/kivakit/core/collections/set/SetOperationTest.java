////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.set;

import com.telenav.kivakit.core.collections.project.CoreCollectionsUnitTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class SetOperationTest extends CoreCollectionsUnitTest
{
    protected Set<Integer> set(final Integer... values)
    {
        return new HashSet<>(Arrays.asList(values));
    }
}
