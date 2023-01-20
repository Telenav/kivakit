package com.telenav.kivakit.mixins;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.mixins.internal.lexakai.DiagramMixin;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.HashMap;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;

/**
 * A {@link Mixin} that allows any object to have a set of attributes associated with it.
 *
 * <p><b>Attributes</b></p>
 *
 * <ul>
 *     <li>{@link #attribute(Object)}</li>
 *     <li>{@link #attribute(Object, Object)}</li>
 *     <li>{@link #attributes()}</li>
 * </ul>
 *
 * <p><b>Example</b></p>
 *
 * <pre>
 * class A implements AttributesMixin&lt;String, String&gt;
 * {
 * }
 *
 * class B implements AttributesMixin&lt;String, String&gt;
 * {
 * }
 *
 * var a = new A();
 * var b = new B();
 *
 * a.attribute("name", "Shibo");
 * b.attribute("name", "Yinyin");
 *
 * assertEquals("Shibo", a.attribute("name"));
 * assertEquals("Yinyin", b.attribute("name"));
 * </pre>
 *
 * @author jonathanl (shibo)
 * @see Mixin
 */
@SuppressWarnings("SpellCheckingInspection")
@UmlClassDiagram(diagram = DiagramMixin.class)
@TypeQuality(stability = STABLE,
             testing = TESTED,
             documentation = DOCUMENTED)
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
