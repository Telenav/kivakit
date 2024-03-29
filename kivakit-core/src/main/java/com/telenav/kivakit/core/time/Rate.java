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

package com.telenav.kivakit.core.time;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramTime;
import com.telenav.kivakit.core.string.AsString;
import com.telenav.kivakit.interfaces.numeric.Maximizable;
import com.telenav.kivakit.interfaces.numeric.Minimizable;
import com.telenav.kivakit.interfaces.value.DoubleValued;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.language.Hash.hash;
import static com.telenav.kivakit.core.time.Duration.ONE_DAY;
import static com.telenav.kivakit.core.time.Duration.ONE_HOUR;
import static com.telenav.kivakit.core.time.Duration.ONE_MINUTE;
import static com.telenav.kivakit.core.time.Duration.ONE_SECOND;
import static com.telenav.kivakit.core.time.Duration.ONE_YEAR;
import static com.telenav.kivakit.core.time.Duration.milliseconds;
import static com.telenav.kivakit.core.time.Duration.seconds;
import static com.telenav.kivakit.core.value.count.Bytes.bytes;
import static java.lang.String.format;

/**
 * An abstract rate in <i>count</i> per {@link Duration}. Rates can be constructed as a count per Duration with
 * {@link #Rate(double, Duration)} or with static factory methods:
 *
 * <ul>
 *     <li>{@link #perDay(double)}</li>
 *     <li>{@link #perHour(double)}</li>
 *     <li>{@link #perMinute(double)}</li>
 *     <li>{@link #perSecond(double)}</li>
 *     <li>{@link #perYear(double)}</li>
 * </ul>
 * <p>
 * Rates can be compared with {@link #compareTo(Rate)}, {@link #isFasterThan(Rate)} and {@link #isSlowerThan(Rate)}.
 * </p>
 *
 * <p>
 * The method {@link #throttle(Rate)} will delay (if this rate is greater than the given maximum) long enough to
 * limit this rate to the maximum rate.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramTime.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class Rate implements
        Comparable<Rate>,
        Maximizable<Rate>,
        Minimizable<Rate>,
        DoubleValued,
        AsString
{
    public static final Rate MAXIMUM_RATE = new Rate(Integer.MAX_VALUE, milliseconds(1));

    /**
     * Returns the given rate per day
     */
    public static Rate perDay(double count)
    {
        return new Rate(count, ONE_DAY);
    }

    /**
     * Returns the given rate per hour
     */
    public static Rate perHour(double count)
    {
        return new Rate(count, ONE_HOUR);
    }

    /**
     * Returns the given rate per minute
     */
    public static Rate perMinute(double count)
    {
        return new Rate(count, ONE_MINUTE);
    }

    /**
     * Returns the given rate per second
     */
    public static Rate perSecond(double count)
    {
        return new Rate(count, ONE_SECOND);
    }

    /**
     * Returns the given rate per year
     */
    public static Rate perYear(double count)
    {
        return new Rate(count, ONE_YEAR);
    }

    private final Duration duration;

    private final double count;

    public Rate(double count, Duration duration)
    {
        this.count = count;
        this.duration = duration;
    }

    public String asBytesPerSecond()
    {
        return bytes(perSecond().count) + " / sec";
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(Rate that)
    {
        if (isFasterThan(that))
        {
            return 1;
        }
        if (isSlowerThan(that))
        {
            return -1;
        }
        return 0;
    }

    public double count()
    {
        return count;
    }

    @Override
    public double doubleValue()
    {
        return perDay().doubleValue();
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Rate that)
        {
            return perMinute().count() == that.perMinute().count();
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return hash(count / duration.asMinutes());
    }

    /**
     * Returns true if this rate is faster than the given rate
     */
    public boolean isFasterThan(Rate that)
    {
        return perMinute().count > that.perMinute().count;
    }

    /**
     * Returns true if this rate is slower than the given rate
     */
    public boolean isSlowerThan(Rate that)
    {
        return perMinute().count < that.perMinute().count;
    }

    @Override
    public long longValue()
    {
        return perDay().longValue();
    }

    @Override
    public Rate map(Long value)
    {
        return perDay(value);
    }

    @Override
    public Rate maximum()
    {
        return MAXIMUM_RATE;
    }

    @Override
    public Rate minimum()
    {
        return perYear(0);
    }

    /**
     * This rate converted to cycles per day
     */
    public Rate perDay()
    {
        return new Rate(count / duration.asDays(), ONE_DAY);
    }

    /**
     * This rate converted to cycles per hour
     */
    public Rate perHour()
    {
        return new Rate(count / duration.asHours(), ONE_HOUR);
    }

    /**
     * This rate converted to cycles per minute
     */
    public Rate perMinute()
    {
        return new Rate(count / duration.asMinutes(), ONE_MINUTE);
    }

    /**
     * This rate converted to cycles per second
     */
    public Rate perSecond()
    {
        return new Rate(count / duration.asSeconds(), ONE_SECOND);
    }

    /**
     * This rate converted to cycles per year
     */
    public Rate perYear()
    {
        return new Rate(count / duration.asYears(), ONE_YEAR);
    }

    /**
     * This rate plus the given rate
     */
    public Rate plus(Rate that)
    {
        return perDay(perDay().count() + that.perDay().count());
    }

    /**
     * Sleeps the appropriate amount of time to make the rates equal, if this rate is faster than the given maximum
     * rate
     */
    public void throttle(Rate maximumRate)
    {
        if (isFasterThan(maximumRate))
        {
            seconds(perSecond().count() / maximumRate.perSecond().count() - 1).sleep();
        }
    }

    @Override
    public String toString()
    {
        var formatted = format(count < 100 ? "%,.1f" : "%,.0f", count);
        if (duration.equals(ONE_SECOND))
        {
            return formatted + " / sec";
        }
        if (duration.equals(ONE_MINUTE))
        {
            return formatted + " / min";
        }
        if (duration.equals(ONE_HOUR))
        {
            return formatted + " / hour";
        }
        return formatted + " per " + duration;
    }
}
