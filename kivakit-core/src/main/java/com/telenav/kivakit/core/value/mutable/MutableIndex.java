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

package com.telenav.kivakit.core.value.mutable;

import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.internal.lexakai.DiagramValue;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A mutable index value for use in lambdas and anonymous inner classes. Can be {@link #increment()}ed, {@link
 * #decrement()}ed, added to with {@link #add(int)}, set with {@link #index(int)} and retrieved with {@link #get()}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramValue.class)
@LexakaiJavadoc(complete = true)
public class MutableIndex
{
    private int index;

    public MutableIndex()
    {
    }

    public MutableIndex(int index)
    {
        if (index < 0)
        {
            Ensure.fail("Negative count ", index);
        }
        this.index = index;
    }

    @SuppressWarnings("UnusedReturnValue")
    public int add(int that)
    {
        return index + that;
    }

    public int decrement()
    {
        return index--;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof MutableIndex)
        {
            var that = (MutableIndex) object;
            return index == that.index;
        }
        return false;
    }

    public int get()
    {
        return index;
    }

    @Override
    public int hashCode()
    {
        return Integer.hashCode(index);
    }

    public int increment()
    {
        return index++;
    }

    public void index(int index)
    {
        this.index = index;
    }

    public void set(int index)
    {
        this.index = index;
    }

    @Override
    public String toString()
    {
        return Count.count(index).quantumAsCommaSeparatedString();
    }
}
