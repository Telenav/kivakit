////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.time;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageTime;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramLanguageTime.class)
public interface ModificationTimestamped
{
    default Duration age()
    {
        return lastModified().elapsedSince();
    }

    default boolean isNewerThan(final ModificationTimestamped that)
    {
        return lastModified().isAfter(that.lastModified());
    }

    default boolean isOlderThan(final ModificationTimestamped that)
    {
        return lastModified().isBefore(that.lastModified());
    }

    default boolean lastModified(final Time modified)
    {
        return false;
    }

    default Time lastModified()
    {
        return Time.now();
    }
}
