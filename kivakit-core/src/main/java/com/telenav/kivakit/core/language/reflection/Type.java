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

package com.telenav.kivakit.core.language.reflection;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.map.ObjectMap;
import com.telenav.kivakit.core.collections.map.StringMap;
import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.internal.lexakai.DiagramReflection;
import com.telenav.kivakit.core.language.Classes;
import com.telenav.kivakit.core.language.module.PackageReference;
import com.telenav.kivakit.core.language.reflection.accessors.FieldGetter;
import com.telenav.kivakit.core.language.reflection.accessors.FieldSetter;
import com.telenav.kivakit.core.language.reflection.accessors.MethodGetter;
import com.telenav.kivakit.core.language.reflection.accessors.MethodSetter;
import com.telenav.kivakit.core.language.reflection.filters.field.NamedField;
import com.telenav.kivakit.core.language.reflection.filters.method.NamedMethod;
import com.telenav.kivakit.core.language.reflection.property.Property;
import com.telenav.kivakit.core.language.reflection.property.PropertyFilter;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.illegalArgument;
import static com.telenav.kivakit.core.language.reflection.property.PropertyNamingConvention.ANY_NAMING_CONVENTION;

/**
 * Reflects on a class and retains a set of {@link Property} objects that can be used to efficiently set property
 * values.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramReflection.class)
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class Type<T> implements Named
{
    /** Map from Class to Type */
    private static final ObjectMap<Class<?>, Type<?>> types = new ObjectMap<>()
    {
        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        protected Type<?> onCreateValue(Class<?> key)
        {
            return new Type(key);
        }
    };

    /**
     * Gets the type of the given object
     */
    @SuppressWarnings("unchecked")
    public static <T> Type<T> type(Object object)
    {
        ensureNotNull(object);

        return (Type<T>) typeForClass(object.getClass());
    }

    /**
     * Gets the {@link Type} wrapper for the given {@link Class}
     */
    @SuppressWarnings("unchecked")
    public static <T> Type<T> typeForClass(Class<T> type)
    {
        return (Type<T>) types.getOrCreate(ensureNotNull(type));
    }

    /**
     * Gets the {@link Type} wrapper for the given class name. Throws an {@link IllegalArgumentException} if the class
     * cannot be found.
     */
    @SuppressWarnings("unchecked")
    public static <T> Type<T> typeForName(String className)
    {
        try
        {
            return (Type<T>) typeForClass(Class.forName(className));
        }
        catch (ClassNotFoundException e)
        {
            return illegalArgument("Cannot find class " + className, e);
        }
    }

    /** True if the type defines a toString() method */
    private Boolean hasToString;

    /** Properties stored by name */
    private final Map<PropertyFilter, Map<String, Property>> propertiesForFilter = new IdentityHashMap<>();

    /** The underlying class */
    private final Class<T> type;

    private Type(Class<T> type)
    {
        this.type = type;
    }

    /**
     * Returns all fields of this type
     */
    public ObjectList<Field> allFields()
    {
        var fields = new ObjectList<Field>();
        for (Type<?> current = this; !current.is(Object.class); current = current.superClass())
        {
            if (current.type != null)
            {
                for (var field : current.type.getDeclaredFields())
                {
                    fields.add(new Field(this, field));
                }
            }
        }
        return fields;
    }

    /**
     * Returns all methods of this type, including inherited methods
     */
    public ObjectList<Method> allMethods()
    {
        var methods = new ObjectList<Method>();
        for (Type<?> current = this; !current.is(Object.class); current = current.superClass())
        {
            if (current.type != null)
            {
                for (var method : current.type.getDeclaredMethods())
                {
                    methods.add(new Method(this, method));
                }
            }
        }
        return methods;
    }

    /**
     * Returns the annotation of the given type on this type
     */
    public <A extends Annotation> A annotation(Class<A> annotationType)
    {
        return type.getAnnotation(annotationType);
    }

    /**
     * Returns all annotations of the given type on this type
     */
    public <A extends Annotation> A[] annotations(Class<A> annotationType)
    {
        return type.getAnnotationsByType(annotationType);
    }

    /**
     * Returns the array element type for this type if it is an array
     */
    public Type<?> arrayElementType()
    {
        if (isArray())
        {
            return Type.type(type.getComponentType());
        }
        return null;
    }

    /**
     * Returns the constructor with the given parameters
     *
     * @param parameters The constructor parameter types
     * @return The constructor
     */
    public Constructor<T> constructor(Class<?>... parameters)
    {
        try
        {
            return type.getConstructor(parameters);
        }
        catch (Exception e)
        {
            return illegalArgument("Unable to find constructor");
        }
    }

    /**
     * Returns true if this type directly declares a toString() method
     */
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

    /**
     * Returns the enum values for this type if it is an enum
     */
    public Set<Enum<?>> enumValues()
    {
        var values = new HashSet<Enum<?>>();
        for (var value : type.getEnumConstants())
        {
            values.add((Enum<?>) value);
        }
        return values;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * Returns the field {@link Property} of the given name in this type
     */
    public Property field(String name)
    {
        var iterator = properties(new NamedField(name)).iterator();
        if (iterator.hasNext())
        {
            return iterator.next();
        }
        return null;
    }

    /**
     * Returns the list of all fields in this type that match the given matcher
     *
     * @param matcher The matcher to match against
     */
    public ObjectList<Field> fields(Matcher<Field> matcher)
    {
        return allFields().matching(matcher);
    }

    /**
     * Returns the fully qualified class name for this type
     */
    public String fullyQualifiedName()
    {
        return type.getName();
    }

    /**
     * Returns true if this type declares the given annotation
     */
    public <A extends Annotation> boolean hasAnnotation(Class<A> annotationType)
    {
        return annotation(annotationType) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return type.hashCode();
    }

    /**
     * Returns a list of any interfaces that this type implements
     */
    public ObjectList<Type<?>> interfaces()
    {
        var interfaces = new ObjectList<Type<?>>();
        for (var at : type.getInterfaces())
        {
            interfaces.add(Type.typeForClass(at));
        }
        return interfaces;
    }

    /**
     * Returns true if this type is the same as the given type
     */
    public boolean is(Class<?> type)
    {
        return this.type.equals(type);
    }

    /**
     * Returns true if this type is an array
     */
    public boolean isArray()
    {
        return type.isArray();
    }

    /**
     * Returns true if this {@link Type} descends from the given class
     *
     * @param that The class to test
     * @return True if this {@link Type} descends from the given class
     */
    public boolean isDescendantOf(Class<?> that)
    {
        return that.isAssignableFrom(type);
    }

    /**
     * Returns true if this type is an enum
     */
    public boolean isEnum()
    {
        return Enum.class.isAssignableFrom(type);
    }

    /**
     * Returns true if this type is in or below the given package
     *
     * @param reference The package reference
     */
    public boolean isInOrUnder(PackageReference reference)
    {
        return packagePath().startsWith(reference);
    }

    /**
     * Returns true if this is a primitive type
     */
    public boolean isPrimitive()
    {
        return type.isPrimitive();
    }

    /**
     * Returns true if this is a system type, meaning that it is under the package java or javax
     */
    public boolean isSystem()
    {
        return name().startsWith("java.") || name().startsWith("javax.");
    }

    /**
     * Returns any method in this type with the given name
     */
    public Method method(String methodName)
    {
        try
        {
            return new Method(this, type.getMethod(methodName));
        }
        catch (Exception e)
        {
            return illegalArgument("Unable to get method $.$", type, methodName);
        }
    }

    /**
     * Returns the simple name of this type
     */
    @Override
    public String name()
    {
        return Classes.simpleName(type);
    }

    /**
     * Creates an instance of this type using any no-argument constructor
     */
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
            return illegalArgument("Cannot instantiate " + this, e);
        }
    }

    /**
     * Creates an instance of this type using any constructor that accepts the given parameters
     */
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
            return illegalArgument("Couldn't construct " + type + "(" + Arrays.toString(parameters) + ")", e);
        }
    }

    /**
     * Returns the package reference to the package that contains this type
     */
    public PackageReference packagePath()
    {
        return PackageReference.packageReference(type);
    }

    /**
     * @return Set of properties matching the given filter, where setter methods are preferred to direct field access
     */
    public ObjectList<Property> properties(PropertyFilter filter)
    {
        // If we haven't reflected on the properties yet
        var properties = propertiesForFilter.get(filter);
        if (properties == null)
        {
            // create a new set of properties
            properties = new StringMap<>();

            // and add a property getter/setter for each declared field in this type and all super classes
            for (var field : allFields())
            {
                if (filter.includeField(field))
                {
                    properties.put(filter.nameForField(field), new Property(filter.nameForField(field), new FieldGetter(field), new FieldSetter(field)));
                }
            }

            // then add setter properties, overriding any fields
            for (var method : allMethods())
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
                        properties.put(name, new Property(name, new MethodGetter(method), null));
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
                        properties.put(name, new Property(name, null, new MethodSetter(method)));
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

    /**
     * Returns a map of the properties of the given object that match the given matcher
     *
     * @param object The object to search
     * @param matcher The matcher to match
     */
    public VariableMap<Object> properties(Object object, PropertyFilter matcher)
    {
        return properties(object, matcher, null);
    }

    /**
     * Returns a map of the properties of the given object that match the given matcher
     *
     * @param object The object to search
     * @param matcher The matcher to match
     * @param nullValue The null value to store when a property value is null
     */
    public VariableMap<Object> properties(Object object, PropertyFilter matcher, Object nullValue)
    {
        var variables = new VariableMap<>();
        for (var property : properties(matcher))
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

    /**
     * Returns the first property with the given name
     *
     * @param name The property name
     * @return The property
     */
    public Property property(String name)
    {
        return properties(new NamedMethod(ANY_NAMING_CONVENTION, name)).first();
    }

    /**
     * Returns a list of fields reachable from the given root that match the given matcher
     *
     * @param root The root object to search
     * @param matcher The field matcher
     */
    public ObjectList<Field> reachableFields(Object root, Matcher<Field> matcher)
    {
        return reachableFields(root, matcher, new HashSet<>());
    }

    /**
     * Returns a list of objects reachable from the given root
     */
    public ObjectList<Object> reachableObjects(Object root)
    {
        return reachableObjects(root, field ->
                !field.isStatic() && !field.isTransient() && !field.isPrimitive());
    }

    /**
     * Returns a list of objects reachable from the root that match the given matcher
     *
     * @param root The root object
     * @param matcher The matcher to match
     */
    public ObjectList<Object> reachableObjects(Object root, Matcher<Field> matcher)
    {
        var values = new ObjectList<>();

        for (var field : reachableFields(root, matcher))
        {
            var value = field.get();
            if (value != null)
            {
                values.add(value);
            }
        }

        return values;
    }

    /**
     * Returns a list of objects reachable from the root that implement the given interface
     *
     * @param root The root object
     * @param _interface The interface that must be implemented
     */
    public ObjectList<Object> reachableObjectsImplementing(Object root, Class<?> _interface)
    {
        return reachableObjects(root, field -> _interface.isAssignableFrom(field.type().type()));
    }

    /**
     * Returns the simple name of this type
     */
    public String simpleName()
    {
        return Classes.simpleName(type);
    }

    /**
     * Returns the simple name of this type without any anonymous class number
     */
    public String simpleNameWithoutAnonymousNestedClassNumber()
    {
        return simpleName().replaceAll("\\.[0-9]+", "");
    }

    /**
     * Returns the superclass of this type
     */
    public Type<?> superClass()
    {
        if (type == null)
        {
            return null;
        }
        else
        {
            Class<? super T> superclass = type.getSuperclass();
            return superclass == null ? null : typeForClass(superclass);
        }
    }

    /**
     * Returns a list of the superclasses of this type
     */
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

    /**
     * Returns a list of the super-interfaces of this type
     */
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

    /**
     * Returns a list of all supertypes of this type
     */
    public ObjectList<Type<?>> superTypes()
    {
        var supertypes = superClasses();

        for (var at : supertypes.copy())
        {
            supertypes.addAll(at.superInterfaces());
        }

        return supertypes;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * Returns the fields reachable from the given root which match the given matcher
     *
     * @param root The root object
     * @param matcher The matcher to match against
     * @param visited The set of fields that have been visited (to prevent cycles)
     */
    private ObjectList<Field> reachableFields(Object root,
                                              Matcher<Field> matcher,
                                              Set<Field> visited)
    {
        var fields = new ObjectList<Field>();
        if (root != null)
        {
            try
            {
                Type<?> type = type(root);
                if (type != null)
                {
                    for (var field : type.allFields())
                    {
                        if (field.type().type().getDeclaringClass().getModule().isOpen(field.type().type().getDeclaringClass().getPackageName()))
                        {
                            if (matcher.matches(field))
                            {
                                if (!field.isPrimitive())
                                {
                                    var value = field.get(root);
                                    if (value != null && !visited.contains(field))
                                    {
                                        visited.add(field);
                                        fields.add(field);
                                        fields.addAll(reachableFields(value, matcher, visited));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                illegalArgument("Error while finding reachable fields");
            }
        }
        return fields;
    }
}
