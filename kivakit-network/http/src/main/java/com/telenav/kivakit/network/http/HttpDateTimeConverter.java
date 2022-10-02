package com.telenav.kivakit.network.http;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.conversion.core.time.TimeConverter;
import com.telenav.kivakit.core.messaging.Listener;

import java.time.format.DateTimeFormatter;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A time converter for HTTP format date/time strings
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = API_STABLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public class HttpDateTimeConverter extends TimeConverter
{
    public HttpDateTimeConverter(Listener listener)
    {
        // Mon, 19 Jul 2021 13:05:31 GMT
        super(listener, DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z"));
    }
}
