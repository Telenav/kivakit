package com.telenav.kivakit.network.core.converters;

import com.telenav.kivakit.kernel.language.time.conversion.converters.DateTimeConverter;
import com.telenav.kivakit.kernel.messaging.Listener;

import java.time.format.DateTimeFormatter;

public class HttpDateTimeConverter extends DateTimeConverter
{
    public HttpDateTimeConverter(final Listener listener)
    {
        // Mon, 19 Jul 2021 13:05:31 GMT
        super(listener, DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z"));
    }
}
