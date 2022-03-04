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

import com.telenav.kivakit.core.logging.LogEntry;
import com.telenav.kivakit.core.path.PackagePath;
import com.telenav.kivakit.interfaces.comparison.Filter;

public class LoggersInPackage implements Filter<LogEntry>
{
    private final PackagePath path;

    public LoggersInPackage(PackagePath path)
    {
        this.path = path;
    }

    @Override
    public boolean accepts(LogEntry value)
    {
        return value.context().packagePath().startsWith(path);
    }

    @Override
    public String toString()
    {
        return "loggersInPackage(" + path + ")";
    }
}
