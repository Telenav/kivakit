package com.telenav.kivakit.kernel.language.mixin;

import com.telenav.kivakit.kernel.interfaces.factory.Factory;
import com.telenav.kivakit.kernel.language.objects.Hash;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stores the state objects for {@link Mixin}s, allowing {@link Mixin} interfaces to be stateful by having associated
 * values.
 *
 * @author jonathanl (shibo)
 * @see Mixin
 */
@LexakaiJavadoc(complete = true)
class MixinState
{
    private static final Map<MixinKey, Object> values = new ConcurrentHashMap<>();

    /**
     * @return Gets a value for the given mixin of the given object, creating a new value with the factory if it doesn't
     * already exist.
     */
    @SuppressWarnings("unchecked")
    public static synchronized <T> T get(final Object object,
                                         final Class<? extends Mixin> mixinType,
                                         final Factory<T> factory)
    {
        // Create a composite key (to allow multiple traits on an object),
        final var key = new MixinKey(object, mixinType);

        // get any current value for the mixin,
        var value = (T) values.get(key);

        // and if none exists,
        if (value == null)
        {
            // create a new value,
            value = factory.newInstance();

            // and store that.
            values.put(key, value);
        }

        return value;
    }

    /**
     * @author jonathanl (shibo)
     */
    private static class MixinKey
    {
        private final Object object;

        private final Class<? extends Mixin> mixinType;

        public MixinKey(final Object object, final Class<? extends Mixin> mixinType)
        {
            this.object = object;
            this.mixinType = mixinType;
        }

        @Override
        public boolean equals(final Object uncast)
        {
            if (uncast instanceof MixinKey)
            {
                final MixinKey that = (MixinKey) uncast;
                return object == that.object && mixinType == that.mixinType;
            }
            return false;
        }

        @Override
        public int hashCode()
        {
            return Hash.many(object, mixinType);
        }
    }
}
