package com.telenav.kivakit.component;

import com.telenav.kivakit.kernel.language.mixin.Mixin;
import com.telenav.kivakit.kernel.messaging.Repeater;
import com.telenav.kivakit.kernel.messaging.repeaters.RepeaterMixin;

/**
 * A mixin for {@link BaseComponent} which can be used by a class that already extends another base class.
 *
 * @author jonathanl (shibo)
 * @see Component
 * @see Mixin
 */
public interface ComponentMixin extends Component, Mixin, RepeaterMixin
{
    /**
     * @return The {@link BaseComponent} implementation associated with this mixin
     */
    default BaseComponent component()
    {
        return state(ComponentMixin.class, BaseComponent::new);
    }

    @Override
    default String objectName()
    {
        return component().objectName();
    }

    @Override
    default void objectName(final String objectName)
    {
        component().objectName(objectName);
    }

    @Override
    default Repeater repeater()
    {
        return component();
    }
}
