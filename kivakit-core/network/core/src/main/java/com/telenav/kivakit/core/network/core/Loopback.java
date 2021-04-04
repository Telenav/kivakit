package com.telenav.kivakit.core.network.core;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.network.core.project.lexakai.diagrams.DiagramPort;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramPort.class)
public class Loopback extends Host
{
    private static final Lazy<Loopback> singleton = Lazy.of(Loopback::new);

    /**
     * @return The loopback address (normally 127.0.01) as a host
     */
    public static Loopback get()
    {
        return singleton.get();
    }

    protected Loopback()
    {
        super("loopback");
    }

    /**
     * @return The loopback address (normally 127.0.0.1)
     */
    @Override
    public InetAddress onResolveAddress()
    {
        try
        {
            final var interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements())
            {
                final var i = interfaces.nextElement();
                if (i.isLoopback())
                {
                    final var addresses = i.getInetAddresses();
                    while (addresses.hasMoreElements())
                    {
                        final var address = addresses.nextElement();
                        if (address instanceof Inet4Address)
                        {
                            return address;
                        }
                    }
                }
            }
        }
        catch (final Exception e)
        {
            return fail(e, "Couldn't find loopback interface");
        }
        return fail("Couldn't find loopback interface");
    }
}
