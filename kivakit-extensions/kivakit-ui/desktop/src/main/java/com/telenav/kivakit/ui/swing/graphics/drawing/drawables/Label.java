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
public class Label extends BaseDrawable
{
    public static Label label(final Style style, final String text)
    {
        return new Label(style, text);
    }

    public static Label label(final Style style)
    {
        return new Label(style, null);
    }

    private String text;

    private int margin = 10;

    protected Label(final Style style, final String text)
    {
        super(style);
        this.text = text;
    }

    protected Label(final Label that)
    {
        super(that);
        text = that.text;
        margin = that.margin;
    }

    @Override
    public Label at(final Coordinate at)
    {
        return (Label) super.at(at);
    }

    @Override
    public Label copy()
    {
        return new Label(this);
    }

    @Override
    public Shape draw(final DrawingSurface surface)
    {
        final var size = surface.size(style(), text).plus(margin * 2, margin * 2);
        final var shape = surface.drawBox(style(), surface.inDrawingUnits(at()), size);
        surface.drawText(style(), at().inDrawingUnits().plus(margin, margin), text);
        return shape;
    }

    @Override
    public Label scaled(final double scaleFactor)
    {
        return unsupported();
    }

    public Label withMargin(final int margin)
    {
        final var copy = copy();
        copy.margin = margin;
        return copy;
    }

    @Override
    public Label withStyle(final Style style)
    {
        return (Label) super.withStyle(style);
    }

    public Label withText(final String text)
    {
        final var copy = copy();
        copy.text = text;
        return copy;
    }
}
