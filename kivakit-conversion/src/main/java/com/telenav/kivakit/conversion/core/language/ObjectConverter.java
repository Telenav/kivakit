package com.telenav.kivakit.conversion.core.language;

import com.telenav.kivakit.conversion.BaseConverter;
import com.telenav.kivakit.core.language.reflection.ObjectPopulator;
import com.telenav.kivakit.core.language.reflection.Type;
import com.telenav.kivakit.core.language.reflection.property.PropertyFilter;
import com.telenav.kivakit.core.language.reflection.property.PropertyValueSource;
import com.telenav.kivakit.core.messaging.Listener;

import static com.telenav.kivakit.core.language.reflection.property.IncludeProperty.CONVERTED_FIELDS_AND_METHODS;

/**
 * Converts a {@link PropertyValueSource} to an object of a given Value type.
 *
 * @author jonathanl (shibo)
 */
public class ObjectConverter<Value> extends BaseConverter<PropertyValueSource, Value>
{
    /** The object type to convert to */
    private final Class<Value> type;

    /**
     * @param listener The conversion listener
     * @param type The type to convert to
     */
    protected ObjectConverter(Listener listener, Class<Value> type)
    {
        super(listener);
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Value onConvert(final PropertyValueSource map)
    {
        try
        {
            var value = Type.forClass(type).newInstance();
            var filter = PropertyFilter.kivakitProperties(CONVERTED_FIELDS_AND_METHODS);
            new ObjectPopulator(filter, map).populate(value);
            return value;
        }
        catch (final Exception e)
        {
            problem(e, "Unable to convert $ to $ object", map, type);
            return null;
        }
    }
}
