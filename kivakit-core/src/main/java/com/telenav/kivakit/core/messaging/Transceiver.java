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

import com.telenav.kivakit.core.internal.lexakai.DiagramBroadcaster;
import com.telenav.kivakit.core.internal.lexakai.DiagramListener;
import com.telenav.kivakit.core.internal.lexakai.DiagramLogging;
import com.telenav.kivakit.core.messaging.context.CodeContext;
import com.telenav.kivakit.core.messaging.messages.lifecycle.OperationHalted;
import com.telenav.kivakit.core.messaging.messages.status.Announcement;
import com.telenav.kivakit.core.messaging.messages.status.FatalProblem;
import com.telenav.kivakit.core.messaging.messages.status.Glitch;
import com.telenav.kivakit.core.messaging.messages.status.Information;
import com.telenav.kivakit.core.messaging.messages.status.Narration;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.messages.status.Quibble;
import com.telenav.kivakit.core.messaging.messages.status.Trace;
import com.telenav.kivakit.core.messaging.messages.status.Warning;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.time.Frequency;
import com.telenav.kivakit.interfaces.messaging.Receiver;
import com.telenav.kivakit.interfaces.messaging.Transmittable;
import com.telenav.kivakit.interfaces.messaging.Transmitter;
import com.telenav.kivakit.interfaces.naming.NamedObject;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.UmlNote;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

/**
 * Functionality that is common to {@link Broadcaster}s, {@link Listener}s, {@link Repeater}s and potentially other
 * classes that are involved in handling messages. The {@link #isTransmitting()} method returns true if transmitting is
 * enabled. The {@link #isReceiving()} method returns true if receiving is enabled. When enabled, the
 * {@link #receive(Transmittable)} method calls {@link #onReceive(Transmittable)} (Transmittable)} to allow the subclass
 * to handle the message.
 *
 * <p><b>Convenience Methods</b></p>
 * <p>
 * The following convenience methods call {@link #receive(Transmittable)} with the appropriate class of message.
 * </p>
 * <ul>
 *     <li>announce*() - The sends a formatted {@link Announcement} message to this {@link Transceiver}</li>
 *     <li>trace*() - The sends a formatted {@link Trace} message to this {@link Transceiver}</li>
 *     <li>information*() - The sends a formatted {@link Information} message to this {@link Transceiver}</li>
 *     <li>narrate() - The sends a formatted {@link Narration} message to this {@link Transceiver}</li>
 *     <li>warning*() - The sends a formatted {@link Warning} message to this {@link Transceiver}</li>
 *     <li>glitch*() - The sends a formatted {@link Glitch} message to this {@link Transceiver}/li>
 *     <li>quibble*() - The sends a formatted {@link Quibble} message to this {@link Transceiver}/li>
 *     <li>problem*() - The listener handles a {@link Problem} message</li>
 *     <li>fatal*() - The sends a formatted {@link FatalProblem} message to this {@link Transceiver}</li>
 *     <li>halted*() - The sends a formatted {@link OperationHalted} message to this {@link Transceiver}</li>
 * </ul>
 *
 * <p><b>Debugging</b></p>
 * <p>
 * A {@link Transceiver} provides access to a {@link Debug} object which has the class context (also {@link
 * CodeContext}) provided by the method {@link #debugClassContext()}.
 * <p>
 * The {@link #isDebugOn()} provides convenient access to {@link Debug#isDebugOn()} and the {@link #ifDebug(Runnable)}
 * executes the given code if debugging is on. Several convenience methods also provide tracing which originates {@link
 * Trace} messages only if debugging is on.
 * </p>
 * <p>
 * Because {@link Broadcaster}s, {@link Listener}s and {@link Repeater}s are debug transceivers, they inherit all the
 * methods in this class. This means that a subclass of {@link BaseRepeater} can simply call a trace() or glitch()
 * method and it will automatically be gated by the functionality of {@link Debug}. This makes it especially easy to
 * declare and control debug tracing. In the example below, the trace() statement can be enabled by running the
 * application with -DKIVAKIT_DEBUG=EmployeeLoader. See @{@link Debug} for more details on the KIVAKIT_DEBUG syntax.
 * </p>
 * <p>
 * <b>Debug Tracing Example</b>
 * </p>
 * <pre>
 * class EmployeeLoader extends BaseRepeater
 * {
 *
 *     [...]
 *
 *     trace("Loaded $ employees", employees.size());
 *
 * }
 * </pre> * @author jonathanl (shibo)
 *
 * @see Listener
 * @see Broadcaster
 * @see Message
 * @see Trace
 * @see Information
 * @see Warning
 * @see Glitch
 * @see Problem
 * @see OperationHalted
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" }) @UmlClassDiagram(diagram = DiagramLogging.class)
@UmlClassDiagram(diagram = DiagramBroadcaster.class)
@UmlClassDiagram(diagram = DiagramListener.class)
@UmlRelation(label = "delegates to", referent = Debug.class)
@UmlExcludeSuperTypes({ NamedObject.class })
@UmlNote(text = "Functionality common to transmitters and receivers")
public interface Transceiver extends
        NamedObject,
        Receiver,
        Transmitter
{
    @Override
    default void onTransmit(Transmittable message)
    {
        receive(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default void onReceive(Transmittable message)
    {
    }
}
