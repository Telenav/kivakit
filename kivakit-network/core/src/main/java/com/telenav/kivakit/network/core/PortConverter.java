package com.telenav.kivakit.network.core;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.core.language.primitive.IntegerConverter;
import com.telenav.kivakit.core.messaging.Listener;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Converts to and from a {@link Port}
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public class PortConverter extends BaseStringConverter<Port>
{
    /** Host converter */
    private final HostConverter hostConverter;

    /** Integer converter for port numbers */
    private final IntegerConverter integerConverter;

    public PortConverter(Listener listener)
    {
        super(listener, Port.class);
        integerConverter = new IntegerConverter(listener);
        hostConverter = new HostConverter(listener);
    }

    @Override
    protected Port onToValue(String value)
    {
        // It is expected that the port is in the format <host>:<portNumber>. If
        // no <portNumber> is specified then 80 is assumed.
        var parts = value.split(":");
        var host = hostConverter.convert(parts[0]);
        var portNumber = 80;
        if (parts.length > 1)
        {
            var portInteger = integerConverter.convert(parts[1]);
            if (portInteger != null)
            {
                portNumber = portInteger;
            }
        }
        return new Port(host, portNumber);
    }
}
