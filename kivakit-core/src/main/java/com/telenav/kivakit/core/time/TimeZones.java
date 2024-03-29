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

package com.telenav.kivakit.core.time;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramTime;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.ZoneId;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.messaging.Listener.nullListener;

/**
 * Utility methods to map between {@link ZoneId}s and their display names
 *
 * <p><b>Access</b></p>
 *
 * <ul>
 *     <li>{@link #utc()}</li>
 * </ul>
 *
 * <p><b>Parsing</b></p>
 *
 * <ul>
 *     <li>{@link #parseZoneId(Listener, String)}</li>
 * </ul>
 *
 * <p><b>Checks</b></p>
 *
 * <ul>
 *     <li>{@link #isUtc(ZoneId)}</li>
 *     <li>{@link #isValidZoneId(String)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramTime.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class TimeZones
{
    /**
     * Returns true if the given zone id represents UTC time
     */
    public static boolean isUtc(ZoneId zone)
    {
        return "UTC".equals(zone.getId()) || "GMT".equals(zone.getId()) || "UT".equals(zone.getId());
    }

    /**
     * Returns true if the given identifier is a valid zone id (America/Los Angeles)
     */
    public static boolean isValidZoneId(String identifier)
    {
        return parseZoneId(nullListener(), identifier) != null;
    }

    /**
     * Returns the zone id (America/Los Angeles) for the given identifier, or null if there is none
     */
    public static ZoneId parseZoneId(Listener listener, String identifier)
    {
        try
        {
            return ZoneId.of(identifier);
        }
        catch (Exception e)
        {
            listener.problem("Invalid zone identifier (America/Los Angeles): $", identifier);
            return null;
        }
    }

    public static ZoneId utc()
    {
        return ZoneId.of("UTC");
    }
}
