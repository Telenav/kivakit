////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.time;

import com.telenav.kivakit.core.kernel.language.time.Rate;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;

public class RateTest
{
    @Test
    public void test()
    {
        final var rate = Rate.perSecond(1);
        ensureEqual(1.0, rate.count());
        ensureEqual(60.0, rate.perMinute().count());
        ensureEqual(3600.0, rate.perHour().count());
        ensureEqual(24 * 3600.0, rate.perDay().count());
        ensureEqual(Rate.perMinute(60.0), rate);
        ensure(rate.compareTo(Rate.perSecond(2)) < 0);
        ensure(rate.isLessThan(Rate.perSecond(2)));
        ensure(rate.isGreaterThan(Rate.perSecond(0.5)));
    }
}
