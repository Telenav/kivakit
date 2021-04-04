////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.values.identifier;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * An identifier whose value is a {@link String}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageValue.class)
public class StringIdentifier implements Comparable<StringIdentifier>
{
    @JsonProperty
    @Schema(description = "The unique identifier",
            required = true)
    private String identifier;

    public StringIdentifier(final String identifier)
    {
        this.identifier = identifier;
    }

    protected StringIdentifier()
    {
    }

    public String asString()
    {
        return identifier;
    }

    @Override
    public int compareTo(final StringIdentifier that)
    {
        return identifier.compareTo(that.identifier);
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof StringIdentifier)
        {
            final var that = (StringIdentifier) object;
            return identifier.equals(that.identifier);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return identifier.hashCode();
    }

    @KivaKitIncludeProperty
    public String identifier()
    {
        return identifier;
    }

    @Override
    public String toString()
    {
        return identifier;
    }
}
