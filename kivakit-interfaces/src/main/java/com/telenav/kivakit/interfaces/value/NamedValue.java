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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.interfaces.internal.lexakai.DiagramNaming;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * A {@link Named} object that has a value, retrievable through {@link Valued#value()}.
 *
 * @param <Value> The type of value
 * @author jonathanl (shibo)
 * @see Named
 * @see Valued
 */
@UmlClassDiagram(diagram = DiagramNaming.class)
@CodeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public interface NamedValue<Value> extends
        Named,
        Valued<Value>
{
}
