package com.telenav.kivakit.ui.swing.graphics.geometry;

import com.telenav.kivakit.core.kernel.language.values.level.Percent;

/**
 * @author jonathanl (shibo)
 */
public class CoordinateHeight extends CoordinateDistance
{
    public static CoordinateHeight height(final CoordinateSystem system, final double units)
    {
        return new CoordinateHeight(system, units);
    }

    public CoordinateHeight(final CoordinateSystem coordinateSystem, final double units)
    {
        super(coordinateSystem, units);
    }

    @Override
    public CoordinateHeight scaled(final double scaleFactor)
    {
        return (CoordinateHeight) super.scaled(scaleFactor);
    }

    @Override
    public CoordinateHeight scaled(final Percent percent)
    {
        return (CoordinateHeight) super.scaled(percent);
    }

    @Override
    protected CoordinateDistance newInstance(final double units)
    {
        return units(coordinateSystem(), units);
    }
}
