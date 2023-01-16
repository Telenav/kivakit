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

package com.telenav.kivakit.interfaces.messaging;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.interfaces.code.Code;
import com.telenav.kivakit.interfaces.internal.lexakai.DiagramMessaging;
import com.telenav.kivakit.interfaces.value.Source;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.util.function.Supplier;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * A transmitter of values with similar function to a {@link Source}, or a {@link Supplier}.
 *
 * <p>
 * If the {@link #isTransmitting()} method returns true, then a call to {@link #transmit(Transmittable)} will result in
 * a call to {@link #onTransmit(Transmittable)}.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see Transmittable
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramMessaging.class)
@UmlRelation(label = "transmits", referent = Transmittable.class)
@TypeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public interface Transmitter
{
    /**
     * Returns true if this transmitter is enabled
     */
    default boolean isTransmitting()
    {
        return true;
    }

    /**
     * Method that transmits a message
     *
     * @param message The message
     */
    default void onTransmit(Transmittable message)
    {
    }

    /**
     * <b>Not public API</b>
     *
     * <p>
     * If this transmitter is enabled, passes the message to {@link #onTransmit(Transmittable)}
     * </p>
     *
     * @param message The message
     * @param <MessageType> The type of message
     * @return The message
     */
    default <MessageType extends Transmittable> MessageType transmit(MessageType message)
    {
        if (isTransmitting())
        {
            onTransmit(message);
        }
        return message;
    }

    /**
     * Turns this transmitter on/off
     *
     * @param enable True to enable this transmitter, false to disable it
     */
    default void transmitting(boolean enable)
    {
    }

    default <T> T withoutTransmitting(Code<T> code)
    {
        var original = isTransmitting();
        transmitting(false);
        try
        {
            return code.run();
        }
        finally
        {

            transmitting(original);
        }
    }
}
