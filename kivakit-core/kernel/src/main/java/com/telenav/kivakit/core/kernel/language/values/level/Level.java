////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.values.level;

import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitExcludeProperty;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A level from 0 to 1
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageValue.class)
public class Level
{
    public static final Level ZERO = new Level(0);

    public static final Level ONE = new Level(1);

    public static Level MINIMUM = ZERO;

    public static Level MAXIMUM = ONE;

    public static Level levelForByte(final byte level)
    {
        return new Level((double) level / Byte.MAX_VALUE);
    }

    public static class Converter extends BaseStringConverter<Level>
    {
        private boolean lenient;

        public Converter(final Listener listener)
        {
            super(listener);
        }

        public void lenient(final boolean lenient)
        {
            this.lenient = lenient;
        }

        @Override
        protected Level onConvertToObject(final String value)
        {
            final var level = Double.parseDouble(value);
            if (level >= 0 && level <= 1.0)
            {
                return new Level(level);
            }
            if (lenient)
            {
                if (level < 0)
                {
                    return ZERO;
                }
                if (level > 1)
                {
                    return ONE;
                }
            }
            warning("Invalid level ${debug}", value);
            return null;
        }
    }

    private double value;

    public Level(final double value)
    {
        Ensure.ensure(value >= 0.0, "Level " + value + " cannot be less than zero");
        Ensure.ensure(value <= 1.0, "Level " + value + " cannot be greater than one");
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

    public Percent asPercentage()
    {
        return new Percent(value * 100.0);
    }

    public double asZeroToOne()
    {
        return value;
    }

    public int compareTo(final Level that)
    {
        return Double.compare(value, that.value);
    }

    public Level divide(final Level that)
    {
        if (that.value == 0)
        {
            return ZERO;
        }
        else
        {
            final var result = value / that.value;
            return result > 1 ? onNewInstance(1.0) : onNewInstance(result);
        }
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof Level)
        {
            final var that = (Level) object;
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

    public boolean isClose(final Level that, final double margin)
    {
        return Math.abs(asZeroToOne() - that.asZeroToOne()) < margin;
    }

    public boolean isGreaterThan(final Level that)
    {
        return value > that.value;
    }

    public boolean isGreaterThanOrEqualTo(final Level that)
    {
        return value >= that.value;
    }

    public boolean isLessThan(final Level that)
    {
        return value < that.value;
    }

    public boolean isLessThanOrEqualTo(final Level that)
    {
        return value <= that.value;
    }

    @KivaKitExcludeProperty
    public boolean isZero()
    {
        return asZeroToOne() == 0.0;
    }

    public Level minus(final Level that)
    {
        if (isLessThan(that))
        {
            Ensure.fail("Level to subtract(" + that.asZeroToOne() + ") has to be smaller than " + value);
        }
        return onNewInstance(asZeroToOne() - that.asZeroToOne());
    }

    public Level plus(final Level that)
    {
        return onNewInstance(asZeroToOne() + that.asZeroToOne());
    }

    public Level reciprocal()
    {
        return onNewInstance(1.0 / value);
    }

    public Level times(final Level that)
    {
        return onNewInstance(value * that.value);
    }

    @Override
    public String toString()
    {
        return Double.toString(value);
    }

    protected Level onNewInstance(final double value)
    {
        return new Level(value);
    }
}
