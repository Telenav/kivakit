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

package com.telenav.kivakit.core.kernel.language.values.level;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Level of certainty in the accuracy of something.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageValue.class)
public class Confidence extends Level
{
    public static final Confidence FULL = new Confidence(1);

    public static final Confidence MEDIUM = new Confidence(.5);

    public static final Confidence LOW = new Confidence(.25);

    public static final Confidence NO = new Confidence(0);

    public static Confidence forByte(final byte level)
    {
        return new Confidence((double) level / Byte.MAX_VALUE);
    }

    public static Confidence forUnsignedByte(final int value)
    {
        return new Confidence(value / 255.0);
    }

    public static class Converter extends BaseStringConverter<Confidence>
    {
        public Converter(final Listener listener)
        {
            super(listener);
        }

        @Override
        protected Confidence onConvertToObject(final String value)
        {
            return new Confidence(Double.parseDouble(value));
        }
    }

    public Confidence(final double value)
    {
        super(value);
    }

    protected Confidence()
    {
        super();
    }

    public int asUnsignedByte()
    {
        return (int) (asZeroToOne() * 255);
    }

    @Override
    public Confidence divide(final Level that)
    {
        return (Confidence) super.divide(that);
    }

    @Override
    public Confidence inverse()
    {
        return (Confidence) super.inverse();
    }

    @Override
    public Confidence minus(final Level that)
    {
        return (Confidence) super.minus(that);
    }

    @Override
    public Confidence plus(final Level that)
    {
        return (Confidence) super.plus(that);
    }

    @Override
    public Confidence reciprocal()
    {
        return (Confidence) super.reciprocal();
    }

    @Override
    public Confidence times(final Level that)
    {
        return (Confidence) super.times(that);
    }

    @Override
    public String toString()
    {
        return String.format("%.1f", asZeroToOne());
    }

    @Override
    protected Confidence onNewInstance(final double value)
    {
        return new Confidence(value);
    }
}
