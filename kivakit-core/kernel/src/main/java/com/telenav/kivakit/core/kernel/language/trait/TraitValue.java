package com.telenav.kivakit.core.kernel.language.trait;

import com.telenav.kivakit.core.kernel.interfaces.factory.Factory;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Stores values for {@link Trait}s, allowing {@link Trait} interfaces to be stateful by having associated values.
 *
 * @author jonathanl (shibo)
 * @see Trait
 */
@LexakaiJavadoc(complete = true)
class TraitValue
{
    private static final Map<TraitValue, Object> values = new IdentityHashMap<>();

    /**
     * @return Gets a value for the given trait of the given object, creating a new value with the factory if it doesn't
     * already exist.
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(final Object object,
                            final Class<? extends Trait> type,
                            final Factory<T> factory)
    {
        // Create a composite key (to allow multiple traits on an object),
        final var key = new TraitValue();
        key.object = object;
        key.type = type;

        // get any current value for the trait,
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

    Object object;

    Class<? extends Trait> type;

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof TraitValue)
        {
            final TraitValue that = (TraitValue) object;
            return object == that.object && type == that.type;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return (int) (object.hashCode() ^ ((long) type.hashCode() >>> 32));
    }
}
