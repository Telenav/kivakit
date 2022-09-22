////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.internal.tests.core.time;

import com.telenav.kivakit.internal.testing.CoreUnitTest;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.value.level.Percent;
import org.junit.Test;

import java.util.HashMap;

import static com.telenav.kivakit.core.time.Duration.ONE_DAY;
import static com.telenav.kivakit.core.time.Duration.ONE_HOUR;
import static com.telenav.kivakit.core.time.Duration.ONE_MINUTE;
import static com.telenav.kivakit.core.time.Duration.ONE_SECOND;
import static com.telenav.kivakit.core.time.Duration.Restriction.ALLOW_NEGATIVE;
import static com.telenav.kivakit.core.time.Duration.Restriction.FORCE_POSITIVE;
import static com.telenav.kivakit.core.time.Duration.ZERO_DURATION;
import static com.telenav.kivakit.core.time.Duration.days;
import static com.telenav.kivakit.core.time.Duration.hours;
import static com.telenav.kivakit.core.time.Duration.milliseconds;
import static com.telenav.kivakit.core.time.Duration.minutes;
import static com.telenav.kivakit.core.time.Duration.nanoseconds;
import static com.telenav.kivakit.core.time.Duration.parseDuration;
import static com.telenav.kivakit.core.time.Duration.seconds;
import static com.telenav.kivakit.core.time.Duration.untilNextSecond;
import static com.telenav.kivakit.core.time.Duration.weeks;
import static com.telenav.kivakit.core.time.Duration.years;
import static com.telenav.kivakit.core.value.count.Count._0;
import static com.telenav.kivakit.core.value.count.Range.rangeInclusive;

