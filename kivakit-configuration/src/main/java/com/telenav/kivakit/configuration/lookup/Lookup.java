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

import com.telenav.kivakit.configuration.InstanceIdentifier;
import com.telenav.kivakit.configuration.project.lexakai.diagrams.DiagramLookup;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@link Lookup} class allows code to register and locate objects by class and instance (if there is more than one
 * instance). The methods {@link #register(Object)} and {@link #register(Object, Enum)} are used to install an object in
 * the lookup and {@link #locate(Class)} and {@link #locate(Class, Enum)} are used to find an object that has been
 * registered.
 *
 * <p><b>Example</b></p>
 *
 * <p>
 * In this example, the initialize() method and the doit() method may be in different classes or packages. The doit()
 * method can find the WEB instance of the Server class using {@link #locate(Class, Enum)}
 * </p>
 * <pre>
 * enum Servers { WEB, TRAFFIC, MAP }
 *
 * void initialize()
 * {
 *     Lookup.get().register(new Server(), WEB);
 * }
 *
 *     [...]
 *
 * void doit()
 * {
 *     var server = Lookup.get().locate(Server.class, WEB);
 *
 *         [...]
 * }
 * </pre>
 */
@UmlClassDiagram(diagram = DiagramLookup.class)
@UmlRelation(label = "locates instances with", referent = InstanceIdentifier.class)
public class Lookup
{
    /** The global lookup */
    private static final Lookup GLOBAL = new Lookup();

    /**
     * @return The global lookup
     */
    public static Lookup global()
    {
        return GLOBAL;
    }

    /** Map from class to type for singleton objects */
    private final Map<Class<?>, Object> objectForType = new HashMap<>();

    /** Map from class and instance to type for multi-instance objects */
    private final Map<String, Object> objectForTypeAndInstance = new HashMap<>();

    /**
     * @return Any registered object of the given type
     */
    @SuppressWarnings("unchecked")
    public <T> T locate(final Class<T> type)
    {
        return (T) objectForType.get(type);
    }

    /**
     * @return Any registered object of the given type with the given instance identifier
     */
    @SuppressWarnings({ "unchecked" })
    public <T> T locate(final Class<T> type, final InstanceIdentifier instance)
    {
        return (T) objectForTypeAndInstance.get(key(type, instance));
    }

    public <T> T locate(final Class<T> type, final Enum<?> instance)
    {
        return locate(type, new InstanceIdentifier(instance));
    }

    /**
     * Registers the given singleton object in the lookup
     */
    public <T> T register(final T singleton)
    {
        for (var at = singleton.getClass(); at != Object.class; at = at.getSuperclass())
        {
            objectForType.put(at, singleton);
        }
        return singleton;
    }

    /**
     * Registers the specified instance of the given object's type in the lookup
     */
    public void register(final Object object, final InstanceIdentifier instance)
    {
        if (InstanceIdentifier.SINGLETON.equals(instance))
        {
            register(object);
        }
        else
        {
            for (var at = object.getClass(); at != Object.class; at = at.getSuperclass())
            {
                objectForTypeAndInstance.put(key(at, instance), object);
            }
        }
    }

    /**
     * Registers the specified instance of the given object's type in the lookup
     */
    public void register(final Object object, final Enum<?> instance)
    {
        register(object, new InstanceIdentifier(instance));
    }

    private String key(final Class<?> at, final InstanceIdentifier identifier)
    {
        return at.getName() + identifier.toString();
    }
}
