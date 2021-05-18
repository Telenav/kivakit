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

package com.telenav.kivakit.kernel.language.time;

import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageTime;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * An abstract rate in {@link Count} per {@link Duration}. Rates can be constructed as a count per Duration with {@link
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
@UmlClassDiagram(diagram = DiagramLanguageTime.class)
@LexakaiJavadoc(complete = true)
public class Rate implements Comparable<Rate>
{
    public static final Rate MAXIMUM = new Rate(Integer.MAX_VALUE, Duration.milliseconds(1));

    public static Rate perDay(final double count)
    {
        return new Rate(count, Duration.ONE_DAY);
    }

    public static Rate perHour(final double count)
    {
        return new Rate(count, Duration.ONE_HOUR);
    }

    public static Rate perMinute(final double count)
    {
        return new Rate(count, Duration.ONE_MINUTE);
    }

    public static Rate perSecond(final double count)
    {
        return new Rate(count, Duration.ONE_SECOND);
    }

    private final Duration duration;

    private final double count;

    public Rate(final double count, final Duration duration)
    {
        this.count = count;
        this.duration = duration;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(final Rate that)
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
    public boolean equals(final Object object)
    {
        if (object instanceof Rate)
        {
            final var that = (Rate) object;
            return perMinute().count() == that.perMinute().count();
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Double.hashCode(count / duration.asMinutes());
    }

    public boolean isFasterThan(final Rate that)
    {
        return perMinute().count > that.perMinute().count;
    }

    public boolean isSlowerThan(final Rate that)
    {
        return perMinute().count < that.perMinute().count;
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

    public void throttle(final Rate maximumRate)
    {
        if (isFasterThan(maximumRate))
        {
            Duration.seconds(perSecond().count() / maximumRate.perSecond().count() - 1).sleep();
        }
    }

    @Override
    public String toString()
    {
        final var formatted = String.format(count < 100 ? "%,.1f" : "%,.0f", count);
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