/**
 * Tests for {@link Duration}
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("EqualsWithItself")
public class DurationTest extends CoreUnitTest
{
    @Test
    public void testAdd()
    {
        ensureEqual(seconds(2), ONE_SECOND.plus(ONE_SECOND));
        ensureEqual(seconds(2), ONE_SECOND.plus(ONE_SECOND, FORCE_POSITIVE));
        ensureEqual(seconds(2), ONE_SECOND.plus(ONE_SECOND, ALLOW_NEGATIVE));
    }

    @Test
    public void testCompareTo()
    {
        ensureEqual(-1, ONE_SECOND.compareTo(ONE_HOUR));
        ensureEqual(0, ONE_SECOND.compareTo(ONE_SECOND));
        ensureEqual(1, ONE_HOUR.compareTo(ONE_SECOND));
    }

    @Test
    public void testConstruction()
    {
        ensureEqual(60.0, milliseconds(60).asMilliseconds());
        ensureEqual(0L, nanoseconds(600_000).milliseconds());
        ensureEqual(0.6, nanoseconds(600_000).asMilliseconds());
        ensureEqual(0.4, nanoseconds(400_000).asMilliseconds());
        ensureEqual(0L, nanoseconds(400_000).milliseconds());
        ensureEqual(60.0, seconds(60).asSeconds());
        ensureEqual(1.0, seconds(60).asMinutes());
        ensureEqual(1.0, minutes(60).asHours());
        ensureEqual(1.0, hours(24).asDays());
        ensureEqual(1.0, days(7).asWeeks());
    }

    @Test
    public void testDifference()
    {
        ensureEqual(seconds(1), seconds(2).difference(seconds(1)));
        ensureEqual(seconds(1), seconds(1).difference(seconds(2)));
    }

    @Test
    public void testDividedBy()
    {
        ensureEqual(hours(1), ONE_DAY.dividedBy(24));
        ensureEqual(24.0, ONE_DAY.dividedBy(ONE_HOUR));
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    public void testEquals()
    {
        ensure(ONE_SECOND.equals(ONE_SECOND));
        ensureFalse(ONE_SECOND.equals(seconds(1.01)));
        ensureEqual(weeks(1), days(7));
        ensureEqual(days(1.5), hours(36));
        ensureEqual(hours(1.5), minutes(90));
        ensureEqual(minutes(1.5), seconds(90));

        var map = new HashMap<Duration, Integer>();

        rangeInclusive(_0, count(59)).forEachInt(at -> map.put(seconds(at), at));
        rangeInclusive(_0, count(59)).forEachInt(at -> ensureEqual(map.get(seconds(at)), at));
    }

    @Test
    public void testHashCode()
    {
        for (var i = 0; i < 100; i++)
        {
            ensureEqual(seconds(i).hashCode(), seconds(i).hashCode());
        }
    }

    @Test
    public void testIsApproximately()
    {
        ensure(ONE_SECOND.isApproximately(ONE_SECOND, ZERO_DURATION));
        ensure(ONE_SECOND.isApproximately(seconds(1.01), seconds(0.1)));
        ensureFalse(ONE_SECOND.isApproximately(seconds(1.01), seconds(0.0001)));
    }

    @Test
    public void testIsNone()
    {
        ensure(ZERO_DURATION.isZero());
        ensure(seconds(0.1).isNonZero());
        ensure(Duration.milliseconds(1).isNonZero());
    }

    @Test
    public void testIsSome()
    {
        ensureFalse(ZERO_DURATION.isNonZero());
        ensure(seconds(0.1).isNonZero());
        ensure(Duration.milliseconds(1).isNonZero());
    }

    @Test
    public void testLessThanGreaterThan()
    {
        ensure(seconds(5).isLessThan(ONE_DAY));
        ensureFalse(seconds(5).isLessThan(ONE_SECOND));
        ensure(seconds(5).isLessThanOrEqualTo(ONE_DAY));
        ensureFalse(seconds(5).isLessThanOrEqualTo(ONE_SECOND));
        ensureFalse(seconds(5).isGreaterThan(ONE_DAY));
        ensure(seconds(5).isGreaterThan(ONE_SECOND));
        ensureFalse(seconds(5).isGreaterThanOrEqualTo(ONE_DAY));
        ensure(seconds(5).isGreaterThanOrEqualTo(ONE_SECOND));
        ensure(seconds(5).isGreaterThanOrEqualTo(seconds(5)));
        ensure(seconds(5).isLessThanOrEqualTo(seconds(5)));
    }

    @Test
    public void testLongerBy()
    {
        ensureEqual(seconds(2), ONE_SECOND.longerBy(Percent._100));
        ensureEqual(seconds(1.5), ONE_SECOND.longerBy(Percent._50));
    }

    @Test
    public void testMaximum()
    {
        ensureEqual(ONE_HOUR, ONE_SECOND.maximum(ONE_HOUR));
    }

    @Test
    public void testMilliseconds()
    {
        ensureEqual(minutes(1).milliseconds(), 60L * 1000);
    }

    @Test
    public void testMinimum()
    {
        ensureEqual(ONE_SECOND, ONE_SECOND.minimum(ONE_HOUR));
    }

    @Test
    public void testMinus()
    {
        ensureEqual(seconds(1), seconds(2).minus(seconds(1)));
        ensureEqual(ZERO_DURATION, seconds(2).minus(seconds(3)));
        ensureEqual(-1.0, seconds(2).minus(seconds(3), ALLOW_NEGATIVE).asSeconds());
    }

    @Test
    public void testModulus()
    {
        ensureEqual(ONE_SECOND, ONE_MINUTE.plus(ONE_SECOND).modulo(ONE_MINUTE));
    }

    @Test
    public void testNearestHour()
    {
        ensureEqual(hours(2), hours(1.6).nearestHour());
        ensureEqual(hours(2), hours(1.5001).nearestHour());
        ensureEqual(hours(1), hours(1.4).nearestHour());
        ensureEqual(hours(1), hours(1.4999).nearestHour());
    }

    @Test
    public void testParse()
    {
        ensureThrows(() -> parseDuration("5"));
        ensureThrows(() -> parseDuration("5ms.5"));
        ensureThrows(() -> parseDuration("a5ms"));
        ensureThrows(() -> parseDuration("mambo"));
        ensureThrows(() -> parseDuration("5.4mambo"));

        ensureEqual(parseDuration("1 ms"), milliseconds(1));
        ensureEqual(parseDuration("2 ms"), milliseconds(2));
        ensureEqual(parseDuration("1ms"), milliseconds(1));
        ensureEqual(parseDuration("2ms"), milliseconds(2));
        ensureEqual(parseDuration("1 millisecond"), milliseconds(1));
        ensureEqual(parseDuration("2 milliseconds"), milliseconds(2));
        ensureEqual(parseDuration("1.5 millisecond"), milliseconds(1.5));
        ensureEqual(parseDuration("2.5 milliseconds"), milliseconds(2.5));

        ensureEqual(parseDuration("1s"), seconds(1));
        ensureEqual(parseDuration("2s"), seconds(2));
        ensureEqual(parseDuration("1 second"), seconds(1));
        ensureEqual(parseDuration("2 seconds"), seconds(2));
        ensureEqual(parseDuration("1.5 second"), seconds(1.5));
        ensureEqual(parseDuration("2.5 seconds"), seconds(2.5));

        ensureEqual(parseDuration("1m"), minutes(1));
        ensureEqual(parseDuration("2m"), minutes(2));
        ensureEqual(parseDuration("1 minute"), minutes(1));
        ensureEqual(parseDuration("2 minutes"), minutes(2));
        ensureEqual(parseDuration("1.5 minute"), minutes(1.5));
        ensureEqual(parseDuration("2.5 minutes"), minutes(2.5));

        ensureEqual(parseDuration("1h"), hours(1));
        ensureEqual(parseDuration("2h"), hours(2));
        ensureEqual(parseDuration("1 hour"), hours(1));
        ensureEqual(parseDuration("2 hours"), hours(2));
        ensureEqual(parseDuration("1.5 hour"), hours(1.5));
        ensureEqual(parseDuration("2.5 hours"), hours(2.5));

        ensureEqual(parseDuration("1d"), days(1));
        ensureEqual(parseDuration("2d"), days(2));
        ensureEqual(parseDuration("1 day"), days(1));
        ensureEqual(parseDuration("2 days"), days(2));
        ensureEqual(parseDuration("1.5 day"), days(1.5));
        ensureEqual(parseDuration("2.5 days"), days(2.5));

        ensureEqual(parseDuration("1 week"), weeks(1));
        ensureEqual(parseDuration("2 weeks"), weeks(2));
        ensureEqual(parseDuration("1.5 week"), weeks(1.5));
        ensureEqual(parseDuration("2.5 weeks"), weeks(2.5));

        ensureEqual(parseDuration("1 year"), years(1));
        ensureEqual(parseDuration("2 years"), years(2));
        ensureEqual(parseDuration("1.5 year"), years(1.5));
        ensureEqual(parseDuration("2.5 years"), years(2.5));
    }

    @Test
    public void testPercent()
    {
        ensureEqual(Percent._100, ONE_SECOND.percentageOf(ONE_SECOND));
        ensureEqual(Percent._50, ONE_SECOND.percentageOf(seconds(2)));
    }

    @Test
    public void testPlus()
    {
        ensureEqual(seconds(2), ONE_SECOND.plus(ONE_SECOND));
    }

    @Test
    public void testShorterBy()
    {
        ensureEqual(seconds(0), ONE_SECOND.shorterBy(Percent._100));
        ensureEqual(seconds(0.5), ONE_SECOND.shorterBy(Percent._50));
    }

    @Test
    public void testTimes()
    {
        ensureEqual(seconds(5), ONE_SECOND.times(5));
    }

    @Test
    public void testUntilNextSecond()
    {
        ensure(untilNextSecond().isLessThanOrEqualTo(seconds(1)));
    }
}
