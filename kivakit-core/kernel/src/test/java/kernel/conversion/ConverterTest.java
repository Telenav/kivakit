////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.conversion;

import com.telenav.kivakit.core.kernel.data.conversion.string.enumeration.EnumConverter;
import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.messaging.listeners.MessageChecker;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Problem;
import org.junit.Test;

import java.util.Objects;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;

public class ConverterTest
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    enum Type
    {
        A,
        B
    }

    @Test
    public void test()
    {
        final EnumConverter<Type> converter = new EnumConverter<>(LOGGER, Type.class);
        ensureEqual(Type.A, converter.convert("A"));
        ensureEqual("A", converter.toString(Type.A));
        new MessageChecker().expect(Problem.class).check(() ->
                ensureEqual(null, new Duration.SecondsConverter(Listener.none()).convert("x")));
        ensureEqual(Duration.seconds(5), new Duration.SecondsConverter(Listener.none()).convert("5"));
    }

    @Test
    public void testCountConverter()
    {
        final Count.Converter converter = new Count.Converter(LOGGER);
        ensureEqual(1000, Objects.requireNonNull(converter.convert("1,000")).asInt());
    }
}
