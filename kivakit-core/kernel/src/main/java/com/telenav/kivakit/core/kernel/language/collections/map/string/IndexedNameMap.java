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

package com.telenav.kivakit.core.kernel.language.collections.map.string;

import com.telenav.kivakit.core.kernel.interfaces.naming.Named;
import com.telenav.kivakit.core.kernel.language.collections.map.BaseIndexedMap;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;

/**
 * Stores named values by name and also makes them accessible by index.
 *
 * @param <T> The type implementing {@link Named}
 * @author jonathanl (shibo)
 */
public class IndexedNameMap<T extends Named> extends BaseIndexedMap<String, T>
{
    public IndexedNameMap()
    {
        super(Maximum.MAXIMUM);
    }

    public IndexedNameMap(final Maximum maximumSize)
    {
        super(maximumSize);
    }

    public final T forName(final String name)
    {
        return get(name);
    }

    public void put(final T value)
    {
        put(value.name(), value);
    }
}
