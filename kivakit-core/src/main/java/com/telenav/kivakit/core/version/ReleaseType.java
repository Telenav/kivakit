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

package com.telenav.kivakit.core.version;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.messaging.Listener;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * The release for a particular {@link Version}. For example "alpha", "beta" or "final".
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public enum ReleaseType
{
    ALPHA,
    BETA,
    M1,
    M2,
    M3,
    M4,
    M5,
    M6,
    M7,
    M8,
    M9,
    RC,
    JRE,
    FINAL,
    RELEASE;

    public static ReleaseType parseRelease(Listener listener, String value)
    {
        if (value != null)
        {
            for (var type : values())
            {
                if (type.name().equalsIgnoreCase(value))
                {
                    return type;
                }
            }
        }
        if ("SNAPSHOT".equalsIgnoreCase(value))
        {
            return null;
        }
        throw listener.problem("Invalid release: " + value).asException();
    }

    public static ReleaseType releaseForIdentifier(int identifier)
    {
        return switch (identifier)
                {
                    case 1 -> ALPHA;
                    case 2 -> BETA;
                    case 3 -> RC;
                    case 4 -> FINAL;
                    case 5 -> M1;
                    case 6 -> M2;
                    case 7 -> M3;
                    case 8 -> M4;
                    case 9 -> M5;
                    case 10 -> M6;
                    case 11 -> M7;
                    case 12 -> M8;
                    case 13 -> M9;
                    default -> throw new IllegalArgumentException();
                };
    }

    /**
     * Returns true if this release is after the given release
     */
    public boolean isAfter(ReleaseType that)
    {
        return ordinal() > that.ordinal();
    }

    /**
     * Returns true if this release is before the given release
     */
    public boolean isBefore(ReleaseType that)
    {
        return ordinal() < that.ordinal();
    }
}
