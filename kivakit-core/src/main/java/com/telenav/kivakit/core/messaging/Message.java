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

package com.telenav.kivakit.core.messaging;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramBroadcaster;
import com.telenav.kivakit.core.internal.lexakai.DiagramListener;
import com.telenav.kivakit.core.internal.lexakai.DiagramMessaging;
import com.telenav.kivakit.core.language.Classes;
import com.telenav.kivakit.core.messaging.context.CodeContext;
import com.telenav.kivakit.core.messaging.context.StackTrace;
import com.telenav.kivakit.core.messaging.messages.Importance;
import com.telenav.kivakit.core.messaging.messages.OperationLifecycleMessage;
import com.telenav.kivakit.core.messaging.messages.OperationStatusMessage;
import com.telenav.kivakit.core.messaging.messages.Severity;
import com.telenav.kivakit.core.messaging.messages.Triaged;
import com.telenav.kivakit.core.messaging.messages.lifecycle.OperationHalted;
import com.telenav.kivakit.core.messaging.messages.status.Alert;
import com.telenav.kivakit.core.messaging.messages.status.CriticalAlert;
import com.telenav.kivakit.core.messaging.messages.status.Glitch;
import com.telenav.kivakit.core.messaging.messages.status.Information;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.messages.status.Trace;
import com.telenav.kivakit.core.messaging.messages.status.Warning;
import com.telenav.kivakit.core.messaging.messages.status.activity.Step;
import com.telenav.kivakit.core.messaging.messages.status.activity.StepFailure;
import com.telenav.kivakit.core.messaging.messages.status.activity.StepIncomplete;
import com.telenav.kivakit.core.messaging.messages.status.activity.StepSuccess;
import com.telenav.kivakit.core.string.Formatter;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.kivakit.core.time.Frequency;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.interfaces.messaging.Transmittable;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.interfaces.string.StringFormattable;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.messaging.MessageFormat.FORMATTED;
import static com.telenav.kivakit.core.messaging.MessageFormat.WITHOUT_EXCEPTION;
import static com.telenav.kivakit.core.messaging.MessageFormat.WITH_EXCEPTION;

