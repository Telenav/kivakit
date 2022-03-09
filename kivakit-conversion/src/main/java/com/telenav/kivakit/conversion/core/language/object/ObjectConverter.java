package com.telenav.kivakit.conversion.core.language.object;

import com.telenav.kivakit.conversion.BaseConverter;
import com.telenav.kivakit.conversion.StringConverter;
import com.telenav.kivakit.core.language.Classes;
import com.telenav.kivakit.core.language.reflection.Type;
import com.telenav.kivakit.core.language.reflection.property.PropertyValues;
import com.telenav.kivakit.core.messaging.Listener;

import static com.telenav.kivakit.core.language.reflection.property.PropertyMembers.CONVERTED_FIELDS_AND_METHODS;
import static com.telenav.kivakit.core.language.reflection.property.PropertyNamingConvention.KIVAKIT;

/**
 * Converts a {@link PropertyValues} to an object of a given Value type.
 *
 * @author jonathanl (shibo)
 */
public class ObjectConverter<Value> extends BaseConverter<PropertyValues, Value>
{
    /** The object type to convert to */
    private final Class<Value> type;

    /**
     * @param listener The conversion listener
     * @param type The type to convert to
     */
    public ObjectConverter(Listener listener, Class<Value> type)
    {
        super(listener);
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Value onConvert(PropertyValues values)
    {
        try
        {
            // Create an object of the given type,
            var object = Type.forClass(type).newInstance();

            // and a filter that matches converted fields and methods,
            var filter = new ConversionPropertyFilterSet(KIVAKIT, CONVERTED_FIELDS_AND_METHODS);

            // and populate the object with converted values.
            new ObjectPopulator(filter, () -> convertedValues(values)).populate(object);

            return object;
        }
        catch (final Exception e)
        {
            problem(e, "Unable to convert to $ object:\n$", type.getSimpleName(), values.toString());
            return null;
        }
    }

    private PropertyValues convertedValues(final PropertyValues values)
    {
        var outer = this;
        return property ->
        {
            try
            {
                var setter = property.setter();
                if (setter != null)
                {
                    var annotation = setter.annotation(KivaKitConverted.class);
                    if (annotation != null)
                    {
                        var constructor = Classes.constructor(annotation.value(), Listener.class);
                        var converter = (StringConverter<?>) constructor.newInstance(outer);
                        var value = values.valueFor(property);
                        return converter.convert(value.toString());
                    }
                }
            }
            catch (Exception e)
            {
                problem("Unable to convert property: $", property);
            }

            return null;
        };
    }
}
