////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.data.conversion;

import com.telenav.kivakit.kernel.data.conversion.string.enumeration.EnumConverter;
import com.telenav.kivakit.kernel.data.conversion.string.enumeration.EnumListConverter;
import com.telenav.kivakit.kernel.data.conversion.string.enumeration.EnumSetConverter;
import com.telenav.kivakit.kernel.data.conversion.string.language.ClassConverter;
import com.telenav.kivakit.kernel.data.conversion.string.language.IdentityConverter;
import com.telenav.kivakit.kernel.data.conversion.string.language.PatternConverter;
import com.telenav.kivakit.kernel.data.conversion.string.language.VersionConverter;
import com.telenav.kivakit.kernel.data.conversion.string.primitive.BooleanConverter;
import com.telenav.kivakit.kernel.data.conversion.string.primitive.DoubleConverter;
import com.telenav.kivakit.kernel.data.conversion.string.primitive.FloatConverter;
import com.telenav.kivakit.kernel.data.conversion.string.primitive.FormattedDoubleConverter;
import com.telenav.kivakit.kernel.data.conversion.string.primitive.FormattedIntegerConverter;
import com.telenav.kivakit.kernel.data.conversion.string.primitive.FormattedLongConverter;
import com.telenav.kivakit.kernel.data.conversion.string.primitive.HexadecimalLongConverter;
import com.telenav.kivakit.kernel.data.conversion.string.primitive.IntegerConverter;
import com.telenav.kivakit.kernel.data.conversion.string.primitive.LongConverter;
import com.telenav.kivakit.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.kernel.language.collections.set.ObjectSet;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.listeners.MessageChecker;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import org.junit.Test;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureEqual;
import static kernel.data.conversion.ConverterTest.Type.A;
import static kernel.data.conversion.ConverterTest.Type.B;

public class ConverterTest
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    enum Type
    {
        A,
        B
    }

    @Test
    public void testBadInputs()
    {
        new MessageChecker().expect(Problem.class).check(() ->
                ensureEqual(null, new Duration.SecondsConverter(Listener.none()).convert(null)));

        new MessageChecker().expect(Problem.class).check(() ->
                ensureEqual(null, new Duration.SecondsConverter(Listener.none()).convert("")));

        new MessageChecker().expect(Problem.class).check(() ->
                ensureEqual(null, new Duration.SecondsConverter(Listener.none()).convert("x")));
    }

    @Test
    public void testClassConverter()
    {
        ensureEqual(new ClassConverter(LOGGER).convert("java.util.List"), List.class);
    }

    @Test
    public void testCountConverter()
    {
        final var converter = new Count.Converter(LOGGER);
        ensureEqual(1000, Objects.requireNonNull(converter.convert("1,000")).asInt());
    }

    @Test
    public void testEnumConverter()
    {
        final EnumConverter<Type> converter = new EnumConverter<>(LOGGER, Type.class);

        ensureEqual(A, converter.convert("A"));
        ensureEqual("A", converter.unconvert(A));

        ensureEqual(B, converter.convert("B"));
        ensureEqual("B", converter.unconvert(B));
    }

    @Test
    public void testEnumListConverter()
    {
        ensureEqual(new EnumListConverter<>(LOGGER, Type.class).convert("A, B, B, A"), ObjectList.objectList(A, B, B, A));
    }

    @Test
    public void testEnumSetConverter()
    {
        ensureEqual(new EnumSetConverter<>(LOGGER, Type.class).convert("A, B, B, A"), ObjectSet.of(A, B, B, A));
    }

    @Test
    public void testIdentityConverter()
    {
        ensureEqual(new IdentityConverter(LOGGER).convert("x"), "x");
    }

    @Test
    public void testPatternConverter()
    {
        ensureEqual(new PatternConverter(LOGGER).convert("a*b").pattern(), Pattern.compile("a*b").pattern());
    }

    @Test
    public void testPrimitiveConverters()
    {
        ensureEqual(true, new BooleanConverter(Listener.none()).convert("true"));
        ensureEqual(false, new BooleanConverter(Listener.none()).convert("false"));
        ensureEqual(3.1415926, new DoubleConverter(Listener.none()).convert("3.1415926"));
        ensureEqual(3.1415926f, new FloatConverter(Listener.none()).convert("3.1415926f"));
        ensureEqual(31415926, new IntegerConverter(Listener.none()).convert("31415926"));
        ensureEqual(31415926L, new LongConverter(Listener.none()).convert("31415926"));
        ensureEqual(0x31415926L, new HexadecimalLongConverter(Listener.none()).convert("0x31415926"));
        ensureEqual(31415926L, new FormattedLongConverter(Listener.none()).convert("31,415,926"));
        ensureEqual(31415926, new FormattedIntegerConverter(Listener.none()).convert("31,415,926"));
        ensureEqual(314159.26, new FormattedDoubleConverter(Listener.none()).convert("314159.26"));
    }

    @Test
    public void testSecondsConverter()
    {
        ensureEqual(Duration.seconds(5), new Duration.SecondsConverter(Listener.none()).convert("5"));
    }

    @Test
    public void testVersionConverter()
    {
        ensureEqual(new VersionConverter(LOGGER).convert("3.0"), Version.parse("3.0"));
        ensureEqual(new VersionConverter(LOGGER).convert("3.0.1"), Version.parse("3.0.1"));
        ensureEqual(new VersionConverter(LOGGER).convert("3.0.1-alpha"), Version.parse("3.0.1-alpha"));
        ensureEqual(new VersionConverter(LOGGER).convert("3.0.1-alpha-SNAPSHOT"), Version.parse("3.0.1-alpha-SNAPSHOT"));
        ensureEqual(new VersionConverter(LOGGER).convert("3.0.1-beta"), Version.parse("3.0.1-beta"));
        ensureEqual(new VersionConverter(LOGGER).convert("3.0.1-beta-SNAPSHOT"), Version.parse("3.0.1-beta-SNAPSHOT"));
        ensureEqual(new VersionConverter(LOGGER).convert("3.0.1-rc"), Version.parse("3.0.1-rc"));
        ensureEqual(new VersionConverter(LOGGER).convert("3.0.1-rc-SNAPSHOT"), Version.parse("3.0.1-rc-SNAPSHOT"));
        ensureEqual(new VersionConverter(LOGGER).convert("3.0.1-final"), Version.parse("3.0.1-final"));
        ensureEqual(new VersionConverter(LOGGER).convert("3.0.1-final-SNAPSHOT"), Version.parse("3.0.1-final-SNAPSHOT"));
        ensureEqual(new VersionConverter(LOGGER).convert("3.0.1-m1"), Version.parse("3.0.1-m1"));
        ensureEqual(new VersionConverter(LOGGER).convert("3.0-SNAPSHOT"), Version.parse("3.0-SNAPSHOT"));
    }
}
