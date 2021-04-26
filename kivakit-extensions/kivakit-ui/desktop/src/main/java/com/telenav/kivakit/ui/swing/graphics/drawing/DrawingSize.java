package com.telenav.kivakit.ui.swing.graphics.drawing;

import java.awt.geom.Dimension2D;

/**
 * @author jonathanl (shibo)
 */
public class DrawingSize
{
    public static DrawingSize size(final double width, final double height)
    {
        return new DrawingSize(width, height);
    }

    public static DrawingSize size(final Dimension2D dimension)
    {
        return size(dimension.getWidth(), dimension.getHeight());
    }

    private final double width;

    private final double height;

    public DrawingSize(final double width, final double height)
    {
        this.width = width;
        this.height = height;
    }

    public DrawingPoint asPoint()
    {
        return DrawingPoint.at(width, height);
    }

    public double height()
    {
        return height;
    }

    public DrawingSize plus(final double width, final double height)
    {
        return size(this.width + width, this.height + height);
    }

    @Override
    public String toString()
    {
        return width + ", " + height;
    }

    public double width()
    {
        return width;
    }
}
