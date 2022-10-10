package com.telenav.kivakit.core.messaging.repeaters;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.messaging.Broadcaster;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.messaging.context.CodeContext;
import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.interfaces.messaging.Transmittable;
import com.telenav.kivakit.mixins.Mixin;

import java.util.List;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A stateful {@link Mixin} that can be used when a class can't extend {@link BaseRepeater} to implement the
 * {@link Repeater} interface.
 *
 * @author jonathanl (shibo)
 * @see Mixin
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public interface RepeaterMixin extends
        Repeater,
        Mixin
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
    default void copyListenersFrom(Broadcaster broadcaster)
    {
        repeater().copyListenersFrom(broadcaster);
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
     * <p>
     * Returns the {@link Repeater} implementation associated with this mixin
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
