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

package com.telenav.kivakit.filesystem;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.path.Path;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.Extension;
import com.telenav.kivakit.resource.FileName;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourcePath;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.collections.list.StringList.split;
import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.ensure.Ensure.illegalState;
import static com.telenav.kivakit.core.messaging.Listener.nullListener;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.os.OperatingSystem.operatingSystem;
import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.core.string.Paths.pathTail;
import static com.telenav.kivakit.core.string.Strings.ensureEndsWith;
import static com.telenav.kivakit.core.string.Strings.isNullOrBlank;
import static com.telenav.kivakit.core.string.Strip.stripLeading;
import static com.telenav.kivakit.core.string.Strip.stripTrailing;
import static com.telenav.kivakit.filesystem.Folders.currentFolder;

/**
 * An abstraction for a path on any kind of filesystem or file-related data source.
 *
 * <p>
 * This class contains numerous methods which down-cast the return value of the superclass to make use easier for
 * clients. Methods that are unique to this class mainly have to do with filesystems and paths.
 * </p>
 *
 * <p><b>Parsing</b></p>
 *
 * <ul>
 *     <li>{@link #parseFilePath(Listener listener, String, Object...)} - The given string as a file path</li>
 * </ul>
 *
 * <p><b>Factory Methods</b></p>
 *
 * <ul>
 *     <li>{@link #filePath(URI)} - A file path for the given URI</li>
 *     <li>{@link #filePath(java.io.File)} - A file path for the given {@link java.io.File}</li>
 *     <li>{@link #filePath(java.nio.file.Path)} - A file path for the given Java NIO path</li>
 *     <li>{@link #filePath(FileName)} - A filepath with only the given filename in it</li>
 *     <li>{@link #filePath(StringPath)} - The given string path as a file path</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #parent()} - Returns the parent of this path</li>
 *     <li>{@link #root()} - Returns the root of this path</li>
 *     <li>{@link #separator()} - Returns the path separator</li>
 *     <li>{@link #schemes()} - List of schemes for this filepath, or an empty list if there are none</li>
 * </ul>
 *
 * <p><b>Checks</b></p>
 *
 * <ul>
 *     <li>{@link #hasScheme()} - True if this filepath has a scheme as in s3://telenav/README.md</li>
 *     <li>{@link #isAbsolute()} - True if this is an absolute path</li>
 *     <li>{@link #isCurrentFolder()} - True if this filepath is the current folder's filepath</li>
 * </ul>
 *
 * <p><b>Files</b></p>
 *
 * <ul>
 *     <li>{@link #withChild(Path)} - This path with the given child path</li>
 *     <li>{@link #withChild(String)} - This path with the given child path</li>
 *     <li>{@link #file(FileName)} - This file path with the given filename appended</li>
 *     <li>{@link #hasExtension(Extension)} - True if this filepath has the given extension</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #asAbsolute()} - This file path as an absolute path</li>
 *     <li>{@link #asFile()} - This file path as a file</li>
 *     <li>{@link #asStringPath()} - This file path without any root or scheme</li>
 *     <li>{@link #asUnixString()} - This file path as a forward-slash separated string</li>
 * </ul>
 *
 * <p><b>Operations</b></p>
 *
 * <ul>
 *     <li>{@link #first()}</li>
 *     <li>{@link #first(int)}</li>
 *     <li>{@link #last()}</li>
 *     <li>{@link #last(int)}</li>
 *     <li>{@link #matches(Matcher)}</li>
 *     <li>{@link #normalized()}</li>
 *     <li>{@link #subpath(int, int)}</li>
 *     <li>{@link #transformed(Function)}</li>
 * </ul>
 *
 * <p><b>Functional</b></p>
 *
 * <ul>
 *     <li>{@link #withChild(Path)}</li>
 *     <li>{@link #withChild(String)}</li>
 *     <li>{@link #withExtension(Extension)}</li>
 *     <li>{@link #withParent(String)}</li>
 *     <li>{@link #withParent(Path)}</li>
 *     <li>{@link #withPrefix(String)}</li>
 *     <li>{@link #withTrailingSlash()}</li>
 *     <li>{@link #withRoot(String)}</li>
 *     <li>{@link #withScheme(String)}</li>
 *     <li>{@link #withSchemes(StringList)}</li>
 *     <li>{@link #withSeparator(String)}</li>
 *     <li>{@link #withoutFirst()}</li>
 *     <li>{@link #withoutLast()}</li>
 *     <li>{@link #withoutOptionalPrefix(Path)}</li>
 *     <li>{@link #withoutOptionalSuffix(Path)}</li>
 *     <li>{@link #withoutPrefix(Path)}</li>
 *     <li>{@link #withoutRoot()}</li>
 *     <li>{@link #withoutSuffix(Path)}</li>
 *     <li>{@link #withoutTrailingSlash()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "unused", "JavadocLinkAsPlainText" })
@UmlClassDiagram(diagram = DiagramResourcePath.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class FilePath extends ResourcePath
{
    private static final Pattern SCHEME_PATTERN = Pattern.compile("([A-Za-z]+):");

    /**
     * Returns file path for the given URI
     */
    public static FilePath filePath(@NotNull URI uri)
    {
        // If there is a scheme,
        var scheme = uri.getScheme();
        if (!isNullOrBlank(scheme))
        {
            // the default root is slash,
            var root = "/";
            var path = uri.getPath();
            var schemes = stringList(scheme);

            // but if there is an authority (host:port),
            var authority = authority(uri);
            if (authority != null)
            {
                // the root is //host:port/.
                root = "//" + authority + "/";
            }

            // Add to the list of schemes if there is more than one (java:file).
            var subPart = uri.getSchemeSpecificPart();
            List<String> elements;
            if (isNullOrBlank(path))
            {
                var subPath = parseFilePath(throwingListener(), subPart);
                schemes.addAll(subPath.schemes());
                var subPathRoot = subPath.rootElement();
                if (subPathRoot != null)
                {
                    root = subPathRoot;
                }
                elements = subPath.elements();
            }
            else
            {
                elements = split(stripLeading(uri.getPath(), "/"), "/");
            }

            // and create a new FilePath with the given scheme, root authority and elements.
            return new FilePath(schemes, root, elements);
        }

        return simpleFilePath(uri);
    }

    /**
     * Returns a file path for the given {@link java.io.File}
     */
    public static FilePath filePath(@NotNull java.io.File file)
    {
        return filePath(file.toPath()).withoutFileScheme();
    }

    /**
     * Returns a file path for the given {@link java.nio.file.Path}
     */
    public static FilePath filePath(@NotNull java.nio.file.Path path)
    {
        return filePath(path.toUri()).withoutFileScheme();
    }

    /**
     * Returns a file path with only the given filename
     */
    public static FilePath filePath(@NotNull FileName name)
    {
        var list = new ArrayList<String>();
        list.add(name.name());
        return new FilePath(list);
    }

    /**
     * Returns a file path for the given string path
     */
    public static FilePath filePath(@NotNull StringPath path)
    {
        return new FilePath(path.elements());
    }

    /**
     * Returns a file path for the given string path
     *
     * @throws RuntimeException Thrown if the path cannot be parsed
     */
    public static FilePath filePath(@NotNull String path, Object... arguments)
    {
        return parseFilePath(throwingListener(), path, arguments);
    }

    /**
     * Returns a file path for the given string
     */
    public static FilePath parseFilePath(@NotNull Listener listener,
                                         @NotNull String path,
                                         @NotNull Object... arguments)
    {
        if (path.isBlank())
        {
            return emptyFilePath();
        }

        path = format(path, arguments);

        var schemes = stringList();

        var matcher = SCHEME_PATTERN.matcher(path);
        while (matcher.find())
        {
            schemes.add(matcher.group(1));
            path = pathTail(path, ":");
        }

        String root = null;
        if (path.startsWith("/"))
        {
            root = "/";
            path = path.substring(1);
        }

        var elements = split(path, "/");
        if (elements.isBlank())
        {
            elements = stringList();
        }
        return filePath(stringPath(elements))
            .withSchemes(schemes)
            .withRoot(root)
            .withoutFileScheme();
    }

    /**
     * Copy constructor
     */
    protected FilePath(@NotNull FilePath that)
    {
        super(that);
    }

    /**
     * Construct with scheme
     */
    protected FilePath(StringList schemes,
                       String root,
                       @NotNull List<String> elements)
    {
        super(schemes, root, elements);
    }

    /**
     * Construct with scheme
     */
    protected FilePath(@NotNull List<String> elements)
    {
        super(null, null, elements);
    }

    /**
     * Construct with scheme
     */
    protected FilePath(StringList schemes,
                       @NotNull StringPath path)
    {
        super(schemes, path.rootElement(), path.elements());
    }

    /**
     * Returns this file path as an absolute path
     */
    @Override
    public FilePath asAbsolute()
    {
        if (isCurrentFolder())
        {
            return currentFolder().path();
        }

        if (schemes().isNonEmpty())
        {
            return this;
        }

        return filePath(asJavaPath().toAbsolutePath());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File asFile()
    {
        return File.file(throwingListener(), this);
    }

    /**
     * Returns this filepath as a string
     */
    @Override
    public String asString()
    {
        return join();
    }

    /**
     * Returns this file path without any scheme and root if it is a URI. For example, "https://telenav.com/a/b.html"
     * would have the path "a/b.html"
     */
    public StringPath asStringPath()
    {
        return stringPath(elements());
    }

    /**
     * Returns converts this path to a UNIX path string, obeying the rules for Windows bash shell where an absolute path
     * like "C:\xyz" is represented as "/c/xyz"
     */
    public String asUnixString()
    {
        if (isAbsolute())
        {
            if (operatingSystem().isWindows())
            {
                var pattern = Pattern.compile("(?<drive>[A-Za-z]):\\\\");
                var matcher = pattern.matcher(rootElement());
                if (matcher.lookingAt())
                {
                    return "/" + matcher.group("drive") + "/" + join("/");
                }
            }
            return "/" + join("/");
        }
        return join("/");
    }

    @Override
    public URI asUri()
    {
        try
        {
            var path = this;
            if (!path.hasScheme())
            {
                path = path.withScheme("file");
            }
            return new URI(ensureEndsWith(path.toString(), "/"));
        }
        catch (Exception e)
        {
            return illegalState(e, "Cannot convert to URI: $", this);
        }
    }

    /**
     * Returns this file path with the given filename appended
     */
    public FilePath file(@NotNull FileName child)
    {
        return withChild(child.name());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath first(int n)
    {
        return (FilePath) super.first(n);
    }

    @Override
    public boolean hasExtension(@NotNull Extension extension)
    {
        return extension.ends(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAbsolute()
    {
        // A FilePath with a scheme is always absolute
        return hasScheme() || super.isAbsolute();
    }

    /**
     * Returns true if this path is the current folder, either as '.' or as an absolute path
     */
    public boolean isCurrentFolder()
    {
        return ".".equals(toString()) || equals(currentFolder().path());
    }

    @Override
    public FilePath last(int n)
    {
        return (FilePath) super.last(n);
    }

    /**
     * Returns the last component of this path
     */
    @Override
    public String last()
    {
        // If this path has a trailing slash, it has a final empty component,
        if (hasTrailingSlash())
        {
            // so we get the component before the empty one at the end.
            return get(size() - 2);
        }

        return super.last();
    }

    /**
     * Returns true if this path matches the given matcher
     */
    @Override
    public boolean matches(@NotNull Matcher<String> matcher)
    {
        return matcher.matches(asString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath normalized()
    {
        return (FilePath) super.normalized();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath parent()
    {
        return (FilePath) super.parent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath root()
    {
        return (FilePath) super.root();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String separator()
    {
        if (hasScheme() && !schemes().contains("file"))
        {
            return "/";
        }

        return java.io.File.separator;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public FilePath subpath(int start, int end)
    {
        return (FilePath) super.subpath(start, end);
    }

    @Override
    public String toString()
    {
        return join();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath transformed(@NotNull Function<String, String> consumer)
    {
        return (FilePath) super.transformed(consumer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath withChild(@NotNull String child)
    {
        if (hasTrailingSlash())
        {
            return withoutTrailingSlash().withChild(child);
        }
        return (FilePath) super.withChild(child);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath withChild(@NotNull Path<String> that)
    {
        return (FilePath) super.withChild(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath withParent(@NotNull String element)
    {
        return (FilePath) super.withParent(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath withParent(@NotNull Path<String> that)
    {
        return (FilePath) super.withParent(that);
    }

    public FilePath withPrefix(@NotNull String prefix)
    {
        return parseFilePath(nullListener(), prefix + this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath withRoot(String root)
    {
        return (FilePath) super.withRoot(root);
    }

    public FilePath withScheme(@NotNull String scheme)
    {
        return withSchemes(stringList(scheme));
    }

    @Override
    public FilePath withSchemes(@NotNull StringList scheme)
    {
        return (FilePath) super.withSchemes(scheme);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath withSeparator(@NotNull String separator)
    {
        return (FilePath) super.withSeparator(separator);
    }

    /**
     * Returns this path with a trailing slash
     */
    public FilePath withTrailingSlash()
    {
        if (!hasTrailingSlash())
        {
            return withTrailingSlash();
        }
        return this;
    }

    /**
     * Returns this path without file: scheme if that is the only scheme
     */
    public FilePath withoutFileScheme()
    {
        if (schemes().equals(stringList("file")))
        {
            return withoutSchemes();
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath withoutFirst()
    {
        return (FilePath) super.withoutFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath withoutLast()
    {
        return (FilePath) super.withoutLast();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath withoutOptionalPrefix(@NotNull Path<String> prefix)
    {
        return (FilePath) super.withoutOptionalPrefix(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath withoutOptionalSuffix(@NotNull Path<String> suffix)
    {
        return (FilePath) super.withoutOptionalSuffix(suffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath withoutPrefix(@NotNull Path<String> prefix)
    {
        return (FilePath) super.withoutPrefix(prefix);
    }

    public FilePath withoutPrefix(@NotNull String prefix)
    {
        return parseFilePath(nullListener(), stripLeading(toString(), prefix));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath withoutRoot()
    {
        return (FilePath) super.withoutRoot();
    }

    /**
     * Returns this filepath without any scheme
     */
    @Override
    public FilePath withoutSchemes()
    {
        return (FilePath) super.withoutSchemes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath withoutSuffix(@NotNull Path<String> suffix)
    {
        return (FilePath) super.withoutSuffix(suffix);
    }

    /**
     * Returns this path without any trailing slash, which is represented by a final component that is empty ("")
     */
    public FilePath withoutTrailingSlash()
    {
        if (hasTrailingSlash())
        {
            return first(size() - 1);
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected FilePath onCopy(String root,
                              @NotNull List<String> elements)
    {
        return new FilePath(schemes(), root, elements);
    }

    @Nullable
    private static String authority(@NotNull URI uri)
    {
        // Get the host and port of the URI
        var host = uri.getHost();
        var port = uri.getPort();

        // and if there is a host,
        String authority = null;
        if (!isNullOrBlank(host))
        {
            // add that to the root,
            authority = host;
            if (port > 0)
            {
                // and append the port.
                authority += ":" + port;
            }
        }

        // If there is an authority, make it a root element.
        return authority;
    }

    @NotNull
    private static FilePath emptyFilePath()
    {
        return new FilePath(new ArrayList<>());
    }

    @NotNull
    private static FilePath simpleFilePath(@NotNull URI uri)
    {
        // There's no scheme, so get the URI's simple path
        var path = uri.getPath();

        // and if it starts with a slash,
        String root = null;
        if (path.startsWith("/"))
        {
            // then the root is "/",
            root = "/";

            // and we strip and leading or trailing slashes from the path
            path = stripLeading(path, "/");
            path = stripTrailing(path, "/");
        }

        // and if the path is not empty,
        var pathElements = new StringList();
        if (!isNullOrBlank(path))
        {
            // we split the path into elements.
            pathElements = split(path, "/");
        }

        return new FilePath(new StringList(), root, pathElements);
    }
}
