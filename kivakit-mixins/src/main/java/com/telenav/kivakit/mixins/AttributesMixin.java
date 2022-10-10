package com.telenav.kivakit.mixins;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.mixins.internal.lexakai.DiagramMixin;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.HashMap;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A {@link Mixin} that allows any object to have a set of attributes associated with it.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMixin.class)
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public interface AttributesMixin<Key, Value> extends Mixin
{
    /**
     * Returns the value for the given key for this mixin
     *
     * @param key The key
     * @return The value
     */
    default Value attribute(Key key)
    {
        return attributes().get(key);
    }

    /**
     * Sets the value for the given key for this mixin
     *
     * @param key The key
     * @param value The value
     * @return Any old value, or null if there was none
     */
    default Value attribute(Key key, Value value)
    {
        return attributes().put(key, value);
    }

    /**
     * Accessor for mixin state
     *
     * @return The {@link HashMap} associated with this mixin
     */
    default HashMap<Key, Value> attributes()
    {
        return mixin(AttributesMixin.class, HashMap::new);
    }
}
