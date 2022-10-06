package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionTime;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.ZoneId;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionTime.class)
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class UtcDateTimeConverter extends BaseFormattedLocalTimeConverter
{
    public UtcDateTimeConverter(Listener listener)
    {
        super(listener, ISO_LOCAL_DATE_TIME, ZoneId.of("UTC"));
    }
}
