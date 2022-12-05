package com.telenav.kivakit.core.messaging.broadcasters;

import com.telenav.kivakit.core.messaging.Broadcaster;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.interfaces.messaging.Transmittable;

import java.util.List;

import static com.telenav.kivakit.core.KivaKit.globalListener;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;

/**
 * Broadcasts messages to the global listener
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public interface GlobalRepeater extends Repeater
{
    @Override
    default void addListener(Listener listener, Filter<Transmittable> filter)
    {
    }

    @Override
    default void clearListeners()
    {
    }

    @Override
    default boolean hasListeners()
    {
        return true;
    }

    @Override
    default boolean isRepeating()
    {
        return true;
    }

    @Override
    default List<Listener> listeners()
    {
        return list(globalListener());
    }

    @Override
    default void messageSource(Broadcaster parent)
    {
    }

    @Override
    default Broadcaster messageSource()
    {
        return this;
    }

    @Override
    default boolean ok()
    {
        return true;
    }

    @Override
    default void onMessage(Message message)
    {
        transmit(message);
    }

    @Override
    default void removeListener(Listener listener)
    {
    }

    @Override
    default <T extends Transmittable> T transmit(T transmittable)
    {
        if (transmittable instanceof Message message)
        {
            globalListener().receive(message);
        }
        return transmittable;
    }
}
