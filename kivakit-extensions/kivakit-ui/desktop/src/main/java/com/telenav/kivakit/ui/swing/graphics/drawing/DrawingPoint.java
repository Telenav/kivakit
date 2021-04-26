package com.telenav.kivakit.ui.swing.graphics.drawing;

import java.awt.geom.Point2D;

/**
 * @author jonathanl (shibo)
 */
public class DrawingPoint
{
    public static DrawingPoint at(final double x, final double y)
    {
        return new DrawingPoint(x, y);
    }

    public static DrawingPoint at(final Point2D point)
    {
        return at(point.getX(), point.getY());
    }

    private final double x;

    private final double y;

    public DrawingPoint(final double x, final double y)
    {
        this.x = x;
        this.y = y;
    }

    public DrawingPoint plus(final double dx, final double dy)
    {
        return at(x + dx, y + dy);
    }

    public DrawingPoint plus(final DrawingSize size)
    {
        return at(x + size.width(), y + size.height());
    }

    @Override
    public String toString()
    {
        return x + ", " + y;
    }

    public double x()
    {
        return x;
    }

    public double y()
    {
        return y;
    }
}
