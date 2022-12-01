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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramListenerType;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.value.count.Count.count;

/**
 * Listens to messages to determine if the expected number and type of messages were received during some operation.
 * Expected messages are added with {@link #expect(Class)} and {@link #expect(Class, int)} and after the operation has
 * finished the messages can be checked with {@link #check()}. For example, the following code should broadcast a
 * {@link Problem} message and return null.
 * <pre>
 * new MessageChecker().expect(Problem.class).check(() -&gt;
 *     ensureEqual(null, new Duration.SecondsConverter(nullListener()).convert("x")));
 * </pre>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramListenerType.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class MessageChecker extends BaseRepeater
{
    /** Map from message type to count */
    private final Map<Class<? extends Message>, Count> expectedCount = new HashMap<>();

    /** List of captured messages */
    private final MessageList messages = new MessageList(at -> true);

    /**
     * Runs the given code and then checks messages received
     */
    public MessageChecker check(Runnable code)
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
        Set<Class<? extends Message>> types = new HashSet<>();
        for (var message : messages)
        {
            types.add(message.getClass());
        }

        for (var type : types)
        {
            var count = messages.messagesOfType(type).size();
            var expected = expectedCount.get(type);
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
    public MessageChecker expect(Class<? extends Message> messageClass)
    {
        expect(messageClass, 1);
        return this;
    }

    /**
     * Registers an expected number of messages of the given class
     */
    public MessageChecker expect(Class<? extends Message> messageClass, int count)
    {
        var expected = expectedCount.get(messageClass);
        if (expected == null)
        {
            expectedCount.put(messageClass, count(count));
        }
        else
        {
            fail("Already have an expectation for: " + messageClass);
        }
        return this;
    }

    /**
     * Receives a message and adds it to the list of messages
     *
     * @param message The message
     */
    @Override
    public void onMessage(Message message)
    {
        messages.add(message);
    }
}
