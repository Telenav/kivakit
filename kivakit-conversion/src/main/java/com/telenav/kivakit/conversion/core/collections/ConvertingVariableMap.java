package com.telenav.kivakit.conversion.core.collections;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.StringConverter;
import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.messaging.Listener;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.language.Classes.newInstance;

/**
 * Wraps a {@link VariableMap} to provide string get/put methods.
 *
 * <p><b>Access</b></p>
 *
 * <ul>
 *     <li>{@link #get(String, StringConverter)}</li>
 *     <li>{@link #get(String, StringConverter, Object)}</li>
 *     <li>{@link #get(String, Class)}</li>
 *     <li>{@link #get(String, Class, Object)}</li>
 *     <li>{@link #put(String, StringConverter, Object)}</li>
 *     <li>{@link #put(String, Class, Object)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class ConvertingVariableMap
{
    /** The listener to report problems to */
    private final Listener listener;

    /** The underlying variable map */
    private final VariableMap<String> map;

    /**
     * Constructs a wrapper around the given map that can access values using converters
     *
     * @param listener The listener to report problems to
     * @param map The variable map to populate
     */
    public ConvertingVariableMap(Listener listener, VariableMap<String> map)
    {
        this.listener = listener;
        this.map = map;
    }

    /**
     * Gets a value from the variable map using the specified string converter
     *
     * @param key The key to get
     * @param converterType The type of converter to instantiate
     * @return The converted value
     */
    public <T> T get(String key, Class<? extends StringConverter<T>> converterType)
    {
        return get(key, newInstance(converterType, Listener.class, listener));
    }

    /**
     * Gets a value from the variable map using the given string converter and default value
     *
     * @param key The key to get
     * @param converterType The type of converter to instantiate
     * @param defaultValue The default value to use if the conversion fails
     * @return The converted value
     */
    public <T> T get(String key, Class<? extends StringConverter<T>> converterType, T defaultValue)
    {
        return get(key, newInstance(converterType, Listener.class, listener), defaultValue);
    }

    /**
     * Gets a value from the variable map using the given converter
     *
     * @param key The key to get
     * @param converter The converter to use
     * @return The converted value
     */
    public <T> T get(String key, StringConverter<T> converter)
    {
        if (converter.listeners().isEmpty())
        {
            listener.listenTo(converter);
        }
        return converter.convert(map.get(key));
    }

    /**
     * Gets a value from the variable map using the given converter
     *
     * @param key The key to get
     * @param converter The converter to use
     * @param defaultValue The default value to use if the conversion fails
     * @return The converted value
     */
    public <T> T get(String key, StringConverter<T> converter, T defaultValue)
    {
        var converted = get(key, converter);
        return converted == null ? defaultValue : converted;
    }

    /**
     * Gets a value from the variable map using the specified string converter
     *
     * @param key The key to get
     * @param converterType The type of converter to instantiate
     * @param text The text to convert and put
     */
    public <T> void put(String key, Class<? extends StringConverter<T>> converterType, T text)
    {
        put(key, newInstance(converterType, Listener.class, listener), text);
    }

    /**
     * Gets a value from the variable map using the given converter
     *
     * @param key The key to get
     * @param converter The converter to use
     * @param text The text to convert and put
     */
    public <T> void put(String key, StringConverter<T> converter, T text)
    {
        if (converter.listeners().isEmpty())
        {
            listener.listenTo(converter);
        }
        map.put(key, converter.unconvert(text));
    }
}
