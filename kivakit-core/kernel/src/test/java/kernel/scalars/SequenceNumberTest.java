////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.scalars;

import com.telenav.kivakit.core.kernel.language.values.identifier.SequenceNumber;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureNotEqual;

public class SequenceNumberTest
{
    @Test
    public void test()
    {
        final var first = new SequenceNumber(0);
        final var next = first.next();
        ensure(first.isLessThan(next));
        ensure(next.isGreaterThan(first));
        ensureNotEqual(first, next);
        ensureEqual(next, new SequenceNumber(1));
    }
}
