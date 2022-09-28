package com.telenav.kivakit.core.vm;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.language.primitive.Booleans;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_DEFAULT_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Trait providing access to the Java virtual machine, system properties and environment variables
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@ApiQuality(stability = STABLE_DEFAULT_EXPANDABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public interface JavaTrait
{
    /**
     * Returns true if the given property is true
     */
    default boolean isSystemPropertyOrEnvironmentVariableTrue(String key)
    {
        return Booleans.isTrue(systemPropertyOrEnvironmentVariable(key));
    }

    /**
     * Returns the Java virtual machine
     */
    default JavaVirtualMachine javaVirtualMachine()
    {
        return JavaVirtualMachine.javaVirtualMachine();
    }

    /**
     * Returns the given property
     */
    default String systemPropertyOrEnvironmentVariable(String key)
    {
        return Properties.systemPropertyOrEnvironmentVariable(key);
    }

    /**
     * Returns the given property or a default value
     */
    default String systemPropertyOrEnvironmentVariable(String key, String defaultValue)
    {
        return Properties.systemPropertyOrEnvironmentVariable(key, defaultValue);
    }
}
