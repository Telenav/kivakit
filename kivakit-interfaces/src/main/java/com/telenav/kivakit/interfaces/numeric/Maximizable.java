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

import com.telenav.kivakit.interfaces.lexakai.DiagramNumeric;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Determines which of two objects is the maximum. For example, the Angle object is {@link Maximizable} and implements
 * this method:
 * <pre>
 *     public Angle maximum( Angle that) { ... }
 * </pre>
 *
 * @param <Value> The type of object to compare
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramNumeric.class)
public interface Maximizable<Value>
{
    /**
     * @return The maximum of this value and the given value.
     */
    Value maximum(Value value);

    /**
     * Returns the maximum possible Value
     */
    Value maximum();
}
