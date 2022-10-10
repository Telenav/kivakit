package com.telenav.kivakit.core.messaging;

import com.telenav.kivakit.core.collections.map.StringMap;
import com.telenav.kivakit.core.messaging.messages.lifecycle.OperationFailed;
import com.telenav.kivakit.core.messaging.messages.lifecycle.OperationHalted;
import com.telenav.kivakit.core.messaging.messages.lifecycle.OperationStarted;
import com.telenav.kivakit.core.messaging.messages.lifecycle.OperationSucceeded;
import com.telenav.kivakit.core.messaging.messages.status.Alert;
import com.telenav.kivakit.core.messaging.messages.status.CriticalAlert;
import com.telenav.kivakit.core.messaging.messages.status.Glitch;
import com.telenav.kivakit.core.messaging.messages.status.Information;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.messages.status.Trace;
import com.telenav.kivakit.core.messaging.messages.status.Warning;
import com.telenav.kivakit.core.messaging.messages.status.activity.Step;

import static com.telenav.kivakit.core.messaging.Listener.throwingListener;

/**
 * Factory methods and parsing for messages.
 *
 * <p><b>Parsing</b></p>
 *
 * <ul>
 *     <li>{@link #parseMessageType(Listener, String)}</li>
 * </ul>
 *
 * <p><b>Factory Methods</b></p>
 *
 * <ul>
 *     <li>{@link #messageForType(Class)}</li>
 *     <li>{@link #messages()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
public class Messages
{
    /** Map from string to message prototype */
    private static StringMap<Message> messagePrototypes;

    /**
     * Gets a message prototype for the given type
     *
     * @param type The type of message
     */
    public static Message messageForType(Class<? extends Message> type)
    {
        return parseMessageType(throwingListener(), type.getSimpleName());
    }

    /**
     * <b>Not public API</b>
     *
     * <p>
     * Returns a map of KivaKit's built-in messages
     * </p>
     */
    public static StringMap<Message> messages()
    {
        if (messagePrototypes == null)
        {
            messagePrototypes = new StringMap<>();
        }
        return messagePrototypes;
    }

    /**
     * Returns a new message instance
     *
     * @param listener The listener to call with any problems
     * @param type The type of message to create
     * @param message The message text
     * @param arguments Formatting arguments
     * @return The message
     */
    public static <MessageType extends Message> MessageType newMessage(Listener listener,
                                                                       Class<MessageType> type,
                                                                       String message,
                                                                       Object[] arguments)
    {
        try
        {
            return type.getConstructor(String.class, Object[].class).newInstance(message, arguments);
        }
        catch (Exception e)
        {
            listener.problem(e, "Unable to create instance: $", type);
            return null;
        }
    }

    /**
     * Parses the given message type
     *
     * @param listener The listener to report errors to
     * @param typeName The message type name
     */
    public static Message parseMessageType(Listener listener, String typeName)
    {
        initialize();

        return listener.problemIfNull(messagePrototypes.get(typeName), "Invalid message name: $", typeName);
    }

    private static void initialize()
    {
        // Pre-populate message prototypes

        // Lifecycle messages
        new OperationStarted();
        new OperationSucceeded();
        new OperationFailed();
        new OperationHalted();

        // Progress messages
        new Step();
        new Alert();
        new CriticalAlert();
        new Information();
        new Problem();
        new Glitch();
        new Trace();
        new Warning();
    }
}
