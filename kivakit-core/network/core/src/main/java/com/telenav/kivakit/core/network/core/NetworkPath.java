////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.core;

import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.language.paths.Path;
import com.telenav.kivakit.core.kernel.language.paths.StringPath;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.network.core.project.lexakai.diagrams.DiagramNetworkLocation;
import com.telenav.kivakit.core.resource.path.Extension;
import com.telenav.kivakit.core.resource.path.FileName;
import com.telenav.kivakit.core.resource.path.FilePath;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.function.Function;

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
 *     <li>{@link #parseNetworkPath(String)} - The given string as a network path</li>
 * </ul>
 *
 * <p><b>Path Factory Methods</b></p>
 *
 * <ul>
 *     <li>{@link #networkPath(URI)} - A network path for the given URI</li>
 *     <li>{@link #networkPath(Port, String)} - The given port (including host and protocol) and path as a network path</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramNetworkLocation.class)
public class NetworkPath extends FilePath
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /**
     * @return A network path for the given URI
     */
    public static NetworkPath networkPath(final URI uri)
    {
        return networkPath(Port.from(uri), uri.getPath());
    }

    /**
     * @return The given path relative to the given port as a network path
     */
    public static NetworkPath networkPath(final Port port, final String path)
    {
        final var root = "/" + port + "/";
        return new NetworkPath(port, StringPath.parseStringPath(path, "/", "/").withRoot(root));
    }

    /**
     * @return A network path for the given string
     */
    public static NetworkPath parseNetworkPath(final String path)
    {
        try
        {
            return networkPath(new URI(path));
        }
        catch (final URISyntaxException e)
        {
            return null;
        }
    }

    public static class Converter extends BaseStringConverter<NetworkPath>
    {
        public Converter(final Listener listener)
        {
            super(listener);
        }

        @Override
        protected NetworkPath onConvertToObject(final String value)
        {
            return parseNetworkPath(value);
        }
    }

    /** The port for this network path */
    private final Port port;

    protected NetworkPath(final Port port, final StringPath path)
    {
        this(port, path.rootElement(), path.elements());
    }

    protected NetworkPath(final Port port, final String root, final List<String> elements)
    {
        super(port.protocol().name(), root, elements);
        this.port = port;
    }

    protected NetworkPath(final NetworkPath that)
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
        catch (final URISyntaxException e)
        {
            LOGGER.problem(e, "Unable to convert $ to a URI", this);
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath file(final FileName child)
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
    public NetworkPath subpath(final int start, final int end)
    {
        return (NetworkPath) super.subpath(start, end);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath transformed(final Function<String, String> consumer)
    {
        return (NetworkPath) super.transformed(consumer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withChild(final String child)
    {
        return (NetworkPath) super.withChild(child);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withChild(final Path<String> that)
    {
        return (NetworkPath) super.withChild(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withExtension(final Extension extension)
    {
        return (NetworkPath) super.withExtension(extension);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withParent(final String element)
    {
        return (NetworkPath) super.withParent(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withParent(final Path<String> that)
    {
        return (NetworkPath) super.withParent(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withRoot(final String root)
    {
        return (NetworkPath) super.withRoot(root);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withSeparator(final String separator)
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
    public NetworkPath withoutOptionalPrefix(final Path<String> prefix)
    {
        return (NetworkPath) super.withoutOptionalPrefix(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withoutOptionalSuffix(final Path<String> suffix)
    {
        return (NetworkPath) super.withoutOptionalSuffix(suffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withoutPrefix(final Path<String> prefix)
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
    public NetworkPath withoutScheme()
    {
        return (NetworkPath) super.withoutScheme();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPath withoutSuffix(final Path<String> suffix)
    {
        return (NetworkPath) super.withoutSuffix(suffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected NetworkPath onCopy(final String root, final List<String> elements)
    {
        return new NetworkPath(port(), root, elements);
    }
}
