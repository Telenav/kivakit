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
import com.telenav.kivakit.kernel.interfaces.code.Loopable;
import com.telenav.kivakit.kernel.interfaces.value.Source;
import com.telenav.kivakit.kernel.language.objects.Objects;
import com.telenav.kivakit.kernel.language.primitives.Booleans;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.language.vm.OperatingSystem;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.listeners.ConsoleWriter;
import com.telenav.kivakit.test.project.lexakai.diagrams.DiagramTest;
import com.telenav.kivakit.test.random.RandomValueFactory;
import com.telenav.kivakit.test.reporters.JUnitValidationReporter;
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
@UmlRelation(label = "reports validation failures with", referent = JUnitValidationReporter.class)
public abstract class UnitTest extends TestWatcher implements Listener
{
    private static boolean quickTest;

    @BeforeClass
    public static void testSetup()
    {
        quickTest = Booleans.isTrue(System.getProperty("testQuick"));
        ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
        Ensure.reporterFactory(messageType -> new JUnitValidationReporter());
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

    public boolean isRandomTest()
    {
        return randomValueFactory.get() != null;
    }

    @Override
    public void onMessage(final Message message)
    {
        console.receive(message);
    }

    @Before
    public void testBeforeUnitTest()
    {
    }

    protected boolean ensure(final boolean condition)
    {
        return Ensure.ensure(condition);
    }

    protected void ensure(final boolean condition, final String message, final Object... arguments)
    {
        Ensure.ensure(condition, message, arguments);
    }

    protected void ensureBetween(final double actual, final double low, final double high)
    {
        ensure(low < high, "The low boundary $ is higher than the high boundary $", low, high);
        if (actual < low || actual > high)
        {
            fail("Value $ is not between $ and $", actual, low, high);
        }
    }

    protected void ensureClose(final Number expected, final Number actual, final int numberOfDecimalsToMatch)
    {
        final var roundedExpected = (int) (expected.doubleValue() * Math.pow(10, numberOfDecimalsToMatch))
                / Math.pow(10, numberOfDecimalsToMatch);
        final var roundedActual = (int) (actual.doubleValue() * Math.pow(10, numberOfDecimalsToMatch))
                / Math.pow(10, numberOfDecimalsToMatch);
        ensureWithin(roundedExpected, roundedActual, 0.0);
    }

    @SuppressWarnings("UnusedReturnValue")
    protected boolean ensureClose(final Duration given, final Duration expected)
    {
        return given.isApproximately(expected, Duration.seconds(0.5));
    }

    protected <T> void ensureEqual(final T given, final T expected)
    {
        Ensure.ensureEqual(given, expected);
    }

    protected <T> void ensureEqual(final T given, final T expected, final String message, final Object... arguments)
    {
        Ensure.ensureEqual(given, expected, message, arguments);
    }

    protected void ensureEqualArray(final byte[] a, final byte[] b)
    {
        ensure(Arrays.equals(a, b));
        ensure(Arrays.equals(b, a));
    }

    protected <T> void ensureEqualArray(final T[] a, final T[] b)
    {
        ensure(Arrays.equals(a, b));
        ensure(Arrays.equals(b, a));
    }

    protected void ensureFalse(final boolean condition)
    {
        Ensure.ensure(!condition);
    }

    protected void ensureFalse(final boolean condition, final String message, final Object... arguments)
    {
        Ensure.ensure(!condition, message, arguments);
    }

    protected <T> void ensureNotEqual(final T a, final T b)
    {
        ensureFalse(Objects.equal(a, b), a + " should not equal " + b);
        ensureFalse(Objects.equal(b, a), a + " should not equal " + b);
    }

    protected <T> T ensureNotNull(final T object)
    {
        return Ensure.ensureNotNull(object);
    }

    protected void ensureNull(final Object object)
    {
        ensure(object == null);
    }

    @SuppressWarnings("MaskedAssertion")
    protected void ensureThrows(final Runnable code)
    {
        try
        {
            code.run();
            fail("Code should have thrown exception");
        }
        catch (final AssertionError e)
        {
            // Expected
        }
    }

    protected void ensureWithin(final double expected, final double actual, final double maximumDifference)
    {
        final var difference = Math.abs(expected - actual);
        if (difference > maximumDifference)
        {
            fail("Expected value $ was not within $ of actual value $", expected, maximumDifference, actual);
        }
    }

    protected void ensureZero(final Number value)
    {
        ensure(value.doubleValue() == 0.0);
    }

    protected void fail(final String message, final Object... arguments)
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

    protected void iterateBytes(final Consumer<Byte> consumer)
    {
        iterations.forEachByte(consumer);
    }

    protected void iterateIndexes(final Consumer<Integer> consumer)
    {
        iterations.forEachInteger(consumer);
    }

    protected void iterateIntegers(final Consumer<Integer> consumer)
    {
        iterations.forEachInteger(consumer);
    }

    protected void iterateLongs(final Consumer<Long> consumer)
    {
        iterations.forEachLong(consumer);
    }

    protected void iterateShorts(final Consumer<Short> consumer)
    {
        iterations.forEachShort(consumer);
    }

    protected void iterations(final Count iterations)
    {
        this.iterations = iterations;
    }

    protected void iterations(final int iterations)
    {
        iterations(Count.count(iterations));
    }

    protected Count iterations()
    {
        return iterations;
    }

    protected void loop(final Runnable code)
    {
        iterations.loop(code);
    }

    protected void loop(final Loopable code)
    {
        iterations.loop(code);
    }

    protected void loop(final int minimum, final int maximum, final Runnable code)
    {
        loop(randomInt(minimum, maximum), code);
    }

    protected void loop(final int minimum, final int maximum, final Loopable code)
    {
        loop(randomInt(minimum, maximum), code);
    }

    protected void loop(final int times, final Runnable code)
    {
        for (var i = 0; i < times; i++)
        {
            code.run();
        }
    }

    protected void loop(final int times, final Loopable code)
    {
        for (var iteration = 0; iteration < times; iteration++)
        {
            code.iteration(iteration);
        }
    }

    @SuppressWarnings("unchecked")
    protected final <T extends RandomValueFactory> T newRandomValueFactory(final Source<T> factory)
    {
        if (randomValueFactory.get() == null)
        {
            randomValueFactory.set(factory.get());
        }
        return (T) randomValueFactory.get();
    }

    protected int nextIndex()
    {
        final var next = index.get();
        index.set(next + 1);
        return next;
    }

    protected char randomAsciiChar()
    {
        return randomValueFactory().newAsciiChar();
    }

    protected String randomAsciiString(final int minimum, final int maximum)
    {
        return randomValueFactory().newAsciiString(minimum, maximum);
    }

    protected String randomAsciiString()
    {
        return randomValueFactory().newAsciiString();
    }

    protected List<Byte> randomByteList(final Repeats repeats)
    {
        final var values = new ArrayList<Byte>();
        randomBytes(repeats, values::add);
        return values;
    }

    protected List<Byte> randomByteList(final Repeats repeats, final byte minimum, final byte maximum)
    {
        final var values = new ArrayList<Byte>();
        randomBytes(repeats, minimum, maximum, null, values::add);
        return values;
    }

    protected void randomBytes(final Repeats repeats, final Consumer<Byte> consumer)
    {
        randomBytes(repeats, iterations, consumer);
    }

    protected void randomBytes(final Repeats repeats, final Count count, final Consumer<Byte> consumer)
    {
        randomBytes(repeats, count, (byte) (Byte.MIN_VALUE + 2), Byte.MAX_VALUE, null, consumer);
    }

    protected void randomBytes(final Repeats repeats, final byte minimum, final byte maximum,
                               final Predicate<Byte> filter, final Consumer<Byte> consumer)
    {
        randomBytes(repeats, iterations, minimum, maximum, filter, consumer);
    }

    protected void randomBytes(final Repeats repeats, final Count count, final byte minimum, final byte maximum,
                               final Predicate<Byte> filter, final Consumer<Byte> consumer)
    {
        if (repeats == Repeats.NO_REPEATS)
        {
            final var values = new HashSet<Byte>();
            while (values.size() < count.minimum(Count.count(250)).asInt())
            {
                final var value = randomValueFactory().newByte(minimum, maximum, filter);
                values.add(value);
            }
            for (final var value : values)
            {
                consumer.accept(value);
            }
        }
        else
        {
            count.loop(() -> consumer.accept(randomValueFactory().newByte(minimum, maximum, filter)));
        }
    }

    protected void randomBytes(final Repeats repeats, final Predicate<Byte> filter, final Consumer<Byte> consumer)
    {
        randomBytes(repeats, iterations, (byte) (Byte.MIN_VALUE + 2), Byte.MAX_VALUE, filter, consumer);
    }

    protected int randomIndex()
    {
        return randomValueFactory().newIndex(iterations().asInt());
    }

    protected void randomIndexes(final Repeats repeats, final Consumer<Integer> consumer)
    {
        randomIndexes(repeats, iterations(), consumer);
    }

    protected void randomIndexes(final Repeats repeats, final Count count, final Consumer<Integer> consumer)
    {
        randomIndexes(repeats, count, count.asInt(), consumer);
    }

    protected void randomIndexes(final Repeats repeats, final Count count, final int maximum,
                                 final Consumer<Integer> consumer)
    {
        final var indexes = randomIntList(repeats, count, 0, maximum);
        Collections.shuffle(indexes);
        indexes.forEach(consumer);
    }

    protected int randomInt(final int minimum, final int maximum)
    {
        return randomValueFactory().newInt(minimum, maximum);
    }

    protected int randomInt()
    {
        return randomValueFactory().newInt();
    }

    protected int randomInt(final int minimum, final int maximum, final Predicate<Integer> filter)
    {
        return randomValueFactory().newInt(minimum, maximum, filter);
    }

    protected List<Integer> randomIntList(final Repeats repeats)
    {
        final var values = new ArrayList<Integer>();
        randomInts(repeats, values::add);
        return values;
    }

    protected List<Integer> randomIntList(final Repeats repeats, final int minimum, final int maximum)
    {
        final var values = new ArrayList<Integer>();
        randomInts(repeats, minimum, maximum, null, values::add);
        return values;
    }

    protected List<Integer> randomIntList(final Repeats repeats, final Count count, final int minimum,
                                          final int maximum)
    {
        final var values = new ArrayList<Integer>();
        randomInts(repeats, count, minimum, maximum, null, values::add);
        return values;
    }

    protected void randomInts(final Repeats repeats, final Consumer<Integer> consumer)
    {
        randomInts(repeats, iterations, consumer);
    }

    protected void randomInts(final Repeats repeats, final Predicate<Integer> filter, final Consumer<Integer> consumer)
    {
        randomInts(repeats, iterations, Integer.MIN_VALUE + 2, Integer.MAX_VALUE, filter, consumer);
    }

    protected void randomInts(final Repeats repeats, final Count count, final Consumer<Integer> consumer)
    {
        randomInts(repeats, count, Integer.MIN_VALUE + 2, Integer.MAX_VALUE, null, consumer);
    }

    protected void randomInts(final Repeats repeats, final int minimum, final int maximum,
                              final Predicate<Integer> filter, final Consumer<Integer> consumer)
    {
        randomInts(repeats, iterations, minimum, maximum, filter, consumer);
    }

    protected void randomInts(final Repeats repeats, final Count count, final int minimum, final int maximum,
                              final Predicate<Integer> filter, final Consumer<Integer> consumer)
    {
        final var range = (long) maximum - (long) minimum;

        assert maximum > minimum;
        assert count.get() <= range : "Count is " + count + " but maximum of " + maximum + " - " + minimum + " = " + range;

        // If we're to allowing repeats,
        if (repeats == Repeats.NO_REPEATS)
        {
            final var values = new HashSet<Integer>();

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
                final var choices = new LinkedList<Integer>();
                for (var i = minimum; i < maximum; i++)
                {
                    choices.add(i);
                }

                // and while we don't have enough values,
                while (values.size() < count.asInt())
                {
                    // we pick a random index in the remaining choices list
                    final var index = choices.size() <= 1 ? 0 : randomInt(0, choices.size() - 1, filter);

                    // and add the chosen value to the set
                    values.add(choices.get(index));

                    // before removing it from the choices,
                    choices.remove(index);
                }
            }

            // until finally, we call the consumer with each value.
            for (final var value : values)
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

    protected List<Long> randomLongList(final Repeats repeats)
    {
        final var values = new ArrayList<Long>();
        randomLongs(repeats, values::add);
        return values;
    }

    protected List<Long> randomLongList(final Repeats repeats, final long minimum, final long maximum)
    {
        final var values = new ArrayList<Long>();
        randomLongs(repeats, minimum, maximum, null, values::add);
        return values;
    }

    protected void randomLongs(final Repeats repeats, final Predicate<Long> filter, final Consumer<Long> consumer)
    {
        randomLongs(repeats, iterations, Long.MIN_VALUE + 2, Long.MAX_VALUE, filter, consumer);
    }

    protected void randomLongs(final Repeats repeats, final Consumer<Long> consumer)
    {
        randomLongs(repeats, iterations, consumer);
    }

    protected void randomLongs(final Repeats repeats, final Count count, final Consumer<Long> consumer)
    {
        randomLongs(repeats, count, Long.MIN_VALUE + 2, Long.MAX_VALUE, null, consumer);
    }

    protected void randomLongs(final Repeats repeats, final long minimum, final long maximum,
                               final Predicate<Long> filter, final Consumer<Long> consumer)
    {
        randomLongs(repeats, iterations, minimum, maximum, filter, consumer);
    }

    protected void randomLongs(final Repeats repeats, final Count count, final long minimum, final long maximum,
                               final Predicate<Long> filter, final Consumer<Long> consumer)
    {
        assert maximum > minimum;

        // Computed the range, handling overflow (well enough for our tests)
        final var range = Math.max(Long.MAX_VALUE, maximum - minimum);

        assert count.get() <= range : "Count is " + count + " but maximum of " + maximum + " - " + minimum + " = " + range;

        // If we're to allowing repeats,
        if (repeats == Repeats.NO_REPEATS)
        {
            final var values = new HashSet<Long>();

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
                final var choices = new LinkedList<Long>();
                for (var i = minimum; i < maximum; i++)
                {
                    choices.add(i);
                }

                // and while we don't have enough values,
                while (values.size() < count.asInt())
                {
                    // we pick a random index in the remaining choices list
                    final var index = choices.size() <= 1 ? 0 : randomInt(0, choices.size() - 1);

                    // and add the chosen value to the set
                    values.add(choices.get(index));

                    // before removing it from the choices,
                    choices.remove(index);
                }
            }

            // until finally, we call the consumer with each value.
            for (final var value : values)
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

    protected List<Short> randomShortList(final Repeats repeats)
    {
        final var values = new ArrayList<Short>();
        randomShorts(repeats, values::add);
        return values;
    }

    protected List<Short> randomShortList(final Repeats repeats, final short minimum, final short maximum)
    {
        final var values = new ArrayList<Short>();
        randomShorts(repeats, minimum, maximum, null, values::add);
        return values;
    }

    protected void randomShorts(final Repeats repeats, final Predicate<Short> filter, final Consumer<Short> consumer)
    {
        randomShorts(repeats, iterations, (short) (Short.MIN_VALUE + 2), Short.MAX_VALUE, filter, consumer);
    }

    protected void randomShorts(final Repeats repeats, final Consumer<Short> consumer)
    {
        randomShorts(repeats, iterations, consumer);
    }

    protected void randomShorts(final Repeats repeats, final Count count, final Consumer<Short> consumer)
    {
        randomShorts(repeats, count, (short) (Short.MIN_VALUE + 2), Short.MAX_VALUE, null, consumer);
    }

    protected void randomShorts(final Repeats repeats, final short minimum, final short maximum,
                                final Predicate<Short> filter, final Consumer<Short> consumer)
    {
        randomShorts(repeats, iterations, minimum, maximum, filter, consumer);
    }

    protected void randomShorts(final Repeats repeats, final Count count, final short minimum, final short maximum,
                                final Predicate<Short> filter, final Consumer<Short> consumer)
    {
        if (repeats == Repeats.NO_REPEATS)
        {
            final var values = new HashSet<Short>();
            while (values.size() < count.minimum(Count.count(Short.MAX_VALUE)).asInt())
            {
                final var value = randomValueFactory().newShort(minimum, maximum, filter);
                values.add(value);
            }
            for (final var value : values)
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
