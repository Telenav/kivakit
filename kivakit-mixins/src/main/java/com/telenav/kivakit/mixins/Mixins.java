package com.telenav.kivakit.mixins;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.interfaces.factory.Factory;
import com.telenav.kivakit.interfaces.naming.NamedObject;
import com.telenav.kivakit.mixins.internal.lexakai.DiagramMixin;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static java.util.Objects.hash;

/**
 * Stores the state objects for {@link Mixin}s, allowing {@link Mixin} interfaces to be stateful by having associated
 * values.
 *
 * <p><b>Mixins</b></p>
 *
 * <ul>
 *     <li>{@link #owner(Object)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see Mixin
 */
@UmlClassDiagram(diagram = DiagramMixin.class)
@TypeQuality(stability = STABLE,
             testing = TESTED,
             documentation = DOCUMENTED)
public class Mixins
{
    /** Map from an object and mixin type to the attached mixin state */
    private static final Map<MixinKey, Object> mixin = new ConcurrentHashMap<>();

    /** Map from mixin state to the object that owns it */
    private static final Map<Object, Object> owner = new IdentityHashMap<>();

    /**
     * <b>Not public API</b>
     *
     * <p>
     * Creates or accesses mixin state of a given type that is associated with an object. The object and mixin type are
     * used as a key to look up state created by the given mixin state factory.
     * </p>
     *
     * @param object The object to attach mixin state to
     * @param mixinType The identity of the mixin that's being attached (multiple mixins can be attached to an object)
     * @param mixinStateFactory A factory that creates mixin state objects
     * @return The mixin state of the given type attached to the given object
     */
    @SuppressWarnings("unchecked")
    public static synchronized <State> State mixin(Object object,
                                                   Class<? extends Mixin> mixinType,
                                                   Factory<State> mixinStateFactory)
    {
        // Create a composite key (to allow multiple traits on an object),
        var key = new MixinKey(object, mixinType);

        // get any current value for the mixin,
        var value = (State) mixin.get(key);

        // and if none exists,
        if (value == null)
        {
            // create a new value,
            value = mixinStateFactory.newInstance();
            if (value instanceof NamedObject)
            {
                ((NamedObject) value).objectName(key.toString());
            }

            // and store that,
            mixin.put(key, value);

            // and a mapping from the value back to the object it's attached to.
            owner.put(value, object);
        }

        return value;
    }

    /**
     * Returns the object that the given state was attached to
     *
     * @param state The mixin state
     * @return The object that owns the mixin state
     */
    public static synchronized <State> Object owner(State state)
    {
        return owner.get(state);
    }

    /**
     * <b>Not public API</b>
     *
     * @author jonathanl (shibo)
     */
    @SuppressWarnings("SpellCheckingInspection")
    @TypeQuality(audience = AUDIENCE_INTERNAL,
                 stability = STABLE,
                 testing = TESTED,
                 documentation = DOCUMENTED)
    private static class MixinKey
    {
        private final Object attachTo;

        private final Class<? extends Mixin> mixinType;

        public MixinKey(Object attachTo, Class<? extends Mixin> mixinType)
        {
            this.attachTo = attachTo;
            this.mixinType = mixinType;
        }

        @Override
        public boolean equals(Object uncast)
        {
            if (uncast instanceof MixinKey that)
            {
                return attachTo == that.attachTo && mixinType == that.mixinType;
            }
            return false;
        }

        @Override
        public int hashCode()
        {
            return hash(attachTo, mixinType);
        }

        @Override
        public String toString()
        {
            return "[Mixin object = " + attachTo.getClass() + ", mixin = " + mixinType + "]";
        }
    }
}
