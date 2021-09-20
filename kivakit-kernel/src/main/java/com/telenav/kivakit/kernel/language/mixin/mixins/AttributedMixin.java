package com.telenav.kivakit.kernel.language.mixin.mixins;

import com.telenav.kivakit.kernel.language.mixin.Mixin;

import java.util.HashMap;

/**
 * A {@link Mixin} that allows any object to have a set of attributes associated with it.
 *
 * @author jonathanl (shibo)
 */
public interface AttributedMixin<Key, Value> extends Mixin
{
    default Value attribute(final Key key)
    {
        return map().get(key);
    }

    default Value attribute(final Key key, final Value value)
    {
        return map().put(key, value);
    }

    default HashMap<Key, Value> map()
    {
        return state(AttributedMixin.class, HashMap::new);
    }
}
