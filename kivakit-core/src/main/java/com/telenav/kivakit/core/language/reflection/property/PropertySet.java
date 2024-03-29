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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramReflection;
import com.telenav.kivakit.core.language.reflection.Field;
import com.telenav.kivakit.core.language.reflection.Method;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.util.Set;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.language.reflection.property.PropertyMemberSelector.ALL_FIELDS_AND_METHODS;
import static com.telenav.kivakit.core.language.reflection.property.PropertyMemberSelector.KIVAKIT_INCLUDED_FIELDS;
import static com.telenav.kivakit.core.language.reflection.property.PropertyMemberSelector.KIVAKIT_INCLUDED_FIELDS_AND_METHODS;
import static com.telenav.kivakit.core.language.reflection.property.PropertyMemberSelector.NON_PUBLIC_METHODS;
import static com.telenav.kivakit.core.language.reflection.property.PropertyMemberSelector.PUBLIC_METHODS;
import static com.telenav.kivakit.core.language.reflection.property.PropertyNamingConvention.KIVAKIT_PROPERTY_NAMING;
import static com.telenav.kivakit.core.string.CaseFormat.decapitalize;
import static java.lang.Character.isUpperCase;

/**
 * Base class for property filters. Supports Java Beans and KivaKit style accessor naming conventions with the enum
 * {@link PropertyNamingConvention} and filtering of properties and fields with {@link PropertyMemberSelector}. The
 * constructor takes a style and a set of explicit includes (nothing is included by default). Static fields and
 * properties are never included. Any field or method tagged with {@link ExcludeProperty} will be excluded regardless of
 * the inclusions specified.
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
 *     <li>{@link #isIncluded(Method)} - True if the method is included under the given set of {@link PropertyMemberSelector}s</li>
 *     <li>{@link #isIncluded(Field)} - True if the field is included under the given set of {@link PropertyMemberSelector}s</li>
 * </ul>
 * <p>
 * <b>KivaKit Annotation Tests</b>
 * <ul>
 *     <li>{@link #isIncludedByAnnotation(Method)} - True if the method is annotated with {@link IncludeProperty}</li>
 *     <li>{@link #isIncludedByAnnotation(Field)} - True if the field is annotated with {@link IncludeProperty}</li>
 *     <li>{@link #isExcludedByAnnotation(Method)} - True if the method is annotated with {@link ExcludeProperty}</li>
 *     <li>{@link #isExcludedByAnnotation(Field)} - True if the field is annotated with {@link ExcludeProperty}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramReflection.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class PropertySet implements PropertyFilter
{
    /** The property naming convention */
    @UmlAggregation
    private final PropertyNamingConvention convention;

    /** The set of selectors that describe what kind of properties to include */
    @UmlAggregation
    private final Set<PropertyMemberSelector> selection;

    /**
     * @param convention The naming convention used for getters and setters
     * @param selection Set of fields and properties to include
     */
    public PropertySet(PropertyNamingConvention convention, PropertyMemberSelector... selection)
    {
        this.convention = convention;
        this.selection = Set.of(selection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof PropertySet that)
        {
            return getClass().equals(that.getClass());
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * Returns the set of selectors for this filter
     */
    public Set<PropertyMemberSelector> included()
    {
        return selection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String nameForField(Field field)
    {
        // If an explicit name was provided,
        var includeAnnotation = field.annotation(IncludeProperty.class);
        if (includeAnnotation != null)
        {
            var name = includeAnnotation.as();
            if (!"".equals(name))
            {
                // use that name
                return name;
            }
        }
        return field.name();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String nameForMethod(Method method)
    {
        // If an explicit name was provided,
        var includeAnnotation = method.annotation(IncludeProperty.class);
        if (includeAnnotation != null)
        {
            var name = includeAnnotation.as();
            if (!"".equals(name))
            {
                // use that name
                return name;
            }
        }

        // Otherwise, use beans naming conventions, if applicable
        var name = method.name();
        if (name.startsWith("is") && name.length() >= 3 && isUpperCase(name.charAt(2)))
        {
            return decapitalize(name.substring(2));
        }
        if ((name.startsWith("get") || name.startsWith("set")) && name.length() >= 4)
        {
            return decapitalize(name.substring(3));
        }
        return name;
    }

    /**
     * Returns true if the method is marked with {@link ExcludeProperty}
     */
    protected boolean isExcludedByAnnotation(Method method)
    {
        return method.annotation(ExcludeProperty.class) != null;
    }

    /**
     * Returns true if the field is marked with {@link ExcludeProperty}
     */
    protected boolean isExcludedByAnnotation(Field field)
    {
        return field.annotation(ExcludeProperty.class) != null;
    }

    /**
     * Returns true if the method is a getter in the filter's {@link PropertyNamingConvention}
     */
    protected boolean isGetter(Method method)
    {
        // If the method takes no parameters, and it's not static,
        if (method.parameterTypes().length == 0)
        {
            // then determine if it's a getter in the given style
            return switch (convention)
                {
                    case JAVA_BEANS_NAMING -> isJavaBeansGetterMethod(method);
                    case KIVAKIT_PROPERTY_NAMING -> isKivaKitGetterMethod(method);
                    case ANY_NAMING_CONVENTION -> isJavaBeansGetterMethod(method) || isKivaKitGetterMethod(method);
                    default -> false;
                };
        }
        return false;
    }

    /**
     * Returns true if the field is included under the set of inclusions
     */
    protected boolean isIncluded(Field field)
    {
        return !field.isStatic() && !isExcludedByAnnotation(field) && isIncludedByAnnotation(field);
    }

    /**
     * Returns true if the field is included under the set of inclusions
     */
    protected boolean isIncluded(Method method)
    {
        return !method.isStatic() && !isExcludedByAnnotation(method) && isIncludedByAnnotation(method);
    }

    /**
     * Returns true if the method is marked with {@link IncludeProperty}
     */
    protected boolean isIncludedByAnnotation(Method method)
    {
        if (!method.isSynthetic() && !method.isStatic())
        {
            if (selection.contains(ALL_FIELDS_AND_METHODS))
            {
                return true;
            }

            if (selection.contains(KIVAKIT_INCLUDED_FIELDS_AND_METHODS)
                    && method.annotation(IncludeProperty.class) != null)
            {
                return true;
            }

            if (selection.contains(PUBLIC_METHODS) && method.isPublic())
            {
                return true;
            }

            return selection.contains(NON_PUBLIC_METHODS);
        }
        return false;
    }

    /**
     * Returns true if the field is marked with {@link IncludeProperty}
     */
    protected boolean isIncludedByAnnotation(Field field)
    {
        if (!field.isSynthetic() && !field.isStatic())
        {
            if (selection.contains(ALL_FIELDS_AND_METHODS))
            {
                return true;
            }
            return field.annotation(IncludeProperty.class) != null &&
                    selection.contains(KIVAKIT_INCLUDED_FIELDS);
        }
        return false;
    }

    /**
     * Returns true if the method is a getter in the filter's {@link PropertyNamingConvention}
     */
    protected boolean isSetter(Method method)
    {
        // If the method takes one parameter, and it's not static,
        if (method.parameterTypes().length == 1)
        {
            return convention == KIVAKIT_PROPERTY_NAMING ||
                    method.name().startsWith("set");
        }
        return false;
    }

    private boolean isJavaBeansGetterMethod(Method method)
    {
        var name = method.name();
        if (!"getClass".equals(name))
        {
            if (name.startsWith("get") && name.matches("get[A-Z].*")
                    || name.startsWith("is") && name.matches("is[A-Z].*"))
            {
                return name.matches("is[A-Z].*");
            }
        }
        return false;
    }

    private boolean isKivaKitGetterMethod(Method method)
    {
        return method.returnType() != Void.class && method.parameterTypes().length == 0;
    }
}
