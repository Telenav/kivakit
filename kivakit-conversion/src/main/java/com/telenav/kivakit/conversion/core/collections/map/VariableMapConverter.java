package com.telenav.kivakit.conversion.core.collections.map;

import com.telenav.kivakit.conversion.StringConverter;
import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.language.Classes;
import com.telenav.kivakit.core.messaging.Listener;

/**
 * Wraps a {@link VariableMap} to provide string conversion methods {@link #get(String, StringConverter)} and {@link
 * #get(String, StringConverter, Object)}.
 *
 * @author jonathanl (shibo)
 */
public class VariableMapConverter
{
    private final Listener listener;

    private final VariableMap<String> map;

    public VariableMapConverter(Listener listener, VariableMap<String> map)
    {
        this.listener = listener;
        this.map = map;
    }

    public <T> T get(String key, Class<? extends StringConverter<T>> converterType)
    {
        return get(key, Classes.newInstance(converterType, Listener.class));
    }

    public <T> T get(String key, Class<? extends StringConverter<T>> converterType, T defaultValue)
    {
        return get(key, Classes.newInstance(converterType, Listener.class), defaultValue);
    }

    public <T> T get(String key, StringConverter<T> converter)
    {
        if (converter.listeners().isEmpty())
        {
            listener.listenTo(converter);
        }
        return converter.convert(map.get(key));
    }

    public <T> T get(String key, StringConverter<T> converter, T defaultValue)
    {
        var converted = get(key, converter);
        return converted == null ? defaultValue : converted;
    }
}
