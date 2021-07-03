package com.telenav.kivakit.kernel.data.conversion;

import com.telenav.kivakit.kernel.messaging.Listener;

/**
 * The superinterface, {@link Converter}, converts values from type &lt;From&gt; to type &lt;To&gt; while this interface
 * adds the ability to convert in the reverse direction from type &lt;To&gt; to type &lt;From&gt;.
 *
 * @param <From> The original type
 * @param <To> The desired type
 * @author jonathanl (shibo)
 */
public interface TwoWayConverter<From, To> extends Converter<From, To>
{
    /**
     * Converts from the destination type back to the original type
     */
    From unconvert(To to);

    /**
     * @return A converter that converts from type &lt;To&gt; back to type &lt;From&gt;.
     */
    default Converter<To, From> unconverter(final Listener listener)
    {
        return new BaseConverter<>(listener)
        {
            @Override
            protected From onConvert(final To to)
            {
                return unconvert(to);
            }
        };
    }
}
