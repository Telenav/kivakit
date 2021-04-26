package com.telenav.kivakit.ui.swing.graphics.drawing.drawables;

import com.telenav.kivakit.ui.swing.graphics.drawing.BaseDrawable;
import com.telenav.kivakit.ui.swing.graphics.drawing.Drawable;
import com.telenav.kivakit.ui.swing.graphics.drawing.DrawingSurface;
import com.telenav.kivakit.ui.swing.graphics.geometry.Coordinate;
import com.telenav.kivakit.ui.swing.graphics.geometry.CoordinateSize;
import com.telenav.kivakit.ui.swing.graphics.style.Style;

import java.awt.Shape;

/**
 * @author jonathanl (shibo)
 */
public class Box extends BaseDrawable
{
    public static Box box(final Style style)
    {
        return new Box(style);
    }

    private CoordinateSize size;

    protected Box(final Box that)
    {
        super(that);
        size = that.size;
    }

    protected Box(final Style style)
    {
        super(style);
    }

    @Override
    public Box at(final Coordinate at)
    {
        return (Box) super.at(at);
    }

    @Override
    public Box copy()
    {
        return new Box(this);
    }

    @Override
    public Shape draw(final DrawingSurface surface)
    {
        return shape(surface.drawBox(style(), at().inDrawingUnits(), size.onDrawingSurface()));
    }

    @Override
    public Drawable scaled(final double scaleFactor)
    {
        final var copy = copy();
        copy.size = size.scaled(scaleFactor);
        return copy;
    }

    public Box withSize(final CoordinateSize size)
    {
        final var copy = copy();
        copy.size = size;
        return copy;
    }

    @Override
    public Box withStyle(final Style style)
    {
        return (Box) super.withStyle(style);
    }
}
