package com.telenav.kivakit.core.vm;

import com.telenav.kivakit.core.language.primitive.Booleans;

public class SystemProperties
{
    public static boolean isPropertyFalse(String key)
    {
        return Booleans.isFalse(property(key));
    }

    public static boolean isPropertyTrue(String key)
    {
        return Booleans.isTrue(property(key));
    }

    public static String property(String key, String defaultValue)
    {
        var value = property(key);
        return value == null ? defaultValue : value;
    }

    public static String property(String key)
    {
        var value = System.getProperty(key);
        if (value == null)
        {
            value = System.getenv(key);
        }
        return value;
    }
}
