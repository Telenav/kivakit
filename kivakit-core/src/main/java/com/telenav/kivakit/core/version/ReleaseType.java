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

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.messaging.Listener;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * The release for a particular {@link Version}. For example "alpha", "beta" or "final".
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
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
        switch (identifier)
        {
            case 1:
                return ALPHA;
            case 2:
                return BETA;
            case 3:
                return RC;
            case 4:
                return FINAL;
            case 5:
                return M1;
            case 6:
                return M2;
            case 7:
                return M3;
            case 8:
                return M4;
            case 9:
                return M5;
            case 10:
                return M6;
            case 11:
                return M7;
            case 12:
                return M8;
            case 13:
                return M9;
            default:
                throw new IllegalArgumentException();
        }
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
