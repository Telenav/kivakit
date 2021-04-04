////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.scalars;

import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.language.values.level.Percent;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureFalse;

public class CountTest
{
    @Test
    public void test()
    {
        final Count count = Count.count(99);
        ensureEqual(99, count.asInt());
        ensureEqual(Count.count(100), count.plus(Count._1));
        ensureEqual(Count.count(100), count.plus(1));
        ensureEqual(Count.count(100), count.plusOne());
        ensureFalse(count.isZero());
        ensure(Count._0.isZero());
        ensureEqual(new Percent(99), count.percentOf(Count.count(100)));
        ensureEqual(-1, count.compareTo(Count.count(100)));
        ensureEqual(0, count.compareTo(count));
        ensureEqual(1, count.compareTo(Count.count(98)));
    }
}
