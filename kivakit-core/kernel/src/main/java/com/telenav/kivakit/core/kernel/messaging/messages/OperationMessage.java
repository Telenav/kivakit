////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.messaging.messages;

import com.telenav.kivakit.core.kernel.interfaces.naming.Named;
import com.telenav.kivakit.core.kernel.language.collections.map.string.NameMap;
import com.telenav.kivakit.core.kernel.language.strings.conversion.StringFormat;
import com.telenav.kivakit.core.kernel.language.threading.context.CodeContext;
import com.telenav.kivakit.core.kernel.language.threading.context.StackTrace;
import com.telenav.kivakit.core.kernel.language.threading.status.ReentrancyTracker;
import com.telenav.kivakit.core.kernel.language.time.Frequency;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.language.types.Classes;
import com.telenav.kivakit.core.kernel.logging.Log;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.logs.BaseLog;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.kernel.messaging.messages.lifecycle.OperationFailed;
import com.telenav.kivakit.core.kernel.messaging.messages.lifecycle.OperationHalted;
import com.telenav.kivakit.core.kernel.messaging.messages.lifecycle.OperationStarted;
import com.telenav.kivakit.core.kernel.messaging.messages.lifecycle.OperationSucceeded;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Activity;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Alert;
import com.telenav.kivakit.core.kernel.messaging.messages.status.CriticalAlert;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Information;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Quibble;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Trace;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Warning;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessageType;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

import static com.telenav.kivakit.core.kernel.messaging.messages.MessageFormatter.Format.WITH_EXCEPTION;

/**
 * Base implementation of the {@link Message} interface. Represents a message destined for a {@link Listener} such as a
 * {@link Logger} with arguments which can be interpolated if the message is formatted with {@link
 * #formatted(MessageFormatter.Format)}. All {@link OperationMessage}s have the attributes defined in {@link Message}.
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
    private static final MessageFormatter FORMATTER = new MessageFormatter();

    private static NameMap<OperationMessage> messages;

    private static final ReentrancyTracker reentrancy = new ReentrancyTracker();

    public static OperationMessage of(final String name)
    {
        initialize();
        return messages.get(name);
    }

    private transient String message;

    private transient Object[] arguments;

    private transient Throwable cause;

    private Time created = Time.now();

    private String formattedMessage;

    private StackTrace stackTrace;

    private Frequency maximumFrequency;

    private CodeContext context;

    protected OperationMessage(final String message)
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

    public void arguments(final Object[] arguments)
    {
        this.arguments = arguments;
    }

    @Override
    public RuntimeException asException()
    {
        return new IllegalStateException(formatted(WITH_EXCEPTION), cause());
    }

    @Override
    public String asString(final StringFormat format)
    {
        switch (format.identifier())
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

    public final OperationMessage cause(final Throwable cause)
    {
        this.cause = cause;
        return this;
    }

    @Override
    public CodeContext context()
    {
        return context;
    }

    public void context(final CodeContext context)
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

    public void created(final Time created)
    {
        this.created = created;
    }

    /**
     * @return The formatted message without any stack trace information
     */
    @Override
    public String description()
    {
        return Message.format(message, arguments);
    }

    /**
     * @return The fully formatted message including stack trace information
     */
    @Override
    public String formatted(final MessageFormatter.Format format)
    {
        if (formattedMessage == null)
        {
            try
            {
                if (reentrancy.enter() && !BaseLog.isAsynchronous())
                {
                    formattedMessage = "A message was logged while attempting to format the log message '" + message + "'\n"
                            + "Formatting this message could result in infinite recursion because KIVAKIT_LOG_SYNCHRONOUS is 'true'";
                }
                else
                {
                    formattedMessage = Message.format(message, arguments);
                    if (format == WITH_EXCEPTION)
                    {
                        final var cause = cause();
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
    public Importance importance()
    {
        return Importance.importance(getClass());
    }

    @Override
    public Frequency maximumFrequency()
    {
        return maximumFrequency;
    }

    public OperationMessage maximumFrequency(final Frequency maximumFrequency)
    {
        this.maximumFrequency = maximumFrequency;
        return this;
    }

    public void message(final String message)
    {
        this.message = message;
    }

    @Override
    public String name()
    {
        return Classes.simpleName(getClass());
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
            final var cause = cause();
            if (cause != null)
            {
                stackTrace = new StackTrace(cause);
            }
        }
        return stackTrace;
    }

    public OperationMessage stackTrace(final StackTrace stackTrace)
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

    /**
     * Override this to format a message in some other way
     *
     * @return The message formatter
     */
    protected MessageFormatter formatter()
    {
        return FORMATTER;
    }

    private static void initialize()
    {
        // Prepopulate the name map

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
        new Quibble();
        new Trace();
        new Warning();
    }

    private static NameMap<OperationMessage> messages()
    {
        if (messages == null)
        {
            messages = new NameMap<>();
        }
        return messages;
    }
}
