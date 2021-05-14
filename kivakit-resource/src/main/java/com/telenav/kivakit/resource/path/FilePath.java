////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
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
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.paths.Path;
import com.telenav.kivakit.kernel.language.paths.StringPath;
import com.telenav.kivakit.kernel.language.strings.Strings;
import com.telenav.kivakit.kernel.language.strings.Strip;
import com.telenav.kivakit.kernel.language.vm.OperatingSystem;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResourcePath;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;

/**
 * An abstraction for a path on any kind of filesystem or file-related data source.
 *
 * <p>
 * This class contains numerous methods which down-cast the return value of the superclass to make use easier for
 * clients. Methods that are unique to this class mainly have to do with filesystems:
 * </p>
 *
 * <ul>
 *     <li>{@link #absolute()} - This file path as an absolute path</li>
 *     <li>{@link #asFile()} - This file path as a file</li>
 *     <li>{@link #asStringPath()} - This file path without any root or scheme</li>
 *     <li>{@link #asUnixString()} - This file path as a forward-slash separated string</li>
 *     <li>{@link #file(FileName)} - This file path with the given filename appended</li>
 *     <li>{@link #hasScheme()} - True if this filepath has a scheme as in s3://telenav/README.md</li>
 *     <li>{@link #isCurrentFolder()} - True if this filepath is the current folder's filepath</li>
 *     <li>{@link #scheme()} - Any scheme for this filepath, or null if there is none</li>
 *     <li>{@link #withoutScheme()} - This filepath without any scheme</li>
 * </ul>
 *
 * <p><b>Parsing</b></p>
 *
 * <ul>
 *     <li>{@link #parseFilePath(String)} - The given string as a file path</li>
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
@UmlClassDiagram(diagram = DiagramResourcePath.class)
@LexakaiJavadoc(complete = true)
public class FilePath extends ResourcePath
{
    /**
     * @return A file path for the given URI
     */
    public static FilePath filePath(final URI uri)
    {
        var root = uri.getHost();
        if (uri.getPort() > 0)
        {
            root += ":" + uri.getPort();
        }
        final var pathString = Strip.leading(uri.getPath(), "/");
        final var path = Strings.isEmpty(pathString) ? new StringList() : StringList.split(pathString, '/');
        return new FilePath(uri.getScheme(), "/" + root + "/", path);
    }

    /**
     * @return A file path for the given {@link java.io.File}
     */
    public static FilePath filePath(final java.io.File file)
    {
        return filePath(file.toPath());
    }

    /**
     * @return A file path for the given {@link java.nio.file.Path}
     */
    public static FilePath filePath(final java.nio.file.Path path)
    {
        final var root = path.getRoot();
        final var elements = new StringList();
        for (int i = 0; i < path.getNameCount(); i++)
        {
            elements.add(path.getName(i).toString());
        }
        return filepath(root == null ? null : root.toString(), elements);
    }

    /**
     * @return A file path with only the given filename
     */
    public static FilePath filePath(final FileName name)
    {
        final var list = new ArrayList<String>();
        list.add(name.name());
        return new FilePath(null, null, list);
    }

    /**
     * @return A file path for the given string path
     */
    public static FilePath filePath(final StringPath path)
    {
        return new FilePath(null, path);
    }

    /**
     * @return A file path for the given string
     */
    public static FilePath parseFilePath(final String path)
    {
        if (path.isBlank())
        {
            return new FilePath(null, null, List.of());
        }

        // If the path starts with a scheme like "hdfs:" or "file:" (but not a drive letter like C:),
        if (path.matches("[A-Za-z0-9-_]{2,}:.*"))
        {
            try
            {
                // parse it as a URI
                return filePath(new URI(path));
            }
            catch (final Exception ignored)
            {
            }
        }
        try
        {
            return filePath(java.nio.file.Path.of(path));
        }
        catch (final Exception ignored)
        {
        }
        return fail("Unable to parse file path: " + path);
    }

    /**
     * Converts to and from {@link FilePath}s
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public static class Converter extends BaseStringConverter<FilePath>
    {
        public Converter(final Listener listener)
        {
            super(listener);
        }

        @Override
        protected FilePath onConvertToObject(final String value)
        {
            return parseFilePath(value);
        }
    }

    /** The {@link URI} scheme for this path */
    private String scheme;

    /**
     * Copy constructor
     */
    protected FilePath(final FilePath that)
    {
        super(that);
        scheme = that.scheme;
    }

    /**
     * Construct with scheme
     */
    protected FilePath(final String scheme, final String root, final List<String> elements)
    {
        super(root, elements);
        this.scheme = scheme;
    }

    /**
     * Construct with scheme
     */
    protected FilePath(final String scheme, final StringPath path)
    {
        super(path.rootElement(), path.elements());
        this.scheme = scheme;
    }

    /**
     * @return This file path as an absolute path
     */
    public FilePath absolute()
    {
        if (isCurrentFolder())
        {
            return Folder.current().path();
        }
        return FilePath.filePath(asJavaPath().toAbsolutePath());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File asFile()
    {
        return File.of(this);
    }

    /**
     * @return This filepath as a string
     */
    @Override
    public String asString()
    {
        return join();
    }

    /**
     * @return This file path without any scheme and root if it is a URI. For example, "http://telenav.com/a/b.html"
     * would have the path "a/b.html"
     */
    public StringPath asStringPath()
    {
        return StringPath.stringPath(elements());
    }

    /**
     * @return Converts this path to a UNIX path string, obeying the rules for Windows bash shell where an absolute path
     * like "C:\xyz" is represented as "/c/xyz"
     */
    public String asUnixString()
    {
        if (isAbsolute())
        {
            if (OperatingSystem.get().isWindows())
            {
                final var pattern = Pattern.compile("(?<drive>[A-Za-z]):\\\\");
                final var matcher = pattern.matcher(rootElement());
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
     * @return This file path with the given filename appended
     */
    public FilePath file(final FileName child)
    {
        return withChild(child.name());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath first(final int n)
    {
        return (FilePath) super.first(n);
    }

    /**
     * @return True if this file path has a scheme
     */
    public boolean hasScheme()
    {
        return scheme != null;
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
     * @return True if this path is the current folder, either as '.' or as an absolute path
     */
    public boolean isCurrentFolder()
    {
        return toString().equals(".") || equals(Folder.current().path());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String join()
    {
        if (scheme != null)
        {
            return scheme + ":/" + super.join("/");
        }
        return super.join();
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
     * @return Any scheme for this filepath, such as 'file' or 's3' as in s3://telenav/file.txt
     */
    public String scheme()
    {
        return scheme;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String separator()
    {
        if (hasScheme() && !"file".equalsIgnoreCase(scheme()))
        {
            return "/";
        }
        return java.io.File.separator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath subpath(final int start, final int end)
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
    public FilePath transformed(final Function<String, String> consumer)
    {
        return (FilePath) super.transformed(consumer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath withChild(final String child)
    {
        return (FilePath) super.withChild(child);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath withChild(final Path<String> that)
    {
        return (FilePath) super.withChild(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath withParent(final String element)
    {
        return (FilePath) super.withParent(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath withParent(final Path<String> that)
    {
        return (FilePath) super.withParent(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath withRoot(final String root)
    {
        return (FilePath) super.withRoot(root);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath withSeparator(final String separator)
    {
        return (FilePath) super.withSeparator(separator);
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
    public FilePath withoutOptionalPrefix(final Path<String> prefix)
    {
        return (FilePath) super.withoutOptionalPrefix(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath withoutOptionalSuffix(final Path<String> suffix)
    {
        return (FilePath) super.withoutOptionalSuffix(suffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath withoutPrefix(final Path<String> prefix)
    {
        return (FilePath) super.withoutPrefix(prefix);
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
     * @return This filepath without any scheme
     */
    public FilePath withoutScheme()
    {
        final FilePath copy = (FilePath) copy();
        copy.scheme = null;
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath withoutSuffix(final Path<String> suffix)
    {
        return (FilePath) super.withoutSuffix(suffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected FilePath onCopy(final String root, final List<String> elements)
    {
        return new FilePath(scheme, root, elements);
    }

    private static FilePath filepath(final String root, final List<String> elements)
    {
        return new FilePath(null, root, elements);
    }
}
