package com.telenav.kivakit.core.registry;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.interfaces.factory.Factory;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.registry.InstanceIdentifier.instanceIdentifier;
import static com.telenav.kivakit.core.registry.InstanceIdentifier.singleton;
import static com.telenav.kivakit.core.registry.Registry.registryFor;

/**
 * <p>
 * A stateless trait for accessing the {@link Registry} for the implementing component. Includes methods to look up,
 * register and require settings objects. When there is more than one settings object of a given type, an
 * {@link InstanceIdentifier} can be given to distinguish between instances. Convenience methods are provided to create
 * instance identifiers for {@link Enum}s and {@link String}s. The Component interface in kivakit-component uses this
 * trait to add easy access to registry methods to all components.
 * </p>
 *
 * <p><b>Lookup methods</b></p>
 * <ul>
 *     <li>{@link #lookup(Class)} - Locates the registered instance of the given class</li>
 *     <li>{@link #lookup(Class, Enum)} - Locates the specified registered instance of the given class</li>
 * </ul>
 *
 * <p><b>Register methods</b></p>
 * <ul>
 *     <li>{@link #register(Object)} - Registers the given object</li>
 *     <li>{@link #register(Object, Enum)} - Registers the given identified object instance</li>
 * </ul>
 *
 * <p><b>Require methods</b></p>
 * <ul>
 *     <li>{@link #require(Class)} - Locates the registered instance of the given class or fails</li>
 *     <li>{@link #require(Class, Enum)} - Locates the specified registered instance of the given class or fails</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see Registry
 */
@SuppressWarnings("unused")
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public interface RegistryTrait
{
    /**
     * Convenience method to look up a singleton object
     *
     * @param type The type of object to look up
     * @return The object
     */
    default <T> T lookup(Class<T> type)
    {
        return lookup(type, singleton());
    }

    /**
     * Convenience method to look up a particular instance of an object
     *
     * @param type The type of object to look up
     * @param instance The instance to find
     * @return The object
     */
    default <T> T lookup(Class<T> type, Enum<?> instance)
    {
        return lookup(type, instanceIdentifier(instance));
    }

    /**
     * @param type The type of object to look up
     * @param instance The instance to find
     * @return The object
     */
    default <T> T lookup(Class<T> type, InstanceIdentifier instance)
    {
        return registry().lookup(type, instance);
    }

    /**
     * Registers the given singleton object in the lookup
     *
     * @param object The object to register
     * @return The object
     */
    default <T> T register(T object)
    {
        return registry().register(object, singleton());
    }

    /**
     * Registers the specified instance of the given object's type in the lookup
     *
     * @param object The object to register
     * @param instance Which instance is being registered
     * @return The object
     */
    default <T> T register(T object, Enum<?> instance)
    {
        return register(object, instanceIdentifier(instance));
    }

    /**
     * Registers the specified instance of the given object's type in the lookup
     *
     * @param object The object to register
     * @param instance Which instance is being registered
     * @return The object
     */
    default <T> T register(T object, InstanceIdentifier instance)
    {
        return registry().register(object, instance);
    }

    /**
     * Retrieves the registry for the component implementing this trait
     *
     * @return The lookup registry for this component
     */
    default Registry registry()
    {
        return registryFor(this);
    }

    /**
     * If the given type cannot be found in the registry with {@link #lookup(Class)}, an instance is created using the
     * given factory and registered with {@link #register(Object)}. The value is then returned.
     *
     * @param type The type that's required
     * @param factory A factory for creating the type if it can't be found
     * @return The required value
     */
    default <T> T require(Class<T> type, Factory<T> factory)
    {
        var value = lookup(type);
        if (value == null)
        {
            value = factory.newInstance();
            register(value);
        }
        ensureNotNull(value, "Could not find or create: $", type);
        return value;
    }

    /**
     * Convenience method to look up the given object and fail if it isn't found
     *
     * @param type The object to find
     * @return The object
     */
    default <T> T require(Class<T> type)
    {
        return require(type, singleton());
    }

    /**
     * Convenience method to look up the given object and fail if it isn't found
     *
     * @param type The object to find
     * @param instance Which instance to find
     * @return The object
     */
    default <T> T require(Class<T> type, Enum<?> instance)
    {
        return require(type, instanceIdentifier(instance));
    }

    /**
     * Convenience method to look up the given object and fail if it isn't found
     *
     * @param type The object to find
     * @param instance Which instance to find
     * @return The object
     */
    default <T> T require(Class<T> type, InstanceIdentifier instance)
    {
        return registry().require(type, instance);
    }

    /**
     * Unregisters the given object
     *
     * @param object The object to unregister
     */
    default void unregister(Object object)
    {
        unregister(object, singleton());
    }

    /**
     * Unregisters the given instance of the given object
     *
     * @param object The object to unregister
     * @param instance Which instance to unregister
     */
    default void unregister(Object object, InstanceIdentifier instance)
    {
        registry().unregister(object, instance);
    }

    /**
     * Unregisters the given instance of the given object
     *
     * @param object The object to unregister
     * @param instance Which instance to unregister
     */
    default void unregister(Object object, Enum<?> instance)
    {
        unregister(object, instanceIdentifier(instance));
    }

    /**
     * Remove all entries from this registry
     */
    default void clearRegistry()
    {
        registry().clearRegistry();
    }
}
