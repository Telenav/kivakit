package com.telenav.kivakit.interfaces.code;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;

import java.util.IdentityHashMap;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_UNDETERMINED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A quick hack to debug code to see how often it is being executed. The {@link #tripwireTripEvery(int)} method can be
 * called with the number of times the method itself should be called before the {@link #tripwireTripped(int)} method is
 * called.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABILITY_UNDETERMINED,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE,
             reviews = 1,
             reviewers = "shibo")
public interface TripwireTrait
{
    /** Map from this trait's implementing class to the current method invocation count */
    Map<Class<?>, Integer> classToCount = new IdentityHashMap<>();

    /**
     * Map from this trait's implementing class to the number of times {@link #tripwireTripEvery(int)} must be called
     * before {@link #tripwireTripped(int)} is called
     */
    Map<Class<?>, Integer> classToTimes = new IdentityHashMap<>();

    default void tripwireReset()
    {
        classToCount.put(getClass(), 0);
    }

    /**
     * The first time this method is called, sets an invocation threshold to set off the tripwire. For each call to this
     * method (including the first call), the invocation count is increased. When the threshold is reached, the tripwire
     * is {@link #tripwireTripped(int)}.
     *
     * @param every The invocation threshold for tripping this tripwire
     * @return True if the tripwire was tripped
     */
    default boolean tripwireTripEvery(int every)
    {
        int times = classToTimes.computeIfAbsent(getClass(), ignored -> every);
        int count = classToCount.computeIfAbsent(getClass(), ignored -> 0);
        try
        {
            if (count > 0 && count % times == 0)
            {
                tripwireTripped(count);
                return true;
            }
            return false;
        }
        finally
        {
            classToCount.put(getClass(), count + 1);
        }
    }

    /**
     * Called when this tripwire is tripped to print the current invocation count to the console.
     */
    default void tripwireTripped(int count)
    {
        System.out.println(getClass().getSimpleName() + " tripwire tripped: " + count);
    }
}
