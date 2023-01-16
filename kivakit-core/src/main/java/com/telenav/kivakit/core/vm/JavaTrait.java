package com.telenav.kivakit.core.vm;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.language.primitive.Booleans.isTrue;

/**
 * Trait providing access to the Java virtual machine, system properties and environment variables
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public interface JavaTrait
{
    /**
     * Returns true if the given property is true
     */
    default boolean isSystemPropertyOrEnvironmentVariableTrue(String key)
    {
        return isTrue(systemPropertyOrEnvironmentVariable(key));
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
