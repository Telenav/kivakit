package com.telenav.kivakit.mixins;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.mixins.internal.lexakai.DiagramMixin;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.HashMap;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * A {@link Mixin} that allows any object to have a set of attributes associated with it.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMixin.class)
@ApiQuality(stability = STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
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
