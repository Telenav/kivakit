////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.scalars;

import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.language.values.count.MutableCount;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureFalse;

public class MutableCountTest
{
    @Test
    public void test()
    {
        final var count = new MutableCount();
        ensureEqual(0L, count.asLong());
        ensureEqual(Count._0, count.count());
        count.increment();
        ensureEqual(1L, count.asLong());
        ensureEqual(Count._1, count.asCount());
        count.plus(1);
        ensureEqual(2L, count.asLong());
        count.clear();
        ensureEqual(0L, count.asLong());
        ensureEqual(Count._0, count.asCount());
        ensure(count.equals(new MutableCount()));
        ensureFalse(count.isGreaterThan(new MutableCount()));
        ensureFalse(count.isLessThan(new MutableCount()));
    }
}
