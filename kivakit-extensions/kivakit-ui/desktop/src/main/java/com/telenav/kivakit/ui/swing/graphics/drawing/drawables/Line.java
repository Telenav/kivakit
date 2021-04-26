////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.ui.swing.graphics.drawing.drawables;

import com.telenav.kivakit.ui.swing.graphics.drawing.BaseDrawable;
import com.telenav.kivakit.ui.swing.graphics.drawing.DrawingSurface;
import com.telenav.kivakit.ui.swing.graphics.geometry.Coordinate;
import com.telenav.kivakit.ui.swing.graphics.geometry.CoordinateSize;
import com.telenav.kivakit.ui.swing.graphics.geometry.CoordinateSlope;
import com.telenav.kivakit.ui.swing.graphics.geometry.CoordinateSystem;
import com.telenav.kivakit.ui.swing.graphics.style.Style;

import java.awt.Shape;

public class Line extends BaseDrawable
{
    public static Line line(final Style style, final Coordinate from, final Coordinate to)
    {
        return new Line(style, from, to.minus(from).asSize());
    }

    private CoordinateSize offset;

    protected Line(final Style style, final Coordinate from, final CoordinateSize offset)
    {
        super(style, from);
        this.offset = offset;
    }

    protected Line(final Line that)
    {
        super(that);
        offset = that.offset;
    }

    public CoordinateSlope angle(final CoordinateSystem system)
    {
        return system.slope(from(), to());
    }

    @Override
    public Line at(final Coordinate at)
    {
        return (Line) super.at(at);
    }

    @Override
    public Line copy()
    {
        return new Line(this);
    }

    @Override
    public Shape draw(final DrawingSurface surface)
    {
        return shape(surface.drawLine(style(), at().inDrawingUnits(), at().plus(offset).inDrawingUnits()));
    }

    public Coordinate from()
    {
        return at();
    }

    public CoordinateSize offset()
    {
        return offset;
    }

    @Override
    public Line scaled(final double scaleFactor)
    {
        final var copy = copy();
        copy.offset = offset.scaled(scaleFactor);
        return copy;
    }

    public Coordinate to()
    {
        return at().plus(offset);
    }

    public Line withFrom(final Coordinate from)
    {
        return at(from);
    }

    public Line withOffset(final CoordinateSize offset)
    {
        final var copy = copy();
        copy.offset = offset;
        return copy;
    }

    @Override
    public Line withStyle(final Style style)
    {
        return (Line) super.withStyle(style);
    }

    public Line withTo(final Coordinate to)
    {
        return withOffset(to.minus(at()).asSize());
    }
}
