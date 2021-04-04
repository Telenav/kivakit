package com.telenav.kivakit.core.kernel.language.time.conversion;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageTime;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageTime.class)
public enum TimeFormat
{
    DATE("yyyy.MM.dd"),
    TIME("h.mma"),
    DATE_TIME("yyyy.MM.dd_h.mma"),
    DATE_TIME_WITH_SECONDS("yyyy.MM.dd_h.mm.ssa"),
    DATE_TIME_WITH_MILLISECONDS("yyyy.MM.dd_h.mm.ss.SSSa");

    private final String pattern;

    TimeFormat(final String pattern)
    {
        this.pattern = pattern;
    }

    DateTimeFormatter formatter()
    {
        return new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern(pattern)
                .toFormatter();
    }
}
