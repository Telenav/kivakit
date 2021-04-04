package com.telenav.kivakit.core.kernel.language.time.conversion;

import com.telenav.kivakit.core.kernel.language.time.LocalTime;
import com.telenav.kivakit.core.kernel.language.time.TimeZones;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageTime;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.strings.PathStrings;
import com.telenav.kivakit.core.kernel.messaging.Listener;

import java.time.Instant;
import java.time.ZoneId;

/**
 * Converts time to and from a format that is a valid filename across Mac, Linux and Windows operating systems. Time
 * zone display names are parsed and formatted manually since Java doesn't support this in the formatting/parsing
 * strings. For example, for local time in California, the format would be 2020.04.01_4.01pm_PST
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageTime.class)
public class BaseFormattedLocalTimeConverter extends BaseFormattedConverter
{
    private final ZoneId timeZone;

    private final TimeFormat type;

    public BaseFormattedLocalTimeConverter(final Listener listener, final TimeFormat type)
    {
        this(listener, type, null);
    }

    public BaseFormattedLocalTimeConverter(final Listener listener, final TimeFormat type, final ZoneId zone)
    {
        super(listener, zone, type.formatter(), type);
        this.type = type;
        timeZone = zone;
    }

    protected boolean addTimeZone()
    {
        return true;
    }

    @Override
    protected LocalTime onConvertToObject(final String value)
    {
        zone(zone(value));
        var time = PathStrings.withoutSuffix(value, '_');
        if (time == null)
        {
            time = value;
        }
        return super.onConvertToObject(time);
    }

    @Override
    protected String onConvertToString(final LocalTime value)
    {
        final var timeZone = value.timeZone();
        return type.formatter().format(Instant.ofEpochMilli(value.asMilliseconds())
                .atZone(timeZone)) + (addTimeZone() ? "_" + TimeZones.displayName(timeZone) : "");
    }

    @SuppressWarnings("ConstantConditions")
    private ZoneId zone(final String value)
    {
        if (timeZone != null)
        {
            return timeZone;
        }
        final var zone = TimeZones.forDisplayName(PathStrings.optionalSuffix(value, '_'));
        if (zone != null)
        {
            return zone;
        }
        return LocalTime.localTimeZone();
    }
}
