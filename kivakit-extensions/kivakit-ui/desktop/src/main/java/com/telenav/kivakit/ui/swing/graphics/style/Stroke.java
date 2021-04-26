package com.telenav.kivakit.ui.swing.graphics.style;

import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;

/**
 * @author jonathanl (shibo)
 */
public class Stroke
{
    public static Stroke create()
    {
        return new Stroke();
    }

    public static Stroke of(final java.awt.Stroke stroke)
    {
        return new Stroke(stroke);
    }

    private java.awt.Stroke stroke;

    private int cap;

    private int join;

    private int miterLimit;

    private float[] dash;

    private float dashPhase;

    private double width;

    protected Stroke()
    {
    }

    protected Stroke(final java.awt.Stroke stroke)
    {
        this.stroke = stroke;
    }

    protected Stroke(final Stroke that)
    {
        stroke = that.stroke;
        width = that.width;
        cap = that.cap;
        join = that.join;
        miterLimit = that.miterLimit;
        dash = that.dash;
        dashPhase = that.dashPhase;
    }

    public void apply(final Graphics2D graphics)
    {
        graphics.setStroke(awtStroke());
    }

    public Stroke copy()
    {
        return new Stroke(this);
    }

    public Stroke scale(final double scaleFactor)
    {
        return withWidth(width * scaleFactor);
    }

    public Shape stroked(final Shape shape)
    {
        return awtStroke().createStrokedShape(shape);
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(stroke).toString();
    }

    public Stroke withCap(final int cap)
    {
        final var copy = copy();
        copy.cap = cap;
        return copy;
    }

    public Stroke withDash(final float[] dash)
    {
        final var copy = copy();
        copy.dash = dash;
        return copy;
    }

    public Stroke withDashPhase(final float dash)
    {
        final var copy = copy();
        copy.dashPhase = dashPhase;
        return copy;
    }

    public Stroke withJoin(final int join)
    {
        final var copy = copy();
        copy.join = join;
        return copy;
    }

    public Stroke withMiterLimit(final int miterLimit)
    {
        final var copy = copy();
        copy.miterLimit = miterLimit;
        return copy;
    }

    public Stroke withWidth(final double width)
    {

        final var copy = copy();
        copy.width = width;
        return copy;
    }

    private java.awt.Stroke awtStroke()
    {
        if (stroke == null)
        {
            stroke = new BasicStroke((float) width, cap, join, miterLimit, dash, dashPhase);
        }
        return stroke;
    }
}
