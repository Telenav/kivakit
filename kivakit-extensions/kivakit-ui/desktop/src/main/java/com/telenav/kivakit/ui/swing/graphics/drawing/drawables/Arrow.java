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

import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.ui.swing.graphics.drawing.Drawable;
import com.telenav.kivakit.ui.swing.graphics.drawing.DrawingSurface;
import com.telenav.kivakit.ui.swing.graphics.geometry.Coordinate;
import com.telenav.kivakit.ui.swing.graphics.style.Style;

import java.awt.Shape;
import java.awt.geom.Area;

public class Arrow extends Line
{
    public static Arrow arrow(final Style style, final Coordinate from, final Coordinate to)
    {
        return new Arrow(style, from, to);
    }

    private Drawable fromArrowHead;

    private Drawable toArrowHead;

    public Arrow(final Style style, final Coordinate from, final Coordinate to)
    {
        super(style, from, to.minus(from).asSize());
    }

    private Arrow(final Arrow that)
    {
        super(that);

        fromArrowHead = that.fromArrowHead;
        toArrowHead = that.toArrowHead;
    }

    @Override
    public Arrow at(final Coordinate at)
    {
        return (Arrow) super.at(at);
    }

    @Override
    public Arrow copy()
    {
        return new Arrow(this);
    }

    @Override
    public Shape draw(final DrawingSurface surface)
    {
        final var shape = new Area();

        shape.add(new Area(super.draw(surface)));
        shape.add(new Area(fromArrowHead.at(from()).draw(surface)));
        shape.add(new Area(toArrowHead.at(to()).draw(surface)));

        return shape;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    public Arrow withFromArrowHead(final Drawable arrowHead)
    {
        final var copy = copy();
        copy.fromArrowHead = arrowHead;
        return copy;
    }

    @Override
    public Arrow withStyle(final Style style)
    {
        return (Arrow) super.withStyle(style);
    }

    public Arrow withToArrowHead(final Drawable arrowHead)
    {
        final var copy = copy();
        copy.toArrowHead = arrowHead;
        return copy;
    }
}