/**
 * An interface to retrieve the basic attributes of a message, find out what it means and format it as text. A message's
 * text is un-formatted when it is created. The message text and any arguments must be passed to {@link Formatter} to
 * produce a final formatted message, which can be retrieved with {@link #formatted(MessageFormat[])}. This is a useful
 * design because it is cheaper to construct messages if the formatting is delayed until the formatted string is need,
 * for example, when a log message is added to a formatted text log.
 * <p>
 * <b>Message Attributes</b>
 * <ul>
 *     <li>{@link #text()} - The un-formatted text of the message</li>
 *     <li>{@link #arguments()} - Arguments that can be applied to the message text when formatting it</li>
 *     <li>{@link #cause()} - Any exception associated with the message</li>
 *     <li>{@link #created()} - The time that the message was created</li>
 *     <li>{@link #severity()} - The severity of the message</li>
 *     <li>{@link #operationStatus()} - The operation that the message relates to, if any</li>
 *     <li>{@link #status()} - The status of a step in an ongoing operation that the message relates to, if any</li>
 * </ul>
 * <p>
 * <b>Types of MessageTransceiver</b>
 * <p>
 * Different types of messages relate to different aspects of a running program. MessageTransceiver relating to a larger goal
 * of the program, such as converting a file or computing a route are <i>operation lifecycle</i> messages and extend
 * {@link OperationLifecycleMessage}. MessageTransceiver that relate to the smaller steps required to achieve an operation are
 * <i>status</i> messages and extend {@link OperationStatusMessage}. If the {@link #operationStatus()} method returns a non-null
 * value, the message relates to the operation lifecycle. If the {@link #status()} method returns a non-null value the
 * message relates to the most recent step in the current operation.
 * <p>
 * Each class implementing {@link Message} will define one or the other of these two return values. For example,
 * {@link OperationHalted} is a lifecycle message which means that the current operation has transitioned to the
 * {@link OperationStatus#HALTED} state. {@link Warning} relates to a step in an operation which {@link Status#SUCCEEDED},
 * even if it did so imperfectly. Inspection of message classes will reveal what their meaning is with respect to
 * operations and the steps used to complete them.
 * <p>
 * <b>Operation Lifecycle MessageTransceiver</b>
 * <p>
 * An {@link OperationStatus} lifecycle message relates to a state change in the status of an operation. Operations start,
 * run and then complete with some kind of result:
 * <ul>
 *     <li>{@link OperationStatus#STARTED} - The operation has started</li>
 *     <li>{@link OperationStatus#RUNNING} - The operation is running</li>
 *     <li>{@link OperationStatus#SUCCEEDED} - The operation completed successfully</li>
 *     <li>{@link OperationStatus#FAILED} - The operation completed but did not succeed in meeting its goal</li>
 *     <li>{@link OperationStatus#HALTED} - The operation was unable to complete</li>
 * </ul>
 * <p>
 * <b>Operation Status MessageTransceiver</b>
 * <p>
 * A {@link Status} message relates to the result of executing a step in a larger operation:
 * <ul>
 *     <li>{@link Status#SUCCEEDED} - The step succeeded and the message is reporting progress: {@link Step}, {@link Information}, {@link StepSuccess}, {@link Trace}</li>
 *     <li>{@link Status#COMPLETED} - The step completed and produced a result but there was an actual or potential negative effect that should be noted: {{@link Warning}}</li>
 *     <li>{@link Status#RESULT_COMPROMISED} - The step completed successfully amd data was not discarded, but the result may be partly invalid: {@link Glitch}</li>
 *     <li>{@link Status#RESULT_INCOMPLETE} - The step completed but some aspect of the result had to be discarded: {@link StepIncomplete}</li>
 *     <li>{@link Status#PROBLEM} - The step didn't complete correctly because something needs attention: {@link Problem}</li>
 *     <li>{@link Status#FAILED} - The step did not complete or did not produce any usable result: {@link Alert}, {@link CriticalAlert}, {@link StepFailure}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramBroadcaster.class)
@UmlClassDiagram(diagram = DiagramMessaging.class)
@UmlClassDiagram(diagram = DiagramListener.class)
@UmlExcludeSuperTypes({ StringFormattable.class })
@UmlRelation(label = "formats with", diagram = DiagramMessaging.class, referent = Formatter.class)
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public interface Message extends
        Transmittable,
        Triaged,
        StringFormattable,
        Named
{
    /**
     * Returns the given string with message markers escaped
     */
    static String escapeMessageText(String text)
    {
        return Strings.replaceAll(text, "$", "$$");
    }

    /**
     * Returns the message with the given simple name (Problem, Warning, etc.)
     */
    static Message parseMessageName(Listener listener, String name)
    {
        return Messages.parseMessageType(listener, name);
    }

    /**
     * The meaning of a message if it represents a state change in the lifecycle of an operation
     */
    @UmlClassDiagram(diagram = DiagramMessaging.class)
    enum OperationStatus
    {
        /** This message does not represent an operation lifecycle state transition */
        NOT_APPLICABLE,

        /** The operation has started */
        STARTED,

        /** The operation is executing */
        RUNNING,

        /** The operation succeeded */
        SUCCEEDED,

        /** The operation completed but did not succeed */
        FAILED,

        /** The operation halted before completion due to an unrecoverable failure */
        HALTED;

        public boolean failed()
        {
            return this == FAILED || this == HALTED;
        }

        public boolean halted()
        {
            return this == FAILED || this == HALTED;
        }

        public boolean succeeded()
        {
            return this == SUCCEEDED;
        }
    }

    /**
     * The meaning of a message if it relates to the current step in an ongoing operation
     */
    @UmlClassDiagram(diagram = DiagramMessaging.class)
    enum Status
    {
        /** This message does not represent a step in an ongoing operation */
        NOT_APPLICABLE,

        /** The step succeeded and produced a result without any significant negative effect to forward progress */
        SUCCEEDED,

        /**
         * The step completed and the result was not compromised but some actual or potential minor negative effect is
         * being reported
         */
        COMPLETED,

        /** The step succeeded but some aspect of the result was compromised, but the result is not incomplete */
        RESULT_COMPROMISED,

        /** The result is incomplete because something had to be discarded */
        RESULT_INCOMPLETE,

        /** The step didn't complete correctly because something needs attention */
        PROBLEM,

        /** The step did not produce any correct result */
        FAILED;

        public boolean failed()
        {
            return this == FAILED || this == PROBLEM;
        }

        /**
         * @return True if the status is above the given status in terms of negative effect
         */
        public boolean isWorseThan(Status minimum)
        {
            return ordinal() > minimum.ordinal();
        }

        /**
         * @return True if the status is above the given status in terms of negative effect
         */
        public boolean isWorseThanOrEqualTo(Status minimum)
        {
            return ordinal() >= minimum.ordinal();
        }

        public boolean succeeded()
        {
            return this == SUCCEEDED;
        }
    }

    /**
     * Returns message arguments used to produce formatted message
     */
    Object[] arguments();

    /**
     * Returns this message as an exception
     */
    RuntimeException asException();

    /**
     * Returns any cause or null if none
     */
    Throwable cause();

    /**
     * <b>Not public API</b>
     *
     * <p>
     * Sets the cause for this message
     * </p>
     */
    Message cause(Throwable cause);

    /**
     * Returns the context where this message was created
     */
    CodeContext context();

    /**
     * Returns the time at which this message was created
     */
    Time created();

    /**
     * Returns the formatted message without stack trace information
     */
    String description();

    /**
     * Returns formatted message, including any stack trace information
     */
    String formatted(MessageFormat... format);

    default String formatted()
    {
        return formatted(FORMATTED, WITHOUT_EXCEPTION);
    }

    /**
     * Returns the importance of this message, without respect to severity
     */
    @UmlRelation(label = "message importance")
    Importance importance();

    default boolean isFailure()
    {
        return status().failed() || operationStatus().failed();
    }

    @UmlExcludeMember
    default boolean isMoreImportantThan(Class<? extends Message> type)
    {
        return importance().isGreaterThan(Importance.importanceOfMessage(type));
    }

    @UmlExcludeMember
    default boolean isMoreImportantThanOrEqualTo(Class<? extends Message> type)
    {
        return importance().isGreaterThanOrEqualTo(Importance.importanceOfMessage(type));
    }

    /**
     * Returns true if the status of this message is worse than the given value
     */
    @UmlExcludeMember
    default boolean isWorseThan(Status status)
    {
        return status().isWorseThan(status);
    }

    /**
     * Returns true if the status of this message is worse than the given message
     */
    @UmlExcludeMember
    default <T extends Message> boolean isWorseThan(Class<T> message)
    {
        return isWorseThan(Classes.newInstance(message).status());
    }

    /**
     * Returns true if the status of this message is worse than the given value
     */
    @UmlExcludeMember
    default boolean isWorseThanOrEqualTo(Status status)
    {
        return status().isWorseThanOrEqualTo(status);
    }

    /**
     * Returns true if the status of this message is worse than the given message
     */
    @UmlExcludeMember
    default <T extends Message> boolean isWorseThanOrEqualTo(Class<T> message)
    {
        return isWorseThanOrEqualTo(Classes.newInstance(message).status());
    }

    /**
     * Returns the frequency with which this message should be logged or null if the message should always be logged.
     * <p>
     * NOTE: Message identity is determined by looking at the un-formatted message returned by message(). If message()
     * does not return a constant string, a bounded map error may occur as the system attempts to track too many message
     * frequencies.
     */
    Frequency maximumFrequency();

    @Override
    default String name()
    {
        return Classes.simpleName(getClass());
    }

    /**
     * Returns the operational status represented by this message, if any
     */
    @UmlRelation(label = "operation status")
    OperationStatus operationStatus();

    /**
     * Returns the severity of this message
     */
    @Override
    @UmlRelation(label = "message severity")
    Severity severity();

    /**
     * Returns any stack trace associated with this message (or null if none applies)
     */
    StackTrace stackTrace();

    /**
     * Returns the status of a step in an ongoing operation, if the message is relevant to this
     */
    @UmlRelation(label = "message status")
    Status status();

    /**
     * Returns the un-formatted message text. See {@link Formatter#format(String, Object...)} for details on how this
     * text is formatted using the {@link #arguments()} to the message.
     */
    String text();

    @UmlExcludeMember
    default void throwAsIllegalArgumentException()
    {
        throw new IllegalArgumentException(formatted(WITH_EXCEPTION));
    }

    @UmlExcludeMember
    default void throwAsIllegalStateException()
    {
        throw new IllegalStateException(formatted(WITH_EXCEPTION));
    }

    /**
     * The formatted message, including any exception
     */
    @Override
    String toString();
}
