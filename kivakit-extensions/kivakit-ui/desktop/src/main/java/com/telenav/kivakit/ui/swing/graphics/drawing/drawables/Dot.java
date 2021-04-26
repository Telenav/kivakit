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
import com.telenav.kivakit.ui.swing.graphics.drawing.BaseDrawable;
import com.telenav.kivakit.ui.swing.graphics.drawing.DrawingSurface;
import com.telenav.kivakit.ui.swing.graphics.geometry.Coordinate;
import com.telenav.kivakit.ui.swing.graphics.geometry.CoordinateDistance;
import com.telenav.kivakit.ui.swing.graphics.style.Style;

import java.awt.Shape;

public class Dot extends BaseDrawable
{
    public static Dot dot(final Style style)
    {
        return new Dot(style);
    }

    private CoordinateDistance radius;

    protected Dot(final Style style)
    {
        super(style);
    }

    private Dot(final Dot that)
    {
        super(that);
        radius = that.radius;
    }

    @Override
    public Dot at(final Coordinate at)
    {
        return (Dot) super.at(at);
    }

    @Override
    public Dot copy()
    {
        return new Dot(this);
    }

    @Override
    public Shape draw(final DrawingSurface surface)
    {
        return shape(surface.drawCircle(style(), at().inDrawingUnits(), radius.onDrawingSurface()));
    }

    @Override
    public Dot scaled(final double scaleFactor)
    {
        return withRadius(radius.scaled(scaleFactor));
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    public Dot withRadius(final CoordinateDistance radius)
    {
        final var copy = copy();
        copy.radius = radius;
        return copy;
    }

    @Override
    public Dot withStyle(final Style style)
    {
        return (Dot) super.withStyle(style);
    }
}
