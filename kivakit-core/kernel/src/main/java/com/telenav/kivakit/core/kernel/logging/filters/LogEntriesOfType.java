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

package com.telenav.kivakit.core.kernel.logging.filters;

import com.telenav.kivakit.core.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.core.kernel.logging.LogEntry;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;
import com.telenav.kivakit.core.kernel.language.types.Classes;
import com.telenav.kivakit.core.kernel.messaging.Message;

import java.util.ArrayList;
import java.util.List;

public class LogEntriesOfType implements Filter<LogEntry>
{
    private final Class<? extends Message>[] types;

    private final List<Class<?>> includedClasses = new ArrayList<>();

    @SafeVarargs
    public LogEntriesOfType(final Class<? extends Message>... types)
    {
        this.types = types;
    }

    @Override
    public boolean accepts(final LogEntry value)
    {
        if (!includedClasses.isEmpty())
        {
            if (!includedClasses.contains(value.context().type()))
            {
                return false;
            }
        }
        for (final var type : types)
        {
            if (type.equals(value.message().getClass()))
            {
                return true;
            }
        }
        return false;
    }

    public void fromClass(final Class<?> within)
    {
        includedClasses.add(within);
    }

    @Override
    public String toString()
    {
        final var names = new StringList();
        for (final var type : types)
        {
            names.add(Classes.simpleName(type));
        }
        return "logEntriesOfType(" + names + ")";
    }
}
