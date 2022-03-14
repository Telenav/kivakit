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

package com.telenav.kivakit.core.messaging.messages;

import com.telenav.kivakit.core.collections.map.NameMap;
import com.telenav.kivakit.core.language.Hash;
import com.telenav.kivakit.core.language.Objects;
import com.telenav.kivakit.core.logging.Log;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.context.CodeContext;
import com.telenav.kivakit.core.messaging.context.StackTrace;
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
import com.telenav.kivakit.core.messaging.messages.status.activity.Activity;
import com.telenav.kivakit.core.lexakai.DiagramMessageType;
import com.telenav.kivakit.core.string.Formatter;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.kivakit.core.thread.ReentrancyTracker;
import com.telenav.kivakit.core.time.Frequency;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

import java.util.Arrays;

import static com.telenav.kivakit.core.string.Formatter.Format.WITH_EXCEPTION;
import static com.telenav.kivakit.core.thread.ReentrancyTracker.Reentrancy.REENTERED;

/**
 * Base implementation of the {@link Message} interface. Represents a message destined for a {@link Listener} such as a
 * {@link Logger} with arguments which can be interpolated if the message is formatted with {@link
 * #formatted(Formatter.Format)}. All {@link OperationMessage}s have the attributes defined in {@link Message}.
 * <p>
 * For messages that might be sent to frequently, {@link #maximumFrequency(Frequency)} can be used to specify that the
 * receiver only handle the message every so often. {@link Log}s support this feature, so it is possible to easily tag a
 * message as something to only log once in a while.
 *
 * @see Message
 * @see Log
 * @see Listener
 * @see Frequency
 * @see Severity
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
@UmlExcludeSuperTypes({ Named.class })
public abstract class OperationMessage implements Named, Message
{
    private static NameMap<OperationMessage> messages;

    private static final ReentrancyTracker reentrancy = new ReentrancyTracker();

    /** This flag can be helpful in detecting infinite recursion of message formatting */
    private static final boolean DETECT_REENTRANCY = false;

    public static <T extends Message> T newInstance(Listener listener,
                                                    Class<T> type,
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

    public static Message of(Class<? extends Message> type)
    {
        return parse(Listener.throwing(), type.getSimpleName());
    }

    public static Message parse(Listener listener, String name)
    {
        initialize();

        return listener.problemIfNull(messages.get(name), "Invalid message name: $", name);
    }

    private Object[] arguments;

    private transient Throwable cause;

    private CodeContext context;

    private Time created = Time.now();

    private String formattedMessage;

    private Frequency maximumFrequency;

    private String message;

    private StackTrace stackTrace;

    protected OperationMessage(String message)
    {
        this.message = message;
        messages().add(this);
    }

    protected OperationMessage()
    {
        messages().add(this);
    }

    @Override
    public Object[] arguments()
    {
        return arguments;
    }

    public void arguments(Object[] arguments)
    {
        this.arguments = arguments;
    }

    @Override
    public MessageException asException()
    {
        return new MessageException(this);
    }

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    @Override
    public String asString(Format format)
    {
        switch (format)
        {
            default:
                return formatted(WITH_EXCEPTION);
        }
    }

    @Override
    public final Throwable cause()
    {
        return cause;
    }

    public final OperationMessage cause(Throwable cause)
    {
        this.cause = cause;
        return this;
    }

    @Override
    public CodeContext context()
    {
        return context;
    }

    public void context(CodeContext context)
    {
        if (this.context == null)
        {
            this.context = context;
        }
    }

    @Override
    public Time created()
    {
        return created;
    }

    public void created(Time created)
    {
        this.created = created;
    }

    /**
     * @return The formatted message without any stack trace information
     */
    @Override
    public String description()
    {
        return Strings.format(message, arguments);
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof OperationMessage)
        {
            var that = (OperationMessage) object;
            return Objects.equalPairs(this.getClass(), that.getClass(),
                    this.created, that.created,
                    this.message, that.message,
                    this.stackTrace, that.stackTrace) && Arrays.equals(this.arguments, that.arguments);
        }
        return false;
    }

    /**
     * @return The fully formatted message including stack trace information
     */
    @Override
    public String formatted(Formatter.Format format)
    {
        if (formattedMessage == null)
        {
            try
            {
                if (reentrancy.enter() == REENTERED && DETECT_REENTRANCY)
                {
                    formattedMessage = "Re-entrant message formatting detected. This could result in infinite recursion: '" + message + "'";
                }
                else
                {
                    formattedMessage = Strings.format(message, arguments);
                    if (format == WITH_EXCEPTION)
                    {
                        var cause = cause();
                        if (cause != null)
                        {
                            formattedMessage += "\n" + stackTrace().toString();
                        }
                    }
                }
            }
            finally
            {
                reentrancy.exit();
            }
        }
        return formattedMessage;
    }

    @Override
    public int hashCode()
    {
        return Hash.many(getClass(), created, message, stackTrace, Arrays.hashCode(arguments));
    }

    @Override
    public Importance importance()
    {
        return Importance.importance(getClass());
    }

    @Override
    public Frequency maximumFrequency()
    {
        return maximumFrequency;
    }

    public OperationMessage maximumFrequency(Frequency maximumFrequency)
    {
        this.maximumFrequency = maximumFrequency;
        return this;
    }

    public void message(String message)
    {
        this.message = message;
    }

    @Override
    public Severity severity()
    {
        return Severity.NONE;
    }

    @Override
    public StackTrace stackTrace()
    {
        if (stackTrace == null)
        {
            var cause = cause();
            if (cause != null)
            {
                stackTrace = new StackTrace(cause);
            }
        }
        return stackTrace;
    }

    public OperationMessage stackTrace(StackTrace stackTrace)
    {
        this.stackTrace = stackTrace;
        return this;
    }

    @Override
    public String text()
    {
        return message;
    }

    @Override
    public String toString()
    {
        return formatted(WITH_EXCEPTION);
    }

    private static void initialize()
    {
        // Pre-populate the name map

        // Lifecycle messages
        new OperationStarted();
        new OperationSucceeded();
        new OperationFailed();
        new OperationHalted();

        // Progress messages
        new Activity();
        new Alert();
        new CriticalAlert();
        new Information();
        new Problem();
        new Glitch();
        new Trace();
        new Warning();
    }

    private static NameMap<OperationMessage> messages()
    {
        if (messages == null)
        {
            messages = new NameMap<>();
            messages.caseSensitive(false);
        }
        return messages;
    }
}
