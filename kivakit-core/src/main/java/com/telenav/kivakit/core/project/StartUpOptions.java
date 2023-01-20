package com.telenav.kivakit.core.project;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.collections.set.ConcurrentHashSet;

import java.util.Set;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * A set of options relevant to application startup
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public class StartUpOptions
{
    /** Enabled startup options */
    private static final Set<StartupOption> enabled = new ConcurrentHashSet<>();

    /** Disabled startup options */
    private static final Set<StartupOption> disabled = new ConcurrentHashSet<>();

    /** Disables the given option */
    public static void disableStartupOption(StartupOption option)
    {
        enabled.remove(option);
        disabled.add(option);
    }

    /** Enables the given option */
    public static void enableStartupOption(StartupOption option)
    {
        disabled.remove(option);
        enabled.add(option);
    }

    /**
     * Returns true if the given option is enabled
     */
    public static boolean isStartupOptionEnabled(StartupOption option)
    {
        return enabled.contains(option) && !disabled.contains(option);
    }

    public enum StartupOption
    {
        /** True to start-up without showing application details */
        QUIET
    }
}
