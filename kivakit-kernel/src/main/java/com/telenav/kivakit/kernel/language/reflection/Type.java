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

package com.telenav.kivakit.kernel.language.reflection;

import com.telenav.kivakit.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.kernel.interfaces.naming.Named;
import com.telenav.kivakit.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.kernel.language.collections.map.ClassMap;
import com.telenav.kivakit.kernel.language.collections.map.string.NameMap;
import com.telenav.kivakit.kernel.language.collections.map.string.VariableMap;
import com.telenav.kivakit.kernel.language.paths.PackagePath;
import com.telenav.kivakit.kernel.language.reflection.access.field.FieldGetter;
import com.telenav.kivakit.kernel.language.reflection.access.field.FieldSetter;
import com.telenav.kivakit.kernel.language.reflection.access.method.MethodGetter;
import com.telenav.kivakit.kernel.language.reflection.access.method.MethodSetter;
import com.telenav.kivakit.kernel.language.reflection.property.NamingConvention;
import com.telenav.kivakit.kernel.language.reflection.property.Property;
import com.telenav.kivakit.kernel.language.reflection.property.PropertyFilter;
import com.telenav.kivakit.kernel.language.reflection.property.filters.field.NamedField;
import com.telenav.kivakit.kernel.language.reflection.property.filters.method.NamedMethod;
import com.telenav.kivakit.kernel.language.types.Classes;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.project.KernelLimits;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * Reflects on a class and retains a set of {@link Property} objects that can be used to efficiently set property
 * values.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageReflection.class)
