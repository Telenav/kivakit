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

package com.telenav.kivakit.interfaces.comparison;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.interfaces.internal.lexakai.DiagramComparison;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.SUFFICIENT;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNNECESSARY;

/**
 * Determines if two objects are equal. Unlike the {@link Object#equals(Object)} method, this interface is typesafe.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramComparison.class)
@ApiQuality(stability = STABLE,
            testing = UNNECESSARY,
            documentation = SUFFICIENT)
public interface Equality<Value>
{
    /**
     * @return True if value a is equal to value b
     */
    boolean isEqual(Value a, Value b);
}
