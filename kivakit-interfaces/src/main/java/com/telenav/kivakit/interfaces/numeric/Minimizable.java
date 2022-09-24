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

package com.telenav.kivakit.interfaces.numeric;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.interfaces.factory.MapFactory;
import com.telenav.kivakit.interfaces.internal.lexakai.DiagramNumeric;
import com.telenav.kivakit.interfaces.value.LongValued;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_REQUIRED;

/**
 * Determines which of two objects is the minimum. For example, the Angle object is {@link Minimizable} and implements
 * this method:
 * <pre>
 *     public Angle minimum( Angle that) { ... }
 * </pre>
 *
 * @param <Value> The type of object to compare
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramNumeric.class)
@ApiQuality(stability = STABLE,
            testing = TESTING_NOT_REQUIRED,
            documentation = FULLY_DOCUMENTED)
public interface Minimizable<Value> extends
        LongValued,
        MapFactory<Long, Value>

{
    default Value minimize(long value)
    {
        return newInstance(Math.min(longValue(), value));
    }

    default Value minimize(LongValued value)
    {
        return minimize(value.longValue());
    }

    /**
     * Returns The minimum possible value
     */
    Value minimum();
}
