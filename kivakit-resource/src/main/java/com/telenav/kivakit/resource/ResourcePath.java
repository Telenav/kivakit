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
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.path.Path;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResource;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourcePath;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.commandline.SwitchParser.switchParser;
import static com.telenav.kivakit.core.collections.list.StringList.split;
import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.filesystem.File.parseFile;
import static com.telenav.kivakit.filesystem.FilePath.filePath;
import static com.telenav.kivakit.filesystem.Folders.userHome;
import static com.telenav.kivakit.resource.FileName.parseFileName;
import static com.telenav.kivakit.resource.UriAuthority.uriAuthority;
import static com.telenav.kivakit.resource.UriSchemes.uriSchemes;

/**
 * A path to a resource of any kind, based on URI syntax:
 *
 * <pre>[scheme ":"]* ["//" authority]? [root] [path] ["?" query]? ["#" fragment]?</pre>
 *
 * <p>
 * Note that separating out the [root] part of paths enables non-URI paths like <i>C:\Windows</i>, where the C:\ part is
 * the path root.
 * </p>
 *
 * <p>
 * By default, the separator character for a resource is forward slash. But in the case of some file paths this may be
 * overridden. For example, in {@link FilePath}.
 * </p>
 *
 * <p>
 * This class contains numerous methods which down-cast the return value of the superclass to make use easier for
 * clients. Methods that are unique to this class mainly have to do with filenames and file paths:
 * </p>
 * <ul>
 *     <li>{@link #asFile()} - This resource path as a file</li>
 *     <li>{@link #asFilePath()} - This resource path as a file path</li>
 *     <li>{@link #fileName()} - The last component of this path as a {@link FileName}</li>
 *     <li>{@link #normalized()} - This path without characters invalid in a resource path</li>
 *     <li>{@link #withExtension(Extension)} - This path with the given extension</li>
 * </ul>
 *
 * <p><b>Parsing</b></p>
 *
 * <ul>
 *     <li>{@link #parseResourcePath(Listener listener, String)}</li>
 *     <li>{@link #parseResourcePath(Listener, URI)}</li>
 * </ul>
 *
 * <p><b>Factories</b></p>
 *
 * <ul>
 *     <li>{@link #resourcePath(String)}</li>
 *     <li>{@link #resourcePath(StringPath)}</li>
 *     <li>{@link #resourcePath(URI)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramResource.class)
@UmlClassDiagram(diagram = DiagramResourcePath.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             documentation = DOCUMENTED,
             testing = UNTESTED)
public class ResourcePath extends StringPath implements
    UriIdentified,
    ResourcePathed
{
    @NotNull
    protected static ResourcePath newResourcePath(Listener listener, @NotNull String string, URI uri)
    {
        var schemes = uriSchemes(string);
        if (uri == null)
        {
            uri = URI.create(string);
        }
        var authority = uriAuthority(uri);
        var prefix = schemes.toString() + authority;
        var path = string.substring(prefix.length());

        if (path.startsWith("///"))
        {
            path = path.substring(2);
        }

        String root = null;
        if (path.startsWith("/"))
        {
            root = "/";
            path = path.substring(1);
        }

        var elements = split(path, "/");

        return new ResourcePath(uri, schemes, authority, root, elements);
    }

    /**
     * Returns a resource path for the given URI
     *
     * @param listener The listener to broadcast any problems to
     * @param uri The resource {@link URI}
     */
    public static ResourcePath parseResourcePath(@NotNull Listener listener,
                                                 @NotNull URI uri)
    {
        return newResourcePath(listener, uri.toString(), uri);
    }

    /**
     * Returns a resource path for the given string
     *
     * @param listener The listener to broadcast any problems to
     * @param path The path to parse
     */
    public static ResourcePath parseResourcePath(@NotNull Listener listener,
                                                 @NotNull String path)
    {
        return newResourcePath(listener, path, null);
    }

    /**
     * Returns a resource path for the given URI
     *
     * @throws RuntimeException Should not be thrown except in the case of an internal error
     */
    public static ResourcePath resourcePath(@NotNull URI uri)
    {
        return parseResourcePath(throwingListener(), uri);
    }

    /**
     * Returns a resource path for the given string path
     */
    public static ResourcePath resourcePath(@NotNull StringPath path)
    {
        return new ResourcePath(null, uriSchemes(), uriAuthority(), path.rootElement(), path.elements());
    }

    /**
     * Returns a resource path for the given URI
     *
     * @param path The path to parse
     * @throws RuntimeException Thrown if the path cannot be parsed
     */
    public static ResourcePath resourcePath(@NotNull String path)
    {
        return parseResourcePath(throwingListener(), path);
    }

    public static SwitchParser.Builder<ResourcePath> resourcePathSwitchParser(@NotNull Listener listener,
                                                                              @NotNull String name,
                                                                              @NotNull String description)
    {
        return switchParser(ResourcePath.class)
            .name(name)
            .converter(new ResourcePathConverter(listener))
            .description(description);
    }

    /** The {@link URI} schemes for this resource path, if any */
    private UriSchemes schemes;

    /** The {@link URI} authority information for this resource path, if any */
    private UriAuthority authority;

    /** Any {@link URI} for this resource path */
    private final URI uri;

    /**
     * Creates a resource path for the given URI
     *
     * @param uri The URI
     */
    protected ResourcePath(URI uri)
    {
        this(newResourcePath(throwingListener(), uri.toString(), uri));
    }

    /**
     * Creates a resource path with the given {@link UriSchemes}, {@link UriAuthority}, path root and path elements.
     *
     * @param uri The URI for this resource path, if any
     * @param schemes The list of schemes for this path, such as http: or jar:file:)
     * @param authority The authority component of a {@link URI}, such as "kivakit.org:8080"
     * @param pathRoot The root element, if any, such as "C:\" or "/"
     * @param pathElements The path elements
     */
    protected ResourcePath(URI uri,
                           UriSchemes schemes,
                           UriAuthority authority,
                           String pathRoot,
                           @NotNull List<String> pathElements)
    {
        super(pathRoot, normalize(pathElements));

        if (!pathElements.isEmpty() && pathElements.get(0).equals("~"))
        {
            rootElement("/");
        }

        this.schemes = schemes == null ? uriSchemes() : schemes.copy();
        this.authority = authority == null ? uriAuthority() : authority;
        this.uri = uri;
    }

    /**
     * Copy constructor
     *
     * @param that The path to copy
     */
    protected ResourcePath(@NotNull ResourcePath that)
    {
        super(that);

        schemes = that.schemes.copy();
        authority = that.authority;
        uri = null;
    }

    /**
     * Convenience constructor
     *
     * @param schemes The list of schemes for this path, such as http: or jar:file:
     * @param pathRoot The root element, if any, such as "C:\" or "/"
     * @param pathElements The path elements
     */
    protected ResourcePath(UriSchemes schemes, String pathRoot, @NotNull List<String> pathElements)
    {
        this(null, schemes, null, pathRoot, pathElements);
    }

    /**
     * Convenience constructor
     *
     * @param pathRoot The root element, if any, such as "C:\" or "/"
     * @param pathElements The path elements
     */
    protected ResourcePath(String pathRoot, @NotNull List<String> pathElements)
    {
        this(null, null, null, pathRoot, pathElements);
    }

    /**
     * Returns this path as an absolute path.
     *
     * @return The absolute path for this path
     */
    public ResourcePath asAbsolute()
    {
        return this;
    }

    /**
     * Returns this resource path as a file
     */
    public File asFile()
    {
        ensure(isFile());
        return parseFile(throwingListener(), asString());
    }

    /**
     * Returns this resource path as a file path
     */
    public FilePath asFilePath()
    {
        return filePath(this);
    }

    /**
     * Returns this resource path as a Java file
     */
    @Override
    public java.io.File asJavaFile()
    {
        return asFile().asJavaFile();
    }

    @Override
    public String asString()
    {
        return schemes().toString() + authority() + join();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URI asUri()
    {
        if (uri != null)
        {
            return uri;
        }
        return URI.create(asString());
    }

    /**
     * The {@link UriAuthority} for this path, if any
     */
    public UriAuthority authority()
    {
        return authority;
    }

    @Override
    public ResourcePath copy()
    {
        return new ResourcePath(this);
    }

    /**
     * Returns the file extension of this resource path's filename
     */
    @Override
    public Extension extension()
    {
        return fileName().extension();
    }

    /**
     * Returns the file name of this resource path, if any. The only case where there will be no filename is when the
     * path is the root path, like "/" or "C:\"
     */
    @Override
    public FileName fileName()
    {
        var last = last();
        return last == null ? null : parseFileName(throwingListener(), last);
    }

    /**
     * Returns true if this file path has at least one scheme
     */
    public boolean hasScheme()
    {
        return schemes.isNonEmpty();
    }

    /**
     * Determines if this path has a trailing slash. A trailing slash is represented by having a final path component
     * that is empty ("").
     *
     * @return True if the last component of this path is the empty string.
     */
    public boolean hasTrailingSlash()
    {
        return size() > 0 && "".equals(get(size() - 1));
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
     * Returns true if this resource path is a file
     */
    public boolean isFile()
    {
        return schemes.isFile();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath last(int n)
    {
        return (ResourcePath) super.last(n);
    }

    /**
     * Returns this path without characters that are unacceptable in a resource path
     */
    public ResourcePath normalized()
    {
        return transformed(element -> element
            .replace(',', '_')
            .replace(';', '_')
            .replace(' ', '_')
            .replace('\'', '_'));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath parent()
    {
        return (ResourcePath) super.parent();
    }

    /**
     * Returns only the path component of this resource path, without any schemes or authority.
     */
    @Override
    public ResourcePath path()
    {
        return withoutSchemes().withoutAuthority();
    }

    /**
     * Returns this resource path relative to the given path
     *
     * @param path The path
     * @return This path relative to the given path
     */
    public ResourcePath relativeTo(@NotNull ResourcePath path)
    {
        if (startsWith(path))
        {
            return withoutOptionalPrefix(path);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath root()
    {
        return (ResourcePath) super.root();
    }

    /**
     * Returns any schemes for this filepath. For example, a file such as "jar:file:/test.zip" would have the schemes
     * "jar" and "file". In "s3://telenav/file.txt", there is only one scheme, "s3".
     */
    public UriSchemes schemes()
    {
        return schemes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath subpath(int start, int end)
    {
        return (ResourcePath) super.subpath(start, end);
    }

    @Override
    public String toString()
    {
        return asString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath transformed(@NotNull Function<String, String> consumer)
    {
        return (ResourcePath) super.transformed(consumer);
    }

    /**
     * Returns a copy of this path with the given {@link URI} authority
     *
     * @param authority The authority
     * @return A copy of this path
     */
    public ResourcePath withAuthority(@NotNull UriAuthority authority)
    {
        var copy = (ResourcePath) copy();
        copy.authority = authority;
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withChild(@NotNull Path<String> that)
    {
        return (ResourcePath) super.withChild(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withChild(@NotNull String element)
    {
        return (ResourcePath) super.withChild(element);
    }

    /**
     * Returns this resource path with the given extension
     */
    public FilePath withExtension(@NotNull Extension extension)
    {
        return (FilePath) withoutLast().withChild(last() + extension);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withParent(@NotNull String element)
    {
        return (ResourcePath) super.withParent(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withParent(@NotNull Path<String> that)
    {
        return (ResourcePath) super.withParent(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withRoot(String root)
    {
        return (ResourcePath) super.withRoot(root);
    }

    /**
     * Returns a copy of this path with the given {@link URI} schemes
     *
     * @param schemes The schemes
     * @return A copy of this path
     */
    public ResourcePath withSchemes(@NotNull UriSchemes schemes)
    {
        var copy = (ResourcePath) copy();
        copy.schemes = schemes.copy();
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withSeparator(@NotNull String separator)
    {
        return (ResourcePath) super.withSeparator(separator);
    }

    /**
     * Returns this filepath without any authority
     */
    public ResourcePath withoutAuthority()
    {
        var copy = copy();
        copy.authority = authority();
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withoutFirst()
    {
        return (ResourcePath) super.withoutFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withoutLast()
    {
        return (ResourcePath) super.withoutLast();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withoutOptionalPrefix(@NotNull Path<String> prefix)
    {
        return (ResourcePath) super.withoutOptionalPrefix(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withoutOptionalSuffix(@NotNull Path<String> suffix)
    {
        return (ResourcePath) super.withoutOptionalSuffix(suffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withoutPrefix(@NotNull Path<String> prefix)
    {
        return (ResourcePath) super.withoutPrefix(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withoutRoot()
    {
        return (ResourcePath) super.withoutRoot();
    }

    /**
     * Returns this filepath without any scheme
     */
    public ResourcePath withoutSchemes()
    {
        var copy = copy();
        copy.schemes = uriSchemes();
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withoutSuffix(@NotNull Path<String> suffix)
    {
        return (ResourcePath) super.withoutSuffix(suffix);
    }

    private static List<String> normalize(List<String> elements)
    {
        if (elements.size() == 1)
        {
            if (elements.get(0).isBlank())
            {
                return new ArrayList<>();
            }
        }

        if (!elements.isEmpty() && elements.get(0).equals("~"))
        {
            return stringList(elements).rightOf(0).prepending(userHome().path());
        }
        return elements;
    }
}
