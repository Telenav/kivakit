package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionTime;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionTime.class)
public class UtcDateTimeConverter extends BaseFormattedLocalTimeConverter
{
    public UtcDateTimeConverter(Listener listener)
    {
        super(listener, DateTimeFormatter.ISO_LOCAL_DATE_TIME, ZoneId.of("UTC"));
    }
}
