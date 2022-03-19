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

package com.telenav.kivakit.core.test;

import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.ensure.Failure;
import com.telenav.kivakit.core.language.primitive.Booleans;
import com.telenav.kivakit.core.language.trait.LanguageTrait;
import com.telenav.kivakit.core.lexakai.DiagramTest;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.LoggerFactory;
import com.telenav.kivakit.core.messaging.Broadcaster;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.messaging.listeners.MessageList;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.repeaters.RepeaterMixin;
import com.telenav.kivakit.core.os.ConsoleWriter;
import com.telenav.kivakit.core.os.OperatingSystem;
import com.telenav.kivakit.core.project.ProjectTrait;
import com.telenav.kivakit.core.registry.RegistryTrait;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.core.value.count.Minimum;
import com.telenav.kivakit.core.vm.JavaTrait;
import com.telenav.kivakit.interfaces.naming.NamedObject;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestWatcher;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
public abstract class UnitTest extends TestWatcher implements
        RepeaterMixin,
        JavaTrait,
        ProjectTrait,
        RegistryTrait,
        LanguageTrait,
        Repeater,
        NamedObject
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

    private final ConsoleWriter console = new ConsoleWriter();

    private final ThreadLocal<RandomValueFactory> randomValueFactory = ThreadLocal.withInitial(this::newRandomValueFactory);

    public int index;

    public UnitTest()
    {
        LOGGER.listenTo(this);
    }

    public Count count(long value)
    {
        return Count.count(value);
    }

    public <T> T ensure(Supplier<Boolean> valid, String message, Object... arguments)
    {
        return ensure(valid.get(), message, arguments);
    }

    public <T> T ensure(boolean condition, Throwable e, String message, Object... arguments)
    {
        return Ensure.ensure(condition, e, message, arguments);
    }

    public boolean ensure(boolean condition)
    {
        return Ensure.ensure(condition);
    }

    public <T> T ensure(boolean condition, String message, Object... arguments)
    {
        return Ensure.ensure(condition, message, arguments);
    }

    public double ensureBetween(double actual, double low, double high)
    {
        return Ensure.ensureBetween(actual, low, high);
    }

    public long ensureBetweenExclusive(long value, long minimum, long maximum)
    {
        return Ensure.ensureBetweenExclusive(value, minimum, maximum);
    }

    public long ensureBetweenExclusive(long value, long minimum, long maximum, String message, Object... arguments)
    {
        return Ensure.ensureBetweenExclusive(value, minimum, maximum, message, arguments);
    }

    public long ensureBetweenInclusive(long value, long minimum, long maximum, String message, Object... arguments)
    {
        return Ensure.ensureBetweenInclusive(value, minimum, maximum, message, arguments);
    }

    public long ensureBetweenInclusive(long value, long minimum, long maximum)
    {
        return Ensure.ensureBetweenInclusive(value, minimum, maximum);
    }

    public <T extends Broadcaster> void ensureBroadcastsNoProblem(T broadcaster, Consumer<T> code)
    {
        var messages = new MessageList();
        messages.listenTo(broadcaster);
        code.accept(broadcaster);
        ensure(messages.count(Problem.class).equals(Count._0));
    }

    public <T extends Broadcaster> void ensureBroadcastsProblem(T broadcaster, Consumer<T> code)
    {
        var messages = new MessageList();
        messages.listenTo(broadcaster);
        code.accept(broadcaster);
        ensure(messages.count(Problem.class).equals(Count._1));
    }

    public void ensureClose(Number expected, Number actual, int numberOfDecimalsToMatch)
    {
        var roundedExpected = (int) (expected.doubleValue() * Math.pow(10, numberOfDecimalsToMatch))
                / Math.pow(10, numberOfDecimalsToMatch);
        var roundedActual = (int) (actual.doubleValue() * Math.pow(10, numberOfDecimalsToMatch))
                / Math.pow(10, numberOfDecimalsToMatch);
        ensureWithin(roundedExpected, roundedActual, 1E-3);
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean ensureClose(Duration given, Duration expected)
    {
        return given.isApproximately(expected, Duration.seconds(0.5));
    }

    public <T> void ensureEqual(T given, T expected)
    {
        Ensure.ensureEqual(given, expected);
    }

    public <T> void ensureEqual(T given, T expected, String message, Object... arguments)
    {
        Ensure.ensureEqual(given, expected, message, arguments);
    }

    public void ensureEqualArray(byte[] a, byte[] b)
    {
        ensure(Arrays.equals(a, b));
        ensure(Arrays.equals(b, a));
    }

    public <T> void ensureEqualArray(T[] a, T[] b)
    {
        ensure(Arrays.equals(a, b));
        ensure(Arrays.equals(b, a));
    }

    public boolean ensureFalse(boolean condition)
    {
        return Ensure.ensureFalse(condition);
    }

    public void ensureFalse(boolean condition, String message, Object... arguments)
    {
        Ensure.ensureFalse(condition, message, arguments);
    }

    public <T> T ensureNotEqual(T given, T expected)
    {
        return Ensure.ensureNotEqual(given, expected);
    }

    public <T> T ensureNotNull(T object)
    {
        return Ensure.ensureNotNull(object);
    }

    public <T> T ensureNull(T object, String message, Object... arguments)
    {
        return Ensure.ensureNull(object, message, arguments);
    }

    public <T> T ensureNull(T object)
    {
        return Ensure.ensureNull(object);
    }

    public void ensureThrows(Runnable code)
    {
        var threw = false;
        try
        {
            code.run();
        }
        catch (AssertionError | Exception ignored)
        {
            threw = true;
        }

        if (!threw)
        {
            fail("Code should have thrown exception");
        }
    }

    public void ensureWithin(double expected, double actual, double maximumDifference)
    {
        Ensure.ensureWithin(expected, actual, maximumDifference);
    }

    public void ensureZero(Number value)
    {
        Ensure.ensureZero(value);
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

    @Override
    public <T> T register(final T object)
    {
        return RegistryTrait.super.register(object);
    }

    @Before
    public void testBeforeUnitTest()
    {
    }

    protected void fail(String message, Object... arguments)
    {
        Ensure.fail(message, arguments);
    }

    protected boolean isMac()
    {
        return OperatingSystem.get().isMac();
    }

    protected boolean isQuickTest()
    {
        return quickTest;
    }

    protected boolean isWindows()
    {
        return OperatingSystem.get().isWindows();
    }

    protected Maximum maximum(long minimum)
    {
        return Maximum.maximum(minimum);
    }

    protected Minimum minimum(long minimum)
    {
        return Minimum.minimum(minimum);
    }

    protected RandomValueFactory newRandomValueFactory()
    {
        return new RandomValueFactory();
    }

    protected RandomValueFactory random()
    {
        return randomValueFactory.get();
    }
}
