package com.telenav.kivakit.core.time;

import org.junit.Test;

import static com.telenav.kivakit.core.ensure.Ensure.ensureEqual;
import static com.telenav.kivakit.core.time.Duration.minutes;

/**
 * Tests for {@link AverageDuration}
 *
 * @author jonathanl (shibo)
 */
public class AverageDurationTest
{
    @Test
    public void test()
    {
        var average = new AverageDuration();
        average.add(minutes(30));
        average.add(minutes(15));
        average.add(minutes(45));
        ensureEqual(average.averageDuration().asMilliseconds(), 30);
        ensureEqual(average.average(), minutes(30).asMilliseconds());
        ensureEqual(average.samples(), 3);
        ensureEqual(average.maximum(), minutes(45).asMilliseconds());
        ensureEqual(average.maximumDuration(), minutes(45));
        ensureEqual(average.minimumDuration(), minutes(15));
        ensureEqual(average.totalDuration(), minutes(90));
    }
}
