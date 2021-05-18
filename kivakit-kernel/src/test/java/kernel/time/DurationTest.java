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

package kernel.time;

import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.values.level.Percent;
import org.junit.Test;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureEqual;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureFalse;

public class DurationTest
{
    @Test
    public void testAdd()
    {
        ensureEqual(Duration.seconds(2), Duration.ONE_SECOND.add(Duration.ONE_SECOND));
    }

    @Test
    public void testCompareTo()
    {
        ensureEqual(-1, Duration.ONE_SECOND.compareTo(Duration.ONE_HOUR));
        //noinspection EqualsWithItself
        ensureEqual(0, Duration.ONE_SECOND.compareTo(Duration.ONE_SECOND));
        ensureEqual(1, Duration.ONE_HOUR.compareTo(Duration.ONE_SECOND));
    }

    @Test
    public void testConstruction()
    {
        ensureEqual(60.0, Duration.seconds(60).asSeconds());
        ensureEqual(1.0, Duration.seconds(60).asMinutes());
        ensureEqual(1.0, Duration.minutes(60).asHours());
        ensureEqual(1.0, Duration.hours(24).asDays());
        ensureEqual(1.0, Duration.days(7).asWeeks());
    }

    @Test
    public void testDifference()
    {
        ensureEqual(Duration.seconds(1), Duration.seconds(2).difference(Duration.seconds(1)));
        ensureEqual(Duration.seconds(1), Duration.seconds(1).difference(Duration.seconds(2)));
    }

    @Test
    public void testDivide()
    {
        ensureEqual(Duration.hours(1), Duration.ONE_DAY.divide(24));
        ensureEqual(24.0, Duration.ONE_DAY.divide(Duration.ONE_HOUR));
    }

    @Test
    public void testEquals()
    {
        ensure(Duration.ONE_SECOND.equals(Duration.ONE_SECOND));
        ensureFalse(Duration.ONE_SECOND.equals(Duration.seconds(1.01)));
    }

    @Test
    public void testFromStartOfWeekModuloWeekLength()
    {
        ensureEqual("MONDAY, 00:00", Duration.minutes(0).fromStartOfWeekModuloWeekLength());
        ensureEqual("MONDAY, 00:00", Duration.minutes(10080).fromStartOfWeekModuloWeekLength());
        ensureEqual("MONDAY, 02:40", Duration.minutes(160).fromStartOfWeekModuloWeekLength());
        ensureEqual("WEDNESDAY, 02:40", Duration.minutes(160 + 2 * 1440).fromStartOfWeekModuloWeekLength());
        ensureEqual("FRIDAY, 15:40", Duration.minutes(940 + 4 * 1440).fromStartOfWeekModuloWeekLength());
    }

    @Test
    public void testHashCode()
    {
        for (var i = 0; i < 100; i++)
        {
            ensureEqual(Duration.seconds(i).hashCode(), Duration.seconds(i).hashCode());
        }
    }

    @Test
    public void testIsApproximately()
    {
        ensure(Duration.ONE_SECOND.isApproximately(Duration.ONE_SECOND, Duration.NONE));
        ensure(Duration.ONE_SECOND.isApproximately(Duration.seconds(1.01), Duration.seconds(0.1)));
        ensureFalse(Duration.ONE_SECOND.isApproximately(Duration.seconds(1.01), Duration.seconds(0.0001)));
    }

    @Test
    public void testIsNone()
    {
        ensure(Duration.NONE.isNone());
        ensureFalse(Duration.seconds(0.1).isNone());
        ensureFalse(Duration.milliseconds(1).isNone());
    }

    @Test
    public void testLessThanGreaterThan()
    {
        ensure(Duration.seconds(5).isLessThan(Duration.ONE_DAY));
        ensureFalse(Duration.seconds(5).isLessThan(Duration.ONE_SECOND));
        ensure(Duration.seconds(5).isLessThanOrEqualTo(Duration.ONE_DAY));
        ensureFalse(Duration.seconds(5).isLessThanOrEqualTo(Duration.ONE_SECOND));
        ensureFalse(Duration.seconds(5).isGreaterThan(Duration.ONE_DAY));
        ensure(Duration.seconds(5).isGreaterThan(Duration.ONE_SECOND));
        ensureFalse(Duration.seconds(5).isGreaterThanOrEqualTo(Duration.ONE_DAY));
        ensure(Duration.seconds(5).isGreaterThanOrEqualTo(Duration.ONE_SECOND));
        ensure(Duration.seconds(5).isGreaterThanOrEqualTo(Duration.seconds(5)));
        ensure(Duration.seconds(5).isLessThanOrEqualTo(Duration.seconds(5)));
    }

    @Test
    public void testMaximum()
    {
        ensureEqual(Duration.ONE_HOUR, Duration.ONE_SECOND.maximum(Duration.ONE_HOUR));
    }

    @Test
    public void testMinimum()
    {
        ensureEqual(Duration.ONE_SECOND, Duration.ONE_SECOND.minimum(Duration.ONE_HOUR));
    }

    @Test
    public void testModulus()
    {
        ensureEqual(Duration.ONE_SECOND, Duration.ONE_MINUTE.add(Duration.ONE_SECOND).modulus(Duration.ONE_MINUTE));
    }

    @Test
    public void testNearestHour()
    {
        ensureEqual(Duration.hours(2), Duration.hours(1.6).nearestHour());
        ensureEqual(Duration.hours(2), Duration.hours(1.5001).nearestHour());
        ensureEqual(Duration.hours(1), Duration.hours(1.4).nearestHour());
        ensureEqual(Duration.hours(1), Duration.hours(1.4999).nearestHour());
    }

    @Test
    public void testPercentage()
    {
        ensureEqual(Percent._100, Duration.ONE_SECOND.percentageOf(Duration.ONE_SECOND));
        ensureEqual(Percent._50, Duration.ONE_SECOND.percentageOf(Duration.seconds(2)));
    }

    @Test
    public void testPlus()
    {
        ensureEqual(Duration.seconds(2), Duration.ONE_SECOND.plus(Duration.ONE_SECOND));
    }

    @Test
    public void testSubtract()
    {
        ensureEqual(Duration.seconds(1), Duration.seconds(2).minus(Duration.seconds(1)));
        ensureEqual(Duration.NONE, Duration.seconds(2).minus(Duration.seconds(3)));
    }

    @Test
    public void testTimes()
    {
        ensureEqual(Duration.seconds(5), Duration.ONE_SECOND.times(5));
    }
}
