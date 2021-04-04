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
