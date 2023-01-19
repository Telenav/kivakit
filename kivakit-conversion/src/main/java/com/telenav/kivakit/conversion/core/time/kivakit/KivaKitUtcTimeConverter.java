package com.telenav.kivakit.conversion.core.time.kivakit;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.core.time.BaseTimeConverter;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionTime;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.time.KivaKitTimeFormats.KIVAKIT_UTC_DATE_TIME;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionTime.class)
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class KivaKitUtcTimeConverter extends BaseTimeConverter
{
    public KivaKitUtcTimeConverter(Listener listener)
    {
        super(listener, KIVAKIT_UTC_DATE_TIME);
    }

    public KivaKitUtcTimeConverter()
    {
        this(throwingListener());
    }
}
