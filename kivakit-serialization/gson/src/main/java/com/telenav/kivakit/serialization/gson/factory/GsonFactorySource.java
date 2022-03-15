package com.telenav.kivakit.serialization.gson.factory;

/**
 * Interface to an object that provides its own {@link GsonFactory}
 *
 * @author jonathanl (shibo)
 */
public interface GsonFactorySource
{
    /**
     * Returns the {@link GsonFactory} for this object
     */
    GsonFactory gsonFactory();
}
