package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionTime;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionTime.class)
@ApiQuality(stability = STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class IsoLocalDateTimeConverter extends BaseFormattedLocalTimeConverter
{
    /**
     * @param listener The listener to report problems to
     * @param zone The timezone
     */
    public IsoLocalDateTimeConverter(Listener listener,
                                     ZoneId zone)
    {
        super(listener, DateTimeFormatter.ISO_LOCAL_DATE_TIME, zone);
    }
}
