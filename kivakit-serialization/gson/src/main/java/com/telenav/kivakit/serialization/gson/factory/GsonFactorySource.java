package com.telenav.kivakit.serialization.gson.factory;

import com.telenav.kivakit.annotations.code.ApiQuality;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Interface to an object that provides its own {@link GsonFactory}
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public interface GsonFactorySource
{
    /**
     * Returns the {@link GsonFactory} for this object
     */
    GsonFactory gsonFactory();
}
