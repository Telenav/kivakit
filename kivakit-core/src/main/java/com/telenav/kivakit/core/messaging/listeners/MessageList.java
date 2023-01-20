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

package com.telenav.kivakit.core.messaging.listeners;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.code.UncheckedCode;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.internal.lexakai.DiagramListenerType;
import com.telenav.kivakit.core.messaging.Broadcaster;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Collection;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.messaging.MessageFormat.WITH_EXCEPTION;
import static com.telenav.kivakit.core.string.AsciiArt.bullet;
import static com.telenav.kivakit.core.value.count.Maximum.MAXIMUM;
import static com.telenav.kivakit.interfaces.comparison.Filter.acceptAll;

/**
 * A list of messages that listens for and adds incoming messages. Only messages that are accepted by a {@link Matcher}
 * (or {@link Filter} subclass) are added.
 *
 * <p><b>List Methods</b></p>
 *
 * <p>
 * This list of messages can be rebroadcast with {@link Broadcaster#transmitAll(Iterable)}. A filtered list of messages
 * can be retrieved with {@link #matching(Matcher)} and a list of formatted messages with {@link #formatted()}.
 * </p>
 *
 * <p><b>Superinterface Methods</b></p>
 *
 * <p>
 * The superinterface {@link MessageCounter} provides methods that count messages and collect statistics.
 * </p>
 *
 * <p><b>Factory Methods</b></p>
 *
 * <ul>
 *     <li>{@link #emptyMessageList()}</li>
 * </ul>
 *
 * <p><b>Counting Messages</b></p>
 *
 * <ul>
 *     <li>{@link #count(Message.Status)}</li>
 *     <li>{@link #count(Class)}</li>
 *     <li>{@link #countWorseThanOrEqualTo(Message.Status)}</li>
 *     <li>{@link #countWorseThanOrEqualTo(Class)}</li>
 * </ul>
 *
 * <p><b>Filtering</b></p>
 *
 * <ul>
 *     <li>{@link #matching(Matcher)}</li>
 *     <li>{@link #messagesOfType(Class)}</li>
 * </ul>
 *
 * <p><b>Rebroadcasting</b></p>
 *
 * <ul>
 *     <li>{@link #broadcastTo(Listener)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramListenerType.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class MessageList extends ObjectList<Message> implements MessageCounter
{
    private static final MessageList EMPTY = new MessageList()
    {
        @Override
        public void onMessage(Message message)
        {
            unsupported("The message list returned by empty() is read-only");
        }
    };

    public static MessageList captureMessages(Broadcaster broadcaster, Runnable code)
    {
        var issues = new MessageList();
        issues.listenTo(broadcaster);
        try
        {
            code.run();
        }
        catch (Exception e)
        {
            issues.problem(e, "Code threw exception");
        }
        finally
        {
            broadcaster.removeListener(issues);
        }
        return issues;
    }

    public static MessageList emptyMessageList()
    {
        return EMPTY;
    }

    /** The message filter to include messages in this list */
    private Matcher<Message> filter;

    public MessageList(Matcher<Message> filter)
    {
        this(MAXIMUM, filter);
    }

    public MessageList()
    {
        this(acceptAll());
    }

    public MessageList(Maximum maximumSize, Matcher<Message> filter)
    {
        super(maximumSize);
        this.filter = filter;
    }

    /**
     * Transmits all the messages in this list to the given listener
     */
    public void broadcastTo(Listener listener)
    {
        forEach(listener::receive);
    }

    /**
     * Runs the given code. If it throws an exception, it is captured in this message list as a problem.
     *
     * @param code The code to execute
     * @param message The problem message to use if something goes wrong
     * @param arguments The arguments to the problem message
     * @return The value if the code executed without throwing and exception, otherwise null
     */
    public <T> T captureMessages(UncheckedCode<T> code, String message, Object... arguments)
    {
        try
        {
            return code.run();
        }
        catch (Exception e)
        {
            problem(message, arguments);
            return null;
        }
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
            messages.add(message.formatted(WITH_EXCEPTION));
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
    public MessageList matching(Matcher<Message> filter)
    {
        return (MessageList) super.matching(filter);
    }

    /**
     * @param type The message type
     * @return The messages of the given type
     */
    public MessageList messagesOfType(Class<? extends Message> type)
    {
        var messages = new MessageList();
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

    @Override
    public String toString()
    {
        return isEmpty()
            ? "[No issues]"
            : formatted().prefixedWith(bullet()).titledBox("Issues");
    }

    @Override
    public MessageList with(Message... value)
    {
        return (MessageList) super.with(value);
    }

    @Override
    public MessageList with(Iterable<Message> value)
    {
        return (MessageList) super.with(value);
    }

    @Override
    public MessageList with(Collection<Message> value)
    {
        return (MessageList) super.with(value);
    }

    @Override
    public MessageList without(Matcher<Message> matcher)
    {
        return (MessageList) super.without(matcher);
    }

    @Override
    public MessageList without(Message message)
    {
        return (MessageList) super.without(message);
    }

    @Override
    public MessageList without(Collection<Message> that)
    {
        return (MessageList) super.without(that);
    }

    @Override
    public MessageList without(Message[] that)
    {
        return (MessageList) super.without(that);
    }

    @Override
    protected MessageList onNewCollection()
    {
        return new MessageList();
    }

    @Override
    protected MessageList onNewList()
    {
        return new MessageList();
    }
}
