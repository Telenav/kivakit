package com.telenav.kivakit.core.ensure;

import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.messaging.messages.status.Unsupported;
import com.telenav.kivakit.core.messaging.messages.status.Warning;
import com.telenav.kivakit.core.project.lexakai.DiagramEnsure;
import com.telenav.kivakit.core.string.Formatter;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * A class for providing flexibility and consistency in the checking of states and parameters. There are multiple kinds
 * of validation tools available in the Java environment, including manual checks, exceptions, assertions, loggers,
 * Objects.require(), JUnit assertions and others. This class provides a consistent, abstracted interface to this
 * functionality, such that all messages are formatted using {@link Formatter} and the kind of failure reporting can be
 * changed at runtime for different classes of failures.
 *
 * <p>
 * An application can specify what kind of reporting it wants by calling {@link Failure#reporter(Class,
 * FailureReporter)}, passing in the class of failure and the {@link FailureReporter} that should be used to report it.
 * </p>
 *
 * <p><b>Failure Types</b></p>
 *
 * <ul>
 *     <li><b>{@link Failure}</b> - A generic failure message reported by {@link #fail(String, Object...)}</li>
 *     <li><b>{@link EnsureProblem}</b> - An ensure failure reported by one of the ensure() methods</li>
 *     <li><b>{@link Unsupported}</b> - An unsupported operation reported by {@link #unsupported(String, Object...)}</li>
 * </ul>
 *
 * <p><b>Validation Reporters</b></p>
 *
 * <p>
 * The {@link FailureReporter} methods used to report {@link Ensure} failures:
 * <ul>
 *     <li><b>{@link FailureReporter#asserting()}</b> - Fails with a Java assertion</li>
 *     <li><b>{@link FailureReporter#logging()}</b> - Logs the failure</li>
 *     <li><b>{@link FailureReporter#none()}</b> - Does nothing</li>
 *     <li><b>{@link FailureReporter#throwing()}</b> - Throws an exception</li>
 * </ul>
 *
 * <p><b>Default Reporting</b></p>
 *
 * <p>
 * All failure types are linked to the {@link FailureReporter} by default.
 * </p>
 *
 * <p><b>Ensure Methods</b></p>
 *
 * <p>
 * The method {@link #ensure(boolean, String, Object...)} will report an {@link EnsureProblem} if the given boolean
 * condition value is false. Other convenience methods include:
 * <ul>
 *     <li>{@link #ensure(boolean, Throwable, String, Object...)}</li>
 *     <li>{@link #ensure(boolean)}</li>
 *     <li>{@link #ensure(boolean, String, Object...)}</li>
 *     <li>{@link #ensure(Supplier, String, Object...)}</li>
 *     <li>{@link #ensureNotNull(Object)}</li>
 *     <li>{@link #ensureNotNull(Object, String, Object...)}</li>
 *     <li>{@link #ensureEqual(Object, Object)}</li>
 *     <li>{@link #ensureEqual(Object, Object, String, Object...)}</li>
 * </ul>
 *
 * <p><i>Examples:</i></p>
 * <pre>
 * ensure(x &lt; 100);
 * ensure(x &lt; 100, "The value $ is not less than 100", x);
 * ensure(() -&gt; computeX() &lt; 100);
 * ensure(() -&gt; isValid(), "$ is not valid", name());
 * ensureEqual(a, b, "Point $ is not equal to point $", a, b);
 * </pre>
 *
 * <p><b>Fail Methods</b></p>
 *
 * <p>
 * Methods are provided to report outright failures with a {@link Failure} message:
 * <ul>
 *     <li>{@link #fail()} - Fails with a generic message</li>
 *     <li>{@link #fail(String, Object...)} - Fails with the formatted message</li>
 *     <li>{@link #fail(Throwable, String, Object...)} - Fails with the formatted message</li>
 * </ul>
 *
 * <p><b>Unsupported Methods</b></p>
 *
 * <p>
 * Unsupported operations are reported with an {@link Unsupported} message:
 * <ul>
 *     <li>{@link #unsupported()} - Reports an unsupported operation with an {@link Unsupported} message</li>
 *     <li>{@link #unsupported(String, Object...)} - Reports an unsupported operation with an {@link Unsupported} message</li>
 * </ul>
 *
 * <p><b>Exception-Throwing Methods</b></p>
 *
 * <p>
 * Methods are also provided to throw two common exceptions:
 * <ul>
 *     <li>{@link #illegalState(String, Object...)} - Throws a formatted {@link IllegalStateException}</li>
 *     <li>{@link #illegalArgument(String, Object...)} - Throws a formatted {@link IllegalArgumentException}</li>
 * </ul>
 *
 * <p><i>Examples:</i></p>
 * <pre>
 * unsupported();
 * illegalArgument("$ should be between $ and $", x, minimum, maximum);
 * illegalState("Must be initialized to add elements");
 * int size() { return unsupported(); }
 * </pre>
 *
 * @author jonathanl (shibo)
 * @see Logger
 * @see Warning
 * @see Formatter
 */
@UmlClassDiagram(diagram = DiagramEnsure.class)
@UmlRelation(label = "reports", referent = EnsureProblem.class)
public class Ensure
{
    /**
     * @see #ensure(boolean, String, Object...)
     */
    public static <T> T ensure(Supplier<Boolean> valid, String message, Object... arguments)
    {
        return ensure(valid.get(), message, arguments);
    }

    /**
     * @see #ensure(boolean, String, Object...)
     */
    public static boolean ensure(boolean condition)
    {
        if (!condition)
        {
            ensure(false, "Ensure failure: the boolean condition is false");
            return false;
        }
        return true;
    }

    /**
     * @see #ensure(boolean, Throwable, String, Object...)
     */
    public static <T> T ensure(boolean condition, String message, Object... arguments)
    {
        return ensure(condition, null, message, arguments);
    }

    /**
     * If the condition is false (the check is invalid), a {@link EnsureProblem} message is given to the {@link
     * FailureReporter} that message type.
     */
    public static <T> T ensure(boolean condition, Throwable e, String message,
                               Object... arguments)
    {
        if (!condition)
        {
            Failure.report(EnsureProblem.class, e, message, arguments);
        }
        return null;
    }

    public static <T> T ensureEqual(T given, T expected)
    {
        return ensureEqual(given, expected, "Values $ and $ are not equal", given, expected);
    }

    /**
     * Ensures the two objects are equals.
     */
    public static <T> T ensureEqual(T given, T expected, String message, Object... arguments)
    {
        // If the given and expected values are non-null,
        if (given != null && expected != null)
        {
            // check that given == expected,
            ensure(Objects.equals(given, expected), message + " (Given " + given + " != expected " + expected + ")", arguments);

            // that expected == given,
            ensure(Objects.equals(expected, given), message + " (Expected " + expected + " != given " + given + ")", arguments);

            // and that the hash codes match
            var givenHashCode = Objects.hashCode(given);
            var expectedHashCode = Objects.hashCode(expected);
            ensure(givenHashCode == expectedHashCode, "${class}: Hash code for $ != $ ", given.getClass(), givenHashCode, expectedHashCode);
        }
        else
        {
            // otherwise, if one value is null the other has to be.
            ensure(given == expected);
        }
        return null;
    }

    /**
     * @see #ensure(boolean, String, Object...)
     */
    public static boolean ensureFalse(boolean condition)
    {
        return Boolean.TRUE.equals(ensure(!condition));
    }

    /**
     * @see #ensure(boolean, String, Object...)
     */
    public static boolean ensureFalse(boolean condition, String message, Object... arguments)
    {
        return Boolean.TRUE.equals(ensure(!condition, message, arguments));
    }

    public static <T> T ensureNotEqual(T given, T expected)
    {
        ensure(!Objects.equals(given, expected));
        return null;
    }

    public static <T> T ensureNotNull(T object)
    {
        return ensureNotNull(object, "Value cannot be null");
    }

    public static <T> T ensureNotNull(T object, String message, Object... arguments)
    {
        ensure(object != null, message, arguments);
        return object;
    }

    public static <T> T ensureNull(T object)
    {
        return ensureNull(object, "Value must be null");
    }

    public static <T> T ensureNull(T object, String message, Object... arguments)
    {
        ensure(object == null, message, arguments);
        return object;
    }

    public static void ensureWithin(double expected, double actual, double maximumDifference)
    {
        var difference = Math.abs(expected - actual);
        if (difference > maximumDifference)
        {
            fail("Expected value $ was not within $ of actual value $", expected, maximumDifference, actual);
        }
    }

    public static <T> T fail()
    {
        return fail("Operation failure");
    }

    public static <T> T fail(Throwable e, String message, Object... arguments)
    {
        return Failure.report(EnsureProblem.class, e, message, arguments);
    }

    public static <T> T fail(String message, Object... arguments)
    {
        fail(null, message, arguments);
        return null;
    }

    public static <T> T illegalArgument(String message, Object... arguments)
    {
        throw new IllegalArgumentException(format(message, arguments));
    }

    public static <T> T illegalState(String message, Object... arguments)
    {
        throw new IllegalStateException(format(message, arguments));
    }

    public static <T> T illegalState(Throwable e, String message, Object... arguments)
    {
        throw new IllegalStateException(format(message, arguments), e);
    }

    public static <T> T unimplemented()
    {
        return fail("Not implemented");
    }

    public static <T> T unsupported()
    {
        return unsupported("Unsupported operation");
    }

    public static <T> T unsupported(String message, Object... arguments)
    {
        Failure.report(Unsupported.class, message, arguments);
        return null;
    }

    public static void warning(String message, Object... arguments)
    {
        System.out.println("Warning: " + Strings.format(message, arguments));
    }

    public static void warning(Throwable throwable, String message, Object... arguments)
    {
        System.out.println("Warning: " + Strings.format(message, arguments) + "\n" + throwable);
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
        ensureWithin(roundedExpected, roundedActual, 0.0);
    }

    private static String format(String message, Object[] arguments)
    {
        return format(null, message, arguments);
    }

    @SuppressWarnings("SameParameterValue")
    private static String format(Throwable e, String message, Object[] arguments)
    {
        if (e != null)
        {
            var argumentsPlus = new Object[arguments.length + 1];
            System.arraycopy(arguments, 0, argumentsPlus, 0, arguments.length);
            argumentsPlus[arguments.length] = e;
            return Strings.format(message + "\n$", argumentsPlus);
        }
        else
        {
            return Strings.format(message, arguments);
        }
    }
}
