package com.telenav.kivakit.service.registry.serialization.serializers;

import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.serialization.json.PrimitiveGsonSerializer;

/**
 * @author jonathanl (shibo)
 */
public class TimeSerializer extends PrimitiveGsonSerializer<Time, Long>
{
    public TimeSerializer()
    {
        super(Long.class);
    }

    @Override
    protected Time toObject(final Long identifier)
    {
        return Time.milliseconds(identifier);
    }

    @Override
    protected Long toPrimitive(final Time time)
    {
        return time.asMilliseconds();
    }
}
