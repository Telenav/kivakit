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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramCount;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.interfaces.value.DoubleValued;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;

/**
 * A level from 0 to 1
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramCount.class)
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class Level implements
        Comparable<Level>,
        DoubleValued
{
    /** Level 0.0 */
    public static final Level ZERO = new Level(0);

    /** Level 1.0 */
    public static final Level ONE = new Level(1);

    /** The minimum level */
    public static Level MINIMUM = ZERO;

    /** The maximum level */
    public static Level MAXIMUM = ONE;

    /**
     * Returns a level for the given byte
     */
    public static Level levelForByte(byte level)
    {
        return new Level((double) level / Byte.MAX_VALUE);
    }

    /**
     * Parses the given value into a {@link Level}, reporting any problems to the given listener. If lenient is true,
     * values less than 0 are resolved to zero, and levels greater than one are resolved to one.
     *
     * @param listener The listener
     * @param text The text to parse
     * @param lenient True to allow out-of-range values
     * @return The level
     */
    public static Level parseLevel(Listener listener, String text, boolean lenient)
    {
        var level = Double.parseDouble(text);
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
        listener.warning("Invalid level ${debug}", text);
        return null;
    }

    /** The level, between 0 and 1, inclusive */
    private double value;

    public Level(double value)
    {
        ensure(value >= 0.0, "Level " + value + " cannot be less than zero");
        ensure(value <= 1.0, "Level " + value + " cannot be greater than one");

        this.value = value;
    }

    protected Level()
    {
    }

    /**
     * Returns this level as a byte value
     */
    @Override
    public byte asByte()
    {
        return (byte) (Byte.MAX_VALUE * value);
    }

    /**
     * Returns this level
     */
    public Level asLevel()
    {
        return this;
    }

    /**
     * Returns this level as a percentage between 0 and 100
     */
    public Percent asPercent()
    {
        return new Percent(value * 100.0);
    }

    /**
     * Returns this level as a value from 0 to 1, inclusive
     */
    public double asZeroToOne()
    {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Level that)
    {
        return Double.compare(value, that.value);
    }

    /**
     * Divides this level by the given level
     */
    public Level dividedBy(Level that)
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
    public double doubleValue()
    {
        return value;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * Returns this level minus the given level
     */
    public Level minus(Level that)
    {
        if (isLessThan(that))
        {
            throw new IllegalStateException("Level to subtract(" + that.asZeroToOne() + ") has to be smaller than " + value);
        }
        return onNewInstance(asZeroToOne() - that.asZeroToOne());
    }

    /**
     * Returns this level plus the given level
     */
    public Level plus(Level that)
    {
        return onNewInstance(asZeroToOne() + that.asZeroToOne());
    }

    /**
     * Returns the reciprocal of this level (1.0 / level)
     */
    public Level reciprocal()
    {
        return onNewInstance(1.0 / value);
    }

    /**
     * Returns this level times the given level
     */
    public Level times(Level that)
    {
        return onNewInstance(value * that.value);
    }

    @Override
    public String toString()
    {
        return Double.toString(value);
    }

    /**
     * Factory method that can be overridden in subclasses
     */
    protected Level onNewInstance(double value)
    {
        return new Level(value);
    }
}
