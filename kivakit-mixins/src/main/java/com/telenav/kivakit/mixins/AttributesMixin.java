package com.telenav.kivakit.mixins;

import java.util.HashMap;

/**
 * A {@link Mixin} that allows any object to have a set of attributes associated with it.
 *
 * @author jonathanl (shibo)
 */
public interface AttributesMixin<Key, Value> extends Mixin
{
    default Value attribute(Key key)
    {
        return attributes().get(key);
    }

    default Value attribute(Key key, Value value)
    {
        return attributes().put(key, value);
    }

    default HashMap<Key, Value> attributes()
    {
        return mixin(AttributesMixin.class, HashMap::new);
    }
}
