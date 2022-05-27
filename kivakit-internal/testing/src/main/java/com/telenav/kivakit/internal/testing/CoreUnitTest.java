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

package com.telenav.kivakit.internal.testing;

import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.ensure.Failure;
import com.telenav.kivakit.core.language.primitive.Booleans;
import com.telenav.kivakit.core.language.trait.LanguageTrait;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.LoggerFactory;
import com.telenav.kivakit.core.messaging.Broadcaster;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.messaging.repeaters.RepeaterMixin;
import com.telenav.kivakit.core.os.ConsoleWriter;
import com.telenav.kivakit.core.os.OperatingSystem;
import com.telenav.kivakit.core.project.Project;
import com.telenav.kivakit.core.project.ProjectTrait;
import com.telenav.kivakit.core.registry.RegistryTrait;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.core.value.count.Minimum;
import com.telenav.kivakit.core.vm.JavaTrait;
import com.telenav.kivakit.interfaces.naming.NamedObject;
import com.telenav.kivakit.internal.testing.lexakai.DiagramTest;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestWatcher;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.telenav.kivakit.core.project.Project.resolveProject;

/**
 * This is the base class for all unit tests. It provides useful methods that are common to all tests. Several ensure*()
 * methods delegate to the {@link Ensure} class to provide easy access to these methods.
 *
 * <p><b>Random Value Testing</b></p>
 *
 * <p>
 * The method {@link #random()} retrieves a {@link RandomValueFactory} for the test, which can be specialized by
 * overriding {@link #newRandomValueFactory()}. Methods in {@link RandomValueFactory} provide support for random value
 * testing. In the event that a randomized test fails the seed number to reproduce the test will be output:
 * </p>
 *
 * <pre>
 * // random().seed(123456789L);</pre>
 *
 * <p>
 * To reproduce the test, insert this code at the start of the test method that failed.
 * </p>
 *
 * <p><b>Quick Tests</b></p>
 *
 * <p>
 * If the system property "testQuick" is set to "true", only unit tests that override {@link #isQuickTest()} to return
 * true will be executed. Alternatively, quick tests can be labeled with the QuickTest annotation.
 *
 * <p><b>Test Methods</b></p>
 *
 * <ul>
 *     <li>{@link #random()}</li>
 *     <li>{@link #initializeProject(Class)}</li>
 *     <li>{@link #isRandomTest()}</li>
 *     <li>{@link #isQuickTest()}</li>
 *     <li>{@link #isMac()}</li>
 *     <li>{@link #isWindows()}</li>
 *     <li>{@link #count(long)}</li>
 *     <li>{@link #count(Collection)}</li>
 *     <li>{@link #maximum(long)}</li>
 *     <li>{@link #minimum(long)}</li>
 *     <li>{@link #register(Object)}</li>
 * </ul>
 *
 * <p><b>Ensure</b></p>
 *
 * <ul>
 *     <li>{@link #ensure(boolean)}</li>
 *     <li>{@link #ensure(boolean, String, Object...)}</li>
 *     <li>{@link #ensure(Supplier, String, Object...)}</li>
 *     <li>{@link #ensure(boolean, Throwable, String, Object...)}</li>
 *     <li>{@link #ensureFalse(boolean)}</li>
 *     <li>{@link #ensureFalse(boolean, String, Object...)}</li>
 *     <li>{@link #fail(String, Object...)}</li>
 * </ul>
 *
 * <p><b>Ensure Null</b></p>
 *
 * <ul>
 *     <li>{@link #ensureNull(Object)}</li>
 *     <li>{@link #ensureNull(Object, String, Object...)}</li>
 *     <li>{@link #ensureNotNull(Object)}</li>
 *     <li>{@link #ensureNotNull(Object, String, Object...)}</li>
 * </ul>
 *
 * <p><b>Ensure Equality</b></p>
 *
 * <ul>
 *     <li>{@link #ensureEqual(Object, Object)}</li>
 *     <li>{@link #ensureEqual(Object, Object, String, Object...)}</li>
 *     <li>{@link #ensureNotEqual(Object, Object)}</li>
 *     <li>{@link #ensureNotEqual(Object, Object, String, Object...)}</li>
 *     <li>{@link #ensureEqualArray(Object[], Object[])}</li>
 *     <li>{@link #ensureEqualArray(byte[], byte[])}</li>
 * </ul>
 *
 *  <p><b>Ensure Behavior</b></p>
 *
 *  <ul>
 *      <li>{@link #ensureBroadcastsProblem(Broadcaster, Consumer)}</li>
 *      <li>{@link #ensureBroadcastsNoProblem(Broadcaster, Consumer)}</li>
 *      <li>{@link #ensureThrows(Runnable)}</li>
 *  </ul>
 *
 * <p><b>Ensure Math</b></p>
 *
 * <ul>
 *     <li>{@link #ensureZero(Number)}</li>
 *     <li>{@link #ensureNonZero(Number)}</li>
 *     <li>{@link #ensureClose(Duration, Duration)}</li>
 *     <li>{@link #ensureClose(Number, Number, int)}</li>
 *     <li>{@link #ensureBetweenExclusive(long, long, long)}</li>
 *     <li>{@link #ensureBetweenInclusive(long, long, long)}</li>
 *     <li>{@link #ensureBetweenInclusive(long, long, long, String, Object...)}</li>
 *     <li>{@link #ensureBetweenExclusive(long, long, long, String, Object...)}</li>
 *     <li>{@link #ensureBetween(double, double, double)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "SameParameterValue", "unused" })
@UmlClassDiagram(diagram = DiagramTest.class)
@UmlRelation(label = "uses", referent = RandomValueFactory.class)
@UmlRelation(label = "reports validation failures with", referent = JUnitFailureReporter.class)
public abstract class CoreUnitTest extends TestWatcher implements
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


    @Rule
    public UnitTestWatcher watcher = new UnitTestWatcher(this);

    private final ConsoleWriter console = new ConsoleWriter();

    private final ThreadLocal<RandomValueFactory> randomValueFactory = ThreadLocal.withInitial(this::newRandomValueFactory);

    protected int index;

    protected CoreUnitTest()
    {
        LOGGER.listenTo(this);
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

    protected Count count(long value)
    {
        return Count.count(value);
    }

    protected Count count(Collection<?> value)
    {
        return Count.count(value);
    }

    protected <T> T ensure(Supplier<Boolean> valid, String message, Object... arguments)
    {
        return ensure(valid.get(), message, arguments);
    }

    protected <T> T ensure(boolean condition, Throwable e, String message, Object... arguments)
    {
        return Ensure.ensure(condition, e, message, arguments);
    }

    protected boolean ensure(boolean condition)
    {
        return Ensure.ensure(condition);
    }

    protected <T> T ensure(boolean condition, String message, Object... arguments)
    {
        return Ensure.ensure(condition, message, arguments);
    }

    protected double ensureBetween(double actual, double low, double high)
    {
        return Ensure.ensureBetween(actual, low, high);
    }

    protected long ensureBetweenExclusive(long value, long minimum, long maximum)
    {
        return Ensure.ensureBetweenExclusive(value, minimum, maximum);
    }

    protected long ensureBetweenExclusive(long value, long minimum, long maximum, String message, Object... arguments)
    {
        return Ensure.ensureBetweenExclusive(value, minimum, maximum, message, arguments);
    }

    protected long ensureBetweenInclusive(long value, long minimum, long maximum, String message, Object... arguments)
    {
        return Ensure.ensureBetweenInclusive(value, minimum, maximum, message, arguments);
    }

    protected long ensureBetweenInclusive(long value, long minimum, long maximum)
    {
        return Ensure.ensureBetweenInclusive(value, minimum, maximum);
    }

    protected <T extends Broadcaster> void ensureBroadcastsNoProblem(T broadcaster, Consumer<T> code)
    {
        Ensure.ensureBroadcastsNoProblem(broadcaster, code);
    }

    protected <T extends Broadcaster> void ensureBroadcastsProblem(T broadcaster, Consumer<T> code)
    {
        Ensure.ensureBroadcastsProblem(broadcaster, code);
    }

    protected void ensureClose(Number expected, Number actual, int numberOfDecimalsToMatch)
    {
        Ensure.ensureClose(expected, actual, numberOfDecimalsToMatch);
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
        Ensure.ensureEqualArray(a, b);
    }

    protected <T> void ensureEqualArray(T[] a, T[] b)
    {
        Ensure.ensureEqualArray(a, b);
    }

    protected boolean ensureFalse(boolean condition)
    {
        return Ensure.ensureFalse(condition);
    }

    protected void ensureFalse(boolean condition, String message, Object... arguments)
    {
        Ensure.ensureFalse(condition, message, arguments);
    }

    protected void ensureNonZero(Number value)
    {
        Ensure.ensureNonZero(value);
    }

    protected <T> T ensureNotEqual(T given, T expected)
    {
        return Ensure.ensureNotEqual(given, expected);
    }

    protected <T> T ensureNotEqual(T given, T expected, String message, Object... objects)
    {
        return Ensure.ensureNotEqual(given, expected, message, objects);
    }

    protected <T> T ensureNotNull(T object, String message, Object... objects)
    {
        return Ensure.ensureNotNull(object, message, objects);
    }

    protected <T> T ensureNotNull(T object)
    {
        return Ensure.ensureNotNull(object);
    }

    protected <T> T ensureNull(T object, String message, Object... arguments)
    {
        return Ensure.ensureNull(object, message, arguments);
    }

    protected <T> T ensureNull(T object)
    {
        return Ensure.ensureNull(object);
    }

    protected void ensureThrows(Runnable code)
    {
        Ensure.ensureThrows(code);
    }

    protected void ensureWithin(double expected, double actual, double maximumDifference)
    {
        Ensure.ensureWithin(expected, actual, maximumDifference);
    }

    protected void ensureZero(Number value)
    {
        Ensure.ensureZero(value);
    }

    protected void fail(String message, Object... arguments)
    {
        Ensure.fail(message, arguments);
    }

    protected <T extends Project> void initializeProject(Class<T> project)
    {

        Listener.emptyListener().listenTo(resolveProject(project)).initialize();
    }

    protected boolean isMac()
    {
        return OperatingSystem.get().isMac();
    }

    protected boolean isQuickTest()
    {
        return quickTest;
    }

    protected boolean isRandomTest()
    {
        return randomValueFactory.get() != null;
    }

    protected boolean isWindows()
    {
        return OperatingSystem.get().isWindows();
    }

    protected Count iterations()
    {
        return random().iterations();
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
