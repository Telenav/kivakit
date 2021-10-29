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

package com.telenav.kivakit.kernel.language.values.count;

import com.telenav.kivakit.kernel.interfaces.collection.Contains;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageValue.class)
public class LongRange implements Contains<Long>
{
    private long minimum;

    private long maximum;

    public LongRange(long minimum, long maximum)
    {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    protected LongRange()
    {
    }

    public Long constrainTo(Long value)
    {
        value = Math.min(maximum, value);
        value = Math.max(minimum, value);
        return value;
    }

    @Override
    public boolean contains(Long value)
    {
        return value >= minimum && value <= maximum;
    }

    public Long maximum(Long that)
    {
        return Math.max(maximum, that);
    }

    public Long maximum()
    {
        return maximum;
    }

    public Long minimum(Long that)
    {
        return Math.min(minimum, that);
    }

    public Long minimum()
    {
        return minimum;
    }
}
