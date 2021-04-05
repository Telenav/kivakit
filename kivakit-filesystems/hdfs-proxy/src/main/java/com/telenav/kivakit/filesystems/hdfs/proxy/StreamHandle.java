package com.telenav.kivakit.filesystems.hdfs.proxy;

import com.telenav.kivakit.core.kernel.language.values.identifier.Identifier;
import com.telenav.kivakit.core.kernel.language.values.identifier.IdentifierFactory;

import java.util.Objects;

/**
 * Identifies a stream between the proxy server and a client
 *
 * @author jonathanl (shibo)
 */
class StreamHandle
{
    private static final IdentifierFactory identifiers = new IdentifierFactory();

    public static StreamHandle create()
    {
        return new StreamHandle(identifiers.newInstance());
    }

    public static StreamHandle of(final long identifier)
    {
        return new StreamHandle(new Identifier(identifier));
    }

    private final Identifier identifier;

    private StreamHandle(final Identifier identifier)
    {
        this.identifier = identifier;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof StreamHandle)
        {
            final StreamHandle that = (StreamHandle) object;
            return identifier.equals(that.identifier);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(identifier);
    }

    public Identifier identifier()
    {
        return identifier;
    }
}
