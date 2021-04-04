////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.reflection;

import com.telenav.kivakit.core.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.core.kernel.interfaces.naming.Named;
import com.telenav.kivakit.core.kernel.language.paths.PackagePath;
import com.telenav.kivakit.core.kernel.language.types.Classes;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.project.CoreKernelLimits;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.core.kernel.language.collections.map.ClassMap;
import com.telenav.kivakit.core.kernel.language.collections.map.string.NameMap;
import com.telenav.kivakit.core.kernel.language.collections.map.string.VariableMap;
import com.telenav.kivakit.core.kernel.language.reflection.access.field.FieldGetter;
import com.telenav.kivakit.core.kernel.language.reflection.access.field.FieldSetter;
import com.telenav.kivakit.core.kernel.language.reflection.access.method.MethodGetter;
import com.telenav.kivakit.core.kernel.language.reflection.access.method.MethodSetter;
import com.telenav.kivakit.core.kernel.language.reflection.property.Property;
import com.telenav.kivakit.core.kernel.language.reflection.property.PropertyFilter;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.field.NamedField;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.method.NamedMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

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

    private static final ClassMap<Type<?>> types = new ClassMap<>(CoreKernelLimits.REFLECTED_CLASSES)
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
        return (Type<T>) types.getOrCreate(type);
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
        return (Type<T>) forClass(object.getClass());
    }

    /** Properties stored by name */
    private final Map<PropertyFilter, NameMap<Property>> propertiesForFilter = new HashMap<>();

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

    public Property field(final String name)
    {
        final var iterator = properties(new NamedField(name)).iterator();
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
        final var iterator = properties(new NamedMethod(name)).iterator();
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
            properties = new NameMap<>(CoreKernelLimits.PROPERTIES_PER_OBJECT, new TreeMap<>());

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
