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

package com.telenav.kivakit.kernel.messaging.listeners;

import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.strings.Align;
import com.telenav.kivakit.kernel.language.strings.Plural;
import com.telenav.kivakit.kernel.language.types.Classes;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.messages.OperationMessage;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.kernel.messaging.messages.status.Quibble;
import com.telenav.kivakit.kernel.messaging.messages.status.Warning;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramDataValidation;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessageListenerType;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A {@link Listener}, such as {@link MessageList} that is able to report how many of different kinds of messages it has
 * seen.
 *
 * <p><b>Counting Message by Message Type</b></p>
 *
 * <p>
 * To get a count of a specific message type like {@link Warning} or {@link Problem}, the method {@link #count(Class)}
 * can be used:
 * </p>
 *
 * <pre>
 * var warnings = count(Warning.class)
 * var problems = count(Problem.class)</pre>
 *
 * <p>
 * To get the count of messages with a status worse than or equal to a given message type:
 * </p>
 *
 * <pre>
 * var errors = countWorseThanOrEqualTo(Problem.class)</pre>
 *
 * <p><b>Message Statistics</b></p>
 * <p>
 * To display message statistics to a user, the {@link #statistics(Class[])} method will returns a {@link StringList}
 * detailing how many of each message type were found:
 *
 * <pre>
 * var statistics = statistics(Warning.class, Problem.class)</pre>
 *
 * <p><b>Counting Messages by Status</b></p>
 *
 * <p>
 * The {@link #count(Message.Status)} method returns the number of messages of a given {@link Message.Status} type it
 * has seen. For example, messages that represent failure would have the status type {@link Message.Status#FAILED}:
 * </p>
 *
 * <pre>
 * var failed = count(FAILED)</pre>
 *
 * @author jonathanl (shibo)
 * @see Message
 * @see Message.Status
 * @see MessageList
 */
@UmlClassDiagram(diagram = DiagramMessageListenerType.class)
@UmlClassDiagram(diagram = DiagramDataValidation.class)
public interface MessageCounter extends Listener
{
    /**
     * Returns the total number of messages with the given status
     *
     * @return The message count
     */
    Count count(Message.Status type);

    /**
     * Returns the total number of messages of the given type
     *
     * @return The message count
     */
    Count count(Class<? extends Message> type);

    /**
     * Returns the number of messages that are worse than or equal to the given status
     *
     * @return The message count
     */
    Count countWorseThanOrEqualTo(Message.Status status);

    /**
     * Returns the count of messages worse than or equal to the status of the given message type
     *
     * @param type The message type
     * @return The count of messages
     */
    default Count countWorseThanOrEqualTo(Class<? extends Message> type)
    {
        return countWorseThanOrEqualTo(OperationMessage.of(type).status());
    }

    /**
     * Returns true if there are one or more messages worse than or equal to the given message type
     *
     * @return True for failure, false otherwise
     */
    default boolean failed(Class<? extends Message> type)
    {
        return countWorseThanOrEqualTo(type).isNonZero();
    }

    /**
     * Returns true if there are one or more message(s) with a status worse than or equal to {@link Problem}
     *
     * @return True for failure, false otherwise
     */
    default boolean failed()
    {
        return failed(Problem.class);
    }

    /**
     * If the counted message represent failure, as determined by {@link #failed()}, throws an {@link
     * IllegalStateException} with the statistics for messages.
     */
    @SuppressWarnings("unchecked")
    default void ifFailedThrow()
    {
        if (failed())
        {
            throw new IllegalStateException("Failed:\n\n" + statistics(Problem.class, Quibble.class, Warning.class).bulleted(2));
        }
    }

    /**
     * Returns the counts of each message with the given statuses
     *
     * @return Statistics for the given list of operation step types
     */
    default StringList statistics(Message.Status... statuses)
    {
        var statistics = new StringList();
        for (var status : statuses)
        {
            statistics.append(Align.right(status.name(), 24, ' '))
                    .append(": ").append(count(status).toCommaSeparatedString());
        }
        return statistics;
    }

    /**
     * Returns the counts of each each of the given messages
     *
     * @param types The message types to include in the statistics
     * @return A string list with counts for each listed message type
     */
    @SuppressWarnings("unchecked")
    default StringList statistics(Class<? extends Message>... types)
    {
        var statistics = new StringList();
        for (var type : types)
        {
            var count = count(type);
            if (count != null)
            {
                statistics.append(Align.right(Plural.pluralize(Classes.simpleName(type)), 24, ' ')
                        + ": " + count.toCommaSeparatedString());
            }
        }
        return statistics;
    }

    /**
     * Returns true if there are no messages worse than or equal to {@link Problem}
     *
     * @return True for success, false otherwise
     */
    default boolean succeeded()
    {
        return !failed();
    }
}
