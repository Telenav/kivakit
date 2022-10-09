package com.telenav.kivakit.conversion;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversion;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

/**
 * The superinterface, {@link Converter}, converts values from type &lt;From&gt; to type &lt;To&gt; while this interface
 * adds the ability to convert in the reverse direction from type &lt;To&gt; to type &lt;From&gt;.
 *
 * @param <From> The original type
 * @param <To> The desired type
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "unused", "SpellCheckingInspection" })
@UmlClassDiagram(diagram = DiagramConversion.class)
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public interface TwoWayConverter<From, To> extends Converter<From, To>
{
    /**
     * Converts from the destination type back to the original type
     */
    From unconvert(To to);

    /**
     * Returns a converter that converts from type &lt;To&gt; back to type &lt;From&gt;.
     */
    default Converter<To, From> unconverter(Listener listener)
    {
        return new BaseConverter<>(listener)
        {
            @Override
            protected From onConvert(To to)
            {
                return unconvert(to);
            }
        };
    }
}
