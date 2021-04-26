package com.telenav.kivakit.ui.swing.graphics.drawing.drawables;

import com.telenav.kivakit.ui.swing.graphics.drawing.BaseDrawable;
import com.telenav.kivakit.ui.swing.graphics.drawing.DrawingSurface;
import com.telenav.kivakit.ui.swing.graphics.geometry.Coordinate;
import com.telenav.kivakit.ui.swing.graphics.style.Style;

import java.awt.Shape;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * @author jonathanl (shibo)
 */
public class Text extends BaseDrawable
{
    public static Text text(final Style style)
    {
        return new Text(style, null);
    }

    public static Text text(final Style style, final String text)
    {
        return new Text(style, text);
    }

    private String text;

    protected Text(final Style style, final String text)
    {
        super(style);
        this.text = text;
    }

    protected Text(final Text that)
    {
        super(that);

        text = that.text;
    }

    @Override
    public Text at(final Coordinate at)
    {
        return (Text) super.at(at);
    }

    @Override
    public Text copy()
    {
        return new Text(this);
    }

    @Override
    public Shape draw(final DrawingSurface surface)
    {
        return shape(surface.drawText(style(), at().inDrawingUnits(), text));
    }

    @Override
    public Text scaled(final double scaleFactor)
    {
        return unsupported();
    }

    @Override
    public Text withStyle(final Style style)
    {
        return (Text) super.withStyle(style);
    }

    public Text withText(final String text)
    {
        final var copy = copy();
        copy.text = text;
        return copy;
    }
}
