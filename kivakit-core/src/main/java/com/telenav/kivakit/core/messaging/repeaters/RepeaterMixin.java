package com.telenav.kivakit.core.messaging.repeaters;

import com.telenav.kivakit.core.messaging.Broadcaster;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.interfaces.messaging.Transmittable;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.messaging.context.CodeContext;
import com.telenav.kivakit.mixins.Mixin;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import java.util.List;

/**
 * A stateful {@link Mixin} that can be used when a class can't extend {@link BaseRepeater} to implement the {@link
 * Repeater} interface.
 *
 * @author jonathanl (shibo)
 * @see Mixin
 */
@LexakaiJavadoc(complete = true)
public interface RepeaterMixin extends Repeater, Mixin
{
    /**
     * {@inheritDoc}
     */
    @Override
    default void addListener(Listener listener, Filter<Transmittable> filter)
    {
        repeater().addListener(listener, filter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default void clearListeners()
    {
        repeater().clearListeners();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default void copyListeners(Broadcaster broadcaster)
    {
        repeater().copyListeners(broadcaster);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default CodeContext debugCodeContext()
    {
        return repeater().debugCodeContext();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default void debugCodeContext(CodeContext context)
    {
        repeater().debugCodeContext(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default boolean hasListeners()
    {
        return repeater().hasListeners();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default boolean isReceiving()
    {
        return repeater().isReceiving();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default boolean isTransmitting()
    {
        return repeater().isTransmitting();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default List<Listener> listeners()
    {
        return repeater().listeners();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default void messageSource(Broadcaster source)
    {
        repeater().messageSource(source);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default Broadcaster messageSource()
    {
        return repeater().messageSource();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default void onMessage(Message message)
    {
        repeater().onMessage(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <M extends Transmittable> M receive(M message)
    {
        return repeater().receive(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default void removeListener(Listener listener)
    {
        repeater().removeListener(listener);
    }

    /**
     * <b>Not public API</b>
     *
     * @return The {@link Repeater} implementation associated with this mixin
     */
    default Repeater repeater()
    {
        return mixin(RepeaterMixin.class, BaseRepeater::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <T extends Message> T transmit(T message)
    {
        return repeater().transmit(message);
    }
}
