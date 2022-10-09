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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.language.Classes;
import com.telenav.kivakit.core.logging.LogEntry;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.interfaces.comparison.Filter;

import java.util.ArrayList;
import java.util.List;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

/**
 * A {@link LogEntry} filter based on message types
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABILITY_STABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class LogEntriesOfType implements Filter<LogEntry>
{
    /** The message types to include */
    private final Class<? extends Message>[] include;

    /** Code contexts where messages should be included */
    private final List<Class<?>> includedCodeContexts = new ArrayList<>();

    /**
     * Constructs a filter that includes only the given message types
     */
    @SafeVarargs
    public LogEntriesOfType(Class<? extends Message>... include)
    {
        this.include = include;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean accepts(LogEntry entry)
    {
        if (!includedCodeContexts.isEmpty())
        {
            if (!includedCodeContexts.contains(entry.context().type()))
            {
                return false;
            }
        }

        for (var type : include)
        {
            if (type.equals(entry.message().getClass()))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Includes the given class for accepting log entries by code context
     */
    public void includeCodeContext(Class<?> within)
    {
        includedCodeContexts.add(within);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        var names = new StringList();
        for (var type : include)
        {
            names.add(Classes.simpleName(type));
        }
        return names.join();
    }
}
