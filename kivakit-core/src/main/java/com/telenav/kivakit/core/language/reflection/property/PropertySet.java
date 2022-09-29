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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramReflection;
import com.telenav.kivakit.core.language.reflection.Field;
import com.telenav.kivakit.core.language.reflection.Method;
import com.telenav.kivakit.core.string.CaseFormat;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.util.Set;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Base class for property filters. Supports Java Beans and KivaKit style accessor naming conventions with the enum
 * {@link PropertyNamingConvention} and filtering of properties and fields with {@link PropertyMemberSelector}. The
 * constructor takes a style and a set of explicit includes (nothing is included by default). Static fields and
 * properties are never included. Any field or method tagged with {@link KivaKitExcludeProperty} will be excluded
 * regardless of the inclusions specified.
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
 *     <li>{@link #isKivaKitIncluded(Method)} - True if the method is annotated with {@link KivaKitIncludeProperty}</li>
 *     <li>{@link #isKivaKitIncluded(Field)} - True if the field is annotated with {@link KivaKitIncludeProperty}</li>
 *     <li>{@link #isKivaKitExcluded(Method)} - True if the method is annotated with {@link KivaKitExcludeProperty}</li>
 *     <li>{@link #isKivaKitExcluded(Field)} - True if the field is annotated with {@link KivaKitExcludeProperty}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramReflection.class)
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
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
        if (object instanceof PropertySet)
        {
            var that = (PropertySet) object;
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
        var includeAnnotation = field.annotation(KivaKitIncludeProperty.class);
        if (includeAnnotation != null)
        {
            var name = includeAnnotation.name();
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
        var includeAnnotation = method.annotation(KivaKitIncludeProperty.class);
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
        var name = method.name();
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
        if (method.parameterTypes().length == 0)
        {
            // then determine if it's a getter in the given style
            var name = method.name();
            switch (convention)
            {
                case JAVA_BEANS_NAMING:
                    if (!"getClass".equals(method.name()))
                    {
                        if (name.startsWith("get") && name.matches("get[A-Z].*")
                                || name.startsWith("is") && name.matches("is[A-Z].*"))
                        {
                            return name.matches("is[A-Z].*");
                        }
                    }
                    break;

                case KIVAKIT_PROPERTY_NAMING:
                    return method.returnType() != Void.class && method.parameterTypes().length == 0;

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
        return !field.isStatic() && !isKivaKitExcluded(field) && isKivaKitIncluded(field);
    }

    /**
     * @return True if the field is included under the set of inclusions
     */
    protected boolean isIncluded(Method method)
    {
        return !method.isStatic() && !isKivaKitExcluded(method) && isKivaKitIncluded(method);
    }

    /**
     * @return True if the method is marked with {@link KivaKitExcludeProperty}
     */
    protected boolean isKivaKitExcluded(Method method)
    {
        return method.annotation(KivaKitExcludeProperty.class) != null;
    }

    /**
     * @return True if the field is marked with {@link KivaKitExcludeProperty}
     */
    protected boolean isKivaKitExcluded(Field field)
    {
        return field.annotation(KivaKitExcludeProperty.class) != null;
    }

    /**
     * @return True if the field is marked with {@link KivaKitIncludeProperty}
     */
    protected boolean isKivaKitIncluded(Field field)
    {
        if (!field.isSynthetic() && !field.isStatic())
        {
            return field.annotation(KivaKitIncludeProperty.class) != null &&
                    selection.contains(PropertyMemberSelector.KIVAKIT_ANNOTATION_INCLUDED_FIELDS);
        }
        return false;
    }

    /**
     * @return True if the method is marked with {@link KivaKitIncludeProperty}
     */
    protected boolean isKivaKitIncluded(Method method)
    {
        if (!method.isSynthetic() && !method.isStatic())
        {
            if (method.annotation(KivaKitIncludeProperty.class) != null
                    && selection.contains(PropertyMemberSelector.KIVAKIT_ANNOTATION_INCLUDED_FIELDS_AND_METHODS))
            {
                return true;
            }

            if (selection.contains(PropertyMemberSelector.PUBLIC_METHODS) && method.isPublic())
            {
                return true;
            }

            return selection.contains(PropertyMemberSelector.NON_PUBLIC_METHODS);
        }
        return false;
    }

    /**
     * @return True if the method is a getter in the filter's {@link PropertyNamingConvention}
     */
    protected boolean isSetter(Method method)
    {
        // If the method takes one parameter, and it's not static,
        if (method.parameterTypes().length == 1)
        {
            return convention == PropertyNamingConvention.KIVAKIT_PROPERTY_NAMING ||
                    method.name().startsWith("set");
        }
        return false;
    }
}
