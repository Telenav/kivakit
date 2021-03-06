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

package com.telenav.kivakit.core.logging.filters;

import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.language.Classes;
import com.telenav.kivakit.core.logging.LogEntry;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.interfaces.comparison.Filter;

public class LogEntriesSubclassing implements Filter<LogEntry>
{
    private final Class<? extends Message>[] types;

    @SafeVarargs
    public LogEntriesSubclassing(Class<? extends Message>... types)
    {
        this.types = types;
    }

    @Override
    public boolean accepts(LogEntry value)
    {
        for (var type : types)
        {
            if (type.isAssignableFrom(value.message().getClass()))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString()
    {
        var names = new StringList();
        for (var type : types)
        {
            names.add(Classes.simpleName(type));
        }
        return "logEntriesSubclassing(" + names + ")";
    }
}
