////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.project;

import com.telenav.kivakit.core.serialization.kryo.CoreKernelKryoTypes;
import com.telenav.kivakit.core.serialization.kryo.KryoTypes;
import com.telenav.kivakit.core.serialization.kryo.KryoUnitTest;

/**
 * This is the base test class for all unit tests. It provides some methods common to all tests.
 *
 * @author jonathanl (shibo)
 */
public abstract class CoreCollectionsUnitTest extends KryoUnitTest
{
    @Override
    protected KryoTypes kryoTypes()
    {
        return new CoreKernelKryoTypes().mergedWith(new CoreCollectionsKryoTypes());
    }
}
