package com.telenav.kivakit.ui.swing.graphics.drawing;

import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.values.level.Percent;
import com.telenav.kivakit.ui.swing.graphics.geometry.Coordinate;
import com.telenav.kivakit.ui.swing.graphics.style.Style;

import java.awt.Shape;

/**
 * @author jonathanl (shibo)
 */
public interface Drawable
{
    /**
     * @return The location of the drawable relative to the upper left
     */
    @KivaKitIncludeProperty
    Coordinate at();

    /**
     * @return This drawable at the given new location
     */
    Drawable at(Coordinate at);

    /**
     * @return A copy of this drawable
     */
    Drawable copy();

    /**
     * Draws this on the given surface
     *
     * @return The {@link Shape} that was drawn
     */
    Shape draw(final DrawingSurface surface);

    /**
     * @return This drawable scaled by the given scaling factor
     */
    Drawable scaled(final double scaleFactor);

    /**
     * @return This drawable scaled by the given scaling factor
     */
    default Drawable scaled(final Percent scaleFactor)
    {
        return copy().scaled(scaleFactor.asZeroToOne());
    }

    /**
     * @return The shape of this drawable (only once it has been drawn)
     */
    Shape shape();

    /**
     * @return The style of this drawable
     */
    @KivaKitIncludeProperty
    Style style();
}
