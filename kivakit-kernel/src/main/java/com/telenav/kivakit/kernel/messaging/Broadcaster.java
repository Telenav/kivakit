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

package com.telenav.kivakit.kernel.messaging;

import com.telenav.kivakit.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.kernel.interfaces.messaging.Transmittable;
import com.telenav.kivakit.kernel.interfaces.messaging.Transmitter;
import com.telenav.kivakit.kernel.messaging.filters.operators.All;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessageBroadcaster;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessageRepeater;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

/**
 * Broadcasts a message to zero or more listeners via {@link #transmit(Transmittable)}. Listeners can be added with
 * {@link #addListener(Listener)} and can be cleared with {@link #clearListeners()}. A number of convenience methods in
 * the {@link Transceiver} superclass  make it easier to broadcast specific messages.
 *
 * <p><b>Convenience Methods</b></p>
 *
 * <p>
 * A number of convenience methods in the {@link Transceiver} superinterface make it easy to transmit specific common
 * messages. {@link Repeater}s are {@link Broadcaster}s (as well as {@link Listener}s) which inherit the methods in this
 * class as well as those in {@link Transceiver}. For details on how to take advantage of this in object design, see
 * {@link Repeater}.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see Transceiver
 * @see Broadcaster
 * @see Repeater
 * @see Listener
 * @see Message
 */
@UmlClassDiagram(diagram = DiagramMessageBroadcaster.class)
@UmlClassDiagram(diagram = DiagramMessageRepeater.class)
@UmlRelation(label = "transmits", referent = Listener.class, refereeCardinality = "1", referentCardinality = "*")
public interface Broadcaster extends Transceiver, Transmitter<Transmittable>
{
    /**
     * Adds a listener to this broadcaster that wants to receive future messages. This is the mirror method of {@link
     * Listener#listenTo(Broadcaster)} as both methods achieve the same thing.
     *
     * @param listener Listener to broadcast to. Implementations should ignore null listeners.
     */
    void addListener(final Listener listener, Filter<Transmittable> filter);

    /**
     * Adds a listener to this broadcaster that wants to receive future messages.
     */
    default void addListener(final Listener listener)
    {
        addListener(listener, new All<>());
    }

    /**
     * Removes all listeners from this broadcaster
     */
    void clearListeners();

    /**
     * @return True if this broadcaster has any listeners
     */
    boolean hasListeners();

    /**
     * A broadcaster handles a message by transmitting it
     */
    @Override
    default void onHandle(final Transmittable message)
    {
        transmit(message);
    }

    /**
     * <b>Not public API</b>
     * <p>
     * Allows subclass to transmit message to listeners
     * </p>
     */
    default void onTransmit(final Transmittable message)
    {
    }

    /**
     * Removes the given listener from this broadcaster
     */
    void removeListener(Listener listener);

    /**
     * Causes this broadcaster to broadcast only to the null listener
     */
    default void silence()
    {
        clearListeners();
        addListener(Listener.none());
    }

    /**
     * <b>Not public API</b>
     * <p>
     * Transmits the given message by calling the subclass
     * </p>
     */
    @Override
    default void transmit(final Transmittable message)
    {
        onTransmit(message);
    }

    default void transmit(final Message message)
    {
        transmit((Transmittable) message);
    }

    /**
     * Broadcasts the given messages to any listeners in the audience of this broadcaster
     *
     * @param messages The messages to broadcast
     */
    default void transmitAll(final Iterable<Transmittable> messages)
    {
        messages.forEach(this::transmit);
    }
}
