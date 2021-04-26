package com.telenav.kivakit.ui.swing.graphics.drawing;

import java.awt.geom.Rectangle2D;

/**
 * @author jonathanl (shibo)
 */
public class DrawingRectangle
{
    public static DrawingRectangle rectangle(final DrawingPoint point, final DrawingSize size)
    {
        return new DrawingRectangle(point, size);
    }

    public static DrawingRectangle rectangle(final double x,
                                             final double y,
                                             final double width,
                                             final double height)
    {
        return new DrawingRectangle(DrawingPoint.at(x, y), DrawingSize.size(width, height));
    }

    public static DrawingRectangle rectangle(final Rectangle2D rectangle)
    {
        return rectangle(DrawingPoint.at(rectangle.getX(), rectangle.getY()),
                DrawingSize.size(rectangle.getWidth(), rectangle.getHeight()));
    }

    private DrawingPoint at;

    private final DrawingSize size;

    public DrawingRectangle(final DrawingPoint at, final DrawingSize size)
    {
        this.at = at;
        this.size = size;
    }

    public DrawingPoint at()
    {
        return at;
    }

    public DrawingRectangle centeredIn(final DrawingSize size)
    {
        final var copy = copy();
        at = DrawingPoint.at(
                (size.width() - this.size.width()) / 2,
                (size.height() - this.size.height()) / 2);
        return copy;
    }

    public DrawingRectangle copy()
    {
        return new DrawingRectangle(at, size);
    }

    public double height()
    {
        return size.height();
    }

    public DrawingSize size()
    {
        return size;
    }

    public DrawingPoint to()
    {
        return at.plus(size);
    }

    @Override
    public String toString()
    {
        return at + ", " + size;
    }

    public double width()
    {
        return size.width();
    }

    public double x()
    {
        return at.x();
    }

    public double y()
    {
        return at.y();
    }
}
