////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
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
    FINAL(4);

    public static Release forIdentifier(final int identifier)
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
            default:
                throw new IllegalArgumentException();
        }
    }

    public static Release parse(final String value)
    {
        if (value != null)
        {
            for (final var type : values())
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

    Release(final int identifier)
    {
        this.identifier = identifier;
    }

    public int identifier()
    {
        return identifier;
    }

    public boolean isAfter(final Release that)
    {
        return ordinal() > that.ordinal();
    }

    public boolean isBefore(final Release that)
    {
        return ordinal() < that.ordinal();
    }
}
