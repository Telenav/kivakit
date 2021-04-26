package com.telenav.kivakit.ui.swing.graphics.geometry;

import com.telenav.kivakit.core.kernel.language.values.level.Percent;
import com.telenav.kivakit.ui.swing.graphics.drawing.DrawingDistance;

/**
 * @author jonathanl (shibo)
 */
public class CoordinateDistance extends Coordinated
{
    public static CoordinateDistance units(final CoordinateSystem system, final double units)
    {
        return new CoordinateDistance(system, units);
    }

    private final double units;

    public CoordinateDistance(final CoordinateSystem coordinateSystem, final double units)
    {
        super(coordinateSystem);
        this.units = units;
    }

    public DrawingDistance onDrawingSurface()
    {
        return coordinateSystem().inDrawingUnits(this);
    }

    public CoordinateDistance scaled(final double scaleFactor)
    {
        return newInstance(units * scaleFactor);
    }

    public CoordinateDistance scaled(final Percent percent)
    {
        return newInstance(percent.scale(units));
    }

    public double units()
    {
        return units;
    }

    protected CoordinateDistance newInstance(final double units)
    {
        return units(coordinateSystem(), units);
    }
}
