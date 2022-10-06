package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionTime;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionTime.class)
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
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
