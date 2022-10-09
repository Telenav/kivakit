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
import com.telenav.kivakit.core.internal.lexakai.DiagramRepeater;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.messaging.listeners.ThrowingListener;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.os.Console;
import com.telenav.kivakit.interfaces.messaging.Transmittable;
import com.telenav.kivakit.interfaces.naming.NamedObject;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

/**
 * Handles messages through {@link #onMessage(Message)}.
 *
 * <p><b>Listening to Broadcasters</b></p>
 *
 * <p>
 * A listener can listen to a particular {@link Broadcaster} with {@link #listenTo(Broadcaster)}. Conversely, a listener
 * can be added to a broadcaster with {@link Broadcaster#addListener(Listener)}. Both methods achieve the same result.
 * </p>
 *
 * <p><b>Convenience Methods and Logging</b></p>
 *
 * <p>
 * A number of convenience methods in the {@link Transceiver} superinterface make it easy to transmit common messages to
 * listeners. For example, {@link #problem(String, Object...)} is inherited by {@link Broadcaster}s, {@link Repeater}s
 * and Components, making it easy to transmit {@link Problem} messages:
 * </p>
 *
 * <pre>
 * problem("Unable to read $", file);</pre>
 *
 *
 * <p><b>Repeater Chains</b></p>
 *
 * <p>
 * In the example below, EmployeeLoader is a {@link Repeater} which transmits a warning to all of its registered
 * listeners. The PayrollProcessor class is also a {@link Repeater} which listens to messages transmitted by the
 * EmployeeLoader and re-transmits them to its own listeners. Clients of the PayrollProcessor can listen to it in turn,
 * and they will receive the {@link Problem} transmitted EmployeeLoader, when it is repeated by the PayrollProcessor.
 *
 * <pre>
 * class EmployeeLoader extends BaseRepeater
 * {
 *
 *     [...]
 *
 *     problem("Unable to load $", employee);
 *
 *     [...]
 * }
 *
 * class PayrollProcessor extends BaseRepeater
 * {
 *
 *     [...]
 *
 *     var loader = listenTo(new EmployeeLoader());
 *
 *     [...]
 *
 * }
 *
 * var processor = LOGGER.listenTo(new PayrollProcessor());</pre>
 *
 * <p>
 * This pattern creates a chain of repeaters that terminates in one or more listeners. The final listener is often, but
 * not always a {@link Logger}:
 * </p>
 *
 * <p>
 * &nbsp;&nbsp;&nbsp;&nbsp;<b>EmployeeLoader ==> PayrollProcessor ==> Logger</b>
 * </p>
 *
 * @author jonathanl (shibo)
 * @see Broadcaster
 * @see <a href="https://state-of-the-art.org#broadcaster">State(Art) Blog Article</a>
 */
@UmlClassDiagram(diagram = DiagramBroadcaster.class)
@UmlClassDiagram(diagram = DiagramRepeater.class)
@UmlClassDiagram(diagram = DiagramListener.class)
@UmlExcludeSuperTypes({ NamedObject.class })
@FunctionalInterface
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public interface Listener extends MessageTransceiver
{
    /**
     * @return A listener that writes the messages it hears to the console
     */
    static Listener consoleListener()
    {
        return Console.console();
    }

    /**
     * @return A listener that does nothing with messages. Useful only when you want to discard output from something
     */
    static Listener nullListener()
    {
        return ignored ->
        {
        };
    }

    /**
     * @return A listener that throws exceptions
     */
    static Listener throwingListener()
    {
        return new ThrowingListener();
    }

    /**
     * @return True if this listener doesn't do anything with the messages it gets
     */
    @UmlExcludeMember
    default boolean isDeaf()
    {
        return false;
    }

    /**
     * Registers this listener with the given broadcaster in being interested in transmitted messages
     *
     * @param broadcaster The broadcaster that should send to this listener
     * @param filter The message filter to apply
     * @return The broadcaster
     */
    default <T extends Broadcaster> T listenTo(T broadcaster, MessageFilter filter)
    {
        broadcaster.addListener(this, filter);
        return broadcaster;
    }

    /**
     * Registers this listener with the given broadcaster in being interested in transmitted messages
     *
     * @param broadcaster The broadcaster that should send to this listener
     * @return The broadcaster
     */
    default <T extends Broadcaster> T listenTo(T broadcaster)
    {
        if (broadcaster != null)
        {
            broadcaster.addListener(this);
        }
        else
        {
            warning("Null broadcaster");
        }

        return broadcaster;
    }

    /**
     * Functional interface method called when a message is received by this listener
     *
     * @param message The message
     */
    void onMessage(Message message);

    /**
     * <b>Not public API</b>
     */
    @Override
    @UmlExcludeMember
    default void onReceive(Transmittable transmittable)
    {
        if (transmittable instanceof Message)
        {
            onMessage((Message) transmittable);
        }
    }
}
