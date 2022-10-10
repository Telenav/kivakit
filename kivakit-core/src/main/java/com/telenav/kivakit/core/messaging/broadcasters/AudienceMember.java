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

package com.telenav.kivakit.core.messaging.broadcasters;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramRepeater;
import com.telenav.kivakit.core.messaging.Broadcaster;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.interfaces.messaging.Transmittable;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.util.Objects;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A filtered {@link Multicaster} listener
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramRepeater.class)
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE,
             audience = AUDIENCE_INTERNAL)
class AudienceMember
{
    /** The listener */
    @UmlAggregation
    private final Listener listener;

    /** Any filter */
    @UmlAggregation
    private final Filter<Transmittable> filter;

    /**
     * Creates an audience member for a {@link Broadcaster} that listens to messages from the given listener. The
     * messages are filtered by the given filter.
     *
     * @param listener The listener
     * @param filter The filter
     */
    AudienceMember(Listener listener, Filter<Transmittable> filter)
    {
        this.listener = listener;
        this.filter = filter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof AudienceMember)
        {
            AudienceMember that = (AudienceMember) object;
            return listener == that.listener;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(listener);
    }

    /**
     * The listener
     */
    public Listener listener()
    {
        return listener;
    }

    /**
     * If the given filter accepts the message, transmits it to the listener.
     */
    public void receive(Transmittable message)
    {
        if (filter.accepts(message))
        {
            listener.receive(message);
        }
    }
}
