package com.telenav.kivakit.core.kernel.messaging.repeaters;

import com.telenav.kivakit.core.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.core.kernel.interfaces.messaging.Transmittable;
import com.telenav.kivakit.core.kernel.language.threading.context.CodeContext;
import com.telenav.kivakit.core.kernel.language.trait.Trait;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.kernel.messaging.Repeater;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * A stateful {@link Trait} that can be used when a class can't extend {@link BaseRepeater} to implement the {@link
 * Repeater} interface.
 *
 * @author jonathanl (shibo)
 * @see Trait
 */
@LexakaiJavadoc(complete = true)
public interface RepeaterTrait extends Trait, Repeater
{
    @Override
    default void addListener(final Listener listener, final Filter<Transmittable> filter)
    {
        repeater().addListener(listener, filter);
    }

    @Override
    default void clearListeners()
    {
        repeater().clearListeners();
    }

    @Override
    default CodeContext debugCodeContext()
    {
        return repeater().debugCodeContext();
    }

    @Override
    default void debugCodeContext(final CodeContext context)
    {
        repeater().debugCodeContext(context);
    }

    @Override
    default boolean hasListeners()
    {
        return repeater().hasListeners();
    }

    @Override
    default void onMessage(final Message message)
    {
        repeater().onMessage(message);
    }

    @Override
    default void removeListener(final Listener listener)
    {
        repeater().removeListener(listener);
    }

    /**
     * @return The {@link Repeater} implementation associated with this stateful trait
     */
    default Repeater repeater()
    {
        return value(RepeaterTrait.class, BaseRepeater::new);
    }

    @Override
    default void transmit(final Message message)
    {
        repeater().transmit(message);
    }
}