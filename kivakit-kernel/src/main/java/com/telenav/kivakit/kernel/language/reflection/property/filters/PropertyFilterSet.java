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

package com.telenav.kivakit.kernel.language.reflection.property.filters;

import com.telenav.kivakit.kernel.language.reflection.populator.KivaKitPropertyConverter;
import com.telenav.kivakit.kernel.language.reflection.property.IncludeProperty;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitExcludeProperty;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.reflection.property.NamingConvention;
import com.telenav.kivakit.kernel.language.reflection.property.PropertyFilter;
import com.telenav.kivakit.kernel.language.strings.CaseFormat;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

import static com.telenav.kivakit.kernel.language.reflection.property.IncludeProperty.CONVERTED_FIELDS_AND_METHODS;
import static com.telenav.kivakit.kernel.language.reflection.property.IncludeProperty.INCLUDED_FIELDS;
import static com.telenav.kivakit.kernel.language.reflection.property.IncludeProperty.INCLUDED_FIELDS_AND_METHODS;
import static com.telenav.kivakit.kernel.language.reflection.property.IncludeProperty.NON_PUBLIC_METHODS;
import static com.telenav.kivakit.kernel.language.reflection.property.IncludeProperty.PUBLIC_METHODS;
import static com.telenav.kivakit.kernel.language.reflection.property.NamingConvention.KIVAKIT;

/**
 * Base class for property filters. Supports Java Beans and KivaKit style accessor naming conventions with the enum
 * {@link NamingConvention} and filtering of properties and fields with {@link IncludeProperty}. The constructor takes a
 * style and a set of explicit includes (nothing is included by default). Static fields and properties are never
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
 *     <li>{@link #isIncluded(Method)} - True if the method is included under the given set of {@link IncludeProperty}s</li>
 *     <li>{@link #isIncluded(Field)} - True if the field is included under the given set of {@link IncludeProperty}s</li>
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
@UmlClassDiagram(diagram = DiagramLanguageReflection.class)
public class PropertyFilterSet implements PropertyFilter
{
    @UmlAggregation
    private final NamingConvention convention;

    @UmlAggregation
    private final Set<IncludeProperty> included;

    /**
     * @param convention The naming convention used for getters and setters
     * @param included Set of fields and properties to include
     */
    public PropertyFilterSet(final NamingConvention convention, final IncludeProperty... included)
    {
        this.convention = convention;
        this.included = Set.of(included);
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof PropertyFilterSet)
        {
            final var that = (PropertyFilterSet) object;
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
    public boolean includeAsGetter(final Method method)
    {
        return isIncluded(method) && isGetter(method);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean includeAsSetter(final Method method)
    {
        return isIncluded(method) && isSetter(method);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean includeField(final Field field)
    {
        return isIncluded(field);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String nameForField(final Field field)
    {
        return field.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String nameForMethod(final Method method)
    {
        // If explicit name was provided,
        final var includeAnnotation = method.getAnnotation(KivaKitIncludeProperty.class);
        if (includeAnnotation != null)
        {
            final var name = includeAnnotation.name();
            if (!"".equals(name))
            {
                // use that name
                return name;
            }
        }

        // Otherwise, use beans naming conventions, if applicable
        final var name = method.getName();
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
     * @return True if the method is a getter in the filter's {@link NamingConvention}
     */
    protected boolean isGetter(final Method method)
    {
        // If the method takes no parameters and it's not static,
        if (method.getParameterTypes().length == 0)
        {
            // then determine if it's a getter in the given style
            final var name = method.getName();
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
    protected boolean isIncluded(final Field field)
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
    protected boolean isIncluded(final Method method)
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
    protected boolean isKivaKitExcluded(final Method method)
    {
        return method.getAnnotation(KivaKitExcludeProperty.class) != null;
    }

    /**
     * @return True if the field is marked with {@link KivaKitExcludeProperty}
     */
    protected boolean isKivaKitExcluded(final Field field)
    {
        return field.getAnnotation(KivaKitExcludeProperty.class) != null;
    }

    /**
     * @return True if the field is marked with {@link KivaKitIncludeProperty}
     */
    protected boolean isKivaKitIncluded(final Field field)
    {
        if (!field.isSynthetic() && !java.lang.reflect.Modifier.isStatic(field.getModifiers()))
        {
            if (field.getAnnotation(KivaKitIncludeProperty.class) != null && included.contains(INCLUDED_FIELDS))
            {
                return true;
            }
            return field.getAnnotation(KivaKitPropertyConverter.class) != null && included.contains(CONVERTED_FIELDS_AND_METHODS);
        }
        return false;
    }

    /**
     * @return True if the method is marked with {@link KivaKitIncludeProperty}
     */
    protected boolean isKivaKitIncluded(final Method method)
    {
        if (!method.isSynthetic() && !java.lang.reflect.Modifier.isStatic(method.getModifiers()))
        {
            if (method.getAnnotation(KivaKitPropertyConverter.class) != null && included.contains(CONVERTED_FIELDS_AND_METHODS))
            {
                return true;
            }

            if (method.getAnnotation(KivaKitIncludeProperty.class) != null && included.contains(INCLUDED_FIELDS_AND_METHODS))
            {
                return true;
            }

            if (included.contains(PUBLIC_METHODS) && Modifier.isPublic(method.getModifiers()))
            {
                return true;
            }

            return included.contains(NON_PUBLIC_METHODS);
        }
        return false;
    }

    /**
     * @return True if the method is a getter in the filter's {@link NamingConvention}
     */
    protected boolean isSetter(final Method method)
    {
        // If the method takes one parameter and it's not static,
        if (method.getParameterTypes().length == 1)
        {
            return convention == KIVAKIT || method.getName().startsWith("set");
        }
        return false;
    }
}
