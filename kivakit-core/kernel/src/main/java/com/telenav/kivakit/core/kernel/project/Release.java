////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.project;

import com.telenav.kivakit.core.kernel.language.values.version.Version;

/**
 * The release for a particular {@link Version}. For example "alpha", "beta" or "final".
 *
 * @author jonathanl (shibo)
 */
public enum Release
{
    SNAPSHOT(1),
    ALPHA(2),
    BETA(3),
    RC(4),
    FINAL(5);

    public static Release forIdentifier(final int identifier)
    {
        switch (identifier)
        {
            case 1:
                return SNAPSHOT;
            case 2:
                return ALPHA;
            case 3:
                return BETA;
            case 4:
                return RC;
            case 5:
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
