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

package com.telenav.kivakit.test;

import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.data.validation.ensure.Failure;
import com.telenav.kivakit.interfaces.code.Loopable;
import com.telenav.kivakit.interfaces.value.Source;
import com.telenav.kivakit.kernel.language.objects.Objects;
import com.telenav.kivakit.kernel.language.primitives.Booleans;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.language.vm.OperatingSystem;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.listeners.ConsoleWriter;
import com.telenav.kivakit.kernel.messaging.repeaters.RepeaterMixin;
import com.telenav.kivakit.test.project.lexakai.diagrams.DiagramTest;
import com.telenav.kivakit.test.random.RandomValueFactory;
import com.telenav.kivakit.test.reporters.JUnitFailureReporter;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestWatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * This is the base test class for all unit tests. It provides useful methods that are common to all tests. Several
 * ensure*() methods delegate to the {@link Ensure} class to provide easy access to these methods. A variety of methods
 * provide looping, indexing, iteration and random testing assistance.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramTest.class)
@UmlRelation(label = "uses", referent = RandomValueFactory.class)
@UmlRelation(label = "reports validation failures with", referent = JUnitFailureReporter.class)
public abstract class UnitTest extends TestWatcher implements RepeaterMixin
{
    private static boolean quickTest;

    private static final Logger LOGGER = LoggerFactory.newLogger();

    @BeforeClass
    public static void testSetup()
    {
        quickTest = Booleans.isTrue(System.getProperty("testQuick"));
        ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
        Failure.reporterFactory(messageType -> new JUnitFailureReporter());
    }

