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

package com.telenav.kivakit.kernel.language.values.level;

import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitExcludeProperty;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
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

    /**
     * Converts to and from a {@link Level}
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
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
        protected Level onToValue(final String value)
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

    public Percent asPercent()
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
