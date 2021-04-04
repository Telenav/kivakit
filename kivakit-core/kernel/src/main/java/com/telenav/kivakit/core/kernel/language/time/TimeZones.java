////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.time;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageTime;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@UmlClassDiagram(diagram = DiagramLanguageTime.class)
public class TimeZones
{
    private static final Map<String, String> shortToLong = new HashMap<>();

    static
    {
        for (final var zone : ZoneId.getAvailableZoneIds())
        {
            final var zoneId = ZoneId.of(zone);
            shortToLong.put(displayName(zoneId), zone);
        }
    }

    public static String displayName(final ZoneId zone)
    {
        return zone.getDisplayName(TextStyle.SHORT, Locale.getDefault());
    }

    public static ZoneId forDisplayName(final String displayName)
    {
        return ZoneId.of(shortToLong.get(displayName));
    }
}
