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

package com.telenav.kivakit.kernel.language.collections.map.string;

import com.telenav.kivakit.kernel.language.values.count.Maximum;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageCollectionsMap;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.TreeMap;

/**
 * A bounded string map using a {@link TreeMap} implementation.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageCollectionsMap.class)
public class StringMap<T> extends BaseStringMap<T>
{
    public StringMap()
    {
        super(Maximum.MAXIMUM, new TreeMap<>());
    }

    public StringMap(final Maximum maximumSize)
    {
        super(maximumSize, new TreeMap<>());
    }
}