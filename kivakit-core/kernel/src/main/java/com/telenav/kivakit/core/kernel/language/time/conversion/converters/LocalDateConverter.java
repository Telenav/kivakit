package com.telenav.kivakit.core.kernel.language.time.conversion.converters;

import com.telenav.kivakit.core.kernel.language.time.conversion.BaseFormattedLocalTimeConverter;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageTime;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.time.conversion.TimeFormat;
import com.telenav.kivakit.core.kernel.messaging.Listener;

import java.time.ZoneId;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageTime.class)
public class LocalDateConverter extends BaseFormattedLocalTimeConverter
{
    public LocalDateConverter(final Listener listener, final ZoneId zoneId)
    {
        super(listener, TimeFormat.DATE, zoneId);
    }

    public LocalDateConverter(final Listener listener)
    {
        this(listener, ZoneId.of("UTC"));
    }

    @Override
    protected boolean addTimeZone()
    {
        return false;
    }
}
