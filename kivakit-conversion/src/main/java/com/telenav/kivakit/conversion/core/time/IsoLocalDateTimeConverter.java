package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionTime;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.ZoneId;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionTime.class)
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
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
        super(listener, ISO_LOCAL_DATE_TIME, zone);
    }
}
