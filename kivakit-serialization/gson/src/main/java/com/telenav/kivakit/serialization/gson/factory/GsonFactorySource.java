package com.telenav.kivakit.serialization.gson.factory;

import com.telenav.kivakit.annotations.code.ApiQuality;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Interface to an object that provides its own {@link GsonFactory}
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = API_STABLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public interface GsonFactorySource
{
    /**
     * Returns the {@link GsonFactory} for this object
     */
    GsonFactory gsonFactory();
}
