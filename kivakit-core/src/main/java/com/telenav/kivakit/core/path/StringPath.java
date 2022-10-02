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

package com.telenav.kivakit.core.path;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.KivaKit;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.internal.lexakai.DiagramPath;
import com.telenav.kivakit.core.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.MORE_TESTING_NEEDED;
import static com.telenav.kivakit.core.project.Project.resolveProject;

/**
 * A {@link Path} of strings that has a given separator string. This class contains numerous methods which down-cast the
 * return value of the superclass to make use easier for clients. Methods that are unique to this class mainly have to
 * do with the conversion of {@link StringPath}s to and from string * values:
 *
 * <p><b>String Values</b></p>
 *
 * <ul>
 *     <li>{@link #asContraction(int)} - This path as a contracted string of the given maximum length </li>
 *     <li>{@link #asJavaPath()} - This path as a {@link java.nio.file.Path}</li>
 *     <li>{@link #asString()} - This path as a string</li>
 *     <li>{@link #endsWith(String)} - True if this path ends with the given suffix when joined</li>
 *     <li>{@link #join()} - This path joined by the path {@link #separator()}</li>
 *     <li>{@link #join(String)} - This path joined with the given separator</li>
 *     <li>{@link #separator()} - The separator string for this path, by default this is a forward slash</li>
 *     <li>{@link #startsWith(String)} - True if this path starts with the given prefix when joined</li>
 *     <li>{@link #withSeparator(String)} - This path with the given separator string</li>
 * </ul>
 *
 * <p><b>Parsing</b></p>
 *
 * <ul>
 *     <li>{@link #parseStringPath(Listener listener, String, String)} - A string path for the given string and separator</li>
 *     <li>{@link #parseStringPath(Listener listener, String, String, String)} - A string for the given root pattern, string and separator</li>
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
 * @see Path
 */
@SuppressWarnings({ "SpellCheckingInspection", "SwitchStatementWithTooFewBranches" })
@UmlClassDiagram(diagram = DiagramPath.class)
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = MORE_TESTING_NEEDED,
            documentation = FULLY_DOCUMENTED)
public class StringPath extends Path<String>
{
    private static final Map<String, Pattern> patterns = new HashMap<>();

    /**
     * @param path The path to parse
     * @param rootPattern A Java regular expression matching the path root (such as "/" or "C:\")
     * @param separatorPattern The Java regular expression used to split path elements
     * @return A string path for the given string, root pattern and separator pattern
     */
    @SuppressWarnings("unused")
    public static StringPath parseStringPath(Listener listener,
                                             String path,
                                             String rootPattern,
                                             String separatorPattern)
    {
        if (rootPattern != null)
        {
            var matcher = pattern(rootPattern).matcher(path);
            if (matcher.lookingAt())
            {
                var tail = matcher.group("path");
                return new StringPath(matcher.group("root"), StringList.splitOnPattern(tail, separatorPattern));
            }
        }
        return new StringPath(null, StringList.splitOnPattern(path, separatorPattern));
    }

    /**
     * @return A string path for the given string and separator pattern
     */
    public static StringPath parseStringPath(Listener listener, String path, String separatorPattern)
    {
        return parseStringPath(listener, path, null, separatorPattern);
    }

    /**
     * @return A path (sans scheme) for the given URI
     */
    public static StringPath stringPath(List<String> elements)
    {
        return new StringPath(elements);
    }

    /**
     * @return A path (sans scheme) for the given URI
     */
    public static StringPath stringPath(String root, List<String> elements)
    {
        return new StringPath(root, elements);
    }

    /**
     * @return A path for the given strings
     */
    public static StringPath stringPath(String first, String... rest)
    {
        var list = StringList.stringList();
        if (!Strings.isEmpty(first))
        {
            list.add(first);
        }
        for (var at : rest)
        {
            if (!Strings.isEmpty(at))
            {
                list.add(at);
            }
        }
        return stringPath(list);
    }

    /**
     * @return A path (sans scheme) for the given URI
     */
    public static StringPath stringPath(URI uri)
    {
        return stringPath(java.nio.file.Path.of(uri));
    }

