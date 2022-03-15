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

import com.telenav.kivakit.core.lexakai.DiagramCount;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A weight from 0 to 1 for parametric weighting in mathematics.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramCount.class)
@LexakaiJavadoc(complete = true)
public class Weight extends Level
{
    public static Weight weight(double value)
    {
        return new Weight(value);
    }

    protected Weight()
    {
        super();
    }

    private Weight(double value)
    {
        super(value);
    }

    @Override
    public Weight divide(Level that)
    {
        return (Weight) super.divide(that);
    }

    @Override
    public Weight inverse()
    {
        return (Weight) super.inverse();
    }

    @Override
    public Weight minus(Level that)
    {
        return (Weight) super.minus(that);
    }

    @Override
    public Weight plus(Level that)
    {
        return (Weight) super.plus(that);
    }

    @Override
    public Weight reciprocal()
    {
        return (Weight) super.reciprocal();
    }

    @Override
    public Weight times(Level that)
    {
        return (Weight) super.times(that);
    }

    @Override
    protected Level onNewInstance(double value)
    {
        return new Weight(value);
    }
}
