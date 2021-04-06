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

package com.telenav.kivakit.core.kernel.messaging.broadcasters;

import com.telenav.kivakit.core.kernel.interfaces.messaging.Transmittable;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.kivakit.core.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessageRepeater;

import java.util.Objects;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageRepeater.class)
class AudienceMember
{
    @UmlAggregation
    private final Listener listener;

    @UmlAggregation
    private final Filter<Transmittable> filter;

    AudienceMember(final Listener listener, final Filter<Transmittable> filter)
    {
        this.listener = listener;
        this.filter = filter;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof AudienceMember)
        {
            final AudienceMember that = (AudienceMember) object;
            return listener == that.listener;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(listener);
    }

    public Listener listener()
    {
        return listener;
    }

    public void receive(final Transmittable message)
    {
        if (filter.accepts(message))
        {
            listener.receive(message);
        }
    }
}
