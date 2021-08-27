////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.resource.path;

import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.interfaces.naming.Named;
import com.telenav.kivakit.kernel.language.strings.Paths;
import com.telenav.kivakit.kernel.language.strings.Strings;
import com.telenav.kivakit.kernel.language.strings.Strip;
import com.telenav.kivakit.kernel.language.time.LocalTime;
import com.telenav.kivakit.kernel.language.time.conversion.converters.LocalDateConverter;
import com.telenav.kivakit.kernel.language.time.conversion.converters.LocalDateTimeConverter;
import com.telenav.kivakit.kernel.language.time.conversion.converters.LocalTimeConverter;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResourcePath;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.ZoneId;
import java.util.regex.Pattern;

/**
 * A file name, with a base name and an {@link Extension}.
 *
 * <p>
 * File names can be created with various factory methods, including methods that produce valid filenames for dates and
 * times as well as the general method {@link #parse(String)}.
 * </p>
 *
 * <ul>
 *     <li>{@link #name()} - This filename as a string</li>
 *     <li>{@link #asPath()} - This filename as A {@link FilePath}</li>
 *     <li>{@link #base()} - This filename without any extension</li>
 *     <li>{@link #compoundExtension()} - The (possibly compound) extension of this filename, like ".tar" or ".tar.gz"</li>
 *     <li>{@link #extension()} - The final extension of this filename, for example the extension of "data.tar.gz" is ".gz"</li>
 *     <li>{@link #localDateTime()} - This file name parsed as a {@link LocalTime} object using the KivaKit DATE_TIME format
 *       ("yyyy.MM.dd_h.mma").</li>
 * </ul>
 *
 * <p><b>Matching</b></p>
 *
 * <ul>
 *     <li>{@link #fileMatcher()} - A matcher that matches resources with exactly this file name</li>
 *     <li>{@link #matcher()} - A matcher for this file name</li>
 *     <li>{@link #matches(Pattern)} - True if this filename matches the given {@link Pattern}</li>
 * </ul>
 *
 * <p><b>Checks</b></p>
 *
 * <ul>
 *     <li>{@link #startsWith(String)} - True if this filename starts with the given string</li>
 *     <li>{@link #endsWith(Extension)} - True if this filename ends with the given extension</li>
 *     <li>{@link #endsWith(String)} - True if this filename ends with the given string</li>
 * </ul>
 *
 * <p><b>Functional</b></p>
 *
 * <ul>
 *     <li>{@link #normalized()} - This filename normalized for any filesystem using {@link FilePath#normalized()}.</li>
 *     <li>{@link #prefixedWith(String)} - This filename with the given prefix</li>
 *     <li>{@link #withExtension(Extension)} - This filename with the given extension</li>
 *     <li>{@link #withoutExtension()} - This filename without the final extension</li>
 *     <li>{@link #withoutExtension(Extension)} - This filename without the given final extension if it has that extension</li>
 *     <li>{@link #withoutCompoundExtension()} - This filename without all extensions</li>
 *     <li>{@link #withSuffix(String)} - This filename with the given suffix appended</li>
 *     <li>{@link #toLowerCase()} - This filename in lowercase</li>
 *     <li>{@link #toUpperCase()} - This filename in uppercase</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourcePath.class)
@LexakaiJavadoc(complete = true)
public class FileName implements Named, Comparable<FileName>
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    public static FileName date()
    {
        return parse(LocalTime.now().asDateString());
    }

    public static FileName date(final LocalTime time)
    {
        return parse(new LocalDateConverter(LOGGER).unconvert(time));
    }

    public static FileName date(final LocalTime time, final ZoneId zone)
    {
        return parse(new LocalDateConverter(LOGGER, zone).unconvert(time));
    }

    public static FileName dateTime()
    {
        return dateTime(LocalTime.now());
    }

    public static FileName dateTime(final LocalTime time)
    {
        return parse(new LocalDateTimeConverter(LOGGER).unconvert(time));
    }

    public static FileName dateTime(final LocalTime time, final ZoneId zone)
    {
        return parse(new LocalDateTimeConverter(LOGGER, zone).unconvert(time));
    }

    public static FileName parse(final String name)
    {
        return new FileName(name);
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
        return parse(new LocalTimeConverter(LOGGER, time.timeZone()).unconvert(time));
    }

    public static FileName time(final LocalTime time, final ZoneId zone)
    {
        return parse(new LocalTimeConverter(LOGGER, zone).unconvert(time));
    }

    private final String name;

    protected FileName(final String name)
    {
        this.name = normalize(name);
    }

    /**
     * @return This filename as a {@link FilePath}
     */
    public FilePath asPath()
    {
        return FilePath.filePath(this);
    }

    /**
     * @return This filename without any extensions
     */
    public FileName base()
    {
        if (name().contains("."))
        {
            final var before = Paths.optionalHead(name(), '.');
            if (before != null)
            {
                return parse(before);
            }
        }
        return this;
    }

    @Override
    public int compareTo(final FileName that)
    {
        return name().compareTo(that.name());
    }

    /**
     * @return The compound extension of this filename, like ".tar.gz"
     */
    public Extension compoundExtension()
    {
        if (name().contains("."))
        {
            final var after = Paths.tail(name(), '.');
            if (after != null)
            {
                return new Extension(after);
            }
        }
        return null;
    }

    /**
     * @return True if this filename ends with the given extension
     */
    public boolean endsWith(final Extension extension)
    {
        final var compoundExtension = compoundExtension();
        return compoundExtension != null && compoundExtension.endsWith(extension);
    }

    /**
     * @return True if this filename ends with the given suffix
     */
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

    /**
     * @return The final extension of this filename. For example, the extension of "data.tar.gz" is ".gz". To get the
     * full compound extension, ".tar.gz", use {@link #compoundExtension()}.
     */
    public Extension extension()
    {
        if (name().contains("."))
        {
            return new Extension(Paths.optionalSuffix(name(), '.'));
        }
        return null;
    }

    /**
     * @return A matcher for files with this filename
     */
    public Matcher<File> fileMatcher()
    {
        return file -> name().equals(file.fileName().name());
    }

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

    /**
     * @return The {@link LocalTime} object for this filename
     */
    public LocalTime localDateTime()
    {
        return localDateTime(LOGGER);
    }

    /**
     * @return This file name parsed as a {@link LocalTime} object using the KivaKit DATE_TIME format
     * ("yyyy.MM.dd_h.mma").
     */
    public LocalTime localDateTime(final Listener listener)
    {
        final LocalTime time;
        if ((time = new LocalDateTimeConverter(listener).convert(name())) != null)
        {
            return time;
        }
        return new LocalDateConverter(listener).convert(name());
    }

    /**
     * @return A matcher for this filename
     */
    public Matcher<FileName> matcher()
    {
        return fileName -> name().equals(fileName.name());
    }

    /**
     * @return True if this filename matches the given {@link Pattern}
     */
    public boolean matches(final Pattern pattern)
    {
        return pattern.matcher(name()).matches();
    }

    /**
     * @return This filename as a string
     */
    @Override
    public String name()
    {
        return name;
    }

    /**
     * @return This filename normalized for any filesystem using {@link FilePath#normalized()}.
     */
    public FileName normalized()
    {
        return parse(FilePath.filePath(this).normalized().toString());
    }

    /**
     * @return This filename with the given prefix
     */
    public FileName prefixedWith(final String prefix)
    {
        return parse(prefix + name);
    }

    /**
     * @return True if this filename starts with the given prefix
     */
    public boolean startsWith(final String prefix)
    {
        return name().startsWith(prefix);
    }

    /**
     * @return This filename in lower case
     */
    public FileName toLowerCase()
    {
        return parse(name().toLowerCase());
    }

    @Override
    public String toString()
    {
        return name();
    }

    /**
     * @return This filename in uppercase
     */
    public FileName toUpperCase()
    {
        return parse(name().toUpperCase());
    }

    /**
     * @return This filename with the given extension added
     */
    public FileName withExtension(final Extension extension)
    {
        return parse(name() + extension);
    }

    /**
     * @return This filename with the given suffix
     */
    public FileName withSuffix(final String suffix)
    {
        return parse(name + suffix);
    }

    /**
     * @return This filename without any extension
     */
    public FileName withoutCompoundExtension()
    {
        final var extension = extension();
        if (extension != null)
        {
            return parse(Paths.optionalHead(name(), '.'));
        }
        return this;
    }

    /**
     * @return This filename without its final extension
     */
    public FileName withoutExtension()
    {
        final var extension = extension();
        if (extension != null)
        {
            final var before = Paths.optionalHead(name(), '.');
            if (before != null)
            {
                return parse(before);
            }
        }
        return this;
    }

    /**
     * @return This filename without the given extension if it ends in that extension
     */
    public FileName withoutExtension(final Extension extension)
    {
        if (name().endsWith(extension.toString()))
        {
            return parse(Strip.trailing(name(), extension.toString()));
        }
        return this;
    }

    /**
     * @return This filename with all known extensions removed
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
                    name = parse(Strip.ending(name.toString(), extension.toString()));
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
