package com.telenav.kivakit.core.registry;

import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.interfaces.factory.Factory;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.registry.InstanceIdentifier.SINGLETON;

/**
 * <p>
 * A stateless trait for accessing the {@link Registry} for the implementing component. Includes methods to look up,
 * register and require settings objects. When there is more than one settings object of a given type, an {@link
 * InstanceIdentifier} can be given to distinguish between instances. Convenience methods are provided to create
 * instance identifiers for {@link Enum}s and {@link String}s. The Component interface in kivakit-component uses this
 * trait to add easy access to registry methods to all components.
 * </p>
 *
 * <p><b>Lookup methods</b></p>
 * <ul>
 *     <li>{@link #lookup(Class)} - Locates the registered instance of the given class</li>
 *     <li>{@link #lookup(Class, String)} - Locates the specified registered instance of the given class</li>
 *     <li>{@link #lookup(Class, Enum)} - Locates the specified registered instance of the given class</li>
 * </ul>
 *
 * <p><b>Register methods</b></p>
 * <ul>
 *     <li>{@link #register(Object)} - Registers the given object</li>
 *     <li>{@link #register(Object, String)} - Registers the given identified object instance</li>
 *     <li>{@link #register(Object, Enum)} - Registers the given identified object instance</li>
 * </ul>
 *
 * <p><b>Require methods</b></p>
 * <ul>
 *     <li>{@link #require(Class)} - Locates the registered instance of the given class or fails</li>
 *     <li>{@link #require(Class, String)} - Locates the specified registered instance of the given class or fails</li>
 *     <li>{@link #require(Class, Enum)} - Locates the specified registered instance of the given class or fails</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see Registry
 */
public interface RegistryTrait
{
    /**
     * Convenience method
     */
    default <T> T lookup(Class<T> type)
    {
        return lookup(type, SINGLETON);
    }

    /**
     * Convenience method
     */
    default <T> T lookup(Class<T> type, Enum<?> instance)
    {
        return lookup(type, InstanceIdentifier.of(instance));
    }

    /**
     * Convenience method
     */
    default <T> T lookup(Class<T> type, String instance)
    {
        return lookup(type, InstanceIdentifier.of(instance));
    }

    /**
     * @return Any settings object of the given type and instance
     */
    default <T> T lookup(Class<T> type, InstanceIdentifier instance)
    {
        return registry().lookup(type, instance);
    }

    /**
     * Registers the given singleton object in the lookup
     */
    default <T> T register(T object)
    {
        for (var at = object.getClass(); at != Object.class; at = at.getSuperclass())
        {
            register(object, SINGLETON);
        }

        return object;
    }

    /**
     * Registers the specified instance of the given object's type in the lookup
     */
    default <T> T register(T object, String instance)
    {
        return register(object, InstanceIdentifier.of(instance));
    }

    /**
     * Registers the specified instance of the given object's type in the lookup
     */
    default <T> T register(T object, Enum<?> instance)
    {
        return register(object, InstanceIdentifier.of(instance));
    }

    /**
     * Registers the specified instance of the given object's type in the lookup
     */
    default <T> T register(T object, InstanceIdentifier instance)
    {
        return registry().register(object, instance);
    }

    /**
     * @return The lookup registry for this component
     */
    default Registry registry()
    {
        return Registry.of(this);
    }

    /**
     * If the given type cannot be found in the registry with {@link #lookup(Class)}, an instance is created using the
     * given factory and registered with {@link #register(Object)}. The value is then returned.
     *
     * @param type The type that's required
     * @param factory A factory for creating the type if it can't be foundf
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
     * Convenience method
     */
    default <T> T require(Class<T> type)
    {
        return require(type, SINGLETON);
    }

    /**
     * Convenience method
     */
    default <T> T require(Class<T> type, Enum<?> instance)
    {
        return require(type, InstanceIdentifier.of(instance));
    }

    /**
     * Convenience method
     */
    default <T> T require(Class<T> type, String instance)
    {
        return require(type, InstanceIdentifier.of(instance));
    }

    /**
     * @return The object of the given instance and type, or {@link Ensure#fail()} if there is no such object
     */
    default <T> T require(Class<T> type, InstanceIdentifier instance)
    {
        return registry().require(type, instance);
    }

    /**
     * Unregisters the given object
     */
    default void unregister(Object object)
    {
        unregister(object, SINGLETON);
    }

    /**
     * Unregisters the given instance of the given object
     */
    default void unregister(Object object, InstanceIdentifier instance)
    {
        registry().unregister(object, instance);
    }

    /**
     * Unregisters the given instance of the given object
     */
    default void unregister(Object object, Enum<?> instance)
    {
        unregister(object, InstanceIdentifier.of(instance));
    }

    /**
     * Unregisters the given instance of the given object
     */
    default void unregister(Object object, String instance)
    {
        unregister(object, InstanceIdentifier.of(instance));
    }

    /**
     * Remove all entries from this registry
     */
    default void unregisterAll()
    {
        registry().unregisterAll();
    }
}
