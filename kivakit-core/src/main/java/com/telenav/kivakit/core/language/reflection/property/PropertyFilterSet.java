////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.language.reflection.property;

import com.telenav.kivakit.core.project.lexakai.DiagramReflection;
import com.telenav.kivakit.core.string.CaseFormat;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

/**
 * Base class for property filters. Supports Java Beans and KivaKit style accessor naming conventions with the enum
 * {@link PropertyNamingConvention} and filtering of properties and fields with {@link PropertyMembers}. The constructor
 * takes a style and a set of explicit includes (nothing is included by default). Static fields and properties are never
 * included. Any field or method tagged with {@link KivaKitExcludeProperty} will be excluded regardless of the
 * inclusions specified.
 * <p>
 * Subclasses can utilize the following tests to implement filters that don't follow the pattern implemented by this
 * base class.
 * <p>
 * <b>Getters and Setters</b>
 * <ul>
 *     <li>{@link #isGetter(Method)} - True if the method is a getter under the given naming convention</li>
 *     <li>{@link #isSetter(Method)} - True if the method is a setter under the given naming convention</li>
 * </ul>
 * <p>
 * <b>Include Tests</b>
 * <ul>
 *     <li>{@link #isIncluded(Method)} - True if the method is included under the given set of {@link PropertyMembers}s</li>
 *     <li>{@link #isIncluded(Field)} - True if the field is included under the given set of {@link PropertyMembers}s</li>
 * </ul>
 * <p>
 * <b>KivaKit Annotation Tests</b>
 * <ul>
 *     <li>{@link #isKivaKitIncluded(Method)} - True if the method is annotated with {@link KivaKitIncludeProperty}</li>
 *     <li>{@link #isKivaKitIncluded(Field)} - True if the field is annotated with {@link KivaKitIncludeProperty}</li>
 *     <li>{@link #isKivaKitExcluded(Method)} - True if the method is annotated with {@link KivaKitExcludeProperty}</li>
 *     <li>{@link #isKivaKitExcluded(Field)} - True if the field is annotated with {@link KivaKitExcludeProperty}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramReflection.class)
public class PropertyFilterSet implements PropertyFilter
{
    @UmlAggregation
    private final PropertyNamingConvention convention;

    @UmlAggregation
    private final Set<PropertyMembers> included;

    /**
     * @param convention The naming convention used for getters and setters
     * @param included Set of fields and properties to include
     */
    public PropertyFilterSet(PropertyNamingConvention convention, PropertyMembers... included)
    {
        this.convention = convention;
        this.included = Set.of(included);
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof PropertyFilterSet)
        {
            var that = (PropertyFilterSet) object;
            return getClass().equals(that.getClass());
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return getClass().hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean includeAsGetter(Method method)
    {
        return isIncluded(method) && isGetter(method);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean includeAsSetter(Method method)
    {
        return isIncluded(method) && isSetter(method);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean includeField(Field field)
    {
        return isIncluded(field);
    }

    public Set<PropertyMembers> included()
    {
        return included;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String nameForField(Field field)
    {
        return field.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String nameForMethod(Method method)
    {
        // If explicit name was provided,
        var includeAnnotation = method.getAnnotation(KivaKitIncludeProperty.class);
        if (includeAnnotation != null)
        {
            var name = includeAnnotation.name();
            if (!"".equals(name))
            {
                // use that name
                return name;
            }
        }

        // Otherwise, use beans naming conventions, if applicable
        var name = method.getName();
        if (name.startsWith("is") && name.length() >= 3 && Character.isUpperCase(name.charAt(2)))
        {
            return CaseFormat.decapitalize(name.substring(2));
        }
        if ((name.startsWith("get") || name.startsWith("set")) && name.length() >= 4)
        {
            return CaseFormat.decapitalize(name.substring(3));
        }
        return name;
    }

    /**
     * @return True if the method is a getter in the filter's {@link PropertyNamingConvention}
     */
    protected boolean isGetter(Method method)
    {
        // If the method takes no parameters, and it's not static,
        if (method.getParameterTypes().length == 0)
        {
            // then determine if it's a getter in the given style
            var name = method.getName();
            switch (convention)
            {
                case JAVA_BEANS:
                    if (!"getClass".equals(method.getName()))
                    {
                        if (name.startsWith("get") && name.matches("get[A-Z].*")
                                || name.startsWith("is") && name.matches("is[A-Z].*"))
                        {
                            return name.matches("is[A-Z].*");
                        }
                    }
                    break;

                case KIVAKIT:
                    return true;

                default:
                    return false;
            }
        }
        return false;
    }

    /**
     * @return True if the field is included under the set of inclusions
     */
    protected boolean isIncluded(Field field)
    {
        if (Modifier.isStatic(field.getModifiers()))
        {
            return false;
        }

        if (isKivaKitExcluded(field))
        {
            return false;
        }

        return isKivaKitIncluded(field);
    }

    /**
     * @return True if the field is included under the set of inclusions
     */
    protected boolean isIncluded(Method method)
    {
        if (Modifier.isStatic(method.getModifiers()))
        {
            return false;
        }

        if (isKivaKitExcluded(method))
        {
            return false;
        }

        return isKivaKitIncluded(method);
    }

    /**
     * @return True if the method is marked with {@link KivaKitExcludeProperty}
     */
    protected boolean isKivaKitExcluded(Method method)
    {
        return method.getAnnotation(KivaKitExcludeProperty.class) != null;
    }

    /**
     * @return True if the field is marked with {@link KivaKitExcludeProperty}
     */
    protected boolean isKivaKitExcluded(Field field)
    {
        return field.getAnnotation(KivaKitExcludeProperty.class) != null;
    }

    /**
     * @return True if the field is marked with {@link KivaKitIncludeProperty}
     */
    protected boolean isKivaKitIncluded(Field field)
    {
        if (!field.isSynthetic() && !java.lang.reflect.Modifier.isStatic(field.getModifiers()))
        {
            return field.getAnnotation(KivaKitIncludeProperty.class) != null && included.contains(PropertyMembers.INCLUDED_FIELDS);
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
            if (method.getAnnotation(KivaKitIncludeProperty.class) != null && included.contains(PropertyMembers.INCLUDED_FIELDS_AND_METHODS))
            {
                return true;
            }

            if (included.contains(PropertyMembers.PUBLIC_METHODS) && Modifier.isPublic(method.getModifiers()))
            {
                return true;
            }

            return included.contains(PropertyMembers.NON_PUBLIC_METHODS);
        }
        return false;
    }

    /**
     * @return True if the method is a getter in the filter's {@link PropertyNamingConvention}
     */
    protected boolean isSetter(Method method)
    {
        // If the method takes one parameter, and it's not static,
        if (method.getParameterTypes().length == 1)
        {
            return convention == PropertyNamingConvention.KIVAKIT || method.getName().startsWith("set");
        }
        return false;
    }
}
