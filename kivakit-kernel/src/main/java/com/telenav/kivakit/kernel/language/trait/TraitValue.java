package com.telenav.kivakit.kernel.language.trait;

import com.telenav.kivakit.kernel.interfaces.factory.Factory;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import java.util.HashMap;
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
    private static final Map<TraitKey, Object> values = new HashMap<>();

    /**
     * @return Gets a value for the given trait of the given object, creating a new value with the factory if it doesn't
     * already exist.
     */
    @SuppressWarnings("unchecked")
    public static synchronized <T> T get(final Object object,
                                         final Class<? extends Trait> traitType,
                                         final Factory<T> factory)
    {
        // Create a composite key (to allow multiple traits on an object),
        final var key = new TraitKey(object, traitType);

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
}
