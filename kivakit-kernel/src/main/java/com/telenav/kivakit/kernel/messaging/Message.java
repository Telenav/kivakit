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

package com.telenav.kivakit.kernel.messaging;

import com.telenav.kivakit.kernel.interfaces.messaging.Transmittable;
import com.telenav.kivakit.kernel.language.strings.Strings;
import com.telenav.kivakit.kernel.language.strings.conversion.AsString;
import com.telenav.kivakit.kernel.language.threading.context.CodeContext;
import com.telenav.kivakit.kernel.language.threading.context.StackTrace;
import com.telenav.kivakit.kernel.language.time.Frequency;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.messaging.messages.Importance;
import com.telenav.kivakit.kernel.messaging.messages.MessageFormatter;
import com.telenav.kivakit.kernel.messaging.messages.OperationLifecycleMessage;
import com.telenav.kivakit.kernel.messaging.messages.OperationStatusMessage;
import com.telenav.kivakit.kernel.messaging.messages.Severity;
import com.telenav.kivakit.kernel.messaging.messages.Triaged;
import com.telenav.kivakit.kernel.messaging.messages.lifecycle.OperationHalted;
import com.telenav.kivakit.kernel.messaging.messages.status.Activity;
import com.telenav.kivakit.kernel.messaging.messages.status.Alert;
import com.telenav.kivakit.kernel.messaging.messages.status.CriticalAlert;
import com.telenav.kivakit.kernel.messaging.messages.status.Information;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.kernel.messaging.messages.status.Quibble;
import com.telenav.kivakit.kernel.messaging.messages.status.StepFailure;
import com.telenav.kivakit.kernel.messaging.messages.status.StepIncomplete;
import com.telenav.kivakit.kernel.messaging.messages.status.StepSuccess;
import com.telenav.kivakit.kernel.messaging.messages.status.Trace;
import com.telenav.kivakit.kernel.messaging.messages.status.Warning;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessageBroadcaster;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessageListener;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessaging;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

