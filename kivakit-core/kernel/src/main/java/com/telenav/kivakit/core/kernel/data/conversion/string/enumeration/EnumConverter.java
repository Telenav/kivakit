////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.data.conversion.string.enumeration;

import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.language.strings.CaseFormat;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataConversionOther;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * Converts between strings and enum values. Lower case hyphenated values are accepted. For example, the enum value
 * max-value is equivalent to MAX_VALUE.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataConversionOther.class)
public class EnumConverter<T extends Enum<T>> extends BaseStringConverter<T>
{
    /** The type of enum */
    private final Class<T> enumType;

    /**
     * @param listener The listener to hear any conversion issues
     * @param enumType The enum type to convert
     */
    public EnumConverter(final Listener listener, final Class<T> enumType)
    {
        super(listener);
        ensureNotNull(enumType);
        this.enumType = enumType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected T onConvertToObject(final String value)
    {
        return Enum.valueOf(enumType, CaseFormat.lowerHyphenToUpperUnderscore(value));
    }
}
