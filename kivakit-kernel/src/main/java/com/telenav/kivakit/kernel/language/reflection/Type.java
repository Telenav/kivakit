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
import com.telenav.kivakit.kernel.language.collections.set.ObjectSet;
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
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.project.KernelLimits;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
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
        protected Type<?> onInitialize(Class<?> key)
        {
            return new Type(key);
        }
    };

    @SuppressWarnings("unchecked")
    public static <T> Type<T> forClass(Class<T> type)
    {
        return (Type<T>) types.getOrCreate(ensureNotNull(type));
    }

    @SuppressWarnings("unchecked")
    public static <T> Type<T> forName(String className)
    {
        try
        {
            return (Type<T>) forClass(Class.forName(className));
        }
        catch (ClassNotFoundException e)
        {
            throw new IllegalStateException("Cannot find class " + className, e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Type<T> of(Object object)
    {
        ensureNotNull(object);

        return (Type<T>) forClass(object.getClass());
    }

    /** Properties stored by name */
    private final Map<PropertyFilter, NameMap<Property>> propertiesForFilter = new IdentityHashMap<>();

    private final Class<T> type;

    private Boolean hasToString;

    private Type(Class<T> type)
    {
        this.type = type;
    }

    public List<java.lang.reflect.Field> allFields()
    {
        List<java.lang.reflect.Field> fields = new ArrayList<>();
        for (Type<?> current = this; !current.is(Object.class); current = current.superClass())
        {
            if (current.type != null)
            {
                var declaredFields = current.type.getDeclaredFields();
                fields.addAll(Arrays.asList(declaredFields));
            }
        }
        return fields;
    }

    public <A extends Annotation> A annotation(Class<A> annotationType)
    {
        return type.getAnnotation(annotationType);
    }

    public <A extends Annotation> A[] annotations(Class<A> annotationType)
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

    public Constructor<T> constructor(Class<?>... types)
    {
        try
        {
            return type.getConstructor(types);
        }
        catch (Exception e)
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
            catch (Exception e)
            {
                hasToString = Boolean.FALSE;
            }
        }
        return hasToString;
    }

    public Set<Enum<?>> enumValues()
    {
        var values = new HashSet<Enum<?>>();
        for (var value : type.getEnumConstants())
        {
            values.add((Enum<?>) value);
        }
        return values;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Type<?>)
        {
            Type<?> that = (Type<?>) object;
            return type.equals(that.type);
        }
        return false;
    }

    public Property field(String name)
    {
        var iterator = properties(new NamedField(NamingConvention.KIVAKIT, name)).iterator();
        if (iterator.hasNext())
        {
            return iterator.next();
        }
        return null;
    }

    public List<java.lang.reflect.Field> fields(Filter<java.lang.reflect.Field> filter)
    {
        return allFields().stream().filter(filter::accepts).collect(Collectors.toList());
    }

    public String fullyQualifiedName()
    {
        return type.getName();
    }

    public <A extends Annotation> boolean hasAnnotation(Class<A> annotationType)
    {
        return annotation(annotationType) != null;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(type);
    }

    public List<Type<?>> interfaces()
    {
        var interfaces = new ArrayList<Type<?>>();
        for (var at : type.getInterfaces())
        {
            interfaces.add(Type.forClass(at));
        }
        return interfaces;
    }

    public boolean is(Class<?> type)
    {
        return this.type.equals(type);
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
    public boolean isDescendantOf(Class<?> that)
    {
        return that.isAssignableFrom(type);
    }

    public boolean isEnum()
    {
        return Enum.class.isAssignableFrom(type);
    }

    public boolean isInside(PackagePath path)
    {
        return packagePath().startsWith(path);
    }

    public boolean isPrimitive()
    {
        return type.isPrimitive();
    }

    public boolean isSystem()
    {
        return type.getName().startsWith("java.");
    }

    public Method method(String methodName)
    {
        try
        {
            return new Method(type, type.getMethod(methodName));
        }
        catch (Exception e)
        {
            throw new IllegalStateException(Message.format("Unable to get method $.$", type, methodName));
        }
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
            var constructor = type.getConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        }
        catch (Exception e)
        {
            throw new IllegalStateException("Cannot instantiate " + this, e);
        }
    }

    public T newInstance(Object... parameters)
    {
        try
        {
            var types = new Class<?>[parameters.length];
            int i = 0;
            for (var at : parameters)
            {
                types[i++] = at.getClass();
            }
            return type.getConstructor(types).newInstance(parameters);
        }
        catch (Exception e)
        {
            throw new IllegalStateException("Couldn't construct " + type + "(" + Arrays.toString(parameters) + ")", e);
        }
    }

    public PackagePath packagePath()
    {
        return PackagePath.packagePath(type);
    }

    /**
     * @return Set of properties, where setter methods are preferred to direct field access
     */
    public ObjectList<Property> properties(PropertyFilter filter)
    {
        // If we haven't reflected on the properties yet
        var properties = propertiesForFilter.get(filter);
        if (properties == null)
        {
            // create a new set of properties
            properties = new NameMap<>(KernelLimits.PROPERTIES_PER_OBJECT, new TreeMap<>());

            // and add a property getter/setter for each declared field in this type and all super classes
            for (var field : allFields())
            {
                if (filter.includeField(field))
                {
                    properties.add(new Property(filter.nameForField(field), new FieldGetter(field), new FieldSetter(field)));
                }
            }

            // then add setter properties, overriding any fields
            for (var method : type.getMethods())
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
                    var name = filter.nameForMethod(method);
                    var property = properties.get(name);
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
                    var name = filter.nameForMethod(method);
                    var property = properties.get(name);
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

    public Property property(String name)
    {
        return properties(new NamedMethod(NamingConvention.KIVAKIT, name)).first();
    }

    public List<Field> reachableFields(Object root, Filter<java.lang.reflect.Field> filter)
    {
        return reachableFields(root, filter, new HashSet<>());
    }

    public List<Object> reachableObjects(Object root)
    {
        return reachableObjects(root, (field) ->
        {
            var modifiers = field.getModifiers();
            return !Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers) && !field.getType().isPrimitive();
        });
    }

    public List<Object> reachableObjects(Object root, Filter<java.lang.reflect.Field> filter)
    {
        List<Object> values = new ArrayList<>();

        for (var field : reachableFields(root, filter))
        {
            var value = field.value();
            if (value != null)
            {
                values.add(value);
            }
        }

        return values;
    }

    public List<Object> reachableObjectsImplementing(Object root, Class<?> _interface)
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
        if (type == null)
        {
            return null;
        }
        else
        {
            Class<? super T> superclass = type.getSuperclass();
            return superclass == null ? null : forClass(superclass);
        }
    }

    public ObjectList<Type<?>> superClasses()
    {
        var superclasses = new ObjectList<Type<?>>();

        var superClass = superClass();
        if (superClass != null)
        {
            superclasses.add(superClass);
            superclasses.addAll(superClass.superClasses());
        }

        return superclasses;
    }

    @NotNull
    public ObjectSet<Type<?>> superInterfaces()
    {
        var superinterfaces = new ObjectSet<Type<?>>(new LinkedHashSet<>());

        // Go through each interface of this type,
        for (var at : interfaces())
        {
            // and recursively add any superinterfaces,
            superinterfaces.add(at);
            superinterfaces.addAll(at.superInterfaces());
        }

        // then get the superclass and add all supertypes of that class
        return superinterfaces;
    }

    public ObjectList<Type<?>> superTypes()
    {
        var supertypes = superClasses();

        for (var at : supertypes.copy())
        {
            supertypes.addAll(at.superInterfaces());
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

    public VariableMap<Object> variables(Object object, PropertyFilter filter)
    {
        return variables(object, filter, null);
    }

    public VariableMap<Object> variables(Object object, PropertyFilter filter, Object nullValue)
    {
        var variables = new VariableMap<>();
        for (var property : properties(filter))
        {
            if (!"class".equals(property.name()))
            {
                var getter = property.getter();
                if (getter != null)
                {
                    var value = getter.get(object);
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

    private List<Field> reachableFields(Object root, Filter<java.lang.reflect.Field> filter,
                                        Set<Field> visited)
    {
        List<Field> fields = new ArrayList<>();
        if (root != null)
        {
            try
            {
                Type<?> type = of(root);
                if (type != null)
                {
                    for (var field : type.allFields())
                    {
                        if (field.getDeclaringClass().getModule().isOpen(field.getDeclaringClass().getPackageName()))
                        {
                            if (filter.accepts(field))
                            {
                                var objectField = new Field(root, field);
                                if (!objectField.isPrimitive())
                                {
                                    var value = objectField.value();
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
            catch (Exception e)
            {
                LOGGER.problem(e, "Error while finding reachable fields");
            }
        }
        return fields;
    }
}
