package com.telenav.kivakit.conversion.core.language.object;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.language.reflection.Field;
import com.telenav.kivakit.core.language.reflection.Method;
import com.telenav.kivakit.core.language.reflection.property.IncludeProperty;
import com.telenav.kivakit.core.language.reflection.property.PropertyMemberSelector;
import com.telenav.kivakit.core.language.reflection.property.PropertyNamingConvention;
import com.telenav.kivakit.core.language.reflection.property.PropertySet;

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.language.reflection.property.PropertyMemberSelector.KIVAKIT_CONVERTED_MEMBERS;
import static com.telenav.kivakit.core.language.reflection.property.PropertyMemberSelector.KIVAKIT_INCLUDED_FIELDS;
import static com.telenav.kivakit.core.language.reflection.property.PropertyMemberSelector.KIVAKIT_INCLUDED_FIELDS_AND_METHODS;
import static com.telenav.kivakit.core.language.reflection.property.PropertyMemberSelector.NON_PUBLIC_METHODS;
import static com.telenav.kivakit.core.language.reflection.property.PropertyMemberSelector.PUBLIC_METHODS;

/**
 * <b>Not public API</b>
 *
 * <p>
 * Selects properties based on a list of {@link PropertyMemberSelector}s and a {@link PropertyNamingConvention}. This
 * selector uses the annotation {{@literal }@}ConvertedProperty property to label fields and/or methods as converted.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@TypeQuality(audience = AUDIENCE_INTERNAL,
             stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED) public
class ConvertedPropertySet extends PropertySet
{
    /**
     * @param convention The naming convention used for getters and setters
     * @param included Set of fields and properties to include
     */
    public ConvertedPropertySet(PropertyNamingConvention convention, PropertyMemberSelector... included)
    {
        super(convention, included);
    }

    /**
     * Returns true if the field is marked with {@link IncludeProperty}
     */
    @Override
    protected boolean isIncludedByAnnotation(Field field)
    {
        if (!field.isSynthetic() && !field.isStatic())
        {
            if (field.hasAnnotation(IncludeProperty.class) && included().contains(KIVAKIT_INCLUDED_FIELDS))
            {
                return true;
            }
            return field.hasAnnotation(ConvertedProperty.class) && included().contains(KIVAKIT_CONVERTED_MEMBERS);
        }
        return false;
    }

    /**
     * Returns true if the method is marked with {@link IncludeProperty}
     */
    @Override
    protected boolean isIncludedByAnnotation(Method method)
    {
        if (!method.isSynthetic() && !method.isStatic())
        {
            if (method.hasAnnotation(ConvertedProperty.class) && included().contains(KIVAKIT_CONVERTED_MEMBERS))
            {
                return true;
            }

            if (method.hasAnnotation(IncludeProperty.class) && included().contains(KIVAKIT_INCLUDED_FIELDS_AND_METHODS))
            {
                return true;
            }

            if (included().contains(PUBLIC_METHODS) && method.isPublic())
            {
                return true;
            }

            return included().contains(NON_PUBLIC_METHODS);
        }
        return false;
    }
}
