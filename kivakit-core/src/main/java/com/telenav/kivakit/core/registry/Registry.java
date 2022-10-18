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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramRegistry;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.util.HashMap;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;

/**
 * <p>
 * The {@link Registry} class allows code to register and locate objects by class and instance (if there is more than
 * one instance). The methods {@link #register(Object)} and {@link #register(Object, Enum)} are used to install an
 * object in the lookup and {@link #lookup(Class)}, {@link #lookup(Class, Enum)}, {@link #require(Class)} and
 * {@link #require(Class, Enum)} are used to find an object that has been registered.
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
 *     Registry.of(this).register(new Server(), WEB);
 * }
 *
 *     [...]
 *
 * void doIt()
 * {
 *     var server = Registry.of(this).lookup(Server.class, WEB);
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
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public class Registry implements RegistryTrait
{
    /** The global lookup */
    private static final Registry GLOBAL_REGISTRY = new Registry();

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
     * Returns the registry to use for the given object. Currently this simply returns the global registry. In the
     * future, it may return registries specific to different objects.
     *
     * @return The lookup for the given object
     */
    @SuppressWarnings("unused")
    public static Registry registryFor(Object object)
    {
        return globalRegistry();
    }

    /** Map from class and instance to type for multi-instance objects */
    private final Map<RegistryKey, Object> registered = new HashMap<>();

    /**
     * Returns any registered object of the given type with the given instance identifier
     */
    @Override
    @SuppressWarnings({ "unchecked" })
    public <T> T lookup(Class<T> type, InstanceIdentifier instance)
    {
        return (T) registered.get(instance.key(type));
    }

    /**
     * Registers the specified instance of the given object's type in the lookup
     */
    @Override
    public <T> T register(T object, InstanceIdentifier instance)
    {
        ensureNotNull(object);

        for (var at = object.getClass(); at != Object.class; at = at.getSuperclass())
        {
            registered.put(instance.key(at), object);
            for (var next : at.getInterfaces())
            {
                registered.put(instance.key(next), object);
            }
        }

        return object;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T require(Class<T> type, InstanceIdentifier instance)
    {
        return ensureNotNull(lookup(type, instance), "Unable to find required object: $:$", type, instance);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregister(Object object, InstanceIdentifier instance)
    {
        registered.remove(instance.key(object.getClass()));
    }

    @Override
    public void unregisterAll()
    {
        registered.clear();
    }
}
