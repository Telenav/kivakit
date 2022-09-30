package com.telenav.kivakit.network.http;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.conversion.core.time.TimeConverter;
import com.telenav.kivakit.core.messaging.Listener;

import java.time.format.DateTimeFormatter;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * A time converter for HTTP format date/time strings
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class HttpDateTimeConverter extends TimeConverter
{
    public HttpDateTimeConverter(Listener listener)
    {
        // Mon, 19 Jul 2021 13:05:31 GMT
        super(listener, DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z"));
    }
}
