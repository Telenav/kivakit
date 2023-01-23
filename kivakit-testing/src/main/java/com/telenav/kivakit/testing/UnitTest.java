package com.telenav.kivakit.testing;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.language.trait.LanguageTrait;
import com.telenav.kivakit.core.messaging.Broadcaster;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.os.ConsoleTrait;
import com.telenav.kivakit.core.project.ProjectTrait;
import com.telenav.kivakit.core.registry.RegistryTrait;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.vm.JavaTrait;
import com.telenav.kivakit.interfaces.naming.NamedObject;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import com.telenav.kivakit.internal.testing.RandomValueFactory;
import com.telenav.kivakit.resource.packages.PackageTrait;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

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
 *     <li>{@link #random()} - Retrieves a {@link RandomValueFactory} for this test</li>
 *     <li>{@link #initializeProject(Class)}</li>
 *     <li>{@link #isRandomTest()} - True if this is a random </li>
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
@TypeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public class UnitTest extends CoreUnitTest implements
    JavaTrait,
    ProjectTrait,
    PackageTrait,
    RegistryTrait,
    LanguageTrait,
    Repeater,
    NamedObject,
    ConsoleTrait
{
}
