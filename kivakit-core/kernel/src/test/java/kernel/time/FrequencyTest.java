////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.time;

import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.language.time.Frequency;
import com.telenav.kivakit.core.kernel.language.time.Frequency.Cycle;
import com.telenav.kivakit.core.kernel.language.time.Time;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureWithin;

public class FrequencyTest
{
    @Test
    public void test()
    {
        final Frequency frequency = Frequency.EVERY_10_SECONDS;
        final var now = Time.now();
        final Cycle cycle = frequency.start(now);
        ensureWithin(Duration.seconds(10).asSeconds(), cycle.untilNext().asSeconds(), 1.0);
        ensureWithin(now.add(Duration.seconds(10)).asSeconds(), cycle.next().asSeconds(), 1.0);
    }
}
