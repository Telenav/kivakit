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

package com.telenav.kivakit.core.collections.primitive.set;

import com.telenav.kivakit.core.collections.primitive.map.PrimitiveMap;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveSet;
import com.telenav.kivakit.core.collections.primitive.iteration.LongIterator;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.strings.Indent;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;

@UmlClassDiagram(diagram = DiagramPrimitiveSet.class)
public abstract class PrimitiveSet extends PrimitiveMap
{
    protected interface SetToString
    {
        String toString(long value);
    }

    protected PrimitiveSet(final String objectName)
    {
        super(objectName);
    }

    protected PrimitiveSet()
    {
    }

    public boolean add(final long next)
    {
        return false;
    }

    public boolean contains(final long next)
    {
        return false;
    }

    protected String toString(final LongIterator values, final SetToString toStringer)
    {
        return toString(values, ", ", 0, "", toStringer);
    }

    protected String toString(final LongIterator values, final String separator, final int every, final String section,
                              final SetToString toStringer)
    {
        final var count = Math.min(size(), TO_STRING_MAXIMUM_ELEMENTS);
        final var builder = new StringBuilder();
        for (var i = 0; values.hasNext() && i < count; i++)
        {
            if (i > 0)
            {
                if (every > 0 && i % every == 0)
                {
                    builder.append(section);
                }
                else
                {
                    builder.append(separator);
                }
            }
            final var value = values.nextLong();
            builder.append(toStringer.toString(value));
        }
        if (size() > TO_STRING_MAXIMUM_ELEMENTS)
        {
            builder.append(separator);
            builder.append("[...]");
        }
        return Indent.by(4, builder.toString());
    }

    protected String toString(final Object[] values)
    {
        final var list = new StringList();
        var index = 0;
        for (; index < values.length; index++)
        {
            if (values[index] != null)
            {
                list.add(values[index].toString());
            }
        }
        if (index == 20)
        {
            list.add("...");
        }
        return "[" + getClass().getSimpleName() + ", size = " + size() + ", capacity = " + values.length
                + ", hashingStrategy = " + hashingStrategy() + ", values = " + list.join(", ") + "]";
    }
}
