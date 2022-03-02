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

import com.telenav.kivakit.core.project.lexakai.DiagramRepeater;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.interfaces.messaging.Receiver;
import com.telenav.kivakit.interfaces.messaging.Transmittable;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

/**
 * A repeater is both a {@link Listener} and a {@link Broadcaster}, receiving messages in {@link
 * #receive(Transmittable)} and rebroadcasting them to its own listeners with {@link #transmit(Transmittable)}.
 * <p>
 * A variety of convenient methods are accessible when an object implements {@link Repeater} by extending {@link
 * BaseRepeater}. In the example below, EmployeeLoader is a repeater which transmits a warning to all of its registered
 * listeners. The PayrollProcessor class is also a {@link Repeater} which listens to messages transmitted by the
 * EmployeeLoader and re-transmits them to its own listeners. Clients of the PayrollProcessor can listen to it in turn
 * and they will receive the warning transmitted EmployeeLoader, when it is repeated by the PayrollProcessor. This
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
 * @see Listener
 */
@UmlClassDiagram(diagram = DiagramRepeater.class)
public interface Repeater extends
        Listener,
        Broadcaster,
        Receiver
{
    /**
     * True if this repeater is repeating messages it receives
     */
    default boolean isRepeating()
    {
        return true;
    }

    /**
     * Handles any received messages by re-broadcasting them
     */
    @Override
    @MustBeInvokedByOverriders
    default void onReceive(Transmittable message)
    {
        if (isRepeating())
        {
            transmit(message);
        }
    }

    /**
     * <b>Not public API</b>
     *
     * <p>
     * Repeaters re-broadcast messages they receive
     * </p>
     */
    @Override
    default <M extends Transmittable> M receive(M message)
    {
        onReceive(message);
        return message;
    }
}
