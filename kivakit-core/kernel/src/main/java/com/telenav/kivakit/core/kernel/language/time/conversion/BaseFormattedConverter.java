package com.telenav.kivakit.core.kernel.language.time.conversion;

import com.telenav.kivakit.core.kernel.language.time.LocalTime;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageTime;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.messaging.Listener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Convert to/from local time
 */
@UmlClassDiagram(diagram = DiagramLanguageTime.class)
public class BaseFormattedConverter extends BaseStringConverter<LocalTime>
{
    /** The date time formatter */
    private final DateTimeFormatter formatter;

    /** The local time zone */
    private ZoneId zone;

    /** Which kind of formatting is going on */
    private final TimeFormat format;

    public BaseFormattedConverter(final Listener listener, final ZoneId zone,
                                  final DateTimeFormatter formatter, final TimeFormat format)
    {
        super(listener);
        this.zone = zone;
        this.format = format;
        this.formatter = formatter;
    }

    public DateTimeFormatter formatter()
    {
        return formatter.withZone(zone);
    }

    @Override
    protected LocalTime onConvertToObject(final String value)
    {
        switch (format)
        {
            case DATE:
            {
                final var date = LocalDate.parse(value, formatter);
                final var instant = date.atStartOfDay(zone).toInstant();
                return LocalTime.milliseconds(zone, instant.toEpochMilli());
            }

            case TIME:
            {
                final var dateTime = java.time.LocalTime.parse(value, formatter);
                final var instant = dateTime.atDate(LocalDate.EPOCH).atZone(zone).toInstant();
                return LocalTime.milliseconds(zone, instant.toEpochMilli());
            }

            case DATE_TIME:
            {
                final var dateTime = LocalDateTime.parse(value, formatter);
                final var instant = dateTime.atZone(zone).toInstant();
                return LocalTime.milliseconds(zone, instant.toEpochMilli());
            }

            default:
                return null;
        }
    }

    @Override
    protected String onConvertToString(final LocalTime value)
    {
        return formatter().format(value.javaLocalDateTime());
    }

    protected void zone(final ZoneId zone)
    {
        this.zone = zone;
    }
}
