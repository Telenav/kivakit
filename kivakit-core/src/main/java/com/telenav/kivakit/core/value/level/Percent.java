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

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramCount;
import com.telenav.kivakit.core.language.primitive.Doubles;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.string.Strip;
import com.telenav.kivakit.interfaces.value.DoubleValued;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A percentage of any range (not only from 0 to 100%). A percent object can be created with {@link #percent(double)},
 * passing in a percentage, like 50 or 90 to get 50% or 90%.
 *
 * <p><b>Operations</b></p>
 *
 * <ul>
 *     <li>{@link #dividedBy(double)} - This percentage divided by the given value</li>
 *     <li>{@link #inverse()} - This percentage's distance from 100%</li>
 *     <li>{@link #minus(Percent)} - This percentage minus the given percentage</li>
 *     <li>{@link #plus(Percent)} - This percentage plus the given percentage</li>
 *     <li>{@link #scale(double)} - The given value scaled by this percentage</li>
 *     <li>{@link #scale(int)} - The given value scaled by this percentage</li>
 *     <li>{@link #scale(long)} - The given value scaled by this percentage</li>
 *     <li>{@link #times(double)} - This percentage times the given percentage</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #asInt()} - This percentage rounded to an int value</li>
 *     <li>{@link #asUnitValue()} - This percentage as a parametric value (divided by 100.0)</li>
 *     <li>{@link #asZeroToOne()} - This value as a unit value between 0 and 1, inclusive </li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused") @UmlClassDiagram(diagram = DiagramCount.class)
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public final class Percent implements
        Comparable<Percent>,
        DoubleValued
{
    /** 0% */
    public static final Percent _0 = new Percent(0);

    /** 50% */
    public static final Percent _50 = new Percent(50);

    /** 100% */
    public static final Percent _100 = new Percent(100);

    /**
     * Parses the given text into a percent
     *
     * @param listener The listener to report any errors to
     * @param text The text to parse
     * @return The percent
     */
    public static Percent parsePercent(Listener listener, String text)
    {
        try
        {
            return Percent.percent(Double.parseDouble(text.endsWith("%")
                    ? Strip.ending(text, "%")
                    : text));
        }
        catch (Exception e)
        {
            listener.problem("Unable to parse: $", text);
            return null;
        }
    }

    /**
     * Creates a {@link Percent} object
     *
     * @param percent The percent on a scale from 0 to 100 (but it can be greater or less, like 200%)
     * @return The percent
     */
    public static Percent percent(double percent)
    {
        return new Percent(percent);
    }

    private double percent;

    Percent(double percent)
    {
        this.percent = percent;
    }

    private Percent()
    {
    }

    /**
     * Returns this percent as a level between 0 and 1
     */
    public Level asLevel()
    {
        return new Level(asZeroToOne());
    }

    /**
     * This percent as a unit value, potentially greater than 1 or less than 0
     *
     * @return This percentage divided by 100
     */
    public double asUnitValue()
    {
        return percent / 100.0;
    }

    /**
     * @return This percentage as a value from 0 to 1, both inclusive.
     */
    public double asZeroToOne()
    {
        return Doubles.inRange(percent / 100.0, 0.0, 1.0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(@NotNull Percent that)
    {
        return Double.compare(percent, that.percent);
    }

    /**
     * Returns this percent divided by the given divisor
     *
     * @param divisor The divisor
     * @return The new percent
     */
    public Percent dividedBy(double divisor)
    {
        return new Percent(percent / divisor);
    }

    /**
     * Returns the double value scaled from 0 to 100%, but possibly more like 200%, or less like -50%
     */
    @Override
    public double doubleValue()
    {
        return percent;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Percent)
        {
            Percent that = (Percent) object;
            return percent == that.percent;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(percent);
    }

    /**
     * This percent as a distance from 100, for example the inverse of 25% is 75%.
     */
    public Percent inverse()
    {
        return new Percent(100.0 - asZeroToOne());
    }

    /**
     * Returns this percent minus the given percent
     */
    public Percent minus(Percent that)
    {
        return new Percent(percent - that.percent);
    }

    /**
     * Returns this percentage from 0 to 100
     */
    public double percent()
    {
        return percent;
    }

    /**
     * Returns this percent plus the given percent
     */
    public Percent plus(Percent that)
    {
        return new Percent(percent + that.percent);
    }

    /**
     * Returns this percent scaled by the given factor
     */
    public double scale(double factor)
    {
        return factor * doubleValue();
    }

    /**
     * Scales the given value by this percentage
     *
     * @param value The value
     * @return The scaled value
     */
    public long scale(long value)
    {
        return (long) (value * asUnitValue());
    }

    /**
     * Scales the given value by this percentage
     *
     * @param value The value
     * @return The scaled value
     */
    public int scale(int value)
    {
        return (int) (value * asUnitValue());
    }

    /**
     * Returns this percent plus the given percent
     */
    public Percent times(double scaleFactor)
    {
        return new Percent(percent * scaleFactor);
    }

    @Override
    public String toString()
    {
        return Doubles.format(percent, 1) + "%";
    }
}
