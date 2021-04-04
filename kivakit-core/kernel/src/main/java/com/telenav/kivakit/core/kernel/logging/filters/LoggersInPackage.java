////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.logging.filters;

import com.telenav.kivakit.core.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.core.kernel.logging.LogEntry;
import com.telenav.kivakit.core.kernel.language.paths.PackagePath;

public class LoggersInPackage implements Filter<LogEntry>
{
    private final PackagePath path;

    public LoggersInPackage(final PackagePath path)
    {
        this.path = path;
    }

    @Override
    public boolean accepts(final LogEntry value)
    {
        return value.context().packagePath().startsWith(path);
    }

    @Override
    public String toString()
    {
        return "loggersInPackage(" + path + ")";
    }
}
