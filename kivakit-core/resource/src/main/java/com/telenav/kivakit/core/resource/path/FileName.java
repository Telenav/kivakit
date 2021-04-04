////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.path;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.filesystem.File;
import com.telenav.kivakit.core.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.core.kernel.interfaces.naming.Named;
import com.telenav.kivakit.core.kernel.language.strings.PathStrings;
import com.telenav.kivakit.core.kernel.language.strings.Strings;
import com.telenav.kivakit.core.kernel.language.strings.Strip;
import com.telenav.kivakit.core.kernel.language.time.LocalTime;
import com.telenav.kivakit.core.kernel.language.time.conversion.converters.LocalDateConverter;
import com.telenav.kivakit.core.kernel.language.time.conversion.converters.LocalDateTimeConverter;
import com.telenav.kivakit.core.kernel.language.time.conversion.converters.LocalTimeConverter;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourcePath;

import java.time.ZoneId;
import java.util.regex.Pattern;

@UmlClassDiagram(diagram = DiagramResourcePath.class)
public class FileName implements Named, Comparable<FileName>
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    public static FileName date()
    {
        return new FileName(LocalTime.now().asDateString());
    }

    public static FileName date(final LocalTime time)
    {
        return new FileName(new LocalDateConverter(LOGGER).toString(time));
    }

    public static FileName date(final LocalTime time, final ZoneId zone)
    {
        return new FileName(new LocalDateConverter(LOGGER, zone).toString(time));
    }

    public static FileName dateTime()
    {
        return dateTime(LocalTime.now());
    }

    public static FileName dateTime(final LocalTime time)
    {
        return new FileName(new LocalDateTimeConverter(LOGGER).toString(time));
    }

    public static FileName dateTime(final LocalTime time, final ZoneId zone)
    {
        return new FileName(new LocalDateTimeConverter(LOGGER, zone).toString(time));
    }

    public static LocalTime parseDateTime(final String dateTime)
    {
        return new LocalDateTimeConverter(LOGGER).convert(dateTime);
    }

    public static LocalTime parseDateTime(final String dateTime, final ZoneId zone)
    {
        return new LocalDateTimeConverter(LOGGER, zone).convert(dateTime);
    }

    public static FileName time(final LocalTime time)
    {
        return new FileName(new LocalTimeConverter(LOGGER, time.timeZone()).toString(time));
    }

    public static FileName time(final LocalTime time, final ZoneId zone)
    {
        return new FileName(new LocalTimeConverter(LOGGER, zone).toString(time));
    }

    private final String name;

    public FileName(final String name)
    {
        this.name = normalize(name);
    }

    public FilePath asPath()
    {
        return FilePath.filePath(this);
    }

    public FileName base()
    {
        if (name().contains("."))
        {
            final var before = PathStrings.optionalHead(name(), '.');
            if (before != null)
            {
                return new FileName(before);
            }
        }
        return this;
    }

    @Override
    public int compareTo(final FileName that)
    {
        return name().compareTo(that.name());
    }

    public Extension compoundExtension()
    {
        if (name().contains("."))
        {
            final var after = PathStrings.tail(name(), '.');
            if (after != null)
            {
                return new Extension(after);
            }
        }
        return null;
    }

    public boolean contains(final CharSequence name)
    {
        return name().contains(name);
    }

    public boolean endsWith(final Extension extension)
    {
        final var compoundExtension = compoundExtension();
        return compoundExtension != null && compoundExtension.endsWith(extension);
    }

    public boolean endsWith(final String suffix)
    {
        return name().endsWith(suffix);
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof FileName)
        {
            final var that = (FileName) object;
            return name.equals(that.name);
        }
        return false;
    }

    public Extension extension()
    {
        if (name().contains("."))
        {
            return new Extension(PathStrings.optionalSuffix(name(), '.'));
        }
        return null;
    }

    public Matcher<File> fileMatcher()
    {
        return file -> name().equals(file.fileName().name());
    }

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

    public boolean isArchive()
    {
        return extension().isArchive();
    }

    public boolean isExecutable()
    {
        return extension().isExecutable();
    }

    public LocalTime localDateTime()
    {
        return localDateTime(LOGGER);
    }

    public LocalTime localDateTime(final Listener listener)
    {
        final LocalTime time;
        if ((time = new LocalDateTimeConverter(listener).convert(name())) != null)
        {
            return time;
        }
        return new LocalDateConverter(listener).convert(name());
    }

    public Matcher<FileName> matcher()
    {
        return fileName -> name().equals(fileName.name());
    }

    public boolean matches(final Pattern pattern)
    {
        return pattern.matcher(name()).matches();
    }

    @Override
    public String name()
    {
        return name;
    }

    public FileName normalized()
    {
        return new FileName(FilePath.filePath(this).normalized().toString());
    }

    public FileName prepend(final String prefix)
    {
        return new FileName(prefix + name);
    }

    public boolean startsWith(final String prefix)
    {
        return name().startsWith(prefix);
    }

    public FileName toLowerCase()
    {
        return new FileName(name().toLowerCase());
    }

    @Override
    public String toString()
    {
        return name();
    }

    public FileName toUpperCase()
    {
        return new FileName(name().toUpperCase());
    }

    public FileName withExtension(final Extension extension)
    {
        return new FileName(name() + extension);
    }

    public FileName withSuffix(final String suffix)
    {
        return new FileName(name + suffix);
    }

    public FileName withoutCompoundExtension()
    {
        final var extension = extension();
        if (extension != null)
        {
            return new FileName(PathStrings.optionalHead(name(), '.'));
        }
        return this;
    }

    public FileName withoutExtension()
    {
        final var extension = extension();
        if (extension != null)
        {
            final var before = PathStrings.optionalHead(name(), '.');
            if (before != null)
            {
                return new FileName(before);
            }
        }
        return this;
    }

    public FileName withoutExtension(final Extension extension)
    {
        if (name().endsWith(extension.toString()))
        {
            return new FileName(Strip.trailing(name(), extension.toString()));
        }
        return this;
    }

    /**
     *
     */
    public FileName withoutKnownExtensions()
    {
        var name = this;
        boolean removedOne;
        do
        {
            removedOne = false;
            for (final var extension : Extension.known())
            {
                if (name.endsWith(extension))
                {
                    name = new FileName(Strip.ending(name.toString(), extension.toString()));
                    removedOne = true;
                }
            }
        }
        while (removedOne);
        return name;
    }

    private String normalize(final String name)
    {
        return Strings.removeAll(name, '\'').replaceAll("[,:; ]", "_").replace('/', '-');
    }
}
