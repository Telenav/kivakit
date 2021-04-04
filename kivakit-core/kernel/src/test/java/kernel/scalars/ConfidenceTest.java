////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.scalars;

import com.telenav.kivakit.core.kernel.data.validation.reporters.ValidationFailure;
import com.telenav.kivakit.core.kernel.language.values.level.Confidence;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureFalse;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;

public class ConfidenceTest
{
    @Test
    public void test()
    {
        ensureEqual(Confidence.NO, Confidence.forByte((byte) 0x0));
        ensureEqual(Confidence.FULL, Confidence.forByte(Byte.MAX_VALUE));
        ensureEqual(Confidence.NO, Confidence.forUnsignedByte(0));
        ensureEqual(Confidence.FULL, Confidence.forUnsignedByte(255));
        ensure(Confidence.MEDIUM.isGreaterThan(Confidence.ZERO));
        ensure(Confidence.FULL.isGreaterThan(Confidence.MEDIUM));
        ensure(Confidence.LOW.isLessThan(Confidence.MEDIUM));
        ensure(Confidence.LOW.isLessThan(Confidence.FULL));
        ensure(Confidence.LOW.isGreaterThan(Confidence.ZERO));
        ensure(Confidence.ZERO.isZero());
        ensureFalse(Confidence.LOW.isZero());
        ensureEqual(0, Confidence.NO.asUnsignedByte());
        ensureEqual(255, Confidence.FULL.asUnsignedByte());
        try
        {
            new Confidence(2);
            fail("Should throw");
        }
        catch (final ValidationFailure ignored)
        {
        }
        try
        {
            new Confidence(-1);
            fail("Should throw");
        }
        catch (final ValidationFailure ignored)
        {
        }
        new Confidence(1);
    }
}
