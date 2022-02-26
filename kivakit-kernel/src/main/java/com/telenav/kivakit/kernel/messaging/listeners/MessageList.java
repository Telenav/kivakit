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

import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
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
 * (or {@link Filter} subclass) are added.
 *
 * <p><b>List Methods</b></p>
 *
 * <p>
 * This list of messages can be rebroadcast with {@link Broadcaster#transmitAll(Iterable)}. A filtered list of messages
 * can be retrieved with {@link #matching(Matcher)} and a {@link StringList} of formatted messages with {@link
 * #formatted()}.
 * </p>
 *
 * <p><b>Superinterface Methods</b></p>
 *
 * <p>
 * The superinterface {@link MessageCounter} provides methods that count messages and collect statistics.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageListenerType.class)
@UmlClassDiagram(diagram = DiagramDataValidation.class)
public class MessageList extends ObjectList<Message> implements MessageCounter
{
    private Matcher<Message> filter;

    public MessageList(Matcher<Message> filter)
    {
        this(Maximum.MAXIMUM, filter);
    }

    public MessageList()
    {
        this(Filter.all());
    }

    public MessageList(Maximum maximumSize, Matcher<Message> filter)
    {
        super(maximumSize);
        this.filter = filter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageList copy()
    {
        var copy = (MessageList) super.copy();
        copy.filter = filter;
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Count count(Message.Status status)
    {
        var count = 0;
        for (var message : this)
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
    public Count count(Class<? extends Message> type)
    {
        var count = 0;
        for (var message : this)
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
    public Count countWorseThanOrEqualTo(Message.Status status)
    {
        var count = 0;
        for (var message : this)
        {
            if (message.status().isWorseThanOrEqualTo(status))
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
    public boolean equals(Object object)
    {
        // Local fields are not considered
        return super.equals(object);
    }

    /**
     * The messages in this list, formatted
     */
    public StringList formatted()
    {
        var messages = new StringList(maximumSize());
        for (var message : this)
        {
            messages.add(message.formatted(MessageFormatter.Format.WITH_EXCEPTION));
        }
        return messages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        // Local fields are not considered
        return super.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Message> matching(Matcher<Message> filter)
    {
        return super.matching(filter);
    }

    /**
     * @param type The message type
     * @return The messages of the given type
     */
    public ObjectList<Message> messages(Class<? extends Message> type)
    {
        var messages = new ObjectList<Message>(maximumSize());
        for (var object : this)
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
    public void onMessage(Message message)
    {
        if (filter.matches(message))
        {
            add(message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageList onNewInstance()
    {
        return new MessageList(filter);
    }
}
