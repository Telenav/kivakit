package com.telenav.kivakit.mixins;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.mixins.internal.lexakai.DiagramMixin;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.HashMap;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A {@link Mixin} that allows any object to have a set of attributes associated with it.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMixin.class)
@ApiQuality(stability = API_STABLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
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
