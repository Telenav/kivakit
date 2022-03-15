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

package com.telenav.kivakit.core.messaging.filters;

import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.core.language.Classes;

public class SubClassesOf<T> implements Filter<T>
{
    private final Class<T> type;

    public SubClassesOf(Class<T> type)
    {
        this.type = type;
    }

    @Override
    public boolean accepts(Object value)
    {
        if (value == null)
        {
            return false;
        }
        return value.getClass().isAssignableFrom(type);
    }

    @Override
    public String toString()
    {
        return "subClassOf(" + Classes.simpleName(type) + ")";
    }
}
