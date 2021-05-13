package com.telenav.kivakit.kernel.language.trait;

import com.telenav.kivakit.kernel.interfaces.factory.Factory;
import com.telenav.kivakit.kernel.messaging.Repeater;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * A poor-man's version of <a href="http://scg.unibe.ch/archive/papers/Berg07aStatefulTraits.pdf">stateful traits</a> in
 * Java. Interfaces that extend the {@link Trait} superinterface can retrieve state values for the trait with {@link
 * #traitValue(Class, Factory)}. This obviates the need for hand delegation of an aggregate value when providing an
 * interface implementation for an object that already has a base class. For example, the {@link BaseRepeater} class can
 * be used when there is no base class for an object, or RepeaterTrait can be used if there is already a base class.
 * Both achieve the same result, namely implementing the {@link Repeater} interface.
 *
 * <p><b>Performance Note</b></p>
 * <p>
 * Because trait values are retrieved from an identity map, performance is impacted versus a field implementation and
 * should be considered in design.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public interface Trait
{
    default <T> T traitValue(final Class<? extends Trait> type, final Factory<T> factory)
    {
        return TraitValue.get(this, type, factory);
    }
}
