package com.telenav.kivakit.ui.swing.graphics.geometry;

import com.telenav.kivakit.core.kernel.language.values.level.Percent;
import com.telenav.kivakit.ui.swing.graphics.drawing.DrawingPoint;

/**
 * @author jonathanl (shibo)
 */
public class Coordinate extends Coordinated
{
    public static Coordinate at(final CoordinateSystem system, final double x, final double y)
    {
        return new Coordinate(system, x, y);
    }

    public static Coordinate origin(final CoordinateSystem system)
    {
        return at(system, 0, 0);
    }

    private final double x;

    private final double y;

    public Coordinate(final CoordinateSystem system, final double x, final double y)
    {
        super(system);
        this.x = x;
        this.y = y;
    }

    public CoordinateSize asSize()
    {
        return CoordinateSize.size(coordinateSystem(), x, y);
    }

    public DrawingPoint inDrawingUnits()
    {
        return coordinateSystem().inDrawingUnits(this);
    }

    public Coordinate minus(final Coordinate that)
    {
        return at(coordinateSystem(), x - that.x, y - that.y);
    }

    public Coordinate plus(final Coordinate that)
    {
        return at(coordinateSystem(), x + that.x, y + that.y);
    }

    public Coordinate plus(final CoordinateSize that)
    {
        return at(coordinateSystem(), x + that.width(), y + that.height());
    }

    public Coordinate plus(final double dx, final double dy)
    {
        return at(coordinateSystem(), x + dx, y + dy);
    }

    public Coordinate scaled(final double scaleFactor)
    {
        return at(coordinateSystem(), x * scaleFactor, y * scaleFactor);
    }

    public Coordinate scaled(final Percent percent)
    {
        return at(coordinateSystem(), percent.scale(x), percent.scale(y));
    }

    /**
     * @return The x location of this coordinate
     */
    public double x()
    {
        return x;
    }

    /**
     * @return The x location of this coordinate
     */
    public double y()
    {
        return y;
    }
}
