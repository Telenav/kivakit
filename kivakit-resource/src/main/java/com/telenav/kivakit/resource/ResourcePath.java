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

package com.telenav.kivakit.resource;

import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.paths.Path;
import com.telenav.kivakit.kernel.language.paths.StringPath;
import com.telenav.kivakit.kernel.language.strings.Strip;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.resource.path.Extension;
import com.telenav.kivakit.resource.path.FileName;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResource;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResourcePath;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.List;
import java.util.function.Function;

/**
 * A path to a resource of any kind. By default the separator character for a resource is forward slash. But in the case
 * of some file paths this may be overridden. For example, in {@link FilePath}.
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
 *     <li>{@link #parseResourcePath(String)} - The given string as a resource path</li>
 *     <li>{@link #parseUnixResourcePath(String)} - The given string as a slash-separated UNIX resource path</li>
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
@UmlClassDiagram(diagram = DiagramResource.class)
@UmlClassDiagram(diagram = DiagramResourcePath.class)
public class ResourcePath extends StringPath
{
    /**
     * @return A resource path for the given string
     */
    public static ResourcePath parseResourcePath(final String path)
    {
        return FilePath.parseFilePath(path);
    }

    /**
     * @return A UNIX-style resource path for the given string
     */
    public static ResourcePath parseUnixResourcePath(String path)
    {
        String root = null;
        if (path.startsWith("/"))
        {
            root = "/";
            path = Strip.leading(path, "/");
        }
        return new ResourcePath(root, StringList.split(path, "/"));
    }

    /**
     * @return A resource path for the given string path
     */
    public static ResourcePath resourcePath(final StringPath path)
    {
        return new ResourcePath(path.rootElement(), path.elements());
    }

    /**
     * Converts to and from {@link ResourcePath}s
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public static class Converter extends BaseStringConverter<ResourcePath>
    {
        public Converter(final Listener listener)
        {
            super(listener);
        }

        @Override
        protected ResourcePath onConvertToObject(final String value)
        {
            return parseResourcePath(value);
        }
    }

    /**
     * @param root The root element
     * @param elements The path elements
     */
    protected ResourcePath(final String root, final List<String> elements)
    {
        super(root, elements);
    }

    /**
     * Copy constructor
     */
    protected ResourcePath(final ResourcePath that)
    {
        super(that);
    }

    protected ResourcePath(final List<String> elements)
    {
        super(elements);
    }

    /**
     * @return This resource path as a file
     */
    public File asFile()
    {
        return File.parse(asString());
    }

    /**
     * @return This resource path as a file path
     */
    public FilePath asFilePath()
    {
        return FilePath.parseFilePath(asString());
    }

    /**
     * @return The file name of this resource path
     */
    public FileName fileName()
    {
        final var last = last();
        return last == null ? null : FileName.parse(last);
    }

    @Override
    public ResourcePath last(final int n)
    {
        return (ResourcePath) super.last(n);
    }

    /**
     * @return This path without characters that are unacceptable in a resource path
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
     * {@inheritDoc}
     */
    @Override
    public ResourcePath root()
    {
        return (ResourcePath) super.root();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath subpath(final int start, final int end)
    {
        return (ResourcePath) super.subpath(start, end);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath transformed(final Function<String, String> consumer)
    {
        return (ResourcePath) super.transformed(consumer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withChild(final Path<String> that)
    {
        return (ResourcePath) super.withChild(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withChild(final String element)
    {
        return (ResourcePath) super.withChild(element);
    }

    /**
     * @return This resource path with the given extension
     */
    public FilePath withExtension(final Extension extension)
    {
        return (FilePath) withoutLast().withChild(last() + extension);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withParent(final String element)
    {
        return (ResourcePath) super.withParent(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withParent(final Path<String> that)
    {
        return (ResourcePath) super.withParent(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withRoot(final String root)
    {
        return (ResourcePath) super.withRoot(root);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withSeparator(final String separator)
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
    public ResourcePath withoutOptionalPrefix(final Path<String> prefix)
    {
        return (ResourcePath) super.withoutOptionalPrefix(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withoutOptionalSuffix(final Path<String> suffix)
    {
        return (ResourcePath) super.withoutOptionalSuffix(suffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withoutPrefix(final Path<String> prefix)
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
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withoutSuffix(final Path<String> suffix)
    {
        return (ResourcePath) super.withoutSuffix(suffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ResourcePath onCopy(final String root, final List<String> elements)
    {
        return new ResourcePath(root, elements);
    }
}