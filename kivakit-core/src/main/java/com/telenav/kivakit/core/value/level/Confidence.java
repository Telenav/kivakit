////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.value.level;

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.internal.lexakai.DiagramCount;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Level of certainty in the accuracy of something. Confidence objects can be constructed with {@link
 * Confidence#confidence(double)}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramCount.class)
public class Confidence extends Level
{
    public static final Confidence FULL = new Confidence(1);

    public static final Confidence MEDIUM = new Confidence(.5);

    public static final Confidence LOW = new Confidence(.25);

    public static final Confidence NO = new Confidence(0);

    public static Confidence confidence(double value)
    {
        return new Confidence(value);
    }

    public static Confidence confidenceForByte(byte level)
    {
        return new Confidence((double) level / Byte.MAX_VALUE);
    }

    public static Confidence confidenceForInt(int value)
    {
        return new Confidence(value / 255.0);
    }

    public static Confidence parseConfidence(Listener listener, String value)
    {
        return Confidence.confidence(Double.parseDouble(value));
    }

    protected Confidence(double value)
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
    public Confidence dividedBy(Level that)
    {
        return (Confidence) super.dividedBy(that);
    }

    @Override
    public Confidence inverse()
    {
        return (Confidence) super.inverse();
    }

    @Override
    public Confidence minus(Level that)
    {
        return (Confidence) super.minus(that);
    }

    @Override
    public Confidence plus(Level that)
    {
        return (Confidence) super.plus(that);
    }

    @Override
    public Confidence reciprocal()
    {
        return (Confidence) super.reciprocal();
    }

    @Override
    public Confidence times(Level that)
    {
        return (Confidence) super.times(that);
    }

    @Override
    public String toString()
    {
        return String.format("%.1f", asZeroToOne());
    }

    @Override
    protected Confidence onNewInstance(double value)
    {
        return new Confidence(value);
    }
}
