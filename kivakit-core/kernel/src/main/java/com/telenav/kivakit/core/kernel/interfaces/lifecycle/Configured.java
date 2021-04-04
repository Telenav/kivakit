package com.telenav.kivakit.core.kernel.interfaces.lifecycle;

/**
 * Interface to an object that is configured by a certain kind of configuration object.
 *
 * @author jonathanl (shibo)
 */
public interface Configured<T>
{
    /**
     * Configures this object with the given configuration
     */
    void configure(T configuration);
}
