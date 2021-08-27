package com.telenav.kivakit.kernel.language.time.conversion.converters;

import com.telenav.kivakit.kernel.language.time.conversion.BaseFormattedLocalTimeConverter;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageTime;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageTime.class)
public class IsoLocalDateTimeConverter extends BaseFormattedLocalTimeConverter
{
    public IsoLocalDateTimeConverter(final Listener listener,
                                     final ZoneId zone)
    {
        super(listener, DateTimeFormatter.ISO_LOCAL_DATE_TIME, zone);
    }
}
