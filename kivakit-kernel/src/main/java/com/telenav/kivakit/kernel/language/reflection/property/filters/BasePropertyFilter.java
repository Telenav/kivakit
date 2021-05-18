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
import com.telenav.kivakit.kernel.language.reflection.property.PropertyFilter;
import com.telenav.kivakit.kernel.language.strings.CaseFormat;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

import static com.telenav.kivakit.kernel.language.reflection.property.filters.BasePropertyFilter.Include.KIVAKIT_INCLUDED_FIELDS;
import static com.telenav.kivakit.kernel.language.reflection.property.filters.BasePropertyFilter.Include.KIVAKIT_INCLUDED_PROPERTIES;
import static com.telenav.kivakit.kernel.language.reflection.property.filters.BasePropertyFilter.Include.NON_PUBLIC_PROPERTIES;
import static com.telenav.kivakit.kernel.language.reflection.property.filters.BasePropertyFilter.Include.NON_TRANSIENT_FIELDS;
import static com.telenav.kivakit.kernel.language.reflection.property.filters.BasePropertyFilter.Include.PUBLIC_PROPERTIES;
import static com.telenav.kivakit.kernel.language.reflection.property.filters.BasePropertyFilter.Include.TRANSIENT_FIELDS;
import static com.telenav.kivakit.kernel.language.reflection.property.filters.BasePropertyFilter.NamingConvention.KivaKit;

/**
 * Base class for property filters. Supports Java Beans and KivaKit style accessor naming conventions with the enum
 * {@link NamingConvention} and filtering of properties and fields with {@link Include}. The constructor takes a style
 * and a set of explicit includes (nothing is included by default). Static fields and properties are never included. Any
 * field or method tagged with {@link KivaKitExcludeProperty} will be excluded regardless of the inclusions specified.
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
 *     <li>{@link #isIncluded(Method)} - True if the method is included under the given set of {@link Include}s</li>
 *     <li>{@link #isIncluded(Field)} - True if the field is included under the given set of {@link Include}s</li>
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
public class BasePropertyFilter implements PropertyFilter
{
    /**
     * Kinds of properties and fields to include
     */
    @UmlClassDiagram(diagram = DiagramLanguageReflection.class)
    public enum Include
    {
        /** Include properties with public getters and setters */
        PUBLIC_PROPERTIES,

        /** Include properties with non-public getters and setters */
        NON_PUBLIC_PROPERTIES,

        /** Include properties marked with {@link KivaKitIncludeProperty} */
        KIVAKIT_INCLUDED_PROPERTIES,

        /** Include non-transient fields regardless of visibility */
        NON_TRANSIENT_FIELDS,

        /** Include transient fields (implies FIELDS) */
        TRANSIENT_FIELDS,

        /** Include fields marked with {@link KivaKitIncludeProperty} */
        KIVAKIT_INCLUDED_FIELDS,
    }

    @UmlClassDiagram(diagram = DiagramLanguageReflection.class)
    protected enum NamingConvention
    {
        BEANS,
        KivaKit
    }

    @UmlAggregation
    private final NamingConvention convention;

    @UmlAggregation
    private final Set<Include> included;

    /**
     * @param convention The naming convention used for getters and setters
     * @param included Set of fields and properties to include
     */
    public BasePropertyFilter(final NamingConvention convention, final Set<Include> included)
    {
        this.convention = convention;
        this.included = included;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof BasePropertyFilter)
        {
            final var that = (BasePropertyFilter) object;
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
                case BEANS:
                    if (!"getClass".equals(method.getName()))
                    {
                        if (name.startsWith("get") && name.matches("get[A-Z].*")
                                || name.startsWith("is") && name.matches("is[A-Z].*"))
                        {
                            return name.matches("is[A-Z].*");
                        }
                    }
                    break;

                case KivaKit:
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

        if (included.contains(KIVAKIT_INCLUDED_FIELDS) && isKivaKitIncluded(field))
        {
            return true;
        }

        if (included.contains(TRANSIENT_FIELDS) && Modifier.isTransient(field.getModifiers()))
        {
            return true;
        }

        return included.contains(NON_TRANSIENT_FIELDS);
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

        if (included.contains(KIVAKIT_INCLUDED_PROPERTIES) && isKivaKitIncluded(method))
        {
            return true;
        }

        if (included.contains(PUBLIC_PROPERTIES) && Modifier.isPublic(method.getModifiers()))
        {
            return true;
        }

        return included.contains(NON_PUBLIC_PROPERTIES);
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
            return field.getAnnotation(KivaKitIncludeProperty.class) != null;
        }
        return false;
    }

    /**
     * @return True if the method is marked with {@link KivaKitIncludeProperty}
     */
    protected boolean isKivaKitIncluded(final Method method)
    {
        return method.getAnnotation(KivaKitIncludeProperty.class) != null
                || method.getAnnotation(KivaKitPropertyConverter.class) != null;
    }

    /**
     * @return True if the method is a getter in the filter's {@link NamingConvention}
     */
    protected boolean isSetter(final Method method)
    {
        // If the method takes one parameter and it's not static,
        if (method.getParameterTypes().length == 1)
        {
            return convention == KivaKit || method.getName().startsWith("set");
        }
        return false;
    }
}
