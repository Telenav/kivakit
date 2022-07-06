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

package com.telenav.kivakit.resource;

import com.telenav.kivakit.conversion.core.time.LocalDateConverter;
import com.telenav.kivakit.conversion.core.time.LocalDateTimeConverter;
import com.telenav.kivakit.conversion.core.time.LocalTimeConverter;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.LoggerFactory;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.string.Paths;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.kivakit.core.string.Strip;
import com.telenav.kivakit.core.time.LocalTime;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.resource.lexakai.DiagramResourcePath;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.ZoneId;
import java.util.regex.Pattern;

/**
 * A file name, with a base name and an {@link Extension}.
 *
 * <p>
 * File names can be created with various factory methods, including methods that produce valid filenames for dates and
 * times as well as the general method {@link #parseFileName(Listener listener, String)}.
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
 *     <li>{@link #matcher()} - A matcher that matches resources with exactly this file name</li>
 *     <li>{@link #fileNameMatcher()} - A matcher for this file name</li>
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
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramResourcePath.class)
@LexakaiJavadoc(complete = true)
public class FileName implements Named, Comparable<FileName>
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    public static FileName date()
    {
        return parseFileName(LOGGER, LocalTime.now().asDateString());
    }

    public static FileName date(LocalTime time)
    {
        return parseFileName(LOGGER, new LocalDateConverter(LOGGER).unconvert(time));
    }

    public static FileName date(LocalTime time, ZoneId zone)
    {
        return parseFileName(LOGGER, new LocalDateConverter(LOGGER, zone).unconvert(time));
    }

    public static FileName dateTime()
    {
        return dateTime(LocalTime.now());
    }

    public static FileName dateTime(LocalTime time)
    {
        return parseFileName(LOGGER, new LocalDateTimeConverter(LOGGER).unconvert(time));
    }

    public static FileName dateTime(LocalTime time, ZoneId zone)
    {
        return parseFileName(LOGGER, new LocalDateTimeConverter(LOGGER, zone).unconvert(time));
    }

    public static LocalTime parseDateTime(Listener listener, String dateTime)
    {
        return new LocalDateTimeConverter(listener).convert(dateTime);
    }

    public static LocalTime parseDateTime(Listener listener, String dateTime, ZoneId zone)
    {
        return new LocalDateTimeConverter(listener, zone).convert(dateTime);
    }

    public static FileName parseFileName(Listener listener, String name)
    {
        return new FileName(name);
    }

    public static FileName time(LocalTime time)
    {
        return parseFileName(LOGGER, new LocalTimeConverter(LOGGER, time.timeZone()).unconvert(time));
    }

    public static FileName time(LocalTime time, ZoneId zone)
    {
        return parseFileName(LOGGER, new LocalTimeConverter(LOGGER, zone).unconvert(time));
    }

    private final String name;

    protected FileName(String name)
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
            var before = Paths.optionalHead(name(), '.');
            if (before != null)
            {
                return parseFileName(LOGGER, before);
            }
        }
        return this;
    }

    @Override
    public int compareTo(FileName that)
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
            var after = Paths.tail(name(), '.');
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
    public boolean endsWith(Extension extension)
    {
        var compoundExtension = compoundExtension();
        return compoundExtension != null && compoundExtension.endsWith(extension);
    }

    /**
     * @return True if this filename ends with the given suffix
     */
    public boolean endsWith(String suffix)
    {
        return name().endsWith(suffix);
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof FileName)
        {
            var that = (FileName) object;
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
     * @return A matcher for this filename
     */
    public Matcher<FileName> fileNameMatcher()
    {
        return fileName -> name().equals(fileName.name());
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
    public LocalTime localDateTime(Listener listener)
    {
        LocalTime time;
        if ((time = new LocalDateTimeConverter(listener).convert(name())) != null)
        {
            return time;
        }
        return new LocalDateConverter(listener).convert(name());
    }

    /**
     * @return A matcher for files with this filename
     */
    public Matcher<Resource> matcher()
    {
        return file -> name().equals(file.fileName().name());
    }

    /**
     * @return True if this filename matches the given {@link Pattern}
     */
    public boolean matches(Pattern pattern)
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
        return parseFileName(LOGGER, FilePath.filePath(this).normalized().toString());
    }

    /**
     * @return This filename with the given prefix
     */
    public FileName prefixedWith(String prefix)
    {
        return parseFileName(LOGGER, prefix + name);
    }

    /**
     * @return True if this filename starts with the given prefix
     */
    public boolean startsWith(String prefix)
    {
        return name().startsWith(prefix);
    }

    /**
     * @return This filename in lower case
     */
    public FileName toLowerCase()
    {
        return parseFileName(LOGGER, name().toLowerCase());
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
        return parseFileName(LOGGER, name().toUpperCase());
    }

    /**
     * @return This filename with the given extension added
     */
    public FileName withExtension(Extension extension)
    {
        return parseFileName(LOGGER, name() + extension);
    }

    /**
     * @return This filename with the given suffix
     */
    public FileName withSuffix(String suffix)
    {
        return parseFileName(LOGGER, name + suffix);
    }

    /**
     * @return This filename without any extension
     */
    public FileName withoutCompoundExtension()
    {
        var extension = extension();
        if (extension != null)
        {
            return parseFileName(LOGGER, Paths.optionalHead(name(), '.'));
        }
        return this;
    }

    /**
     * @return This filename without its final extension
     */
    public FileName withoutExtension()
    {
        var extension = extension();
        if (extension != null)
        {
            var before = Paths.optionalHead(name(), '.');
            if (before != null)
            {
                return parseFileName(LOGGER, before);
            }
        }
        return this;
    }

    /**
     * @return This filename without the given extension if it ends in that extension
     */
    public FileName withoutExtension(Extension extension)
    {
        if (name().endsWith(extension.toString()))
        {
            return parseFileName(LOGGER, Strip.trailing(name(), extension.toString()));
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
            for (var extension : Extension.known())
            {
                if (name.endsWith(extension))
                {
                    name = parseFileName(LOGGER, Strip.ending(name.toString(), extension.toString()));
                    removedOne = true;
                }
            }
        }
        while (removedOne);
        return name;
    }

    private String normalize(String name)
    {
        return Strings.removeAll(name, '\'').replaceAll("[,:; ]", "_").replace('/', '-');
    }
}
