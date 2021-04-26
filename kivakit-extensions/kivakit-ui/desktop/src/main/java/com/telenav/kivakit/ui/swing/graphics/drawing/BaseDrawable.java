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

package com.telenav.kivakit.ui.swing.graphics.drawing;

import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.ui.swing.graphics.drawing.drawables.Box;
import com.telenav.kivakit.ui.swing.graphics.drawing.drawables.Dot;
import com.telenav.kivakit.ui.swing.graphics.drawing.drawables.Label;
import com.telenav.kivakit.ui.swing.graphics.drawing.drawables.Line;
import com.telenav.kivakit.ui.swing.graphics.drawing.drawables.Text;
import com.telenav.kivakit.ui.swing.graphics.geometry.Coordinate;
import com.telenav.kivakit.ui.swing.graphics.geometry.CoordinateDistance;
import com.telenav.kivakit.ui.swing.graphics.geometry.CoordinateSize;
import com.telenav.kivakit.ui.swing.graphics.style.Style;

import java.awt.Shape;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;

public abstract class BaseDrawable implements Drawable
{
    private Style style;

    private Coordinate at;

    private Shape shape;

    public BaseDrawable(final Style style, final Coordinate at)
    {
        this(style);
        this.at = at;
    }

    protected BaseDrawable(final Style style)
    {
        this.style = style;
    }

    @SuppressWarnings("ConstantConditions")
    protected BaseDrawable(final BaseDrawable that)
    {
        ensure(that != null);

        style = that.style;
        at = that.at;
        shape = that.shape;
    }

    @Override
    public Drawable at(final Coordinate at)
    {
        final var copy = (BaseDrawable) copy();
        copy.at = at;
        return copy;
    }

    @Override
    public Coordinate at()
    {
        return at;
    }

    @Override
    public Shape shape()
    {
        return shape;
    }

    @Override
    @KivaKitIncludeProperty
    public Style style()
    {
        return style;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    public BaseDrawable withStyle(final Style style)
    {
        final var copy = (BaseDrawable) copy();
        copy.style = style;
        return copy;
    }

    protected Box box(final CoordinateSize size)
    {
        return Box.box(style)
                .at(at)
                .withSize(size);
    }

    protected Dot dot(final CoordinateDistance radius)
    {
        return Dot.dot(style)
                .at(at)
                .withRadius(radius);
    }

    protected Label label(final String text)
    {
        return Label.label(style, text).at(at);
    }

    protected Line line(final Coordinate from, final Coordinate to)
    {
        return Line.line(style, from, to);
    }

    protected Shape shape(final Shape shape)
    {
        this.shape = shape;
        return shape;
    }

    protected Text text(final String text)
    {
        return Text.text(style, text).at(at);
    }
}
