package com.telenav.kivakit.kernel.messaging.repeaters;

import com.telenav.kivakit.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.kernel.interfaces.messaging.Transmittable;
import com.telenav.kivakit.kernel.language.mixin.Mixin;
import com.telenav.kivakit.kernel.language.threading.context.CodeContext;
import com.telenav.kivakit.kernel.messaging.Broadcaster;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.Repeater;
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
        return state(RepeaterMixin.class, BaseRepeater::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default void transmit(Message message)
    {
        repeater().transmit(message);
    }
}
