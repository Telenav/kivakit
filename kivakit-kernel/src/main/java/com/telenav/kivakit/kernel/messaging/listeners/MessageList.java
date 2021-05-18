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

import com.telenav.kivakit.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.strings.Align;
import com.telenav.kivakit.kernel.language.types.Classes;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.language.values.count.Maximum;
import com.telenav.kivakit.kernel.messaging.Broadcaster;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.messages.MessageFormatter;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramDataValidation;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessageListenerType;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A list of messages that listens for and adds incoming messages. Only messages that are accepted by a {@link Matcher}
 * (or {@link Filter} subclass) are added. The list of messages can be rebroadcast with {@link
 * Broadcaster#transmitAll(Iterable)} and they can be counted with {@link #count(Message.Status)} and {@link
 * #count(Class)}. A filtered list of messages can be retrieved with {@link #filtered(Matcher)} and a {@link StringList}
 * of formatted messages with {@link #formatted()}. Statistics can be retrieved for types of messages and for message
 * statuses with {@link #statisticsByType(Class[])} and {@link #statistics(Message.Status...)}, respectively.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageListenerType.class)
@UmlClassDiagram(diagram = DiagramDataValidation.class)
public class MessageList extends ObjectList<Message> implements MessageCounter
{
    private final Matcher<Message> filter;

    public MessageList(final Matcher<Message> filter)
    {
        this(Maximum.MAXIMUM, filter);
    }

    public MessageList(final Maximum maximumSize, final Matcher<Message> filter)
    {
        super(maximumSize);
        this.filter = filter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Count count(final Message.Status status)
    {
        var count = 0;
        for (final var message : this)
        {
            if (message.status() == status)
            {
                count++;
            }
        }
        return Count.count(count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Count count(final Class<? extends Message> type)
    {
        var count = 0;
        for (final var message : this)
        {
            if (type.isAssignableFrom(message.getClass()))
            {
                count++;
            }
        }
        return Count.count(count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Count countWorseThanOrEqualTo(final Message.Status status)
    {
        var count = 0;
        for (final var message : this)
        {
            if (message.status().isWorseThanOrEqualTo(status))
            {
                count++;
            }
        }
        return Count.count(count);
    }

    @Override
    public boolean equals(final Object object)
    {
        // Local fields are not considered
        return super.equals(object);
    }

    @Override
    public ObjectList<Message> filtered(final Matcher<Message> filter)
    {
        return super.filtered(filter);
    }

    /**
     * The messages in this list, formatted
     */
    public StringList formatted()
    {
        final var messages = new StringList(maximumSize());
        for (final var message : this)
        {
            messages.add(message.formatted(MessageFormatter.Format.WITH_EXCEPTION));
        }
        return messages;
    }

    @Override
    public int hashCode()
    {
        // Local fields are not considered
        return super.hashCode();
    }

    /**
     * @param type The message type
     * @return The messages of the given type
     */
    public ObjectList<Message> messages(final Class<? extends Message> type)
    {
        final var messages = new ObjectList<Message>(maximumSize());
        for (final var object : this)
        {
            if (type.isAssignableFrom(object.getClass()))
            {
                messages.add(object);
            }
        }
        return messages;
    }

    /**
     * When we hear a message, add it to the list.
     *
     * @param message The message to add
     */
    @Override
    public void onMessage(final Message message)
    {
        if (filter.matches(message))
        {
            add(message);
        }
    }

    /**
     * @return Statistics for the given list of operation step types
     */
    public StringList statistics(final Message.Status... statuses)
    {
        final var statistics = new StringList();
        for (final var status : statuses)
        {
            statistics.append(Align.right(status.name(), 24, ' '))
                    .append(": ").append(count(status).toCommaSeparatedString());
        }
        return statistics;
    }

    @SafeVarargs
    public final StringList statisticsByType(final Class<? extends Message>... types)
    {
        final var statistics = new StringList();
        for (final var type : types)
        {
            statistics.append(Align.right(Classes.simpleName(type), 24, ' '))
                    .append(": ").append(count(type).toCommaSeparatedString());
        }
        return statistics;
    }
}
