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

package com.telenav.kivakit.kernel.project;

import com.telenav.kivakit.kernel.language.values.version.Version;

/**
 * The release for a particular {@link Version}. For example "alpha", "beta" or "final".
 *
 * @author jonathanl (shibo)
 */
public enum Release
{
    ALPHA(1),
    BETA(2),
    RC(3),
    FINAL(4),
    M1(5),
    M2(6),
    M3(7),
    M4(8),
    M5(9),
    M6(10),
    M7(11),
    M8(12),
    M9(13);

    public static Release forIdentifier(int identifier)
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

    public static Release parse(String value)
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
        throw new IllegalArgumentException();
    }

    private final int identifier;

    Release(int identifier)
    {
        this.identifier = identifier;
    }

    public int identifier()
    {
        return identifier;
    }

    public boolean isAfter(Release that)
    {
        return ordinal() > that.ordinal();
    }

    public boolean isBefore(Release that)
    {
        return ordinal() < that.ordinal();
    }
}
