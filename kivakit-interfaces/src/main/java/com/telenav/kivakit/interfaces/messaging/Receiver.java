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

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.interfaces.internal.lexakai.DiagramMessaging;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.util.function.Consumer;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.SUFFICIENT;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNNECESSARY;

/**
 * A receiver of  {@link Transmittable} messages with similar function to a {@link Consumer}, a sink, a callback or a
 * target.
 *
 * <p>
 * If the {@link #isReceiving()} method returns true, then a call to {@link #receive(Transmittable)} will result in a
 * call to {@link #onReceive(Transmittable)}.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see Transmittable
 */
@UmlClassDiagram(diagram = DiagramMessaging.class)
@UmlRelation(label = "receives", referent = Transmittable.class)
@FunctionalInterface
@CodeQuality(stability = STABLE,
             testing = UNNECESSARY,
             documentation = SUFFICIENT)
public interface Receiver
{
    /**
     * @return True if this receiver is enabled
     */
    default boolean isReceiving()
    {
        return true;
    }

    /**
     * Method that receives a message
     *
     * @param message The message
     */
    void onReceive(Transmittable message);

    /**
     * <b>Not public API</b>
     *
     * <p>
     * If this transceiver is enabled, passes the message to {@link #onReceive(Transmittable)}
     * </p>
     *
     * @param message The message
     * @param <MessageType> The type of message
     * @return The message
     */
    default <MessageType extends Transmittable> MessageType receive(MessageType message)
    {
        if (isReceiving())
        {
            onReceive(message);
        }
        return message;
    }
}
