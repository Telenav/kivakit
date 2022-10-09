package com.telenav.kivakit.core.ensure;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.messaging.Broadcaster;
import com.telenav.kivakit.core.time.Duration;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Trait for {@link Ensure} methods.
 *
 * @author jonathanl (shibo)
 * @see Ensure
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public interface EnsureTrait
{
    default <T> T ensure(Supplier<Boolean> valid, String message, Object... arguments)
    {
        return Ensure.ensure(valid, message, arguments);
    }

    default boolean ensure(boolean condition)
    {
        return Ensure.ensure(condition);
    }

    default <T> T ensure(boolean condition, String message, Object... arguments)
    {
        return Ensure.ensure(condition, message, arguments);
    }

    default <T> T ensure(boolean condition, Throwable e, String message, Object... arguments)
    {
        return Ensure.ensure(condition, e, message, arguments);
    }

    default double ensureBetween(double actual, double low, double high)
    {
        return Ensure.ensureBetween(actual, low, high);
    }

    default long ensureBetweenExclusive(long value, long minimum, long maximum)
    {
        return Ensure.ensureBetweenExclusive(value, minimum, maximum);
    }

    default long ensureBetweenExclusive(long value, long minimum, long maximum, String message, Object... arguments)
    {
        return Ensure.ensureBetweenExclusive(value, minimum, maximum, message, arguments);
    }

    default long ensureBetweenInclusive(long value, long minimum, long maximum, String message, Object... arguments)
    {
        return Ensure.ensureBetweenInclusive(value, minimum, maximum, message, arguments);
    }

    default long ensureBetweenInclusive(long value, long minimum, long maximum)
    {
        return Ensure.ensureBetweenInclusive(value, minimum, maximum);
    }

    default <T extends Broadcaster> void ensureBroadcastsNoProblem(T broadcaster, Consumer<T> code)
    {
        Ensure.ensureBroadcastsNoProblem(broadcaster, code);
    }

    default <T extends Broadcaster> void ensureBroadcastsProblem(T broadcaster, Consumer<T> code)
    {
        Ensure.ensureBroadcastsProblem(broadcaster, code);
    }

    default void ensureClose(Number given, Number expected, int numberOfDecimalsToMatch)
    {
        Ensure.ensureClose(given, expected, numberOfDecimalsToMatch);
    }

    default boolean ensureClose(Duration given, Duration expected)
    {
        return Ensure.ensureClose(given, expected);
    }

    default <T> T ensureEqual(T given, T expected)
    {
        return Ensure.ensureEqual(given, expected);
    }

    default <T> T ensureEqual(T given, T expected, String message, Object... arguments)
    {
        return Ensure.ensureEqual(given, expected, message, arguments);
    }

    default void ensureEqualArray(byte[] a, byte[] b)
    {
        Ensure.ensureEqualArray(a, b);
    }

    default void ensureEqualArray(byte[] a, byte[] b, String message, Object... arguments)
    {
        Ensure.ensureEqualArray(a, b, message, arguments);
    }

    default <T> void ensureEqualArray(T[] a, T[] b)
    {
        Ensure.ensureEqualArray(a, b);
    }

    default boolean ensureFalse(boolean condition)
    {
        return Ensure.ensureFalse(condition);
    }

    default boolean ensureFalse(boolean condition, String message, Object... arguments)
    {
        return Ensure.ensureFalse(condition, message, arguments);
    }

    default void ensureNonZero(Number value)
    {
        Ensure.ensureNonZero(value);
    }

    default <T> T ensureNotEqual(T given, T expected)
    {
        return Ensure.ensureNotEqual(given, expected);
    }

    default <T> T ensureNotEqual(T given, T expected, String message, Object... arguments)
    {
        return Ensure.ensureNotEqual(given, expected, message, arguments);
    }

    default <T> T ensureNotNull(T object)
    {
        return Ensure.ensureNotNull(object);
    }

    default <T> T ensureNotNull(T object, String message, Object... arguments)
    {
        return Ensure.ensureNotNull(object, message, arguments);
    }

    default <T> T ensureNull(T object)
    {
        return Ensure.ensureNull(object);
    }

    default <T> T ensureNull(T object, String message, Object... arguments)
    {
        return Ensure.ensureNull(object, message, arguments);
    }

    default void ensureThrows(Runnable code)
    {
        Ensure.ensureThrows(code);
    }

    default void ensureWithin(double given, double expected, double maximumDifference)
    {
        Ensure.ensureWithin(given, expected, maximumDifference);
    }

    default void ensureZero(Number value)
    {
        Ensure.ensureZero(value);
    }

    default <T> T fail()
    {
        return Ensure.fail();
    }

    default <T> T fail(Throwable e, String message, Object... arguments)
    {
        return Ensure.fail(e, message, arguments);
    }

    default <T> T fail(String message, Object... arguments)
    {
        return Ensure.fail(message, arguments);
    }

    default <T> T illegalArgument(String message, Object... arguments)
    {
        return Ensure.illegalArgument(message, arguments);
    }

    default <T> T illegalState(String message, Object... arguments)
    {
        return Ensure.illegalState(message, arguments);
    }

    default <T> T illegalState(Throwable e, String message, Object... arguments)
    {
        return Ensure.illegalState(e, message, arguments);
    }

    default <T> T unimplemented()
    {
        return Ensure.unimplemented();
    }

    default <T> T unsupported()
    {
        return Ensure.unsupported();
    }

    default <T> T unsupported(String message, Object... arguments)
    {
        return Ensure.unsupported(message, arguments);
    }
}
