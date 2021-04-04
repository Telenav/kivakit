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
public class LocalDateTimeConverter extends BaseFormattedLocalTimeConverter
{
    public LocalDateTimeConverter(final Listener listener)
    {
        super(listener, TimeFormat.DATE_TIME);
    }

    public LocalDateTimeConverter(final Listener listener, final ZoneId zone)
    {
        super(listener, TimeFormat.DATE_TIME, zone);
    }
}
