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

package com.telenav.kivakit.core.value.count;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramCount;
import com.telenav.kivakit.interfaces.collection.Contains;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Represents a range of long values
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramCount.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class LongRange implements Contains<Long>
{
    /** The minimum value */
    private long minimum;

    /** The maximum value, inclusive */
    private long maximum;

    public LongRange(long minimum, long maximum)
    {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    protected LongRange()
    {
    }

    /**
     * Returns the given long value constrained to this range
     *
     * @param value The value to constrain
     * @return The constrained value
     */
    public long constrainTo(long value)
    {
        value = min(maximum, value);
        value = max(minimum, value);
        return value;
    }

    /**
     * Returns true if the given value is in range, inclusive
     */
    @Override
    public boolean contains(Long value)
    {
        return value >= minimum && value <= maximum;
    }
}
