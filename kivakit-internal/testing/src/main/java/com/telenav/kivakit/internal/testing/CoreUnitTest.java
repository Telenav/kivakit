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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.ensure.EnsureTrait;
import com.telenav.kivakit.core.ensure.Failure;
import com.telenav.kivakit.core.function.ResultTrait;
import com.telenav.kivakit.core.language.primitive.Booleans;
import com.telenav.kivakit.core.language.trait.LanguageTrait;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.LoggerFactory;
import com.telenav.kivakit.core.messaging.Broadcaster;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.messaging.repeaters.RepeaterMixin;
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
import com.telenav.kivakit.internal.testing.internal.lexakai.DiagramTest;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestWatcher;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.telenav.kivakit.annotations.code.ApiStability.UNSTABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.messaging.Listener.emptyListener;
import static com.telenav.kivakit.core.os.Console.console;
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
@ApiQuality(stability = UNSTABLE,
            testing = TESTING_NOT_NEEDED,
            documentation = FULLY_DOCUMENTED)
public abstract class CoreUnitTest extends TestWatcher implements
        RepeaterMixin,
        ResultTrait,
        JavaTrait,
        ProjectTrait,
        EnsureTrait,
        RegistryTrait,
        LanguageTrait,
        Repeater,
        NamedObject
{
    /** True if this is a quick test */
    private static boolean quickTest;

    /** Logger for test output */
    private static final Logger LOGGER = LoggerFactory.newLogger();

    @BeforeClass
    public static void testSetup()
    {
        quickTest = Booleans.isTrue(System.getProperty("testQuick"));
        ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
        Failure.reporterFactory(messageType -> new JUnitFailureReporter());
    }

    /** Watches for unit test failures and reports the random value factory seed value so failed tests can be reproduced */
    @Rule
    public UnitTestWatcher watcher = new UnitTestWatcher(this);

    private final ThreadLocal<RandomValueFactory> randomValueFactory = ThreadLocal.withInitial(this::newRandomValueFactory);

    /** An index variable for use by subclasses */
    protected int index;

    protected CoreUnitTest()
    {
        LOGGER.listenTo(this);
    }

    @Override
    public void onMessage(Message message)
    {
        console().receive(message);
    }

    @Override
    public <T> T register(T object)
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

    /**
     * Initialized the given project
     */
    protected <T extends Project> void initializeProject(Class<T> project)
    {
        emptyListener().listenTo(resolveProject(project)).initialize();
    }

    /**
     * Returns true if this is running on a mac
     */
    protected boolean isMac()
    {
        return OperatingSystem.operatingSystem().isMac();
    }

    /**
     * Returns true if this is a quick test
     */
    protected boolean isQuickTest()
    {
        return quickTest;
    }

    /**
     * Returns true if this is a randomized test
     */
    protected boolean isRandomTest()
    {
        return randomValueFactory.get() != null;
    }

    /**
     * Returns true if this is running on UNIX (other than MacOS)
     */
    protected boolean isUnix()
    {
        return OperatingSystem.operatingSystem().isUnix();
    }

    /**
     * Returns true if this is running on Windows
     */
    protected boolean isWindows()
    {
        return OperatingSystem.operatingSystem().isWindows();
    }

    protected Maximum maximum(long maximum)
    {
        return Maximum.maximum(maximum);
    }

    protected Minimum minimum(long minimum)
    {
        return Minimum.minimum(minimum);
    }

    protected RandomValueFactory newRandomValueFactory()
    {
        return new RandomValueFactory();
    }

    /**
     * Gets a random value factory (or subclass)
     */
    protected RandomValueFactory random()
    {
        return randomValueFactory.get();
    }

    /**
     * Returns a random number of iterations
     */
    protected Count randomIterations()
    {
        return random().iterations();
    }
}
