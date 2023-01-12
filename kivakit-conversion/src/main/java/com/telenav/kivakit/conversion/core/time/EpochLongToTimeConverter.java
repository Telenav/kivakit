package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.conversion.BaseTwoWayConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Time;

import static com.telenav.kivakit.core.time.Time.epochMilliseconds;

public class EpochLongToTimeConverter extends BaseTwoWayConverter<Long, Time>
{
    /**
     * @param listener Listener to report problems to
     */
    public EpochLongToTimeConverter(Listener listener)
    {
        super(listener, Long.class, Time.class);
    }

    @Override
    protected Time onConvert(Long value)
    {
        return epochMilliseconds(value);
    }

    @Override
    protected Long onUnconvert(Time time)
    {
        return time.epochMilliseconds();
    }
}
