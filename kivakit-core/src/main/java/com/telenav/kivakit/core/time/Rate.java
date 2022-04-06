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

import com.telenav.kivakit.core.lexakai.DiagramTime;
import com.telenav.kivakit.interfaces.numeric.Maximizable;
import com.telenav.kivakit.interfaces.numeric.Minimizable;
import com.telenav.kivakit.interfaces.numeric.Quantizable;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * An abstract rate in <i>count</i> per {@link Duration}. Rates can be constructed as a count per Duration with {@link
 * #Rate(double, Duration)} or with static factory methods:
 *
 * <ul>
 *     <li>{@link #perDay(double)}</li>
 *     <li>{@link #perHour(double)}</li>
 *     <li>{@link #perMinute(double)}</li>
 *     <li>{@link #perSecond(double)}</li>
 * </ul>
 * <p>
 * Rates can be compared with {@link #isFasterThan(Rate)} and {@link #isSlowerThan(Rate)}.
 * </p>
 *
 * <p>
 * The method {@link #throttle(Rate)} will delay (if this rate is greater than the given maximum) long enough to
 * limit this rate to the maximum rate.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramTime.class)
@LexakaiJavadoc(complete = true)
public class Rate implements
        Comparable<Rate>,
        Quantizable,
        Maximizable<Rate>,
        Minimizable<Rate>
{
    public static final Rate MAXIMUM = new Rate(Integer.MAX_VALUE, Duration.milliseconds(1));

    public static Rate perDay(double count)
    {
        return new Rate(count, Duration.ONE_DAY);
    }

    public static Rate perHour(double count)
    {
        return new Rate(count, Duration.ONE_HOUR);
    }

    public static Rate perMinute(double count)
    {
        return new Rate(count, Duration.ONE_MINUTE);
    }

    public static Rate perSecond(double count)
    {
        return new Rate(count, Duration.ONE_SECOND);
    }

    public static Rate perYear(double count)
    {
        return new Rate(count, Duration.ONE_YEAR);
    }

    private final Duration duration;

    private final double count;

    public Rate(double count, Duration duration)
    {
        this.count = count;
        this.duration = duration;
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
    public double doubleQuantum()
    {
        return perSecond().count();
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Rate)
        {
            var that = (Rate) object;
            return perMinute().count() == that.perMinute().count();
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Double.hashCode(count / duration.asMinutes());
    }

    public boolean isFasterThan(Rate that)
    {
        return perMinute().count > that.perMinute().count;
    }

    public boolean isSlowerThan(Rate that)
    {
        return perMinute().count < that.perMinute().count;
    }

    @Override
    public Rate maximum(Rate that)
    {
        return isGreaterThan(that) ? this : that;
    }

    @Override
    public Rate maximumInclusive()
    {
        return Rate.MAXIMUM;
    }

    @Override
    public Rate minimum(Rate that)
    {
        return isGreaterThan(that) ? this : that;
    }

    @Override
    public Rate minimum()
    {
        return Rate.perYear(0);
    }

    public Rate perDay()
    {
        return new Rate(count / duration.asDays(), Duration.ONE_DAY);
    }

    public Rate perHour()
    {
        return new Rate(count / duration.asHours(), Duration.ONE_HOUR);
    }

    public Rate perMinute()
    {
        return new Rate(count / duration.asMinutes(), Duration.ONE_MINUTE);
    }

    public Rate perSecond()
    {
        return new Rate(count / duration.asSeconds(), Duration.ONE_SECOND);
    }

    public Rate perYear()
    {
        return new Rate(count / duration.asYears(), Duration.ONE_YEAR);
    }

    public Rate plus(Rate that)
    {
        return Rate.perDay(perDay().count() + that.perDay().count());
    }

    @Override
    public long quantum()
    {
        return (long) perSecond().count();
    }

    public void throttle(Rate maximumRate)
    {
        if (isFasterThan(maximumRate))
        {
            Duration.seconds(perSecond().count() / maximumRate.perSecond().count() - 1).sleep();
        }
    }

    @Override
    public String toString()
    {
        var formatted = String.format(count < 100 ? "%,.1f" : "%,.0f", count);
        if (duration.equals(Duration.ONE_SECOND))
        {
            return formatted + " per second";
        }
        if (duration.equals(Duration.ONE_MINUTE))
        {
            return formatted + " per minute";
        }
        if (duration.equals(Duration.ONE_HOUR))
        {
            return formatted + " per hour";
        }
        return formatted + " per " + duration;
    }
}
