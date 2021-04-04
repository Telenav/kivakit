////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.patterns.group;

import com.telenav.kivakit.core.kernel.data.conversion.string.enumeration.EnumConverter;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguagePattern;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.messaging.Listener;

@UmlClassDiagram(diagram = DiagramLanguagePattern.class)
public final class EnumGroup<T extends Enum<T>> extends Group<T>
{
    public EnumGroup(final Class<T> enumeration, final Listener listener)
    {
        super(anyOf(enumeration.getEnumConstants()).caseInsensitive(),
                new EnumConverter<>(listener, enumeration));
    }
}
