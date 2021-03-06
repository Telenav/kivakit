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

package com.telenav.kivakit.interfaces.value;

import com.telenav.kivakit.interfaces.lexakai.DiagramValue;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Locates a value of the given type. This interface is the same as other interfaces in terms of function, but sometimes
 * {@link Locator} is the right name to use.
 *
 * @param <Value> the type of value to locate.
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramValue.class)
public interface Locator<Value>
{
    /**
     * @return The located value
     */
    Value locate();
}
