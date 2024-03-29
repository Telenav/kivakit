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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.path.Path;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.network.core.internal.lexakai.DiagramNetworkLocation;
import com.telenav.kivakit.resource.Extension;
import com.telenav.kivakit.resource.FileName;
import com.telenav.kivakit.resource.UriAuthority;
import com.telenav.kivakit.resource.UriSchemes;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.function.Function;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.commandline.SwitchParser.switchParser;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.resource.UriSchemes.uriScheme;

/**
 * An abstraction for network paths.
 *
 * <p>
 * This class contains numerous methods which down-cast the return value of the superclass to make use easier for
 * clients. Methods that are unique to this class mainly have to do with networks:
 * </p>
 *
 * <p><b>Path Factory Methods</b></p>
 *
 * <ul>
 *     <li>{@link #networkPath(Listener, URI)}</li>
 *     <li>{@link #networkPath(Listener, Port, String)}</li>
 * </ul>
 *
 * <p><b>Path Parsing Methods</b></p>
 *
 * <ul>
 *     <li>{@link #parseNetworkPath(Listener, String)}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #parent()}</li>
 *     <li>{@link #port()}</li>
 *     <li>{@link #separator()}</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #asAbsolute()}</li>
 *     <li>{@link #asStringPath()}</li>
 *     <li>{@link #asUri()}</li>
 *     <li>{@link #normalized()}</li>
 * </ul>
 *
 * <p><b>Functional</b></p>
 *
 * <ul>
 *     <li>{@link #subpath(int, int)}</li>
 *     <li>{@link #transformed(Function)}</li>
 *     <li>{@link #withChild(String)}</li>
 *     <li>{@link #withChild(Path)}</li>
 *     <li>{@link #withExtension(Extension)}</li>
 *     <li>{@link #withParent(String)}</li>
 *     <li>{@link #withParent(Path)}</li>
 *     <li>{@link #withRoot(String)}</li>
 *     <li>{@link #withSeparator(String)}</li>
 *     <li>{@link #withoutFirst()}</li>
 *     <li>{@link #withoutLast()}</li>
 *     <li>{@link #withoutOptionalPrefix(Path)}</li>
 *     <li>{@link #withoutOptionalSuffix(Path)}</li>
 *     <li>{@link #withoutPrefix(String)}</li>
 *     <li>{@link #withoutPrefix(Path)}</li>
 *     <li>{@link #withoutRoot()}</li>
 *     <li>{@link #withoutSchemes()}</li>
 *     <li>{@link #withoutSuffix(Path)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramNetworkLocation.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public class NetworkPath extends FilePath
{
    /**
     * Returns a network path for the given URI
     *
     * @param listener The listener to call with any problems
     * @return A network path for the given URI
     */
    public static NetworkPath networkPath(Listener listener, URI uri)
    {
        return networkPath(listener, Port.port(uri), uri.getPath());
    }

    /**
     * Returns a network path for the given port and path
     *
     * @param port The port for the network path
     * @return The given path relative to the given port as a network path
     */
    public static NetworkPath networkPath(Port port, String path)
    {
        return networkPath(throwingListener(), port, path);
    }

    /**
     * Returns a network path for the given port and path
     *
     * @param listener The listener to call with any problems
     * @return The given path relative to the given port as a network path
     */
    public static NetworkPath networkPath(Listener listener, Port port, String path)
    {
        var root = "//" + port + "/";
        return new NetworkPath(port, parseStringPath(listener, path, "/", "/").withRoot(root));
    }

    /**
     * Returns a switch parser builder for network paths
     *
     * @param listener The listener to call with any problems
     * @param name The name of the switch
     * @param description The switch description
     * @return The switch parser builder
     */
    public static SwitchParser.Builder<NetworkPath> networkPathSwitchParser(Listener listener,
                                                                            String name,
                                                                            String description)
    {
        return switchParser(NetworkPath.class)
            .name(name)
            .converter(new NetworkPathConverter(listener))
            .description(description);
    }

    /**
     * Parses the given text into a {@link NetworkPath}
     *
     * @param text The text to parse
     * @return A network path for the given string
     */
    public static NetworkPath parseNetworkPath(Listener listener, String text)
    {
        try
        {
            return networkPath(listener, new URI(text));
        }
        catch (URISyntaxException e)
        {
            listener.problem("Invalid network path: $", text);
            return null;
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
        super(uriScheme(port.protocol().name()), root, elements);
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
    public NetworkPath asAbsolute()
    {
        return (NetworkPath) super.asAbsolute();
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
     * Returns this network path as a URI
     */
    @Override
    public URI asUri()
    {
        try
        {
            return new URI(port.toString() + "/" + this);
        }
        catch (URISyntaxException e)
        {
            throw new IllegalStateException("Unable to convert " + this + " to a URI", e);
        }
    }

    @Override
    public NetworkPath copy()
    {
        return new NetworkPath(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath file(@NotNull FileName child)
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
     * Returns the port for this network path (which includes the host and protocol)
     */
    @FormatProperty
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
    public NetworkPath transformed(@NotNull Function<String, String> consumer)
    {
        return (NetworkPath) super.transformed(consumer);
    }

    @Override
    public FilePath withAuthority(@NotNull UriAuthority authority)
    {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withChild(@NotNull String child)
    {
        return (NetworkPath) super.withChild(child);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withChild(@NotNull Path<String> that)
    {
        return (NetworkPath) super.withChild(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withExtension(@NotNull Extension extension)
    {
        return (NetworkPath) super.withExtension(extension);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withParent(@NotNull String element)
    {
        return (NetworkPath) super.withParent(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withParent(@NotNull Path<String> that)
    {
        return (NetworkPath) super.withParent(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withRoot(@NotNull String root)
    {
        return (NetworkPath) super.withRoot(root);
    }

    @Override
    public FilePath withScheme(@NotNull String scheme)
    {
        return this;
    }

    @Override
    public FilePath withSchemes(@NotNull UriSchemes scheme)
    {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withSeparator(@NotNull String separator)
    {
        return (NetworkPath) super.withSeparator(separator);
    }

    @Override
    public FilePath withoutAuthority()
    {
        return this;
    }

    @Override
    public FilePath withoutFileScheme()
    {
        return this;
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
    public NetworkPath withoutOptionalPrefix(@NotNull Path<String> prefix)
    {
        return (NetworkPath) super.withoutOptionalPrefix(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withoutOptionalSuffix(@NotNull Path<String> suffix)
    {
        return (NetworkPath) super.withoutOptionalSuffix(suffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withoutPrefix(@NotNull Path<String> prefix)
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
    public NetworkPath withoutSuffix(@NotNull Path<String> suffix)
    {
        return (NetworkPath) super.withoutSuffix(suffix);
    }
}
