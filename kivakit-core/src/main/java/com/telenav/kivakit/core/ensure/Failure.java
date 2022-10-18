package com.telenav.kivakit.core.ensure;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.messages.OperationMessage;
import com.telenav.kivakit.interfaces.function.Mapper;

import java.util.HashMap;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.FailureReporter.throwingFailureReporter;
import static com.telenav.kivakit.core.language.Classes.newInstance;
import static com.telenav.kivakit.core.logging.LoggerFactory.newLogger;

/**
 * Used by {@link Ensure} to report failures in a flexible way.
 *
 * <p><b>Reporting Failures</b></p>
 * <p>
 * Different kinds of failure messages are reported in different ways. The report methods use the message class to look
 * up which reporter to use.
 *
 * <ul>
 *     <li>{@link #report(Class, String, Object...)}</li>
 *     <li>{@link #report(Class, Throwable e, String, Object...)}</li>
 * </ul>
 *
 * <p><b>Failure Reporters</b></p>
 *
 * <ul>
 *     <li>{@link #reporter(Class, FailureReporter)} - Sets the failure reporter to use for the given message</li>
 *     <li>{@link #reporter(Class)} - Gets the failure reporter to use for the given message type</li>
 *     <li>{@link #reporterFactory(Mapper)}  - Uses the given factory to create failure reporters instead of the reporters registered with {@link #reporter(Class, FailureReporter)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class Failure
{
    /** Logger to receive announcements */
    private static final Logger LOGGER = newLogger();

    /** Creates a {@link FailureReporter} given a message type. The default failure reporter throws an exception */
    public static Mapper<Class<? extends Message>, FailureReporter> reporterFactory =
            ignored -> throwingFailureReporter();

    /** Thread-local map from message type to reporter, useful in reporting different messages differently */
    public static ThreadLocal<Map<Class<? extends Message>, FailureReporter>> reporterMap = ThreadLocal.withInitial(HashMap::new);

    /**
     * Reports a failure of the given type
     *
     * @param type The failure message type
     * @param message The message to format
     * @param arguments Any arguments to the message
     */
    public static void report(Class<? extends Message> type, String message, Object... arguments)
    {
        report(type, null, message, arguments);
    }

    /**
     * Reports a failure of the given type
     *
     * @param type The failure message type
     * @param e Any exception thrown
     * @param message The message to format
     * @param arguments Any arguments to the message
     */
    public static void report(Class<? extends Message> type, Throwable e, String message, Object... arguments)
    {
        var failureMessage = (OperationMessage) newInstance(type);
        failureMessage.cause(e);
        failureMessage.messageForType(message);
        failureMessage.arguments(arguments);
        reporter(type).report(failureMessage);
    }

    /**
     * Gets the failure reporter for a given message type
     *
     * @param type The type of message
     * @return The corresponding failure reporter
     */
    public static FailureReporter reporter(Class<? extends Message> type)
    {
        return reporterMap.get().computeIfAbsent(type, ignored -> reporterFactory.map(type));
    }

    /**
     * Sets the failure reporter for a given message type
     *
     * @param type The type of message
     */
    public static void reporter(Class<? extends Message> type, FailureReporter reporter)
    {
        LOGGER.announce("Validation will report ${class} messages with ${class}", type, reporter.getClass());
        reporterMap.get().put(type, reporter);
    }

    /**
     * Changes the factory being used to map message types to validation reporters on a per-thread basis. Note that this
     * method should only be called once at the start of a program. Calling it again can result in unpredictable results
     * because the method clears the thread-local map from message type to reporter and other threads might be using
     * values in the map.
     */
    public static void reporterFactory(Mapper<Class<? extends Message>, FailureReporter> factory)
    {
        // Install the reporter factory
        reporterFactory = factory;

        // and clear the reporter map by replacing it.
        reporterMap = ThreadLocal.withInitial(HashMap::new);
    }
}
