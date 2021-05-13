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

package com.telenav.kivakit.serialization.core;

import com.telenav.kivakit.kernel.interfaces.factory.Factory;
import com.telenav.kivakit.kernel.messaging.Listener;

/**
 * Creates new thread-local instances of {@link SerializationSession} using the {@link Factory} passed to the
 * constructor.
 *
 * @author jonathanl (shibo)
 */
public class SerializationSessionFactory
{
    /** Factory that produces serialization objects */
    private final Factory<SerializationSession> factory;

    /**
     * @param factory The factory for creating {@link SerializationSession} objects
     */
    public SerializationSessionFactory(final Factory<SerializationSession> factory)
    {
        this.factory = factory;
    }

    /**
     * @return A thread-local {@link SerializationSession} object with only the given listener
     */
    public SerializationSession session(final Listener listener)
    {
        return listener.listenTo(factory.newInstance());
    }
}
