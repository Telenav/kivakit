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

import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.messages.status.Warning;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramDataValidation;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessageListenerType;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A listener, such as {@link MessageList} that is able to report how many of different kinds of messages it has seen.
 * The {@link #count(Message.Status)} message returns the number of messages of a given {@link Message.Status} type it
 * has seen. For example, messages that represent failure (of which there is more than one) would have the step type
 * {@link Message.Status#FAILED}. To get a count of a specific message type like {@link Warning}, the method {@link
 * #count(Message.Status)} can be used. To get the total of all messages that are higher than a given status severity
 * (as determined by {@link Message.Status#isWorseThan(Message.Status)}, the method {@link
 * #countWorseThanOrEqualTo(Message.Status)} can be used. For example, to find out how many messages represented
 * something other than full success (warnings and worse), this code might be used:
 * <pre>
 * var issues = messages.countWorseThan(Status.SUCCESS)
 * </pre>
 * To find out how many messages represented a failure to complete:
 * <pre>
 * var issues = message.countWorseThan(Status.COMPLETED)
 * </pre>
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
     * @return The number of messages with the given status
     */
    Count count(Message.Status type);

    /**
     * @return The number of messages of the given type
     */
    Count count(Class<? extends Message> type);

    /**
     * @return The number of messages that are worse than or equal to the given status
     */
    Count countWorseThanOrEqualTo(Message.Status status);
}
