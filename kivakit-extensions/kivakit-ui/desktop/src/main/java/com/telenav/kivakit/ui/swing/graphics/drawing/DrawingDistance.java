package com.telenav.kivakit.ui.swing.graphics.drawing;

/**
 * @author jonathanl (shibo)
 */
public class DrawingDistance
{
    public static DrawingDistance of(final double units)
    {
        return new DrawingDistance(units);
    }

    private final double units;

    public DrawingDistance(final double units)
    {
        this.units = units;
    }

    @Override
    public String toString()
    {
        return units + " units";
    }

    public double units()
    {
        return units;
    }
}