    /**
     * Values that allow or disallow repeats in random sequences
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public enum Repeats
    {
        NO_REPEATS,
        ALLOW_REPEATS
    }

    @Rule
    public UnitTestWatcher watcher = new UnitTestWatcher(this);

    private Count iterations = isQuickTest() ? Count.count(100) : Count._1_000;

    private final ConsoleWriter console = new ConsoleWriter();

    private final ThreadLocal<Object> randomValueFactory = new ThreadLocal<>();

    private final ThreadLocal<Integer> index = new ThreadLocal<>();

    public UnitTest()
    {
        LOGGER.listenTo(this);
    }

    public boolean isRandomTest()
    {
        return randomValueFactory.get() != null;
    }

    @Override
    public void onMessage(Message message)
    {
        console.receive(message);
    }

    @Before
    public void testBeforeUnitTest()
    {
    }

    protected boolean ensure(boolean condition)
    {
        return Ensure.ensure(condition);
    }

    protected void ensure(boolean condition, String message, Object... arguments)
    {
        Ensure.ensure(condition, message, arguments);
    }

    protected void ensureBetween(double actual, double low, double high)
    {
        ensure(low < high, "The low boundary $ is higher than the high boundary $", low, high);
        if (actual < low || actual > high)
        {
            fail("Value $ is not between $ and $", actual, low, high);
        }
    }

    protected void ensureClose(Number expected, Number actual, int numberOfDecimalsToMatch)
    {
        var roundedExpected = (int) (expected.doubleValue() * Math.pow(10, numberOfDecimalsToMatch))
                / Math.pow(10, numberOfDecimalsToMatch);
        var roundedActual = (int) (actual.doubleValue() * Math.pow(10, numberOfDecimalsToMatch))
                / Math.pow(10, numberOfDecimalsToMatch);
        ensureWithin(roundedExpected, roundedActual, 1E-3);
    }

    @SuppressWarnings("UnusedReturnValue")
    protected boolean ensureClose(Duration given, Duration expected)
    {
        return given.isApproximately(expected, Duration.seconds(0.5));
    }

    protected <T> void ensureEqual(T given, T expected)
    {
        Ensure.ensureEqual(given, expected);
    }

    protected <T> void ensureEqual(T given, T expected, String message, Object... arguments)
    {
        Ensure.ensureEqual(given, expected, message, arguments);
    }

    protected void ensureEqualArray(byte[] a, byte[] b)
    {
        ensure(Arrays.equals(a, b));
        ensure(Arrays.equals(b, a));
    }

    protected <T> void ensureEqualArray(T[] a, T[] b)
    {
        ensure(Arrays.equals(a, b));
        ensure(Arrays.equals(b, a));
    }

    protected void ensureFalse(boolean condition)
    {
        Ensure.ensure(!condition);
    }

    protected void ensureFalse(boolean condition, String message, Object... arguments)
    {
        Ensure.ensure(!condition, message, arguments);
    }

    protected <T> void ensureNotEqual(T a, T b)
    {
        ensureFalse(Objects.equal(a, b), a + " should not equal " + b);
        ensureFalse(Objects.equal(b, a), a + " should not equal " + b);
    }

    protected <T> T ensureNotNull(T object)
    {
        return Ensure.ensureNotNull(object);
    }

    protected void ensureNull(Object object)
    {
        ensure(object == null);
    }

    @SuppressWarnings("MaskedAssertion")
    protected void ensureThrows(Runnable code)
    {
        try
        {
            code.run();
            fail("Code should have thrown exception");
        }
        catch (AssertionError e)
        {
            // Expected
        }
    }

    protected void ensureWithin(double expected, double actual, double maximumDifference)
    {
        var difference = Math.abs(expected - actual);
        if (difference > maximumDifference)
        {
            fail("Expected value $ was not within $ of actual value $", expected, maximumDifference, actual);
        }
    }

    protected void ensureZero(Number value)
    {
        ensure(value.doubleValue() == 0.0);
    }

    protected void fail(String message, Object... arguments)
    {
        Ensure.fail(message, arguments);
    }

    protected int index()
    {
        return index.get();
    }

    protected boolean isQuickTest()
    {
        return quickTest;
    }

    protected boolean isWindows()
    {
        return OperatingSystem.get().isWindows();
    }

    protected void iterateBytes(Consumer<Byte> consumer)
    {
        iterations.forEachByte(consumer);
    }

    protected void iterateIndexes(Consumer<Integer> consumer)
    {
        iterations.forEachInteger(consumer);
    }

    protected void iterateIntegers(Consumer<Integer> consumer)
    {
        iterations.forEachInteger(consumer);
    }

    protected void iterateLongs(Consumer<Long> consumer)
    {
        iterations.forEachLong(consumer);
    }

    protected void iterateShorts(Consumer<Short> consumer)
    {
        iterations.forEachShort(consumer);
    }

    protected void iterations(Count iterations)
    {
        this.iterations = iterations;
    }

    protected void iterations(int iterations)
    {
        iterations(Count.count(iterations));
    }

    protected Count iterations()
    {
        return iterations;
    }

    protected void loop(Runnable code)
    {
        iterations.loop(code);
    }

    protected void loop(Loopable code)
    {
        iterations.loop(code);
    }

    protected void loop(int minimum, int maximum, Runnable code)
    {
        loop(randomInt(minimum, maximum), code);
    }

    protected void loop(int minimum, int maximum, Loopable code)
    {
        loop(randomInt(minimum, maximum), code);
    }

    protected void loop(int times, Runnable code)
    {
        for (var i = 0; i < times; i++)
        {
            code.run();
        }
    }

    protected void loop(int times, Loopable code)
    {
        for (var iteration = 0; iteration < times; iteration++)
        {
            code.iteration(iteration);
        }
    }

    @SuppressWarnings("unchecked")
    protected final <T extends RandomValueFactory> T newRandomValueFactory(Source<T> factory)
    {
        if (randomValueFactory.get() == null)
        {
            randomValueFactory.set(factory.get());
        }
        return (T) randomValueFactory.get();
    }

    protected int nextIndex()
    {
        var next = index.get();
        index.set(next + 1);
        return next;
    }

    protected char randomAsciiChar()
    {
        return randomValueFactory().newAsciiChar();
    }

    protected String randomAsciiString(int minimum, int maximum)
    {
        return randomValueFactory().newAsciiString(minimum, maximum);
    }

    protected String randomAsciiString()
    {
        return randomValueFactory().newAsciiString();
    }

    protected List<Byte> randomByteList(Repeats repeats)
    {
        var values = new ArrayList<Byte>();
        randomBytes(repeats, values::add);
        return values;
    }

    protected List<Byte> randomByteList(Repeats repeats, byte minimum, byte maximum)
    {
        var values = new ArrayList<Byte>();
        randomBytes(repeats, minimum, maximum, null, values::add);
        return values;
    }

    protected void randomBytes(Repeats repeats, Consumer<Byte> consumer)
    {
        randomBytes(repeats, iterations, consumer);
    }

    protected void randomBytes(Repeats repeats, Count count, Consumer<Byte> consumer)
    {
        randomBytes(repeats, count, (byte) (Byte.MIN_VALUE + 2), Byte.MAX_VALUE, null, consumer);
    }

    protected void randomBytes(Repeats repeats, byte minimum, byte maximum,
                               Predicate<Byte> filter, Consumer<Byte> consumer)
    {
        randomBytes(repeats, iterations, minimum, maximum, filter, consumer);
    }

    protected void randomBytes(Repeats repeats, Count count, byte minimum, byte maximum,
                               Predicate<Byte> filter, Consumer<Byte> consumer)
    {
        if (repeats == Repeats.NO_REPEATS)
        {
            var values = new HashSet<Byte>();
            while (values.size() < count.minimum(Count.count(250)).asInt())
            {
                var value = randomValueFactory().newByte(minimum, maximum, filter);
                values.add(value);
            }
            for (var value : values)
            {
                consumer.accept(value);
            }
        }
        else
        {
            count.loop(() -> consumer.accept(randomValueFactory().newByte(minimum, maximum, filter)));
        }
    }

    protected void randomBytes(Repeats repeats, Predicate<Byte> filter, Consumer<Byte> consumer)
    {
        randomBytes(repeats, iterations, (byte) (Byte.MIN_VALUE + 2), Byte.MAX_VALUE, filter, consumer);
    }

    protected int randomIndex()
    {
        return randomValueFactory().newIndex(iterations().asInt());
    }

    protected void randomIndexes(Repeats repeats, Consumer<Integer> consumer)
    {
        randomIndexes(repeats, iterations(), consumer);
    }

    protected void randomIndexes(Repeats repeats, Count count, Consumer<Integer> consumer)
    {
        randomIndexes(repeats, count, count.asInt(), consumer);
    }

    protected void randomIndexes(Repeats repeats, Count count, int maximum,
                                 Consumer<Integer> consumer)
    {
        var indexes = randomIntList(repeats, count, 0, maximum);
        Collections.shuffle(indexes);
        indexes.forEach(consumer);
    }

    protected int randomInt(int minimum, int maximum)
    {
        return randomValueFactory().newInt(minimum, maximum);
    }

    protected int randomInt()
    {
        return randomValueFactory().newInt();
    }

    protected int randomInt(int minimum, int maximum, Predicate<Integer> filter)
    {
        return randomValueFactory().newInt(minimum, maximum, filter);
    }

    protected List<Integer> randomIntList(Repeats repeats)
    {
        var values = new ArrayList<Integer>();
        randomInts(repeats, values::add);
        return values;
    }

    protected List<Integer> randomIntList(Repeats repeats, int minimum, int maximum)
    {
        var values = new ArrayList<Integer>();
        randomInts(repeats, minimum, maximum, null, values::add);
        return values;
    }

    protected List<Integer> randomIntList(Repeats repeats, Count count, int minimum,
                                          int maximum)
    {
        var values = new ArrayList<Integer>();
        randomInts(repeats, count, minimum, maximum, null, values::add);
        return values;
    }

    protected void randomInts(Repeats repeats, Consumer<Integer> consumer)
    {
        randomInts(repeats, iterations, consumer);
    }

    protected void randomInts(Repeats repeats, Predicate<Integer> filter, Consumer<Integer> consumer)
    {
        randomInts(repeats, iterations, Integer.MIN_VALUE + 2, Integer.MAX_VALUE, filter, consumer);
    }

    protected void randomInts(Repeats repeats, Count count, Consumer<Integer> consumer)
    {
        randomInts(repeats, count, Integer.MIN_VALUE + 2, Integer.MAX_VALUE, null, consumer);
    }

    protected void randomInts(Repeats repeats, int minimum, int maximum,
                              Predicate<Integer> filter, Consumer<Integer> consumer)
    {
        randomInts(repeats, iterations, minimum, maximum, filter, consumer);
    }

    protected void randomInts(Repeats repeats, Count count, int minimum, int maximum,
                              Predicate<Integer> filter, Consumer<Integer> consumer)
    {
        var range = (long) maximum - (long) minimum;

        assert maximum > minimum;
        assert count.get() <= range : "Count is " + count + " but maximum of " + maximum + " - " + minimum + " = " + range;

        // If we're to allowing repeats,
        if (repeats == Repeats.NO_REPEATS)
        {
            var values = new HashSet<Integer>();

            // and we're trying to get values for less than 80% of the range,
            if (100L * count.asInt() / range < 80)
            {
                // just fill the set randomly until it's big enough,
                while (values.size() < count.asInt())
                {
                    values.add(randomInt(minimum, maximum, filter));
                }
            }
            else
            {
                // but if we're trying to fill most or all of the range with no repeats
                // it might take too long to do that randomly, so we keep a list of choices
                // (this is only going to work for relatively small ranges due to memory),
                var choices = new LinkedList<Integer>();
                for (var i = minimum; i < maximum; i++)
                {
                    choices.add(i);
                }

                // and while we don't have enough values,
                while (values.size() < count.asInt())
                {
                    // we pick a random index in the remaining choices list
                    var index = choices.size() <= 1 ? 0 : randomInt(0, choices.size() - 1, filter);

                    // and add the chosen value to the set
                    values.add(choices.get(index));

                    // before removing it from the choices,
                    choices.remove(index);
                }
            }

            // until finally, we call the consumer with each value.
            for (var value : values)
            {
                consumer.accept(value);
            }
        }
        else
        {
            // otherwise we are allowing repeats, so things are simple.
            count.loop(() -> consumer.accept(randomInt(minimum, maximum, filter)));
        }
    }

    protected List<Long> randomLongList(Repeats repeats)
    {
        var values = new ArrayList<Long>();
        randomLongs(repeats, values::add);
        return values;
    }

    protected List<Long> randomLongList(Repeats repeats, long minimum, long maximum)
    {
        var values = new ArrayList<Long>();
        randomLongs(repeats, minimum, maximum, null, values::add);
        return values;
    }

    protected void randomLongs(Repeats repeats, Predicate<Long> filter, Consumer<Long> consumer)
    {
        randomLongs(repeats, iterations, Long.MIN_VALUE + 2, Long.MAX_VALUE, filter, consumer);
    }

    protected void randomLongs(Repeats repeats, Consumer<Long> consumer)
    {
        randomLongs(repeats, iterations, consumer);
    }

    protected void randomLongs(Repeats repeats, Count count, Consumer<Long> consumer)
    {
        randomLongs(repeats, count, Long.MIN_VALUE + 2, Long.MAX_VALUE, null, consumer);
    }

    protected void randomLongs(Repeats repeats, long minimum, long maximum,
                               Predicate<Long> filter, Consumer<Long> consumer)
    {
        randomLongs(repeats, iterations, minimum, maximum, filter, consumer);
    }

    protected void randomLongs(Repeats repeats, Count count, long minimum, long maximum,
                               Predicate<Long> filter, Consumer<Long> consumer)
    {
        assert maximum > minimum;

        // Computed the range, handling overflow (well enough for our tests)
        var range = Math.max(Long.MAX_VALUE, maximum - minimum);

        assert count.get() <= range : "Count is " + count + " but maximum of " + maximum + " - " + minimum + " = " + range;

        // If we're to allowing repeats,
        if (repeats == Repeats.NO_REPEATS)
        {
            var values = new HashSet<Long>();

            // and we're trying to get values for less than 80% of the range,
            if (100L * count.asInt() / range < 80)
            {
                // just fill the set randomly until it's big enough,
                while (values.size() < count.asInt())
                {
                    values.add(randomValueFactory().newLong(minimum, maximum, filter));
                }
            }
            else
            {
                // but if we're trying to fill most or all of the range with no repeats
                // it might take too long to do that randomly, so we keep a list of choices
                // (this is only going to work for relatively small ranges due to memory),
                var choices = new LinkedList<Long>();
                for (var i = minimum; i < maximum; i++)
                {
                    choices.add(i);
                }

                // and while we don't have enough values,
                while (values.size() < count.asInt())
                {
                    // we pick a random index in the remaining choices list
                    var index = choices.size() <= 1 ? 0 : randomInt(0, choices.size() - 1);

                    // and add the chosen value to the set
                    values.add(choices.get(index));

                    // before removing it from the choices,
                    choices.remove(index);
                }
            }

            // until finally, we call the consumer with each value.
            for (var value : values)
            {
                consumer.accept(value);
            }
        }
        else
        {
            // otherwise we are allowing repeats, so things are simple.
            count.loop(() -> consumer.accept(randomValueFactory().newLong(minimum, maximum, filter)));
        }
    }

    protected List<Short> randomShortList(Repeats repeats)
    {
        var values = new ArrayList<Short>();
        randomShorts(repeats, values::add);
        return values;
    }

    protected List<Short> randomShortList(Repeats repeats, short minimum, short maximum)
    {
        var values = new ArrayList<Short>();
        randomShorts(repeats, minimum, maximum, null, values::add);
        return values;
    }

    protected void randomShorts(Repeats repeats, Predicate<Short> filter, Consumer<Short> consumer)
    {
        randomShorts(repeats, iterations, (short) (Short.MIN_VALUE + 2), Short.MAX_VALUE, filter, consumer);
    }

    protected void randomShorts(Repeats repeats, Consumer<Short> consumer)
    {
        randomShorts(repeats, iterations, consumer);
    }

    protected void randomShorts(Repeats repeats, Count count, Consumer<Short> consumer)
    {
        randomShorts(repeats, count, (short) (Short.MIN_VALUE + 2), Short.MAX_VALUE, null, consumer);
    }

    protected void randomShorts(Repeats repeats, short minimum, short maximum,
                                Predicate<Short> filter, Consumer<Short> consumer)
    {
        randomShorts(repeats, iterations, minimum, maximum, filter, consumer);
    }

    protected void randomShorts(Repeats repeats, Count count, short minimum, short maximum,
                                Predicate<Short> filter, Consumer<Short> consumer)
    {
        if (repeats == Repeats.NO_REPEATS)
        {
            var values = new HashSet<Short>();
            while (values.size() < count.minimum(Count.count(Short.MAX_VALUE)).asInt())
            {
                var value = randomValueFactory().newShort(minimum, maximum, filter);
                values.add(value);
            }
            for (var value : values)
            {
                consumer.accept(value);
            }
        }
        else
        {
            count.loop(() -> consumer.accept(randomValueFactory().newShort(minimum, maximum, filter)));
        }
    }

    protected RandomValueFactory randomValueFactory()
    {
        return newRandomValueFactory(RandomValueFactory::new);
    }

    protected void resetIndex()
    {
        index.set(0);
    }
}
