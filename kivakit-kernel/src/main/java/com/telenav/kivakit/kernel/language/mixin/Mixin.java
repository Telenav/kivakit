package com.telenav.kivakit.kernel.language.mixin;

import com.telenav.kivakit.kernel.interfaces.factory.Factory;
import com.telenav.kivakit.kernel.messaging.Repeater;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.kernel.messaging.repeaters.RepeaterMixin;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * A poor-man's implementation of mixins, also known as <a href="https://scg.unibe.ch/archive/papers/Berg07aStatefulTraits.pdf">stateful
 * traits</a>, in Java. Interfaces that extend the {@link Mixin} superinterface can retrieve state values for the mixin
 * with {@link #state(Class, Factory)}. This obviates the need for hand delegation of an aggregate value when providing
 * an interface implementation for an object that already has a base class. For example, the {@link BaseRepeater} class
 * can be used when there is no base class for an object, or {@link RepeaterMixin} can be used if there is already a
 * base class. Both achieve the same result, namely implementing the {@link Repeater} interface.
 *
 * <p><b>Performance Note</b></p>
 * <p>
 * Because mixin values are retrieved from an identity map, performance is impacted versus a field implementation and
 * should be considered in design.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public interface Mixin
{
    /**
     * @param type The type of state to associate with this mixin
     * @param factory Factory that can create a new state to associate with this mixin
     * @return The state for this mixin
     */
    default <T> T state(Class<? extends Mixin> type, Factory<T> factory)
    {
        return MixinState.get(this, type, factory);
    }
}
