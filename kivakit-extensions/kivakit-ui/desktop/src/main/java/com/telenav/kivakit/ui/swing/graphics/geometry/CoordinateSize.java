package com.telenav.kivakit.ui.swing.graphics.geometry;

import com.telenav.kivakit.core.kernel.language.values.level.Percent;
import com.telenav.kivakit.ui.swing.graphics.drawing.DrawingSize;

/**
 * @author jonathanl (shibo)
 */
public class CoordinateSize extends Coordinated
{
    public static CoordinateSize size(final CoordinateSystem coordinateSystem, final double width, final double height)
    {
        return new CoordinateSize(coordinateSystem, width, height);
    }

    private final double width;

    private final double height;

    public CoordinateSize(final CoordinateSystem coordinateSystem, final double width, final double height)
    {
        super(coordinateSystem);
        this.width = width;
        this.height = height;
    }

    public double height()
    {
        return height;
    }

    public CoordinateSize minus(final CoordinateSize that)
    {
        return size(coordinateSystem(), width - that.width, height - that.height);
    }

    public DrawingSize onDrawingSurface()
    {
        return coordinateSystem().inDrawingUnits(this);
    }

    public CoordinateSize plus(final CoordinateSize that)
    {
        return size(coordinateSystem(), width + that.width, height + that.height);
    }

    public CoordinateSize scaled(final double scaleFactor)
    {
        return size(coordinateSystem(), width * scaleFactor, height * scaleFactor);
    }

    public CoordinateSize scaled(final Percent percent)
    {
        return size(coordinateSystem(), percent.scale(width), percent.scale(height));
    }

    public double width()
    {
        return width;
    }
}
