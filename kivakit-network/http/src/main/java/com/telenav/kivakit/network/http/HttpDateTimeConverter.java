package com.telenav.kivakit.network.http;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.conversion.core.time.TimeConverter;
import com.telenav.kivakit.core.messaging.Listener;

import java.time.format.DateTimeFormatter;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A time converter for HTTP format date/time strings
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class HttpDateTimeConverter extends TimeConverter
{
    public HttpDateTimeConverter(Listener listener)
    {
        // Mon, 19 Jul 2021 13:05:31 GMT
        super(listener, DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z"));
    }
}
