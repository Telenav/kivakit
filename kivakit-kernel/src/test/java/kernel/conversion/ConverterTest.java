////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.conversion;

import com.telenav.kivakit.kernel.data.conversion.string.enumeration.EnumConverter;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.listeners.MessageChecker;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import org.junit.Test;

import java.util.Objects;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureEqual;

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
