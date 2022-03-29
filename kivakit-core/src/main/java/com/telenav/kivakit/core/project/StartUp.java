package com.telenav.kivakit.core.project;

import com.telenav.kivakit.core.collections.set.ConcurrentHashSet;

import java.util.Set;

@SuppressWarnings("unused")
public class StartUp
{
    private static final Set<Option> options = new ConcurrentHashSet<>();

    public static void disable(Option option)
    {
        options.remove(option);
    }

    public static void enable(Option option)
    {
        options.add(option);
    }

    public static boolean isEnabled(Option option)
    {
        return options.contains(option);
    }

    public enum Option
    {
        /** True to start-up without showing application details */
        QUIET
    }
}
