package com.telenav.kivakit.core.vm;

import com.telenav.kivakit.core.language.primitive.Booleans;
import com.telenav.kivakit.core.os.Console;
import com.telenav.kivakit.core.string.Formatter;

@SuppressWarnings("unused")
public interface JavaTrait
{
    default String format(String message, Object... arguments)
    {
        return Formatter.format(message, arguments);
    }

    default boolean isSystemPropertyTrue(String key)
    {
        return Booleans.isTrue(systemProperty(key));
    }

    default JavaVirtualMachine javaVirtualMachine()
    {
        return JavaVirtualMachine.local();
    }

    default void println(String message, Object... arguments)
    {
        Console.println(message, arguments);
    }

    default String systemProperty(String key)
    {
        return Properties.property(key);
    }

    default String systemProperty(String key, String defaultValue)
    {
        return Properties.property(key, defaultValue);
    }
}
