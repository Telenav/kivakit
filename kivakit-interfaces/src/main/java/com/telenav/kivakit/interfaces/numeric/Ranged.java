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

import com.telenav.kivakit.interfaces.collection.Contains;
import com.telenav.kivakit.interfaces.lexakai.DiagramNumeric;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Something that has a numeric range of values.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramNumeric.class)
public interface Ranged<Value extends Minimizable<Value> & Maximizable<Value>> extends Contains<Value>
{
    /**
     * @return The given value constrained to this range
     */
    Value constrainTo(Value value);

    /**
     * @return The maximum value of this range
     */
    Value maximum();

    /**
     * @return The minimum value of this range
     */
    Value minimum();
}
