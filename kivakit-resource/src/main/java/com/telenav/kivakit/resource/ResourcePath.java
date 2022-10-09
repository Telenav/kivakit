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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.path.Path;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.core.string.Strip;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResource;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourcePath;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;
import static com.telenav.kivakit.core.messaging.Listener.consoleListener;

/**
 * A path to a resource of any kind. By default, the separator character for a resource is forward slash. But in the
 * case of some file paths this may be overridden. For example, in {@link FilePath}.
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
 *     <li>{@link #parseResourcePath(Listener listener, String)} - The given string as a resource path</li>
 *     <li>{@link #parseUnixResourcePath(Listener listener, String)} - The given string as a slash-separated UNIX resource path</li>
 * </ul>
 *
 * <p><b>Factories</b></p>
 *
 * <ul>
 *     <li>{@link #resourcePath(StringPath)} - The given string path as a resource path</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramResource.class)
@UmlClassDiagram(diagram = DiagramResourcePath.class)
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             documentation = DOCUMENTATION_COMPLETE,
             testing = TESTING_NONE)
public class ResourcePath extends StringPath implements
        UriIdentified,
        ResourcePathed
{
    /**
     * @return A resource path for the given string
     */
    public static ResourcePath parseResourcePath(@NotNull Listener listener,
                                                 @NotNull String path)
    {
        return FilePath.parseFilePath(listener, path);
    }

    /**
     * @return A UNIX-style resource path for the given string
     */
    public static ResourcePath parseUnixResourcePath(@NotNull Listener listener,
                                                     @NotNull String path)
    {
        String root = null;
        if (path.startsWith("/"))
        {
            root = "/";
            path = Strip.leading(path, "/");
        }
        return new ResourcePath(new StringList(), root, StringList.split(path, "/"));
    }

    /**
     * @return A resource path for the given string path
     */
    public static ResourcePath resourcePath(@NotNull StringPath path)
    {
        return new ResourcePath(new StringList(), path.rootElement(), path.elements());
    }

    public static SwitchParser.Builder<ResourcePath> resourcePathSwitchParser(@NotNull Listener listener,
                                                                              @NotNull String name,
                                                                              @NotNull String description)
    {
        return SwitchParser.switchParserBuilder(ResourcePath.class)
                .name(name)
                .converter(new ResourcePath.Converter(listener))
                .description(description);
    }

    /**
     * Converts to and from {@link ResourcePath}s
     *
     * @author jonathanl (shibo)
     */
    @CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
                 testing = TESTING_NONE,
                 documentation = DOCUMENTATION_COMPLETE)
    public static class Converter extends BaseStringConverter<ResourcePath>
    {
        public Converter(@NotNull Listener listener)
        {
            super(listener);
        }

        @Override
        protected ResourcePath onToValue(String value)
        {
            return parseResourcePath(this, value);
        }
    }

    /** The {@link URI} schemes for this resource path */
    private StringList schemes;

    /**
     * @param schemes The list of schemes for this path
     * @param root The root element
     * @param elements The path elements
     */
    protected ResourcePath(StringList schemes, String root, @NotNull List<String> elements)
    {
        super(root, elements);

        if (schemes != null)
        {
            this.schemes = schemes.copy();
        }
        else
        {
            this.schemes = new StringList();
        }
    }

    /**
     * Copy constructor
     */
    protected ResourcePath(@NotNull ResourcePath that)
    {
        super(that);
        schemes = that.schemes.copy();
    }

    protected ResourcePath(@NotNull StringList schemes, @NotNull List<String> elements)
    {
        super(elements);
        this.schemes = schemes.copy();
    }

    public ResourcePath asAbsolute()
    {
        return this;
    }

    /**
     * Returns this resource path as a file
     */
    public File asFile()
    {
        return File.parseFile(consoleListener(), asString());
    }

    /**
     * Returns this resource path as a file path
     */
    public FilePath asFilePath()
    {
        return FilePath.parseFilePath(consoleListener(), asString());
    }

    @Override
    public java.io.File asJavaFile()
    {
        return new java.io.File(asString());
    }

    /**
     * @return The file extension of this resource path's filename
     */
    @Override
    public Extension extension()
    {
        return fileName().extension();
    }

    /**
     * @return The file name of this resource path
     */
    @Override
    public FileName fileName()
    {
        var last = last();
        return last == null ? null : FileName.parseFileName(consoleListener(), last);
    }

    /**
     * @return True if this file path has a scheme
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
    public String join()
    {
        // NOTE: We call super.join(String) here because it is not overridden
        return schemes.join(":")
                + (hasScheme() ? ":" : "")
                + super.join(separator());
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

    @Override
    public ResourcePath path()
    {
        return asFilePath();
    }

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
     * @return Any schemes for this filepath. For example, a file such as "jar:file:/test.zip" would have the schemes
     * "jar" and "file". In "s3://telenav/file.txt", there is only one scheme, "s3".
     */
    public StringList schemes()
    {
        return schemes;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public ResourcePath subpath(int start, int end)
    {
        return (ResourcePath) super.subpath(start, end);
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
     * {@inheritDoc}
     */
    @Override
    public URI uri()
    {
        return URI.create(join());
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
    public ResourcePath withRoot(@NotNull String root)
    {
        return (ResourcePath) super.withRoot(root);
    }

    public ResourcePath withSchemes(@NotNull StringList schemes)
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
        ResourcePath copy = (ResourcePath) copy();
        copy.schemes = new StringList();
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected ResourcePath onCopy(String root,
                                  @NotNull List<String> elements)
    {
        return new ResourcePath(schemes(), root, elements);
    }
}
