package com.telenav.kivakit.mixins;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.interfaces.factory.Factory;
import com.telenav.kivakit.mixins.internal.lexakai.DiagramMixin;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A poor-man's implementation of mixins, also known as <a
 * href="https://scg.unibe.ch/archive/papers/Berg07aStatefulTraits.pdf">stateful traits</a>, in Java. Interfaces that
 * extend the {@link Mixin} superinterface can retrieve state values for the mixin with {@link #mixin(Class, Factory)}.
 * This obviates the need for hand delegation of an aggregate value when providing an interface implementation for an
 * object that already has a base class. For example, the <i>BaseRepeater</i>> class can be used when there is no base
 * class for an object, or <i>RepeaterMixin</i> can be used if there is already a base class. Both achieve the same
 * result, namely implementing the <i>Repeater</i> interface.
 *
 * <p><b>Performance Note</b></p>
 * <p>
 * Because mixin values are retrieved from an identity map, performance is impacted versus a field implementation and
 * should be considered in design.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramMixin.class)
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public interface Mixin
{
    /**
     * Attaches or retrieves mixin state for this object. The given {@link Mixin} type is used to distinguish between
     * different mixins that may be attached to an object. If there is not yet any state of the given {@link Mixin} type
     * attached, the given mixin state factory is used to create and attach an initial value.
     *
     * @param mixinType The mixin type
     * @param mixinStateFactory Factory to create an initial MixinState object to associate with this object
     * @return The mixin state of the given type for this object
     */
    default <MixinState> MixinState mixin(Class<? extends Mixin> mixinType, Factory<MixinState> mixinStateFactory)
    {
        return Mixins.mixin(this, mixinType, mixinStateFactory);
    }

    /**
     * @param state The mixin state
     * @return The object that owns the mixin state (the object to which the state is attached)
     */
    default <MixinState> Object owner(MixinState state)
    {
        return Mixins.owner(state);
    }
}
