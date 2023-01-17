package com.telenav.kivakit.conversion.core.time.local;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.core.time.BaseLocalTimeConverter;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionTime;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.ZoneId;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionTime.class)
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class IsoLocalDateTimeConverter extends BaseLocalTimeConverter
{
    public IsoLocalDateTimeConverter(Listener listener, ZoneId zone)
    {
        super(listener, ISO_LOCAL_DATE_TIME, zone);
    }

    public IsoLocalDateTimeConverter(Listener listener)
    {
        super(listener, ISO_LOCAL_DATE_TIME, ZoneId.systemDefault());
    }

    public IsoLocalDateTimeConverter()
    {
        this(throwingListener());
    }
}
