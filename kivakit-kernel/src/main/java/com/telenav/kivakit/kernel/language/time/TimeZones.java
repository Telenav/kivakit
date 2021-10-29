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

package com.telenav.kivakit.kernel.language.time;

import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageTime;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Utility methods to map between {@link ZoneId}s and their display names
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageTime.class)
@LexakaiJavadoc(complete = true)
public class TimeZones
{
    private static final Map<String, String> shortToLong = new HashMap<>();

    static
    {
        for (var zone : ZoneId.getAvailableZoneIds())
        {
            var zoneId = ZoneId.of(zone);
            shortToLong.put(displayName(zoneId), zone);
        }
    }

    public static String displayName(ZoneId zone)
    {
        return zone.getDisplayName(TextStyle.SHORT, Locale.getDefault());
    }

    public static ZoneId forDisplayName(String displayName)
    {
        return ZoneId.of(shortToLong.get(displayName));
    }
}
