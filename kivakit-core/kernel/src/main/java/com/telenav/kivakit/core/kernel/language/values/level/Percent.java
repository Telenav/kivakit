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

package com.telenav.kivakit.core.kernel.language.values.level;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.language.primitives.Doubles;
import com.telenav.kivakit.core.kernel.language.strings.Strip;
import com.telenav.kivakit.core.kernel.messaging.Listener;

import java.util.Objects;

/**
 * A percentage of any range (not only from 0 to 100%).
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageValue.class)
public final class Percent
{
    public static final Percent _0 = new Percent(0);

    public static final Percent _50 = new Percent(50);

    public static final Percent _100 = new Percent(100);

    public static Percent percent(final double percent)
    {
        return new Percent(percent);
    }

    public static class Converter extends BaseStringConverter<Percent>
    {
        public Converter(final Listener listener)
        {
            super(listener);
        }

        @Override
        protected Percent onConvertToObject(final String value)
        {
            return new Percent(Double.parseDouble(value.endsWith("%") ? Strip.ending(value, "%") : value));
        }
    }

    private double value;

    public Percent(final double value)
    {
        this.value = value;
    }

    protected Percent()
    {
    }

    public int asInt()
    {
        return (int) value;
    }

    public Level asLevel()
    {
        return new Level(asZeroToOne());
    }

    /**
     * @return This percentage divided by 100, from 0.0 to the maximum double value.
     */
    public double asUnitValue()
    {
        return Doubles.inRange(value / 100.0, 0, Double.MAX_VALUE);
    }

    /**
     * @return This percentage as a value from 0 to 1, both inclusive.
     */
    public double asZeroToOne()
    {
        return Doubles.inRange(value / 100.0, 0.0, 1.0);
    }

    public Percent dividedBy(final double scaleFactor)
    {
        return new Percent(value / scaleFactor);
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof Percent)
        {
            final Percent that = (Percent) object;
            return value == that.value;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(value);
    }

    public Percent inverse()
    {
        return new Percent(100.0 - asZeroToOne());
    }

    public boolean isGreaterThan(final Percent that)
    {
        return value > that.value;
    }

    public boolean isGreaterThanOrEqualTo(final Percent that)
    {
        return value >= that.value;
    }

    public boolean isLessThan(final Percent that)
    {
        return value < that.value;
    }

    public boolean isLessThanOrEqualTo(final Percent that)
    {
        return value <= that.value;
    }

    public Percent minus(final Percent that)
    {
        return new Percent(value - that.value);
    }

    public Percent plus(final Percent that)
    {
        return new Percent(value + that.value);
    }

    public double scale(final double value)
    {
        return value * asUnitValue();
    }

    public long scale(final long value)
    {
        return (long) (value * asUnitValue());
    }

    public int scale(final int value)
    {
        return (int) (value * asUnitValue());
    }

    public Percent times(final double scaleFactor)
    {
        return new Percent(value * scaleFactor);
    }

    @Override
    public String toString()
    {
        return Doubles.format(value, 1) + "%";
    }

    public double value()
    {
        return value;
    }
}