public class Type<T> implements Named
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final ClassMap<Type<?>> types = new ClassMap<>(KernelLimits.REFLECTED_CLASSES)
    {
        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        protected Type<?> onInitialize(final Class<?> key)
        {
            return new Type(key);
        }
    };

    @SuppressWarnings("unchecked")
    public static <T> Type<T> forClass(final Class<T> type)
    {
        return (Type<T>) types.getOrCreate(ensureNotNull(type));
    }

    @SuppressWarnings("unchecked")
    public static <T> Type<T> forName(final String className)
    {
        try
        {
            return (Type<T>) forClass(Class.forName(className));
        }
        catch (final ClassNotFoundException e)
        {
            throw new IllegalStateException("Cannot find class " + className, e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Type<T> of(final Object object)
    {
        ensureNotNull(object);

        return (Type<T>) forClass(object.getClass());
    }

    /** Properties stored by name */
    private final Map<PropertyFilter, NameMap<Property>> propertiesForFilter = new IdentityHashMap<>();

    private final Class<T> type;

    private Boolean hasToString;

    private Type(final Class<T> type)
    {
        this.type = type;
    }

    public List<java.lang.reflect.Field> allFields()
    {
        final List<java.lang.reflect.Field> fields = new ArrayList<>();
        for (Type<?> current = this; current != null; current = current.superClass())
        {
            if (current.type != null)
            {
                final var declaredFields = current.type.getDeclaredFields();
                fields.addAll(Arrays.asList(declaredFields));
            }
        }
        return fields;
    }

    public <A extends Annotation> A annotation(final Class<A> annotationType)
    {
        return type.getAnnotation(annotationType);
    }

    public <A extends Annotation> A[] annotations(final Class<A> annotationType)
    {
        return type.getAnnotationsByType(annotationType);
    }

    public Type<?> arrayElementType()
    {
        if (isArray())
        {
            return Type.of(type.getComponentType());
        }
        return null;
    }

    public Constructor<T> constructor(final Class<?>... types)
    {
        try
        {
            return type.getConstructor(types);
        }
        catch (final Exception e)
        {
            LOGGER.problem(e, "Unable to find constructor");
            return null;
        }
    }

    @SuppressWarnings("ConstantConditions")
    public boolean declaresToString()
    {
        if (hasToString == null)
        {
            try
            {
                hasToString = type.getDeclaredMethod("toString") != null;
            }
            catch (final Exception e)
            {
                hasToString = Boolean.FALSE;
            }
        }
        return hasToString;
    }

    public Set<Type<?>> enumValues()
    {
        final var values = new HashSet<Type<?>>();
        for (final var value : type.getEnumConstants())
        {
            values.add(Type.of(value));
        }
        return values;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof Type<?>)
        {
            final Type<?> that = (Type<?>) object;
            return type.equals(that.type);
        }
        return false;
    }

    public Property field(final String name)
    {
        final var iterator = properties(new NamedField(NamingConvention.KIVAKIT, name)).iterator();
        if (iterator.hasNext())
        {
            return iterator.next();
        }
        return null;
    }

    public List<java.lang.reflect.Field> fields(final Filter<java.lang.reflect.Field> filter)
    {
        return allFields().stream().filter(filter::accepts).collect(Collectors.toList());
    }

    public String fullyQualifiedName()
    {
        return type.getName();
    }

    public <A extends Annotation> boolean hasAnnotation(final Class<A> annotationType)
    {
        return annotation(annotationType) != null;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(type);
    }

    public boolean is(final Class<?> type)
    {
        return this.type == type;
    }

    public boolean isArray()
    {
        return type.isArray();
    }

    /**
     * Test if this {@link Type} descends from that class
     *
     * @param that The class to test
     * @return True if this {@link Type} descends from that class
     */
    public boolean isDescendantOf(final Class<?> that)
    {
        Class<?> at = type;
        var isDescendant = at.equals(that);
        while (!isDescendant && !at.equals(Object.class))
        {
            at = at.getSuperclass();
            isDescendant = at.equals(that);
        }
        return isDescendant;
    }

    public boolean isEnum()
    {
        return Enum.class.isAssignableFrom(type);
    }

    public boolean isInside(final PackagePath path)
    {
        return packagePath().startsWith(path);
    }

    public boolean isSystem()
    {
        return type.getName().startsWith("java.");
    }

    public Property method(final String name)
    {
        final var iterator = properties(new NamedMethod(NamingConvention.KIVAKIT, name)).iterator();
        if (iterator.hasNext())
        {
            return iterator.next();
        }
        return null;
    }

    @Override
    public String name()
    {
        return Classes.simpleName(type);
    }

    public T newInstance()
    {
        try
        {
            final var constructor = type.getConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        }
        catch (final Exception e)
        {
            throw new IllegalStateException("Cannot instantiate " + this, e);
        }
    }

    public T newInstance(final Object parameter)
    {
        try
        {
            return type.getConstructor(parameter.getClass()).newInstance(parameter);
        }
        catch (final Exception e)
        {
            throw new IllegalStateException("Couldn't construct " + type + " with " + parameter, e);
        }
    }

    public PackagePath packagePath()
    {
        return PackagePath.packagePath(type);
    }

    /**
     * @return Set of properties, where setter methods are preferred to direct field access
     */
    public ObjectList<Property> properties(final PropertyFilter filter)
    {
        // If we haven't reflected on the properties yet
        var properties = propertiesForFilter.get(filter);
        if (properties == null)
        {
            // create a new set of properties
            properties = new NameMap<>(KernelLimits.PROPERTIES_PER_OBJECT, new TreeMap<>());

            // and add a property getter/setter for each declared field in this type and all super classes
            for (final var field : allFields())
            {
                if (filter.includeField(field))
                {
                    properties.add(new Property(filter.nameForField(field), new FieldGetter(field), new FieldSetter(field)));
                }
            }

            // then add setter properties, overriding any fields
            for (final var method : type.getMethods())
            {
                // this is necessary because the Java compiler creates duplicate method objects for
                // methods inheriting from a generic interface, and the extra synthetic method will
                // not carry any annotation information even when the source code contains a correct
                // @ExcludeProperty
                if (method.isSynthetic())
                {
                    continue;
                }

                if (filter.includeAsGetter(method))
                {
                    final var name = filter.nameForMethod(method);
                    final var property = properties.get(name);
                    if (property == null)
                    {
                        properties.add(new Property(name, new MethodGetter(method), null));
                    }
                    else
                    {
                        property.getter(new MethodGetter(method));
                    }
                }

                if (filter.includeAsSetter(method))
                {
                    final var name = filter.nameForMethod(method);
                    final var property = properties.get(name);
                    if (property == null)
                    {
                        properties.add(new Property(name, null, new MethodSetter(method)));
                    }
                    else
                    {
                        property.setter(new MethodSetter(method));
                    }
                }
            }

            propertiesForFilter.put(filter, properties);
        }
        return ObjectList.objectList(properties.values()).sorted();
    }

    public List<Field> reachableFields(final Object root, final Filter<java.lang.reflect.Field> filter)
    {
        return reachableFields(root, filter, new HashSet<>());
    }

    public List<Object> reachableObjects(final Object root)
    {
        return reachableObjects(root, (field) ->
        {
            final var modifiers = field.getModifiers();
            return !Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers) && !field.getType().isPrimitive();
        });
    }

    public List<Object> reachableObjects(final Object root, final Filter<java.lang.reflect.Field> filter)
    {
        final List<Object> values = new ArrayList<>();

        for (final var field : reachableFields(root, filter))
        {
            final var value = field.value();
            if (value != null)
            {
                values.add(value);
            }
        }

        return values;
    }

    public List<Object> reachableObjectsImplementing(final Object root, final Class<?> _interface)
    {
        return reachableObjects(root, field -> _interface.isAssignableFrom(field.getType()));
    }

    public String simpleName()
    {
        return Classes.simpleName(type);
    }

    public String simpleNameWithoutAnonymousNestedClassNumber()
    {
        return simpleName().replaceAll("\\.[0-9]+", "");
    }

    public Type<?> superClass()
    {
        if (type == null || type == Object.class)
        {
            return null;
        }
        else
        {
            return forClass(type.getSuperclass());
        }
    }

    public Set<Type<?>> superTypes()
    {
        final var supertypes = new HashSet<Type<?>>();

        // Go through each interface of this type,
        for (final var at : type.getInterfaces())
        {
            // and recursively add any superinterfaces,
            supertypes.addAll(Type.forClass(at).superTypes());
        }

        // then get the superclass and add all supertypes of that class
        final var superClass = type.getSuperclass();
        if (superClass != null)
        {
            supertypes.addAll(Type.forClass(superClass).superTypes());
        }

        return supertypes;
    }

    @Override
    public String toString()
    {
        return simpleName();
    }

    /**
     * @return The underlying Java type
     */
    public Class<T> type()
    {
        return type;
    }

    public VariableMap<Object> variables(final Object object, final PropertyFilter filter)
    {
        return variables(object, filter, null);
    }

    public VariableMap<Object> variables(final Object object, final PropertyFilter filter, final Object nullValue)
    {
        final var variables = new VariableMap<>();
        for (final var property : properties(filter))
        {
            if (!"class".equals(property.name()))
            {
                final var getter = property.getter();
                if (getter != null)
                {
                    final var value = getter.get(object);
                    if (value == null)
                    {
                        if (nullValue != null)
                        {
                            variables.add(property.name(), nullValue);
                        }
                    }
                    else
                    {
                        variables.add(property.name(), value);
                    }
                }
            }
        }
        return variables;
    }

    private List<Field> reachableFields(final Object root, final Filter<java.lang.reflect.Field> filter,
                                        final Set<Field> visited)
    {
        final List<Field> fields = new ArrayList<>();
        if (root != null)
        {
            try
            {
                final Type<?> type = of(root);
                if (type != null)
                {
                    for (final var field : type.allFields())
                    {
                        if (field.getDeclaringClass().getModule().isOpen(field.getDeclaringClass().getPackageName()))
                        {
                            if (filter.accepts(field))
                            {
                                final var objectField = new Field(root, field);
                                if (!objectField.isPrimitive())
                                {
                                    final var value = objectField.value();
                                    if (value != null && !visited.contains(objectField))
                                    {
                                        visited.add(objectField);
                                        fields.add(objectField);
                                        fields.addAll(reachableFields(value, filter, visited));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            catch (final Exception e)
            {
                LOGGER.problem(e, "Error while finding reachable fields");
            }
        }
        return fields;
    }
}
