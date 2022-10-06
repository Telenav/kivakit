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

package com.telenav.kivakit.collections.map;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.collections.internal.lexakai.DiagramMap;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Stores named values by name and also makes them accessible by index.
 *
 * @param <T> The type implementing {@link Named}
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMap.class)
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class IndexedNameMap<T extends Named> extends BaseIndexedMap<String, T>
{
    public IndexedNameMap()
    {
        super(Maximum.MAXIMUM);
    }

    public IndexedNameMap(Maximum maximumSize)
    {
        super(maximumSize);
    }

    public final T forName(String name)
    {
        return get(name);
    }

    public void put(T value)
    {
        put(value.name(), value);
    }
}
