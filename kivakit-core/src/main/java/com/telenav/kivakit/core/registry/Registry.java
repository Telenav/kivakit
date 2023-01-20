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

package com.telenav.kivakit.core.registry;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.collections.map.MultiMap;
import com.telenav.kivakit.core.internal.lexakai.DiagramRegistry;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.os.Console.console;
import static com.telenav.kivakit.core.string.AsciiArt.TextBoxStyle.OPEN;
import static com.telenav.kivakit.core.string.ObjectFormatter.ObjectFormat.MULTILINE;

/**
 * <p>
 * The {@link Registry} class allows code to register and locate objects by class and instance (if there is more than
 * one instance). The methods {@link #register(Object)} and {@link #register(Object, Enum)} are used to install an
 * object in the lookup and {@link #lookup(Class)}, {@link #lookup(Class, Enum)}, {@link #require(Class)} and
 * {@link #require(Class, Enum)} are used to find an object that has been registered.
 * </p>
 *
 * <p><b>Supertype Registration</b></p>
 *
 * <p>
 * All supertypes of an object that are marked with the annotation {@link Register} will also be registered. For
 * example, if an object of class PostgresDatabase is registered, and its superinterface Database is marked with
 * {@link Register}, then the object will also be registered under that type as well.
 * </p>
 *
 * <p><b>RegistryTrait</b></p>
 *
 * <p>
 * The {@link RegistryTrait} interface provides a set of default convenience methods that can be added to any class. The
 * Component interface in kivakit-component extends {@link RegistryTrait} to provide easy access to registry methods to
 * all components.
 * </p>
 *
 * <p><b>Example</b></p>
 *
 * <p>
 * In this example, the initialize() method and the doIt() method may be in different classes or packages. The doIt()
 * method can find the WEB instance of the Server class using {@link #lookup(Class, Enum)}
 * </p>
 * <pre>
 * enum Servers { WEB, TRAFFIC, MAP }
 *
 * void initialize()
 * {
 *     register(new Server(), WEB);
 * }
 *
 *     [...]
 *
 * void doIt()
 * {
 *     var server = lookup(Server.class, WEB);
 *
 *         [...]
 * }
 * </pre>
 *
 * @author jonathanl (shibo)
 * @see RegistryTrait
 * @see InstanceIdentifier
 */
@UmlClassDiagram(diagram = DiagramRegistry.class)
@UmlRelation(label = "locates instances with", referent = InstanceIdentifier.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public class Registry implements RegistryTrait, Named
{
    /** The global lookup */
    private static final Registry GLOBAL_REGISTRY = new Registry("Registry");

    /**
     * Returns the global registry
     *
     * @return The global registry
     */
    public static Registry globalRegistry()
    {
        return GLOBAL_REGISTRY;
    }

    /**
     * Returns the registry to use for the given object. Currently, this simply returns the global registry. In the
     * future, it may return registries specific to different objects.
     *
     * @return The lookup for the given object
     */
    @SuppressWarnings("unused")
    public static Registry registryFor(Object object)
    {
        return globalRegistry();
    }

    /** The name of this registry */
    private final String name;

    /** Map from class and instance to type for multi-instance objects */
    private final Map<RegistryKey, Object> registered = new HashMap<>();

    public Registry(String name)
    {
        this.name = name;
    }

    public StringList asStringList()
    {
        var registrations = stringList();

        registrations.add(name() + ":");
        registrations.add("");

        var keysForObject = new MultiMap<Object, RegistryKey>(new IdentityHashMap<>());

        for (var entry : registered.entrySet())
        {
            keysForObject.add(entry.getValue(), entry.getKey());
        }

        for (var object : keysForObject.keySet())
        {
            var keys = keysForObject.list(object);

            var formatted = new ObjectFormatter(object).indent(4).asString(MULTILINE);
            if (formatted.trim().equals("{}"))
            {
                formatted = "{}";
            }
            else
            {
                formatted = "\n" + formatted;
            }
            registrations.add("    $ $\n", keys.join(", "), formatted);
        }

        if (registrations.isEmpty())
        {
            registrations.add("Empty");
        }

        return registrations;
    }

    /**
     * Returns any registered object of the given type with the given instance identifier
     */
    @Override
    @SuppressWarnings({ "unchecked" })
    public synchronized <T> T lookup(Class<T> type, InstanceIdentifier instance)
    {
        if (registered.get(instance.key(type)) == null)
        {
            console().println("hi");
        }
        return (T) registered.get(instance.key(type));
    }

    @Override
    public String name()
    {
        return name;
    }

    /**
     * Registers the specified instance of the given object in this registry.
     *
     * <p><b>Supertype Registration</b></p>
     *
     * <p>
     * All supertypes of an object that are marked with the annotation {@link Register} will also be registered. For
     * example, if an object of class PostgresDatabase is registered, and its superinterface Database is marked with
     * {@link Register}, then the object will also be registered under that type as well.
     * </p>
     */
    @Override
    public synchronized <T> T register(T object, InstanceIdentifier instance)
    {
        ensureNotNull(object);

        var type = object.getClass();

        // Register the object,
        registered.put(instance.key(type), object);

        // and its interfaces,
        registerInterfaces(object, type, instance);

        // then walk up the superclass chain to Object,
        for (type = type.getSuperclass(); type != Object.class; type = type.getSuperclass())
        {
            // and if the class is annotated with @Register,
            if (type.isAnnotationPresent(Register.class))
            {
                // then register it under that key as well,
                registered.put(instance.key(type), object);
            }

            // and also register the object under all of its @Register annotated interfaces.
            registerInterfaces(object, type, instance);
        }

        return object;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized <T> T require(Class<T> type, InstanceIdentifier instance)
    {
        return ensureNotNull(lookup(type, instance), "Unable to find required object: $:$", type, instance);
    }

    @Override
    public String toString()
    {
        return asStringList().titledBox(OPEN, "$", name());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void unregister(Object object, InstanceIdentifier instance)
    {
        registered.remove(instance.key(object.getClass()));
    }

    @Override
    public synchronized void unregisterAll()
    {
        registered.clear();
    }

    /**
     * Registers all interfaces of the given object that are annotated with @Register under the given instance
     * identifier.
     *
     * @param object The object whose interfaces should be registered
     * @param type The type or supertype of the object
     * @param instance The object instance
     */
    private void registerInterfaces(Object object, Class<?> type, InstanceIdentifier instance)
    {
        // and for each interface of the class,
        for (var at : type.getInterfaces())
        {
            // if the interface is marked with @Register,
            if (at.isAnnotationPresent(Register.class))
            {
                // register it under that key as well.
                registered.put(instance.key(at), object);
            }
        }
    }
}
