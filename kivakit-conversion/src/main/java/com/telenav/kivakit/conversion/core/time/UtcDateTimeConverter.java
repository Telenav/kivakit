package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionTime;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.ZoneId;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionTime.class)
@ApiQuality(stability = STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class UtcDateTimeConverter extends BaseFormattedLocalTimeConverter
{
    public UtcDateTimeConverter(Listener listener)
    {
        super(listener, ISO_LOCAL_DATE_TIME, ZoneId.of("UTC"));
    }
}