    /**
     * @return A path for the given NIO path
     */
    public static StringPath stringPath(java.nio.file.Path path)
    {
        var root = path.getRoot();
        var elements = new StringList();
        for (int i = 0; i < path.getNameCount(); i++)
        {
            elements.add(path.getName(i).toString());
        }
        return new StringPath(root == null ? null : root.toString(), elements);
    }

    /** By default, paths are separated by slashes */
    private String separator = "/";

    protected StringPath(@NotNull List<String> elements)
    {
        this(null, elements);
    }

    protected StringPath(String root, @NotNull List<String> elements)
    {
        super(root, substituteSystemVariables(elements));
    }

    protected StringPath(@NotNull StringPath path)
    {
        this(path.rootElement(), path.elements());
        separator = path.separator;
    }

    /**
     * Returns this path with middle elements removed to ensure the path is shorter than the given maximum length
     *
     * @param maximumLength The maximum length of the contracted path in characters
     * @return A contraction of this path as a string. Middle elements are removed until the length is less than the
     * given maximum length.
     */
    public String asContraction(int maximumLength)
    {
        int size = size();
        if (size >= 3)
        {
            var copy = new StringPath(this);
            int middle = size / 2;
            var before = copy.subpath(0, middle);
            var after = copy.subpath(middle + 1, size);
            var ellipsis = "...";
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
     * Returns this path as a String
     */
    @Override
    @KivaKitIncludeProperty
    public String asString()
    {
        return join();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String asString(Format format)
    {
        switch (format)
        {
            case FILESYSTEM:
                return join(File.separator);

            default:
                return join();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath copy()
    {
        return (StringPath) super.copy();
    }

    /**
     * @return True if this path ends with the given suffix
     */
    public boolean endsWith(String suffix)
    {
        return asString().endsWith(suffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath first(int n)
    {
        return (StringPath) super.first(n);
    }

    /**
     * Returns this path joined by the given separator
     */
    public final String join(String separator)
    {
        var list = new StringList();
        list.addAll(super.elements());
        var root = rootElement();
        return Strings.notNull(root) + list.join(separator);
    }

    /**
     * Returns this path joined by the path {@link #separator()}
     */
    public String join()
    {
        return join(separator());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath last(int n)
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
     * Returns the path separator to use when converting to string representation
     */
    public String separator()
    {
        return separator;
    }

    /**
     * @return True if this path starts with the given prefix
     */
    public boolean startsWith(String prefix)
    {
        return asString().startsWith(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath subpath(int start, int end)
    {
        return (StringPath) super.subpath(start, end);
    }

    /**
     * Returns this path joined by the path {@link #separator()}
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
    public StringPath transformed(Function<String, String> consumer)
    {
        return (StringPath) super.transformed(consumer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath withChild(Path<String> that)
    {
        return (StringPath) super.withChild(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath withChild(String element)
    {
        return (StringPath) super.withChild(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath withParent(String element)
    {
        return (StringPath) super.withParent(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath withParent(Path<String> that)
    {
        return (StringPath) super.withParent(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath withRoot(String root)
    {
        return (StringPath) super.withRoot(root);
    }

    /**
     * Returns this string path with the given separator
     */
    public StringPath withSeparator(String separator)
    {
        var copy = (StringPath) copy();
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
    public StringPath withoutOptionalPrefix(Path<String> prefix)
    {
        return (StringPath) super.withoutOptionalPrefix(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath withoutOptionalSuffix(Path<String> suffix)
    {
        return (StringPath) super.withoutOptionalSuffix(suffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath withoutPrefix(Path<String> prefix)
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
    public StringPath withoutSuffix(Path<String> suffix)
    {
        return (StringPath) super.withoutSuffix(suffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Path<String> onCopy(String root, List<String> elements)
    {
        return new StringPath(root, elements);
    }

    private static Pattern pattern(String rootPattern)
    {
        var pattern = patterns.get(rootPattern);
        if (pattern == null)
        {
            pattern = Pattern.compile("(?<root>" + rootPattern + ")(?<path>.*)");
            patterns.put(rootPattern, pattern);
        }
        return pattern;
    }

    private static List<String> substituteSystemVariables(List<String> elements)
    {
        elements.replaceAll(text -> resolveProject(KivaKit.class).properties().expand(text));
        return elements;
    }
}
