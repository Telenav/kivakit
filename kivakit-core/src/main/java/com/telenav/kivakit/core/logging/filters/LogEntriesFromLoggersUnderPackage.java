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
import com.telenav.kivakit.core.language.packaging.PackageReference;
import com.telenav.kivakit.core.logging.LogEntry;
import com.telenav.kivakit.interfaces.comparison.Filter;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A filter that accepts each {@link LogEntry}  that came from a package or sub-package
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class LogEntriesFromLoggersUnderPackage implements Filter<LogEntry>
{
    /** The package */
    private final PackageReference _package;

    public LogEntriesFromLoggersUnderPackage(PackageReference _package)
    {
        this._package = _package;
    }

    /**
     * Accepts log entries from loggers in this filter's package
     *
     * <p>
     * {@inheritDoc}
     * </p>
     */
    @Override
    public boolean accepts(LogEntry value)
    {
        return value.context().packagePath().startsWith(_package);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return _package.join();
    }
}
