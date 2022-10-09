package com.telenav.kivakit.core.project;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.collections.set.ConcurrentHashSet;

import java.util.Set;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * A set of options relevant to application startup
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public class StartUpOptions
{
    /** The options */
    private static final Set<StartupOption> options = new ConcurrentHashSet<>();

    /** Disables the given option */
    public static void disable(StartupOption option)
    {
        options.remove(option);
    }

    /** Enables the given option */
    public static void enable(StartupOption option)
    {
        disable(option);
        options.add(option);
    }

    /**
     * Returns true if the given option is enabled
     */
    public static boolean isEnabled(StartupOption option)
    {
        return options.contains(option);
    }

    public enum StartupOption
    {
        /** True to start-up without showing application details */
        QUIET
    }
}
