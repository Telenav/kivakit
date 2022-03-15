package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.conversion.lexakai.DiagramConversionTime;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionTime.class)
public class IsoLocalDateTimeConverter extends BaseFormattedLocalTimeConverter
{
    public IsoLocalDateTimeConverter(Listener listener,
                                     ZoneId zone)
    {
        super(listener, DateTimeFormatter.ISO_LOCAL_DATE_TIME, zone);
    }
}
