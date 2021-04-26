package com.telenav.kivakit.ui.swing.graphics.drawing;

import com.telenav.kivakit.ui.swing.graphics.geometry.CoordinateSystem;
import com.telenav.kivakit.ui.swing.graphics.style.Style;

import java.awt.Shape;
import java.awt.geom.Path2D;

/**
 * @author jonathanl (shibo)
 */
public interface DrawingSurface extends CoordinateSystem
{
    /**
     * Draws a rectangle at the given point, with the given width and height, in the given style.
     */
    Shape drawBox(Style style, DrawingPoint at, DrawingSize size);

    /**
     * Draws a circle at the given point with the given radius in the given style
     */
    Shape drawCircle(Style style, DrawingPoint at, DrawingDistance radius);

    /**
     * Draws a line from one point to another in the given style
     */
    Shape drawLine(Style style, DrawingPoint from, DrawingPoint to);

    /**
     * Draws a path in the given style
     */
    Shape drawPath(Style style, Path2D path);

    /**
     * Draws the given text, in the given style, at the given point (relative to the upper left)
     */
    Shape drawText(Style style, DrawingPoint at, String text);

    /**
     * @return The size of the given text in the given style when rendered on this surface
     */
    DrawingSize size(final Style style, final String text);
}
