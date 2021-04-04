////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.time.conversion.converters;

import com.telenav.kivakit.core.kernel.language.time.LocalTime;
import com.telenav.kivakit.core.kernel.language.time.conversion.converters.HumanizedLocalDateTimeConverter;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;
import static com.telenav.kivakit.core.kernel.language.time.Meridiem.PM;

public class HumanizedLocalDateTimeConverterTest
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    @Test
    public void convert()
    {
        final var converter = new HumanizedLocalDateTimeConverter(LOGGER);

        final var now = LocalTime.now();

        final var expected = now.startOfDay().withHourOfMeridiem(6, PM).withMinuteOfHour(15);
        ensureEqual(converter.convert("Today 6.15pm"), expected);
        ensureEqual(converter.toString(expected), "Today 6.15PM");
    }
}
