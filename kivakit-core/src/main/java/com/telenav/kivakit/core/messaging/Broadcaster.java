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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramBroadcaster;
import com.telenav.kivakit.core.internal.lexakai.DiagramRepeater;
import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.interfaces.messaging.Transmittable;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.util.List;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_DEFAULT_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

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
 * @see <a href="https://state-of-the-art.org#broadcaster">State(Art) Blog Article</a>
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramBroadcaster.class)
@UmlClassDiagram(diagram = DiagramRepeater.class)
@UmlRelation(label = "transmits", referent = Listener.class, refereeCardinality = "1", referentCardinality = "*")
@ApiQuality(stability = STABLE_DEFAULT_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public interface Broadcaster extends MessageTransceiver
{
    /**
     * Adds a listener to this broadcaster that wants to receive future messages. This is the mirror method of
     * {@link Listener#listenTo(Broadcaster)} as both methods achieve the same thing.
     *
     * <p>
     * <i>Note: adding the same listener two or more times will not replace the original listener. To do that, call
     * {@link #clearListeners()} before calling this method</i>
     * </p>
     *
     * @param listener Listener to broadcast to. Implementations should ignore null listeners.
     * @param filter The filter to apply
     */
    void addListener(Listener listener, Filter<Transmittable> filter);

    /**
     * Adds a listener to this broadcaster that wants to receive future messages.
     *
     * <p>
     * <i>Note: adding the same listener two or more times will not replace the original listener. To do that, call
     * {@link #clearListeners()} before calling this method</i>
     * </p>
     */
    default void addListener(Listener listener)
    {
        addListener(listener, Filter.acceptingAll());
    }

    /**
     * Removes all listeners from this broadcaster
     */
    void clearListeners();

    /**
     * Copies the listeners of another broadcaster
     */
    default void copyListenersFrom(Broadcaster that)
    {
        that.listeners().forEach(this::addListener);
    }

    /**
     * @return True if this broadcaster has any listeners
     */
    boolean hasListeners();

    /**
     * @return The listeners to this broadcaster
     */
    List<Listener> listeners();

    /**
     * <b>Not public API</b>
     */
    Broadcaster messageSource();

    /**
     * <b>Not public API</b>
     */
    void messageSource(Broadcaster parent);

    /**
     * A broadcaster handles a message by transmitting it
     */
    @Override
    default void onReceive(Transmittable message)
    {
        transmit(message);
    }

    /**
     * Allows subclass to transmit message to listeners
     */
    @Override
    default void onTransmit(Transmittable message)
    {
        receive(message);
    }

    /**
     * Allows subclass to process a message after it is transmitted
     *
     * @param message The message
     */
    default void onTransmitted(Transmittable message)
    {
    }

    /**
     * Allows subclass to process a message before it is transmitted
     */
    default void onTransmitting(Transmittable message)
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
        addListener(Listener.emptyListener());
    }

    /**
     * <b>Not public API</b>
     *
     * <p>
     * Transmits the given message by calling the subclass
     * </p>
     */
    @Override
    default <M extends Transmittable> M transmit(M message)
    {
        onTransmitting(message);
        onTransmit(message);
        onTransmitted(message);

        return message;
    }

    /**
     * <b>Not public API</b>
     *
     * <p>
     * Broadcasts the given message to any listeners in the audience of this broadcaster
     * </p>
     */
    default <T extends Message> T transmit(T message)
    {
        transmit((Transmittable) message);
        return message;
    }

    /**
     * <b>Not public API</b>
     *
     * <p>
     * Broadcasts the given messages to any listeners in the audience of this broadcaster
     * </p>
     *
     * @param messages The messages to broadcast
     */
    default void transmitAll(Iterable<Transmittable> messages)
    {
        messages.forEach(this::transmit);
    }
}
