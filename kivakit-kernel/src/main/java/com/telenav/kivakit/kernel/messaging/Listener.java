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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telenav.kivakit.kernel.interfaces.messaging.Transmittable;
import com.telenav.kivakit.kernel.interfaces.naming.NamedObject;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.messaging.listeners.ConsoleWriter;
import com.telenav.kivakit.kernel.messaging.listeners.NullListener;
import com.telenav.kivakit.kernel.messaging.listeners.ThrowingListener;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramDataFailureReporter;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLogging;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessageBroadcaster;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessageListener;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessageRepeater;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

/**
 * Handles messages through {@link #onMessage(Message)}.
 *
 * <p><b>Listening to Broadcasters</b></p>
 * <p>
 * A listener can listen to a particular {@link Broadcaster} with {@link #listenTo(Broadcaster)}. Conversely, a listener
 * can be added to a broadcaster with {@link Broadcaster#addListener(Listener)}. Both methods achieve the same result.
 * </p>
 *
 * <p><b>Convenience Methods and Logging</b></p>
 *
 * <p>
 * A number of convenience methods in the {@link Transceiver} superinterface make it easy to transmit specific common
 * messages to listeners. These convenience methods are particularly useful in logging because {@link Logger}s are
 * {@link Listener}s which log the messages they receive. For example:
 * </p>
 *
 * <pre>
 * private static final Logger LOGGER = LoggerFactory.newLogger();
 *
 *     [...]
 *
 * LOGGER.warning("Unable to read $", file);
 * </pre>
 *
 * <p><b>Repeater Chains</b></p>
 *
 * <p>
 * These convenience methods are even easier to access when an object implements {@link Repeater} by extending {@link
 * BaseRepeater}. In the example below, EmployeeLoader is a {@link Repeater} which transmits a warning to all of its
 * registered listeners. The PayrollProcessor class is also a {@link Repeater} which listens to messages transmitted by
 * the EmployeeLoader and re-transmits them to its own listeners. Clients of the PayrollProcessor can listen to it in
 * turn and they will receive the warning transmitted EmployeeLoader, when it is repeated by the PayrollProcessor. This
 * pattern creates a chain of repeaters that terminates in one or more listeners. The final listener is often, but not
 * always a logger. The base Application class in kivakit-application, for example, is a {@link Repeater} which logs the
 * messages it receives by default.
 * </p>
 * <p>
 * <b>Repeater Example</b>
 * </p>
 * <pre>
 * class EmployeeLoader extends BaseRepeater
 * {
 *
 *     [...]
 *
 *     warning("Unable to load $", employee);
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
 * var processor = LOGGER.listenTo(new PayrollProcessor());
 *
 * </pre>
 *
 * @author jonathanl (shibo)
 * @see Broadcaster
 * @see <a href="https://state-of-the-art.org#broadcaster">State(Art) Blog Article</a>
 */
@UmlClassDiagram(diagram = DiagramMessageBroadcaster.class)
@UmlClassDiagram(diagram = DiagramMessageRepeater.class)
@UmlClassDiagram(diagram = DiagramMessageListener.class)
@UmlClassDiagram(diagram = DiagramLogging.class)
@UmlClassDiagram(diagram = DiagramDataFailureReporter.class)
@UmlExcludeSuperTypes({ NamedObject.class })
@FunctionalInterface
public interface Listener extends Transceiver
{
    /**
     * @return A listener that does nothing with messages. Useful only when you want to discard output from something
     */
    static Listener console()
    {
        return new ConsoleWriter();
    }

    /**
     * @return A listener that does nothing with messages. Useful only when you want to discard output from something
     */
    static Listener none()
    {
        return new NullListener();
    }

    /**
     * @return A listener that throws exceptions
     */
    static Listener throwing()
    {
        return new ThrowingListener();
    }

    /**
     * @return True if this listener doesn't do anything with the messages it gets
     */
    @UmlExcludeMember
    @JsonIgnore
    default boolean isDeaf()
    {
        return false;
    }

    /**
     * Registers this listener with the given broadcaster in being interested in transmitted messages
     */
    default <T extends Broadcaster> T listenTo(final T broadcaster, final MessageFilter filter)
    {
        broadcaster.addListener(this, filter);
        return broadcaster;
    }

    /**
     * Registers this listener with the given broadcaster in being interested in transmitted messages
     */
    default <T extends Broadcaster> T listenTo(final T broadcaster)
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
    void onMessage(final Message message);

    /**
     * <b>Not public API</b>
     */
    @UmlExcludeMember
    default void onReceive(final Transmittable transmittable)
    {
        if (transmittable instanceof Message)
        {
            onMessage((Message) transmittable);
        }
    }
}
