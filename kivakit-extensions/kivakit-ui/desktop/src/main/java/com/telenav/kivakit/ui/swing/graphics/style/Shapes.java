package com.telenav.kivakit.ui.swing.graphics.style;

import java.awt.Shape;
import java.awt.geom.Area;
import java.util.Collection;

/**
 * @author jonathanl (shibo)
 */
public class Shapes
{
    public static Area combine(final Shape... shapes)
    {
        final var area = new Area();
        for (final var shape : shapes)
        {
            if (shape != null)
            {
                area.add(new Area(shape));
            }
        }
        return area;
    }

    public static Area combine(final Collection<Shape> shapes)
    {
        final var area = new Area();
        for (final var shape : shapes)
        {
            if (shape != null)
            {
                area.add(new Area(shape));
            }
        }
        return area;
    }
}
