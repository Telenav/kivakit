package com.telenav.kivakit.conversion.core.language.object;

import com.telenav.kivakit.conversion.StringConverter;
import com.telenav.kivakit.conversion.core.language.IdentityConverter;
import com.telenav.kivakit.core.language.reflection.Member;
import com.telenav.kivakit.core.language.reflection.property.Property;
import com.telenav.kivakit.core.language.reflection.property.PropertyValue;
import com.telenav.kivakit.core.language.trait.TryCatchTrait;
import com.telenav.kivakit.core.messaging.Listener;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.function.Functions.applyTo;
import static com.telenav.kivakit.core.language.Classes.constructor;

/**
 * Converts a string value to an object of the given type using the converter supplied by any {@link ConvertedProperty}
 * annotation on the given {@link Member}.
 *
 * @author Jonathan Locke
 */
public class PropertyConverter implements TryCatchTrait
{
    public static PropertyConverter PROPERTY_CONVERTER = new PropertyConverter();

    public PropertyValue converter(Listener listener, PropertyValue value)
    {
        return property ->
        {
            try
            {
                var setter = property.setter();
                if (setter != null)
                {
                    var text = applyTo(value.propertyValue(property), Object::toString);
                    return new PropertyConverter().convert(property, listener, text);
                }
            }
            catch (Exception e)
            {
                listener.problem("Unable to convert property: $", property);
            }

            return null;
        };
    }

    /**
     * Converts the given value to an object using any converter
     *
     * @param member The member, which may or may not have a {@link ConvertedProperty} annotation
     * @param listener The listener to use when constructing the converter
     * @param value The value to convert
     * @return The object
     */
    @SuppressWarnings("unchecked")
    private <T> T convert(Property property, Listener listener, String value)
    {
        ensureNotNull(property);
        ensureNotNull(listener);

        return tryCatch(() ->
        {
            var annotation = property.member().annotation(ConvertedProperty.class);
            if (annotation != null)
            {
                if (annotation.optional() && value == null)
                {
                    return null;
                }

                ensureNotNull(value, "Member $ is not optional");

                var converterType = annotation.converter();
                if (converterType == IdentityConverter.class)
                {
                    converterType = annotation.value();
                }
                var constructor = constructor(converterType, Listener.class);
                constructor.setAccessible(true);
                var converter = (StringConverter<?>) constructor.newInstance(listener);
                return (T) converter.convert(value);
            }
            return (T) value;
        });
    }
}
