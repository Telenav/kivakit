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

package com.telenav.kivakit.core.collections.map;

import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.core.internal.lexakai.DiagramCollections;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.core.ensure.Ensure.fail;

/**
 * A {@link StringMap} where values that are added with {@link #put(Object, Object)} are automatically converted to
 * lowercase and can be retrieved with {@link #get(Object)} in a case-independent manner.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramCollections.class)
public class CaseFoldingStringMap<Element> extends StringMap<Element>
{
    public CaseFoldingStringMap(Maximum maximumSize)
    {
        super(maximumSize);
    }

    public CaseFoldingStringMap()
    {
        super(Maximum.MAXIMUM);
    }

    @Override
    public Element get(Object key)
    {
        if (key == null)
        {
            return null;
        }
        if (key instanceof String)
        {
            return super.get(key.toString().toLowerCase());
        }
        fail("Key must be a String");
        return null;
    }

    @Override
    public Element put(String key, Element value)
    {
        return super.put(key.toLowerCase(), value);
    }
}
