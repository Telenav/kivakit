package com.telenav.kivakit.ui.swing.graphics.geometry;

/**
 * @author jonathanl (shibo)
 */
public interface CoordinateTransform
{
    <T extends Coordinated> T scale(double x, double y);

    <T extends Coordinated> T scaled(double x, double y);
}
