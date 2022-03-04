package com.telenav.kivakit.conversion.core.language.object;

import com.telenav.kivakit.core.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.core.language.reflection.property.PropertyFilterSet;
import com.telenav.kivakit.core.language.reflection.property.PropertyMembers;
import com.telenav.kivakit.core.language.reflection.property.PropertyNamingConvention;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ConversionPropertyFilterSet extends PropertyFilterSet
{
    /**
     * @param convention The naming convention used for getters and setters
     * @param included Set of fields and properties to include
     */
    public ConversionPropertyFilterSet(PropertyNamingConvention convention, PropertyMembers... included)
    {
        super(convention, included);
    }

    /**
     * @return True if the field is marked with {@link KivaKitIncludeProperty}
     */
    protected boolean isKivaKitIncluded(Field field)
    {
        if (!field.isSynthetic() && !java.lang.reflect.Modifier.isStatic(field.getModifiers()))
        {
            if (field.getAnnotation(KivaKitIncludeProperty.class) != null && included().contains(PropertyMembers.INCLUDED_FIELDS))
            {
                return true;
            }
            return field.getAnnotation(KivaKitPropertyConverter.class) != null && included().contains(PropertyMembers.CONVERTED_FIELDS_AND_METHODS);
        }
        return false;
    }

    /**
     * @return True if the method is marked with {@link KivaKitIncludeProperty}
     */
    protected boolean isKivaKitIncluded(Method method)
    {
        if (!method.isSynthetic() && !java.lang.reflect.Modifier.isStatic(method.getModifiers()))
        {
            if (method.getAnnotation(KivaKitPropertyConverter.class) != null && included().contains(PropertyMembers.CONVERTED_FIELDS_AND_METHODS))
            {
                return true;
            }

            if (method.getAnnotation(KivaKitIncludeProperty.class) != null && included().contains(PropertyMembers.INCLUDED_FIELDS_AND_METHODS))
            {
                return true;
            }

            if (included().contains(PropertyMembers.PUBLIC_METHODS) && Modifier.isPublic(method.getModifiers()))
            {
                return true;
            }

            return included().contains(PropertyMembers.NON_PUBLIC_METHODS);
        }
        return false;
    }
}