/**
 * An interface to retrieve the basic attributes of a message, find out what it means and format it as text. A message's
 * text is unformatted when it is created. The message text and any arguments must be passed to {@link MessageFormatter}
 * to produce a final formatted message, which can be retrieved with {@link #formatted(MessageFormatter.Format)}. This
 * is a useful design because it is cheaper to construct messages if the formatting is delayed until the formatted
 * string is need, for example, when a log message is added to a formatted text log.
 * <p>
 * <b>Message Attributes</b>
 * <ul>
 *     <li>{@link #text()} - The unformatted text of the message</li>
 *     <li>{@link #arguments()} - Arguments that can be applied to the message text when formatting it</li>
 *     <li>{@link #cause()} - Any exception associated with the message</li>
 *     <li>{@link #created()} - The time that the message was created</li>
 *     <li>{@link #severity()} - The severity of the message</li>
 *     <li>{@link #operationStatus()} - The operation that the message relates to, if any</li>
 *     <li>{@link #status()} - The status of a step in an ongoing operation that the message relates to, if any</li>
 * </ul>
 * <p>
 * <b>Types of Messages</b>
 * <p>
 * Different types of messages relate to different aspects of a running program. Messages relating to a larger goal
 * of the program, such as converting a file or computing a route are <i>operation lifecycle</i> messages and extend
 * {@link OperationLifecycleMessage}. Messages that relate to the smaller steps required to achieve an operation are
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
 * <b>Operation Lifecycle Messages</b>
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
 * <b>Operation Status Messages</b>
 * <p>
 * A {@link Status} message relates to the result of executing a step in a larger operation:
 * <ul>
 *     <li>{@link Status#SUCCEEDED} - The step succeeded and the message is reporting progress: {@link Activity}, {@link Information}, {@link StepSuccess}, {@link Trace}</li>
 *     <li>{@link Status#COMPLETED} - The step completed and produced a result but there was an actual or potential negative effect that should be noted: {{@link Warning}}</li>
 *     <li>{@link Status#RESULT_COMPROMISED} - The step completed successfully amd data was not discarded, but the result may be partly invalid: {@link Quibble}</li>
 *     <li>{@link Status#RESULT_INCOMPLETE} - The step completed but some aspect of the result had to be discarded: {@link StepIncomplete}</li>
 *     <li>{@link Status#PROBLEM} - The step didn't complete correctly because something needs attention: {@link Problem}</li>
 *     <li>{@link Status#FAILED} - The step did not complete or did not produce any usable result: {@link Alert}, {@link CriticalAlert}, {@link StepFailure}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageBroadcaster.class)
@UmlClassDiagram(diagram = DiagramMessaging.class)
@UmlClassDiagram(diagram = DiagramMessageListener.class)
@UmlExcludeSuperTypes({ AsString.class })
@UmlRelation(label = "formats with", diagram = DiagramMessaging.class, referent = MessageFormatter.class)
public interface Message extends Transmittable, Triaged, AsString
{
    /** Static instance of a (stateless) message formatter */
    MessageFormatter FORMATTER = new MessageFormatter();

    /**
     * @return The given string with single quotes escaped
     */
    static String escape(final String text)
    {
        return Strings.replaceAll(text, "$", "$$");
    }

    /**
     * @return The message text formatted with the given arguments
     */
    static String format(final String message, final Object... arguments)
    {
        return FORMATTER.format(message, arguments);
    }

    static void println(final String message, final Object... arguments)
    {
        System.out.println(format(message, arguments));
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
            return this == FAILED;
        }

        /**
         * @return True if the status is above the given status in terms of negative effect
         */
        public boolean isWorseThan(final Status minimum)
        {
            return ordinal() > minimum.ordinal();
        }

        /**
         * @return True if the status is above the given status in terms of negative effect
         */
        public boolean isWorseThanOrEqualTo(final Status minimum)
        {
            return ordinal() >= minimum.ordinal();
        }

        public boolean succeeded()
        {
            return this == SUCCEEDED;
        }
    }

    /**
     * @return Message arguments used to produce formatted message
     */
    Object[] arguments();

    /**
     * @return This message as an exception
     */
    RuntimeException asException();

    /**
     * @return Any cause or null if none
     */
    Throwable cause();

    /**
     * @return The context where this message was created
     */
    CodeContext context();

    /**
     * @return The time at which this message was created
     */
    Time created();

    /**
     * @return The formatted message without stack trace information
     */
    String description();

    /**
     * @return Formatted message, including any stack trace information
     */
    String formatted(MessageFormatter.Format format);

    /**
     * @return The importance of this message, without respect to severity
     */
    @UmlRelation(label = "message importance")
    Importance importance();

    @UmlExcludeMember
    default boolean isMoreImportantThan(final Class<? extends Message> type)
    {
        return importance().isGreaterThan(Importance.importance(type));
    }

    @UmlExcludeMember
    default boolean isMoreImportantThanOrEqualTo(final Class<? extends Message> type)
    {
        return importance().isGreaterThanOrEqualTo(Importance.importance(type));
    }

    /**
     * @return True if the status of this message is worse than the given value
     */
    @UmlExcludeMember
    default boolean isWorseThan(final Status status)
    {
        return status().isWorseThan(status);
    }

    /**
     * @return True if the status of this message is worse than the given value
     */
    @UmlExcludeMember
    default boolean isWorseThanOrEqualTo(final Status status)
    {
        return status().isWorseThanOrEqualTo(status);
    }

    /**
     * @return The frequency with which this message should be logged or null if the message should always be logged.
     * <p>
     * NOTE: Message identity is determined by looking at the unformatted message returned by message(). If message()
     * does not return a constant string, a bounded map error may occur as the system attempts to track too many message
     * frequencies.
     */
    Frequency maximumFrequency();

    /**
     * @return The operational status represented by this message, if any
     */
    @UmlRelation(label = "operation status")
    OperationStatus operationStatus();

    /**
     * @return The severity of this message
     */
    @Override
    @UmlRelation(label = "message severity")
    Severity severity();

    /**
     * @return Any stack trace associated with this message (or null if none applies)
     */
    StackTrace stackTrace();

    /**
     * @return The status of a step in an ongoing operation, if the message is relevant to this
     */
    @UmlRelation(label = "message status")
    Status status();

    /**
     * @return The unformatted message text. See {@link #format(String, Object...) and MessageFormatter} for details on
     * how this text is formatted using the {@link #arguments()} to the message.
     */
    String text();

    @UmlExcludeMember
    default void throwAsIllegalArgumentException()
    {
        throw new IllegalArgumentException(formatted(MessageFormatter.Format.WITH_EXCEPTION));
    }

    @UmlExcludeMember
    default void throwAsIllegalStateException()
    {
        throw new IllegalStateException(formatted(MessageFormatter.Format.WITH_EXCEPTION));
    }

    /**
     * The formatted message, including any exception
     */
    @Override
    String toString();
}
