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

package com.telenav.kivakit.configuration.lookup;

import com.telenav.kivakit.configuration.project.lexakai.diagrams.DiagramLookup;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.util.HashMap;
import java.util.Map;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * <p>
 * The {@link Registry} class allows code to register and locate objects by class and instance (if there is more than
 * one instance). The methods {@link #register(Object)} and {@link #register(Object, Enum)} are used to install an
 * object in the lookup and {@link #lookup(Class)}, {@link #lookup(Class, Enum)}, {@link #require(Class)} and {@link
 * #require(Class, Enum)} are used to find an object that has been registered.
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
 * In this example, the initialize() method and the doit() method may be in different classes or packages. The doit()
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
 * void doit()
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
@UmlClassDiagram(diagram = DiagramLookup.class)
@UmlRelation(label = "locates instances with", referent = InstanceIdentifier.class)
public class Registry implements RegistryTrait
{
    /** The global lookup */
    private static final Registry GLOBAL = new Registry();

    /**
     * NOTE: This method should only be called from static methods
     *
     * @return The global registry
     */
    public static Registry global()
    {
        return GLOBAL;
    }

    /**
     * @return The lookup for the given object
     */
    public static Registry of(Object object)
    {
        return global();
    }

    /** Map from class and instance to type for multi-instance objects */
    private final Map<RegistryKey, Object> registered = new HashMap<>();

    /**
     * @return Any registered object of the given type with the given instance identifier
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

    @Override
    public <T> T require(Class<T> type, InstanceIdentifier instance)
    {
        return ensureNotNull(lookup(type, instance));
    }
}
