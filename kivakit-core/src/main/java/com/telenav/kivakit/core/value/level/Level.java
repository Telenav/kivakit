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

package com.telenav.kivakit.core.value.level;

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.project.lexakai.DiagramCount;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A level from 0 to 1
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramCount.class)
public class Level
{
    public static final Level ZERO = new Level(0);

    public static final Level ONE = new Level(1);

    public static Level MINIMUM = ZERO;

    public static Level MAXIMUM = ONE;

    public static Level levelForByte(byte level)
    {
        return new Level((double) level / Byte.MAX_VALUE);
    }

    public static Level parseLevel(Listener listener, String value, boolean lenient)
    {
        var level = Double.parseDouble(value);
        if (level >= 0 && level <= 1.0)
        {
            return new Level(level);
        }
        if (lenient)
        {
            if (level < 0)
            {
                return Level.ZERO;
            }
            if (level > 1)
            {
                return Level.ONE;
            }
        }
        listener.warning("Invalid level ${debug}", value);
        return null;
    }

    private double value;

    public Level(double value)
    {
        assert value >= 0.0 : "Level " + value + " cannot be less than zero";
        assert value <= 1.0 : "Level " + value + " cannot be greater than one";

        this.value = value;
    }

    protected Level()
    {
    }

    public byte asByte()
    {
        return (byte) (Byte.MAX_VALUE * value);
    }

    public Level asLevel()
    {
        return this;
    }

    public Percent asPercent()
    {
        return new Percent(value * 100.0);
    }

    public double asZeroToOne()
    {
        return value;
    }

    public int compareTo(Level that)
    {
        return Double.compare(value, that.value);
    }

    public Level divide(Level that)
    {
        if (that.value == 0)
        {
            return ZERO;
        }
        else
        {
            var result = value / that.value;
            return result > 1 ? onNewInstance(1.0) : onNewInstance(result);
        }
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Level)
        {
            var that = (Level) object;
            return value == that.value;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Double.hashCode(value);
    }

    /**
     * @return The distance of this weight from 1.0
     */
    public Level inverse()
    {
        return onNewInstance(1.0 - asZeroToOne());
    }

    public boolean isClose(Level that, double margin)
    {
        return Math.abs(asZeroToOne() - that.asZeroToOne()) < margin;
    }

    public boolean isGreaterThan(Level that)
    {
        return value > that.value;
    }

    public boolean isGreaterThanOrEqualTo(Level that)
    {
        return value >= that.value;
    }

    public boolean isLessThan(Level that)
    {
        return value < that.value;
    }

    public boolean isLessThanOrEqualTo(Level that)
    {
        return value <= that.value;
    }

    public boolean isZero()
    {
        return asZeroToOne() == 0.0;
    }

    public Level minus(Level that)
    {
        if (isLessThan(that))
        {
            throw new IllegalStateException("Level to subtract(" + that.asZeroToOne() + ") has to be smaller than " + value);
        }
        return onNewInstance(asZeroToOne() - that.asZeroToOne());
    }

    public Level plus(Level that)
    {
        return onNewInstance(asZeroToOne() + that.asZeroToOne());
    }

    public Level reciprocal()
    {
        return onNewInstance(1.0 / value);
    }

    public Level times(Level that)
    {
        return onNewInstance(value * that.value);
    }

    @Override
    public String toString()
    {
        return Double.toString(value);
    }

    protected Level onNewInstance(double value)
    {
        return new Level(value);
    }
}
