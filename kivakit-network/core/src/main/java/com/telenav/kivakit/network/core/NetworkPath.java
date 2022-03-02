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

package com.telenav.kivakit.network.core;

import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.LoggerFactory;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.path.Path;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.network.core.project.lexakai.DiagramNetworkLocation;
import com.telenav.kivakit.resource.path.Extension;
import com.telenav.kivakit.resource.path.FileName;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.function.Function;

import static com.telenav.kivakit.commandline.SwitchParser.builder;

/**
 * An abstraction for network paths.
 *
 * <p>
 * This class contains numerous methods which down-cast the return value of the superclass to make use easier for
 * clients. Methods that are unique to this class mainly have to do with networks:
 * </p>
 *
 * <ul>
 *     <li>{@link #asUri()} - This network path as a URI</li>
 *     <li>{@link #port()} - The port for this network path</li>
 * </ul>
 *
 * <p><b>Path Parsing Methods</b></p>
 *
 * <ul>
 *     <li>{@link #parseNetworkPath(Listener, String)} - The given string as a network path</li>
 * </ul>
 *
 * <p><b>Path Factory Methods</b></p>
 *
 * <ul>
 *     <li>{@link #networkPath(Listener, URI)} - A network path for the given URI</li>
 *     <li>{@link #networkPath(Listener, Port, String)} - The given port (including host and protocol) and path as a network path</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramNetworkLocation.class)
@LexakaiJavadoc(complete = true)
public class NetworkPath extends FilePath
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /**
     * @return A network path for the given URI
     */
    public static NetworkPath networkPath(Listener listener, URI uri)
    {
        return networkPath(listener, Port.from(uri), uri.getPath());
    }

    /**
     * @return The given path relative to the given port as a network path
     */
    public static NetworkPath networkPath(Listener listener, Port port, String path)
    {
        var root = "/" + port + "/";
        return new NetworkPath(port, StringPath.parseStringPath(listener, path, "/", "/").withRoot(root));
    }

    public static SwitchParser.Builder<NetworkPath> networkPathSwitchParser(Listener listener,
                                                                            String name,
                                                                            String description)
    {
        return builder(NetworkPath.class)
                .name(name)
                .converter(new NetworkPath.Converter(listener))
                .description(description);
    }

    /**
     * @return A network path for the given string
     */
    public static NetworkPath parseNetworkPath(Listener listener, String path)
    {
        try
        {
            return networkPath(listener, new URI(path));
        }
        catch (URISyntaxException e)
        {
            listener.problem("Invalid network path: $", path);
            return null;
        }
    }

    /**
     * Converts to and from {@link NetworkPath}s
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public static class Converter extends BaseStringConverter<NetworkPath>
    {
        public Converter(Listener listener)
        {
            super(listener, NetworkPath::parseNetworkPath);
        }
    }

    /** The port for this network path */
    private final Port port;

    protected NetworkPath(Port port, StringPath path)
    {
        this(port, path.rootElement(), path.elements());
    }

    protected NetworkPath(Port port, String root, List<String> elements)
    {
        super(StringList.stringList(port.protocol().name()), root, elements);
        this.port = port;
    }

    protected NetworkPath(NetworkPath that)
    {
        super(that);
        port = that.port;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath absolute()
    {
        return (NetworkPath) super.absolute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath asStringPath()
    {
        return super.asStringPath();
    }

    /**
     * @return This network path as a URI
     */
    public URI asUri()
    {
        try
        {
            return new URI(port.toString() + "/" + super.toString());
        }
        catch (URISyntaxException e)
        {
            LOGGER.problem(e, "Unable to convert $ to a URI", this);
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath file(FileName child)
    {
        return (NetworkPath) super.file(child);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath normalized()
    {
        return (NetworkPath) super.normalized();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath parent()
    {
        return (NetworkPath) super.parent();
    }

    /**
     * @return The port for this network path (which includes the host and protocol)
     */
    @KivaKitIncludeProperty
    public Port port()
    {
        return port;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String separator()
    {
        return "/";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath subpath(int start, int end)
    {
        return (NetworkPath) super.subpath(start, end);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath transformed(Function<String, String> consumer)
    {
        return (NetworkPath) super.transformed(consumer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withChild(String child)
    {
        return (NetworkPath) super.withChild(child);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withChild(Path<String> that)
    {
        return (NetworkPath) super.withChild(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withExtension(Extension extension)
    {
        return (NetworkPath) super.withExtension(extension);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withParent(String element)
    {
        return (NetworkPath) super.withParent(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withParent(Path<String> that)
    {
        return (NetworkPath) super.withParent(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withRoot(String root)
    {
        return (NetworkPath) super.withRoot(root);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withSeparator(String separator)
    {
        return (NetworkPath) super.withSeparator(separator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withoutFirst()
    {
        return (NetworkPath) super.withoutFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withoutLast()
    {
        return (NetworkPath) super.withoutLast();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withoutOptionalPrefix(Path<String> prefix)
    {
        return (NetworkPath) super.withoutOptionalPrefix(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withoutOptionalSuffix(Path<String> suffix)
    {
        return (NetworkPath) super.withoutOptionalSuffix(suffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withoutPrefix(Path<String> prefix)
    {
        return (NetworkPath) super.withoutPrefix(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withoutRoot()
    {
        return (NetworkPath) super.withoutRoot();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withoutSchemes()
    {
        return (NetworkPath) super.withoutSchemes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withoutSuffix(Path<String> suffix)
    {
        return (NetworkPath) super.withoutSuffix(suffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected NetworkPath onCopy(String root, List<String> elements)
    {
        return new NetworkPath(port(), root, elements);
    }
}
