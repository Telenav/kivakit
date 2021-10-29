package com.telenav.kivakit.kernel.data.validation.ensure;

import com.telenav.kivakit.kernel.data.validation.ensure.reporters.ThrowingFailureReporter;
import com.telenav.kivakit.kernel.interfaces.factory.MapFactory;
import com.telenav.kivakit.kernel.language.reflection.Type;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.messages.OperationMessage;

import java.util.HashMap;
import java.util.Map;

public class Failure
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /** Creates a {@link BaseFailureReporter} given a message type */
    public static MapFactory<Class<? extends Message>, FailureReporter> reporterFactory = message ->
    {
        // Initialize everything to throw by default
        return new ThrowingFailureReporter();
    };

    /** Thread-local map from message type to reporter, useful in reporting different messages differently */
    public static ThreadLocal<Map<Class<? extends Message>, FailureReporter>> reporterMap = ThreadLocal.withInitial(HashMap::new);

    public static <T> T report(Class<? extends Message> type,
                               String text,
                               Object... arguments)
    {
        report(type, null, text, arguments);
        return null;
    }

    public static <T> T report(Class<? extends Message> type,
                               Throwable e,
                               String text,
                               Object... arguments)
    {
        var message = (OperationMessage) Type.forClass(type).newInstance();
        message.cause(e);
        message.message(text);
        message.arguments(arguments);
        reporter(type).report(message);
        return null;
    }

    public static FailureReporter reporter(Class<? extends Message> type)
    {
        return reporterMap.get().computeIfAbsent(type, ignored -> reporterFactory.newInstance(type));
    }

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
    public static void reporterFactory(MapFactory<Class<? extends Message>, FailureReporter> factory)
    {
        // Install the reporter factory
        reporterFactory = factory;

        // and clear the reporter map by replacing it.
        reporterMap = ThreadLocal.withInitial(HashMap::new);
    }

    /**
     * Temporarily sets the validation reporter for {@link EnsureFailure} messages to the given reporter (only for the
     * current thread) while the given code is executed.
     */
    public static <T> T withReporter(FailureReporter reporter, Runnable code)
    {
        var reporterMap = Failure.reporterMap.get();
        var originalReporter = reporterMap.get(EnsureFailure.class);
        reporterMap.put(EnsureFailure.class, reporter);
        try
        {
            code.run();
        }
        finally
        {
            reporterMap.put(EnsureFailure.class, originalReporter);
        }
        return null;
    }
}
