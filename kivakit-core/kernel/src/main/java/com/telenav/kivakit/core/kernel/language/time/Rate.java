////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.time;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageTime;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramLanguageTime.class)
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
        if (isGreaterThan(that))
        {
            return 1;
        }
        if (isLessThan(that))
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

    public boolean isGreaterThan(final Rate that)
    {
        return perMinute().count > that.perMinute().count;
    }

    public boolean isLessThan(final Rate that)
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
        if (isGreaterThan(maximumRate))
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
