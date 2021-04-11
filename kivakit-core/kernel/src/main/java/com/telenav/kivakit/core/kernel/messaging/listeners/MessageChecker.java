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

package com.telenav.kivakit.core.kernel.messaging.listeners;

import com.telenav.kivakit.core.kernel.language.collections.map.BaseMap;
import com.telenav.kivakit.core.kernel.language.matchers.AnythingMatcher;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.core.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.kernel.project.CoreKernelLimits;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessageListenerType;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.HashSet;
import java.util.Set;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;

/**
 * Listens to messages to determine if the expected number and type of messages were received during some operation.
 * Expected messages are added with {@link #expect(Class)} and {@link #expect(Class, int)} and after the operation has
 * finished the messages can be checked with {@link #check()}. For example, the following code should broadcast a {@link
 * Problem} message and return null.
 * <pre>
 * new MessageChecker().expect(Problem.class).check(() -&gt;
 *     ensureEqual(null, new Duration.SecondsConverter(Listener.none()).convert("x")));
 * </pre>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageListenerType.class)
public class MessageChecker extends BaseRepeater
{
    private final MessageList messages = new MessageList(new AnythingMatcher<>());

    private final BaseMap<Class<? extends Message>, Count> expectedCount =
            new BaseMap<>(CoreKernelLimits.MESSAGE_CLASSES);

    /**
     * Runs the given code and then checks messages received
     */
    public MessageChecker check(final Runnable code)
    {
        code.run();
        check();
        return this;
    }

    /**
     * Checks to see if the correct count of each expected message has been received and not any other messages
     */
    public boolean check()
    {
        final Set<Class<? extends Message>> types = new HashSet<>();
        for (final var message : messages)
        {
            types.add(message.getClass());
        }

        for (final var type : types)
        {
            final var count = messages.messages(type).size();
            final var expected = expectedCount.get(type);
            if (expected == null)
            {
                if (count == 0)
                {
                    problem("Did not expect any $s", type);
                    return false;
                }
            }
            else
            {
                if (count == expected.asInt())
                {
                    problem("Expected $ $s not $", expected, type, count);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Registers that one message the given type should be received
     */
    public MessageChecker expect(final Class<? extends Message> messageClass)
    {
        expect(messageClass, 1);
        return this;
    }

    /**
     * Registers an expected number of messages of the given class
     */
    public MessageChecker expect(final Class<? extends Message> messageClass, final int count)
    {
        final var expected = expectedCount.get(messageClass);
        if (expected == null)
        {
            expectedCount.put(messageClass, Count.count(count));
        }
        else
        {
            fail("Already have an expectation for $s", messageClass);
        }
        return this;
    }

    /**
     * Receives a message and adds it to the list of messages
     *
     * @param message The message
     */
    @Override
    public void onMessage(final Message message)
    {
        messages.add(message);
    }
}
