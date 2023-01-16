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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.core.time.LocalDateConverter;
import com.telenav.kivakit.conversion.core.time.LocalDateTimeConverter;
import com.telenav.kivakit.conversion.core.time.LocalTimeConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.messages.MessageException;
import com.telenav.kivakit.core.time.LocalTime;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourcePath;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.time.ZoneId;
import java.util.regex.Pattern;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.string.Paths.pathOptionalHead;
import static com.telenav.kivakit.core.string.Paths.pathOptionalSuffix;
import static com.telenav.kivakit.core.string.Paths.pathTail;
import static com.telenav.kivakit.core.string.Strings.removeAll;
import static com.telenav.kivakit.core.string.Strip.stripEnding;
import static com.telenav.kivakit.core.string.Strip.stripTrailing;
import static com.telenav.kivakit.filesystem.FilePath.filePath;
import static com.telenav.kivakit.resource.Extension.allExtensions;
import static com.telenav.kivakit.resource.ResourcePath.parseResourcePath;

/**
 * A file name, with a base name and an {@link Extension}.
 *
 * <p><b>Factory Methods</b></p>
 *
 * <p>
 * File names can be created with various factory methods, including methods that produce valid filenames for dates and
 * times as well as the general method {@link #parseFileName(Listener listener, String)}.
 * </p>
 *
 * <ul>
 *     <li>{@link #fileNameForDate()}</li>
 *     <li>{@link #fileNameForDate(LocalTime)}</li>
 *     <li>{@link #fileNameForDate(LocalTime, ZoneId)}</li>
 *     <li>{@link #fileNameForDateTime()}</li>
 *     <li>{@link #fileNameForDateTime(LocalTime)}</li>
 *     <li>{@link #fileNameForDateTime(LocalTime, ZoneId)}</li>
 *     <li>{@link #fileNameForTime(LocalTime)}</li>
 *     <li>{@link #fileNameForTime(LocalTime, ZoneId)}</li>
 *     <li>{@link #parseDateTimeFileName(Listener, String)}</li>
 *     <li>{@link #parseDateTimeFileName(Listener, String, ZoneId)}</li>
 *     <li>{@link #parseFileName(Listener, String)}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #name()} - This filename as a string</li>
 *     <li>{@link #asPath()} - This filename as A {@link FilePath}</li>
 *     <li>{@link #baseName()} - This filename without any extension</li>
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
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class FileName implements
    Named,
    Comparable<FileName>,
    ResourcePathed
{
    /**
     * Returns a {@link FileName} for the given text
     *
     * @param text The text to parse
     * @return The filename
     * @throws MessageException Thrown if the text cannot be parsed into a filename
     */
    public static FileName fileName(String text)
    {
        return parseFileName(throwingListener(), text);
    }

    /**
     * Returns a filename for the current date
     */
    public static FileName fileNameForDate()
    {
        return parseFileName(throwingListener(), LocalTime.now().asDateString());
    }

    /**
     * Returns a filename for the current local time
     */
    @SuppressWarnings("ConstantConditions")
    public static FileName fileNameForDate(@NotNull LocalTime time)
    {
        return parseFileName(throwingListener(), new LocalDateConverter(throwingListener()).unconvert(time));
    }

    /**
     * Returns a filename for the given time in the given timezone
     */
    @SuppressWarnings("ConstantConditions")
    public static FileName fileNameForDate(@NotNull LocalTime time,
                                           @NotNull ZoneId zone)
    {
        return parseFileName(throwingListener(), new LocalDateConverter(throwingListener(), zone).unconvert(time));
    }

    /**
     * Returns a filename for the current date and time
     */
    public static FileName fileNameForDateTime()
    {
        return fileNameForDateTime(LocalTime.now());
    }

    /**
     * Returns a filename for the current date and time
     */
    @SuppressWarnings("ConstantConditions")
    public static FileName fileNameForDateTime(@NotNull LocalTime time)
    {
        return parseFileName(throwingListener(), new LocalDateTimeConverter(throwingListener()).unconvert(time));
    }

    /**
     * Returns a filename for the given local time in the given timezone
     */
    @SuppressWarnings("ConstantConditions")
    public static FileName fileNameForDateTime(@NotNull LocalTime time,
                                               @NotNull ZoneId zone)
    {
        return parseFileName(throwingListener(), new LocalDateTimeConverter(throwingListener(), zone).unconvert(time));
    }

    /**
     * Returns a filename for the given local time
     *
     * @param time The time
     */
    @SuppressWarnings("ConstantConditions")
    public static FileName fileNameForTime(@NotNull LocalTime time)
    {
        return parseFileName(throwingListener(), new LocalTimeConverter(throwingListener(), time.timeZone()).unconvert(time));
    }

    /**
     * Returns a filename for the given local time and timezone
     *
     * @param time The time
     * @param zone The timezone
     */
    @SuppressWarnings("ConstantConditions")
    public static FileName fileNameForTime(@NotNull LocalTime time,
                                           @NotNull ZoneId zone)
    {
        return parseFileName(throwingListener(), new LocalTimeConverter(throwingListener(), zone).unconvert(time));
    }

    /**
     * Parses the given text into a {@link java.time.LocalTime} object
     *
     * @param listener The listener to call with any problems
     * @param text The text to parse
     */
    public static LocalTime parseDateTimeFileName(@NotNull Listener listener,
                                                  @NotNull String text)
    {
        return new LocalDateTimeConverter(listener).convert(text);
    }

    /**
     * Parses the given text into a {@link java.time.LocalTime} object
     *
     * @param listener The listener to call with any problems
     * @param text The text to parse
     * @param zone The timezone
     */
    public static LocalTime parseDateTimeFileName(@NotNull Listener listener,
                                                  @NotNull String text,
                                                  @NotNull ZoneId zone)
    {
        return new LocalDateTimeConverter(listener, zone).convert(text);
    }

    /**
     * Parses the given text into a {@link FileName} object
     *
     * @param listener The listener to call with any problems
     * @param text The text to parse
     */
    public static FileName parseFileName(@NotNull Listener listener,
                                         @NotNull String text)
    {
        return new FileName(text);
    }

    private final String name;

    protected FileName(String name)
    {
        this.name = normalize(name);
    }

    /**
     * Returns this filename as a {@link FilePath}
     */
    public FilePath asPath()
    {
        return filePath(this);
    }

    /**
     * Returns this filename without any extensions
     */
    public FileName baseName()
    {
        if (name().contains("."))
        {
            var before = pathOptionalHead(name(), '.');
            if (before != null)
            {
                return parseFileName(throwingListener(), before);
            }
        }
        return this;
    }

    @Override
    public int compareTo(@NotNull FileName that)
    {
        return name().compareTo(that.name());
    }

    /**
     * Returns the compound extension of this filename, like ".tar.gz"
     */
    @Override
    public Extension compoundExtension()
    {
        if (name().contains("."))
        {
            var after = pathTail(name(), '.');
            if (after != null)
            {
                return new Extension(after);
            }
        }
        return null;
    }

    /**
     * Returns true if this filename ends with the given extension
     */
    public boolean endsWith(@NotNull Extension extension)
    {
        var compoundExtension = compoundExtension();
        return compoundExtension != null && compoundExtension.endsWith(extension);
    }

    /**
     * Returns true if this filename ends with the given suffix
     */
    public boolean endsWith(@NotNull String suffix)
    {
        return name().endsWith(suffix);
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof FileName that)
        {
            return name.equals(that.name);
        }
        return false;
    }

    /**
     * Returns the final extension of this filename. For example, the extension of "data.tar.gz" is ".gz". To get the
     * full compound extension, ".tar.gz", use {@link #compoundExtension()}.
     */
    @Override
    public Extension extension()
    {
        if (name().contains("."))
        {
            return new Extension(pathOptionalSuffix(name(), '.'));
        }
        return null;
    }

    /**
     * Returns a matcher for this filename
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
     * Returns the {@link LocalTime} object for this filename
     */
    public LocalTime localDateTime()
    {
        return localDateTime(throwingListener());
    }

    /**
     * Returns this file name parsed as a {@link LocalTime} object using the KivaKit DATE_TIME format
     * ("yyyy.MM.dd_h.mma").
     */
    public LocalTime localDateTime(@NotNull Listener listener)
    {
        LocalTime time;
        if ((time = new LocalDateTimeConverter(listener).convert(name())) != null)
        {
            return time;
        }
        return new LocalDateConverter(listener).convert(name());
    }

    /**
     * Returns a matcher for files with this filename
     */
    public Matcher<Resource> matcher()
    {
        return file -> name().equals(file.fileName().name());
    }

    /**
     * Returns true if this filename matches the given {@link Pattern}
     */
    public boolean matches(@NotNull Pattern pattern)
    {
        return pattern.matcher(name()).matches();
    }

    /**
     * Returns this filename as a string
     */
    @Override
    public String name()
    {
        return name;
    }

    /**
     * Returns this filename normalized for any filesystem using {@link FilePath#normalized()}.
     */
    public FileName normalized()
    {
        return parseFileName(throwingListener(), filePath(this).normalized().toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath path()
    {
        return parseResourcePath(throwingListener(), name);
    }

    /**
     * Returns this filename with the given prefix
     */
    public FileName prefixedWith(@NotNull String prefix)
    {
        return parseFileName(throwingListener(), prefix + name);
    }

    /**
     * Returns true if this filename starts with the given prefix
     */
    public boolean startsWith(@NotNull String prefix)
    {
        return name().startsWith(prefix);
    }

    /**
     * Returns this filename in lower case
     */
    public FileName toLowerCase()
    {
        return parseFileName(throwingListener(), name().toLowerCase());
    }

    @Override
    public String toString()
    {
        return name();
    }

    /**
     * Returns this filename in uppercase
     */
    public FileName toUpperCase()
    {
        return parseFileName(throwingListener(), name().toUpperCase());
    }

    /**
     * Returns this filename with the given extension added
     */
    public FileName withExtension(@NotNull Extension extension)
    {
        return parseFileName(throwingListener(), name() + extension);
    }

    /**
     * Returns this filename with the given suffix
     */
    public FileName withSuffix(@NotNull String suffix)
    {
        return parseFileName(throwingListener(), name + suffix);
    }

    /**
     * Returns this filename without any extension
     */
    public FileName withoutCompoundExtension()
    {
        var extension = extension();
        if (extension != null)
        {
            return parseFileName(throwingListener(), pathOptionalHead(name(), '.'));
        }
        return this;
    }

    /**
     * Returns this filename without its final extension
     */
    public FileName withoutExtension()
    {
        var extension = extension();
        if (extension != null)
        {
            var before = pathOptionalHead(name(), '.');
            if (before != null)
            {
                return parseFileName(throwingListener(), before);
            }
        }
        return this;
    }

    /**
     * Returns this filename without the given extension if it ends in that extension
     */
    public FileName withoutExtension(@NotNull Extension extension)
    {
        if (name().endsWith(extension.toString()))
        {
            return parseFileName(throwingListener(), stripTrailing(name(), extension.toString()));
        }
        return this;
    }

    /**
     * Returns this filename with all known extensions removed
     */
    public FileName withoutKnownExtensions()
    {
        var name = this;
        boolean removedOne;
        do
        {
            removedOne = false;
            for (var extension : allExtensions())
            {
                if (name.endsWith(extension))
                {
                    name = parseFileName(throwingListener(), stripEnding(name.toString(), extension.toString()));
                    removedOne = true;
                }
            }
        }
        while (removedOne);
        return name;
    }

    private String normalize(@NotNull String name)
    {
        return removeAll(name, '\'').replaceAll("[,:; ]", "_").replace('/', '-');
    }
}
