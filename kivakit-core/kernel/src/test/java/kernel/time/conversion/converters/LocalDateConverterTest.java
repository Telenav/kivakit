////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.time.conversion.converters;

import com.telenav.kivakit.core.kernel.language.time.LocalTime;
import com.telenav.kivakit.core.kernel.language.time.conversion.converters.LocalDateConverter;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;

public class LocalDateConverterTest
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    @Test
    public void convert()
    {
        final var converter = new LocalDateConverter(LOGGER, LocalTime.utcTimeZone());

        final var time = converter.convert("2011.07.06");

        // Using http://www.epochconverter.com/ midnight of that date UTC time
        // was converted to milliseconds.
        ensure(time != null);
        assert time != null;
        ensureEqual(ChronoUnit.MILLIS.between(Instant.EPOCH, time.startOfDay().asInstant()), 1309910400000L);
    }
}
