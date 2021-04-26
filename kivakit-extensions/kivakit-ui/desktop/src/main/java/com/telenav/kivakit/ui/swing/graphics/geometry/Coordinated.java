package com.telenav.kivakit.ui.swing.graphics.geometry;

/**
 * @author jonathanl (shibo)
 */
public abstract class Coordinated
{
    private final CoordinateSystem coordinateSystem;

    public Coordinated(final CoordinateSystem coordinateSystem)
    {
        this.coordinateSystem = coordinateSystem;
    }

    public CoordinateSystem coordinateSystem()
    {
        return coordinateSystem;
    }
}
