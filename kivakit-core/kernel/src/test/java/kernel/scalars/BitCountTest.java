////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.scalars;

import com.telenav.kivakit.core.kernel.language.values.count.BitCount;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;

public class BitCountTest
{
    @Test
    public void testMaximumSigned()
    {
        ensureEqual((long) Byte.MAX_VALUE, BitCount.bitCount(8).maximumSigned());
        ensureEqual((long) Short.MAX_VALUE, BitCount.bitCount(16).maximumSigned());
        ensureEqual((long) Integer.MAX_VALUE, BitCount.bitCount(32).maximumSigned());
        ensureEqual(Long.MAX_VALUE, BitCount.bitCount(64).maximumSigned());
    }

    @Test
    public void testMaximumUnsigned()
    {
        ensureEqual(0xffL, BitCount.bitCount(8).maximumUnsigned());
        ensureEqual(0xffffL, BitCount.bitCount(16).maximumUnsigned());
        ensureEqual(0xffff_ffffL, BitCount.bitCount(32).maximumUnsigned());
        ensureEqual(Long.MAX_VALUE, BitCount.bitCount(64).maximumUnsigned());
    }

    @Test
    public void testMinimum()
    {
        ensureEqual((long) Byte.MIN_VALUE, BitCount.bitCount(8).minimumSigned());
        ensureEqual((long) Short.MIN_VALUE, BitCount.bitCount(16).minimumSigned());
        ensureEqual((long) Integer.MIN_VALUE, BitCount.bitCount(32).minimumSigned());
        ensureEqual(Long.MIN_VALUE, BitCount.bitCount(64).minimumSigned());
    }
}
