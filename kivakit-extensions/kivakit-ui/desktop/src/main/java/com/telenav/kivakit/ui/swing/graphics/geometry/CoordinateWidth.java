package com.telenav.kivakit.ui.swing.graphics.geometry;

import com.telenav.kivakit.core.kernel.language.values.level.Percent;

/**
 * @author jonathanl (shibo)
 */
public class CoordinateWidth extends CoordinateDistance
{
    public static CoordinateWidth width(final CoordinateSystem system, final double units)
    {
        return new CoordinateWidth(system, units);
    }

    public CoordinateWidth(final CoordinateSystem coordinateSystem, final double units)
    {
        super(coordinateSystem, units);
    }

    @Override
    public CoordinateWidth scaled(final double scaleFactor)
    {
        return (CoordinateWidth) super.scaled(scaleFactor);
    }

    @Override
    public CoordinateWidth scaled(final Percent percent)
    {
        return (CoordinateWidth) super.scaled(percent);
    }

    @Override
    protected CoordinateDistance newInstance(final double units)
    {
        return width(coordinateSystem(), units);
    }
}
