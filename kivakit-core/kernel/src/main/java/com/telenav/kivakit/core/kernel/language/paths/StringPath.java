////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.paths;

import com.telenav.kivakit.core.kernel.KivaKit;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.strings.Strings;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguagePath;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * A {@link Path} of strings that has a given separator string. This class contains numerous methods which down-cast the
 * return value of the superclass to make use easier for clients. Methods that are unique to this class mainly have to
 * do with the conversion of {@link StringPath}s to and from string * values:
 *
 * <p><b>String Values</b></p>
 *
 * <ul>
 *     <li>{@link #asContractedString(int)} - This path as a contracted string of the given maximum length </li>
 *     <li>{@link #asString()} - This path as a string</li>
 *     <li>{@link #asJavaPath()} - This path as a {@link java.nio.file.Path}</li>
 *     <li>{@link #separator()} - The separator string for this path, by default this is a forward slash</li>
 *     <li>{@link #join()} - This path joined by the path {@link #separator()}</li>
 *     <li>{@link #join(String)} - This path joined with the given separator</li>
 *     <li>{@link #startsWith(String)} - True if this path starts with the given prefix when joined</li>
 *     <li>{@link #endsWith(String)} - True if this path ends with the given suffix when joined</li>
 *     <li>{@link #withSeparator(String)} - This path with the given separator string</li>
 * </ul>
 *
 * <p><b>Parsing</b></p>
 *
 * <ul>
 *     <li>{@link #parsePackageStringPath(String)} - A string path for the given Java package path</li>
 *     <li>{@link #parseStringPath(String, String)} - A string path for the given string and separator</li>
 *     <li>{@link #parseStringPath(String, String, String)} - A string for the given root pattern, string and separator</li>
 * </ul>
 *
 * <p><b>Factories</b></p>
 *
 * <ul>
 *     <li>{@link #stringPath(URI)} - A string path for the given URI</li>
 *     <li>{@link #stringPath(List) - A string path for the given list of elements}</li>
 *     <li>{@link #stringPath(java.nio.file.Path)} - A string path for the given Java NIO path</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguagePath.class)
public class StringPath extends Path<String>
{
    private static final Map<String, Pattern> patterns = new HashMap<>();

    /**
     * @return A dot separated Java-style package path
     */
    public static StringPath parsePackageStringPath(final String path)
    {
        return parseStringPath(path, "\\.");
    }

    /**
     * @param path The path to parse
     * @param rootPattern A Java regular expression matching the path root (such as "/" or "C:\")
     * @param separatorPattern The Java regular expression used to split path elements
     * @return A string path for the given string, root pattern and separator pattern
     */
    public static StringPath parseStringPath(final String path, final String rootPattern, final String separatorPattern)
    {
        if (rootPattern != null)
        {
            final var matcher = pattern(rootPattern).matcher(path);
            if (matcher.lookingAt())
            {
                final var tail = matcher.group("path");
                return new StringPath(matcher.group("root"), StringList.splitOnPattern(tail, separatorPattern));
            }
        }
        return new StringPath(null, StringList.splitOnPattern(path, separatorPattern));
    }

    /**
     * @return A string path for the given string and separator pattern
     */
    public static StringPath parseStringPath(final String path, final String separatorPattern)
    {
        return parseStringPath(path, null, separatorPattern);
    }

    /**
     * @return A path (sans scheme) for the given URI
     */
    public static StringPath stringPath(final List<String> elements)
    {
        return new StringPath(elements);
    }

    /**
     * @return A path for the given strings
     */
    public static StringPath stringPath(final String first, final String... more)
    {
        final var list = StringList.stringList(first);
        list.addAll(more);
        return stringPath(list);
    }

    /**
     * @return A path (sans scheme) for the given URI
     */
    public static StringPath stringPath(final URI uri)
    {
        return stringPath(java.nio.file.Path.of(uri));
    }

    /**
     * @return A path for the given NIO path
     */
    public static StringPath stringPath(final java.nio.file.Path path)
    {
        final var root = path.getRoot();
        final var elements = new StringList();
        for (int i = 0; i < path.getNameCount(); i++)
        {
            elements.add(path.getName(i).toString());
        }
        return new StringPath(root == null ? null : root.toString(), elements);
    }

    /** By default paths are separated by slashes */
    private String separator = "/";

    protected StringPath(final List<String> elements)
    {
        this(null, elements);
    }

    protected StringPath(final String root, final List<String> elements)
    {
        super(root, substituteSystemVariables(elements));
    }

    protected StringPath(final StringPath path)
    {
        this(path.rootElement(), path.elements());
        separator = path.separator;
    }

    /**
     * @return A contraction of this path as a string. Middle elements are removed until the length is less than the
     * given maximum length.
     */
    public String asContractedString(final int maximumLength)
    {
        final int size = size();
        if (size >= 3)
        {
            final var copy = new StringPath(this);
            final int middle = size / 2;
            var before = copy.subpath(0, middle);
            var after = copy.subpath(middle + 1, size);
            final var ellipsis = "...";
            while (before.join().length() + after.join().length() + ellipsis.length() + 2 > maximumLength)
            {
                if (before.size() >= after.size())
                {
                    before = before.withoutLast();
                }
                else
                {
                    after = after.withoutFirst();
                }
            }
            return before.withChild(ellipsis).withChild(after).join();
        }
        else
        {
            return toString();
        }
    }

    /**
     * @return A Java NIO {@link Path} object for this path
     */
    public java.nio.file.Path asJavaPath()
    {
        if (!isEmpty() && get(0).equals("."))
        {
            return java.nio.file.Path.of(withoutFirst().join());
        }
        return java.nio.file.Path.of(join());
    }

    /**
     * @return This path as a String
     */
    @KivaKitIncludeProperty
    public String asString()
    {
        return join();
    }

    /**
     * @return True if this path ends with the given suffix
     */
    public boolean endsWith(final String suffix)
    {
        return asString().endsWith(suffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath first(final int n)
    {
        return (StringPath) super.first(n);
    }

    /**
     * @return This path joined by the given separator
     */
    public String join(final String separator)
    {
        final var list = new StringList();
        list.addAll(this);
        final var root = rootElement();
        return Strings.notNull(root) + list.join(separator);
    }

    /**
     * @return This path joined by the path {@link #separator()}
     */
    public String join()
    {
        return join(separator());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath last(final int n)
    {
        return (StringPath) super.last(n);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath parent()
    {
        return (StringPath) super.parent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath root()
    {
        return (StringPath) super.root();
    }

    /**
     * @return The separator to use when converting the path to string representation
     */
    public String separator()
    {
        return separator;
    }

    /**
     * @return True if this path starts with the given prefix
     */
    public boolean startsWith(final String prefix)
    {
        return asString().startsWith(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath subpath(final int start, final int end)
    {
        return (StringPath) super.subpath(start, end);
    }

    /**
     * @return This path joined by the path {@link #separator()}
     */
    @Override
    public String toString()
    {
        return join();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath transformed(final Function<String, String> consumer)
    {
        return (StringPath) super.transformed(consumer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath withChild(final Path<String> that)
    {
        return (StringPath) super.withChild(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath withChild(final String element)
    {
        return (StringPath) super.withChild(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath withParent(final String element)
    {
        return (StringPath) super.withParent(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath withParent(final Path<String> that)
    {
        return (StringPath) super.withParent(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath withRoot(final String root)
    {
        return (StringPath) super.withRoot(root);
    }

    /**
     * @return This string path with the given separator
     */
    public StringPath withSeparator(final String separator)
    {
        final var copy = (StringPath) copy();
        copy.separator = separator;
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath withoutFirst()
    {
        return (StringPath) super.withoutFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath withoutLast()
    {
        return (StringPath) super.withoutLast();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath withoutOptionalPrefix(final Path<String> prefix)
    {
        return (StringPath) super.withoutOptionalPrefix(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath withoutOptionalSuffix(final Path<String> suffix)
    {
        return (StringPath) super.withoutOptionalSuffix(suffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath withoutPrefix(final Path<String> prefix)
    {
        return (StringPath) super.withoutPrefix(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath withoutRoot()
    {
        return (StringPath) super.withoutRoot();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath withoutSuffix(final Path<String> suffix)
    {
        return (StringPath) super.withoutSuffix(suffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Path<String> onCopy(final String root, final List<String> elements)
    {
        return new StringPath(root, elements);
    }

    private static Pattern pattern(final String rootPattern)
    {
        var pattern = patterns.get(rootPattern);
        if (pattern == null)
        {
            pattern = Pattern.compile("(?<root>" + rootPattern + ")(?<path>.*)");
            patterns.put(rootPattern, pattern);
        }
        return pattern;
    }

    private static List<String> substituteSystemVariables(final List<String> elements)
    {
        for (int i = 0; i < elements.size(); i++)
        {
            elements.set(i, KivaKit.get().properties().expanded(elements.get(i)));
        }
        return elements;
    }
}
