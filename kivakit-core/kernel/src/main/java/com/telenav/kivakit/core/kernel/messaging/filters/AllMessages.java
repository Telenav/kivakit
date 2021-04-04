package com.telenav.kivakit.core.kernel.messaging.filters;

import com.telenav.kivakit.core.kernel.interfaces.messaging.Transmittable;
import com.telenav.kivakit.core.kernel.messaging.MessageFilter;

/**
 * @author jonathanl (shibo)
 */
public class AllMessages implements MessageFilter
{
    @Override
    public boolean accepts(final Transmittable message)
    {
        return true;
    }
}
