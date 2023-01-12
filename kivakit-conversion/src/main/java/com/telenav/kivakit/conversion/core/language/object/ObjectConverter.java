package com.telenav.kivakit.conversion.core.language.object;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.conversion.BaseConverter;
import com.telenav.kivakit.conversion.StringConverter;
import com.telenav.kivakit.conversion.core.language.IdentityConverter;
import com.telenav.kivakit.core.language.reflection.property.Property;
import com.telenav.kivakit.core.language.reflection.property.PropertyValue;
import com.telenav.kivakit.core.messaging.Listener;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.language.Classes.constructor;
import static com.telenav.kivakit.core.language.reflection.Type.typeForClass;
import static com.telenav.kivakit.core.language.reflection.property.PropertyMemberSelector.KIVAKIT_CONVERTED_MEMBERS;
import static com.telenav.kivakit.core.language.reflection.property.PropertyNamingConvention.KIVAKIT_PROPERTY_NAMING;

/**
 * Converts a {@link PropertyValue} to an object of a given Value type.
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class ObjectConverter<Value> extends BaseConverter<PropertyValue, Value>
{
    /** The object type to convert to */
    private final Class<Value> type;

    /**
     * @param listener The conversion listener
     * @param type The type to convert to
     */
    public ObjectConverter(Listener listener, Class<Value> type)
    {
        super(listener, PropertyValue.class, type);
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Value onConvert(PropertyValue values)
    {
        try
        {
            // Create an object of the given type,
            var object = typeForClass(type).newInstance();

            // and a filter that matches converted fields and methods,
            var filter = new ConvertedPropertySet(KIVAKIT_PROPERTY_NAMING, KIVAKIT_CONVERTED_MEMBERS);

            // and populate the object with converted values.
            listenTo(new ObjectPopulator(filter, () -> convertedValues(values))
            {
                @Override
                public boolean isOptional(Property property)
                {
                    if (super.isOptional(property))
                    {
                        return true;
                    }
                    var annotation = property.setter().annotation(ConvertedProperty.class);
                    return annotation != null && annotation.optional();
                }
            }).populate(object);

            return object;
        }
        catch (Exception e)
        {
            problem(e, "Unable to convert to $ object:\n$", type.getSimpleName(), values.toString());
            return null;
        }
    }

    private PropertyValue convertedValues(PropertyValue values)
    {
        var outer = this;
        return property ->
        {
            try
            {
                var setter = property.setter();
                if (setter != null)
                {
                    var annotation = setter.annotation(ConvertedProperty.class);
                    if (annotation != null)
                    {
                        var converterType = annotation.converter();
                        if (converterType == IdentityConverter.class)
                        {
                            converterType = annotation.value();
                        }
                        var constructor = constructor(converterType, Listener.class);
                        constructor.setAccessible(true);
                        var converter = (StringConverter<?>) constructor.newInstance(outer);
                        var value = values.propertyValue(property);
                        if (annotation.optional() && value == null)
                        {
                            return null;
                        }
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
