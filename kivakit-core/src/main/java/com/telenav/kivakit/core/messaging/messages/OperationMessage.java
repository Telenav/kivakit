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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramMessageType;
import com.telenav.kivakit.core.language.Hash;
import com.telenav.kivakit.core.logging.Log;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.MessageFormat;
import com.telenav.kivakit.core.messaging.Messages;
import com.telenav.kivakit.core.messaging.context.CodeContext;
import com.telenav.kivakit.core.messaging.context.StackTrace;
import com.telenav.kivakit.core.thread.ReentrancyTracker;
import com.telenav.kivakit.core.time.Frequency;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.language.Arrays.arrayContains;
import static com.telenav.kivakit.core.language.Objects.areEqualPairs;
import static com.telenav.kivakit.core.messaging.MessageFormat.WITH_EXCEPTION;
import static com.telenav.kivakit.core.messaging.messages.Importance.importanceOfMessage;
import static com.telenav.kivakit.core.messaging.messages.Severity.NONE;
import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.core.thread.ReentrancyTracker.Reentrancy.REENTERED;
import static com.telenav.kivakit.core.time.Time.now;

/**
 * Base implementation of the {@link Message} interface. Represents a message destined for a {@link Listener} such as a
 * {@link Logger} with arguments which can be interpolated if the message is formatted with
 * {@link #formatted(MessageFormat[])}. All {@link OperationMessage}s have the attributes defined in {@link Message}.
 * <p>
 * For messages that might be sent to frequently, {@link #maximumFrequency(Frequency)} can be used to specify that the
 * receiver only handle the message every so often. {@link Log}s support this feature, so it is possible to easily tag a
 * message as something to only log once in a while.
 *
 * <p><b>Formatting</b></p>
 *
 * <ul>
 *     <li>{@link #formatted(MessageFormat...)}</li>
 *     <li>{@link #formatted()}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #arguments()}</li>
 *     <li>{@link #arguments(Object[])}</li>
 *     <li>{@link #cause()}</li>
 *     <li>{@link #cause(Throwable)}</li>
 *     <li>{@link #context()}</li>
 *     <li>{@link #context(CodeContext)}</li>
 *     <li>{@link #created()}</li>
 *     <li>{@link #created(Time)}</li>
 *     <li>{@link #description()}</li>
 *     <li>{@link #importance()}</li>
 *     <li>{@link #maximumFrequency()}</li>
 *     <li>{@link #maximumFrequency(Frequency)}</li>
 *     <li>{@link #messageForType(String)}</li>
 *     <li>{@link #severity()}</li>
 *     <li>{@link #stackTrace()}</li>
 *     <li>{@link #stackTrace(StackTrace)}</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #asString(Format)}</li>
 *     <li>{@link #asException()}</li>
 * </ul>
 *
 * @see Message
 * @see Log
 * @see Listener
 * @see Frequency
 * @see Severity
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramMessageType.class)
@UmlExcludeSuperTypes({ Named.class })
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public abstract class OperationMessage implements Named, Message
{
    private static final ReentrancyTracker reentrancy = new ReentrancyTracker();

    /** This flag can be helpful in detecting infinite recursion of message formatting */
    private static final boolean DETECT_REENTRANCY = false;

    /** Formatting arguments */
    private Object[] arguments;

    /** Any exception that was a cause of this message */
    private transient Throwable cause;

    /** The code context that transmitted this message */
    private CodeContext context;

    /** The time this message was created */
    private Time created = now();

    /** Any formatted message string */
    private String formattedMessage;

    /** The maximum frequency at which this message should be transmitted */
    private Frequency maximumFrequency;

    /** The message text */
    private String message;

    /** Any associated stack trace */
    private StackTrace stackTrace;

    protected OperationMessage(String message)
    {
        this.message = message;
        Messages.messages().put(name(), this);
    }

    protected OperationMessage()
    {
        Messages.messages().put(name(), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] arguments()
    {
        return arguments;
    }

    /**
     * Sets the formatting arguments for this message.
     *
     * @param arguments The arguments
     */
    public void arguments(Object[] arguments)
    {
        this.arguments = arguments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageException asException()
    {
        return new MessageException(this);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    @Override
    public String asString(@NotNull Format format)
    {
        switch (format)
        {
            default:
                return formatted(WITH_EXCEPTION);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Throwable cause()
    {
        return cause;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final OperationMessage cause(Throwable cause)
    {
        this.cause = cause;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CodeContext context()
    {
        return context;
    }

    /**
     * Sets the code context for this message
     */
    public void context(CodeContext context)
    {
        if (this.context == null)
        {
            this.context = context;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Time created()
    {
        return created;
    }

    /**
     * Sets the time of creation for this message
     */
    public void created(Time created)
    {
        this.created = created;
    }

    /**
     * Returns the formatted message without any stack trace information
     */
    @Override
    public String description()
    {
        return format(message, arguments);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof OperationMessage)
        {
            var that = (OperationMessage) object;
            return areEqualPairs(
                    this.getClass(), that.getClass(),
                    this.created, that.created,
                    this.message, that.message,
                    this.stackTrace, that.stackTrace,
                    this.arguments, that.arguments);
        }
        return false;
    }

    /**
     * Returns the fully formatted message including stack trace information
     */
    @Override
    public String formatted(MessageFormat... formats)
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
                    formattedMessage = format(message, arguments);
                    if (arrayContains(formats, WITH_EXCEPTION))
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

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return Hash.hashMany(getClass(), created, message, stackTrace, arguments);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Importance importance()
    {
        return importanceOfMessage(getClass());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Frequency maximumFrequency()
    {
        return maximumFrequency;
    }

    /**
     * Sets the maximum frequency at which this message can be transmitted
     */
    public OperationMessage maximumFrequency(Frequency maximumFrequency)
    {
        this.maximumFrequency = maximumFrequency;
        return this;
    }

    /**
     * Sets the message text
     */
    public void messageForType(String message)
    {
        this.message = message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Severity severity()
    {
        return NONE;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * Sets the stack trace for this message
     */
    public OperationMessage stackTrace(StackTrace stackTrace)
    {
        this.stackTrace = stackTrace;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String text()
    {
        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return formatted(WITH_EXCEPTION);
    }
}
