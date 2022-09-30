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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.os.OperatingSystem;
import com.telenav.kivakit.core.path.Path;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.kivakit.core.string.Strip;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.Extension;
import com.telenav.kivakit.resource.FileName;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourcePath;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;

/**
 * An abstraction for a path on any kind of filesystem or file-related data source.
 *
 * <p>
 * This class contains numerous methods which down-cast the return value of the superclass to make use easier for
 * clients. Methods that are unique to this class mainly have to do with filesystems:
 * </p>
 *
 * <ul>
 *     <li>{@link #asAbsolute()} - This file path as an absolute path</li>
 *     <li>{@link #asFile()} - This file path as a file</li>
 *     <li>{@link #asStringPath()} - This file path without any root or scheme</li>
 *     <li>{@link #asUnixString()} - This file path as a forward-slash separated string</li>
 *     <li>{@link #file(FileName)} - This file path with the given filename appended</li>
 *     <li>{@link #hasScheme()} - True if this filepath has a scheme as in s3://telenav/README.md</li>
 *     <li>{@link #isCurrentFolder()} - True if this filepath is the current folder's filepath</li>
 *     <li>{@link #schemes()} - List of schemes for this filepath, or an empty list if there are none</li>
 *     <li>{@link #withoutSchemes()} - This filepath without any scheme</li>
 * </ul>
 *
 * <p><b>Parsing</b></p>
 *
 * <ul>
 *     <li>{@link #parseFilePath(Listener listener, String, Object...)} - The given string as a file path</li>
 * </ul>
 *
 * <p><b>Factories</b></p>
 *
 * <ul>
 *     <li>{@link #filePath(URI)} - A file path for the given URI</li>
 *     <li>{@link #filePath(java.io.File)} - A file path for the given {@link java.io.File}</li>
 *     <li>{@link #filePath(java.nio.file.Path)} - A file path for the given Java NIO path</li>
 *     <li>{@link #filePath(FileName)} - A filepath with only the given filename in it</li>
 *     <li>{@link #filePath(StringPath)} - The given string path as a file path</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "unused", "JavadocLinkAsPlainText" })
@UmlClassDiagram(diagram = DiagramResourcePath.class)
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = TESTING_NOT_NEEDED,
            documentation = FULLY_DOCUMENTED)
public class FilePath extends ResourcePath
{
    /**
     * Returns file path for the given URI
     */
    public static FilePath filePath(URI uri)
    {
        // If there is a scheme,
        var scheme = uri.getScheme();
        if (!Strings.isEmpty(scheme))
        {
            // the default root is slash,
            var root = "/";
            var path = uri.getPath();
            var schemes = StringList.stringList(scheme);

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
            if (Strings.isEmpty(path))
            {
                var subPath = filePath(URI.create(subPart));
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
                elements = StringList.split(Strip.leading(uri.getPath(), "/"), "/");
            }

            // and create a new FilePath with the given scheme, root authority and elements.
            return new FilePath(schemes, root, elements);
        }

        return simpleFilePath(uri);
    }

    /**
     * Returns a file path for the given {@link java.io.File}
     */
    public static FilePath filePath(java.io.File file)
    {
        return filePath(file.toPath()).withoutFileScheme();
    }

    /**
     * Returns a file path for the given {@link java.nio.file.Path}
     */
    public static FilePath filePath(java.nio.file.Path path)
    {
        return filePath(path.toUri()).withoutFileScheme();
    }

    /**
     * Returns a file path with only the given filename
     */
    public static FilePath filePath(FileName name)
    {
        var list = new ArrayList<String>();
        list.add(name.name());
        return new FilePath(null, null, list);
    }

    /**
     * Returns a file path for the given string path
     */
    public static FilePath filePath(StringPath path)
    {
        return new FilePath(StringList.stringList(), path);
    }

    /**
     * Returns a file path for the given string
     */
    public static FilePath parseFilePath(Listener listener, String path, Object... arguments)
    {
        if (path.isBlank())
        {
            return empty();
        }

        path = Strings.format(path, arguments);

        if (path.contains("${"))
        {
            var elements = StringList.split(path, "/");
            return filePath(StringPath.stringPath(elements)).withoutFileScheme();
        }

        try
        {
            return filePath(URI.create(path)).withoutFileScheme();
        }
        catch (Exception ignored)
        {
            throw listener.problem("Unable to parse file path: " + path).asException();
        }
    }

    /**
     * Converts to and from {@link FilePath}s
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public static class Converter extends BaseStringConverter<FilePath>
    {
        public Converter(Listener listener)
        {
            super(listener, FilePath::parseFilePath);
        }
    }

    /**
     * Copy constructor
     */
    protected FilePath(FilePath that)
    {
        super(that);
    }

    /**
     * Construct with scheme
     */
    protected FilePath(StringList schemes, String root, List<String> elements)
    {
        super(schemes, root, elements);
    }

    /**
     * Construct with scheme
     */
    protected FilePath(StringList schemes, StringPath path)
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
            return Folder.current().path();
        }

        if (schemes().isNonEmpty())
        {
            return this;
        }

        return FilePath.filePath(asJavaPath().toAbsolutePath());
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
        return StringPath.stringPath(elements());
    }

    /**
     * Returns converts this path to a UNIX path string, obeying the rules for Windows bash shell where an absolute path
     * like "C:\xyz" is represented as "/c/xyz"
     */
    public String asUnixString()
    {
        if (isAbsolute())
        {
            if (OperatingSystem.operatingSystem().isWindows())
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

    /**
     * Returns the URI for this file path
     */
    public URI asUri()
    {
        return URI.create(toString());
    }

    /**
     * Returns this file path with the given filename appended
     */
    public FilePath file(FileName child)
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
    public boolean hasExtension(Extension extension)
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
        return toString().equals(".") || equals(Folder.current().path());
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
    public boolean matches(Matcher<String> matcher)
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
    public FilePath transformed(Function<String, String> consumer)
    {
        return (FilePath) super.transformed(consumer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath withChild(String child)
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
    public FilePath withChild(Path<String> that)
    {
        return (FilePath) super.withChild(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath withParent(String element)
    {
        return (FilePath) super.withParent(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath withParent(Path<String> that)
    {
        return (FilePath) super.withParent(that);
    }

    public FilePath withPrefix(String prefix)
    {
        return parseFilePath(Listener.emptyListener(), prefix + this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath withRoot(String root)
    {
        return (FilePath) super.withRoot(root);
    }

    public FilePath withScheme(String scheme)
    {
        return withSchemes(StringList.stringList(scheme));
    }

    @Override
    public FilePath withSchemes(StringList scheme)
    {
        return (FilePath) super.withSchemes(scheme);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath withSeparator(String separator)
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
        if (schemes().equals(StringList.stringList("file")))
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
    public FilePath withoutOptionalPrefix(Path<String> prefix)
    {
        return (FilePath) super.withoutOptionalPrefix(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath withoutOptionalSuffix(Path<String> suffix)
    {
        return (FilePath) super.withoutOptionalSuffix(suffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath withoutPrefix(Path<String> prefix)
    {
        return (FilePath) super.withoutPrefix(prefix);
    }

    public FilePath withoutPrefix(String prefix)
    {
        return parseFilePath(Listener.emptyListener(), Strip.leading(toString(), prefix));
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
    public FilePath withoutSuffix(Path<String> suffix)
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
    protected FilePath onCopy(String root, List<String> elements)
    {
        return new FilePath(schemes(), root, elements);
    }

    @Nullable
    private static String authority(URI uri)
    {
        // Get the host and port of the URI
        var host = uri.getHost();
        var port = uri.getPort();

        // and if there is a host,
        String authority = null;
        if (!Strings.isEmpty(host))
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
    private static FilePath empty()
    {
        return new FilePath(null, null, List.of());
    }

    @NotNull
    private static FilePath simpleFilePath(URI uri)
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
            path = Strip.leading(path, "/");
            path = Strip.trailing(path, "/");
        }

        // and if the path is not empty,
        var pathElements = new StringList();
        if (!Strings.isEmpty(path))
        {
            // we split the path into elements.
            pathElements = StringList.split(path, "/");
        }

        return new FilePath(new StringList(), root, pathElements);
    }
}
