package com.telenav.kivakit.configuration.lookup;

import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;

import static com.telenav.kivakit.configuration.lookup.InstanceIdentifier.SINGLETON;

/**
 * A stateless trait for accessing the {@link Registry} for a component
 *
 * @author jonathanl (shibo)
 */
public interface RegistryTrait
{
    /**
     * Convenience method
     */
    default <T> T lookup(final Class<T> type)
    {
        return lookup(type, SINGLETON);
    }

    /**
     * Convenience method
     */
    default <T> T lookup(final Class<T> type, final Enum<?> instance)
    {
        return lookup(type, InstanceIdentifier.of(instance));
    }

    /**
     * Convenience method
     */
    default <T> T lookup(final Class<T> type, final String instance)
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
    default <T> T register(final T object)
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
    default <T> T register(final T object, final String instance)
    {
        return register(object, InstanceIdentifier.of(instance));
    }

    /**
     * Registers the specified instance of the given object's type in the lookup
     */
    default <T> T register(final T object, final Enum<?> instance)
    {
        return register(object, InstanceIdentifier.of(instance));
    }

    /**
     * Registers the specified instance of the given object's type in the lookup
     */
    default <T> T register(final T object, final InstanceIdentifier instance)
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
     * Convenience method
     */
    default <T> T require(final Class<T> type)
    {
        return require(type, SINGLETON);
    }

    /**
     * Convenience method
     */
    default <T> T require(final Class<T> type, final Enum<?> instance)
    {
        return require(type, InstanceIdentifier.of(instance));
    }

    /**
     * Convenience method
     */
    default <T> T require(final Class<T> type, final String instance)
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
}
