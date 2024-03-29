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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.logging.LogEntry;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.interfaces.comparison.Filter;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.language.Classes.simpleName;

/**
 * A {@link LogEntry} filter that accepts a given set of messages and their subtypes
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class LogEntriesSubclassing implements Filter<LogEntry>
{
    /** The message supertypes to include */
    private final Class<? extends Message>[] include;

    /**
     * Constructs a filter that accepts subtypes of the given messages
     */
    @SafeVarargs
    public LogEntriesSubclassing(Class<? extends Message>... include)
    {
        this.include = include;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean accepts(LogEntry entry)
    {
        for (var type : include)
        {
            if (type.isAssignableFrom(entry.message().getClass()))
            {
                return true;
            }
        }
        return false;
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
            names.add(simpleName(type));
        }
        return names.join();
    }
}
