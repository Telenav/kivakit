package com.telenav.kivakit.interfaces.code;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Quick hack to debug code to see how often it is being executed. The {@link #tripEvery(int)} method can be called with
 * the number of times the method itself should be called before the {@link #tripped(int)} method is called.
 *
 * @author jonathanl (shibo)
 */
public interface TripwireTrait
{
    /** Map from this trait's implementing class to the current method invocation count */
    Map<Class<?>, Integer> classToCount = new IdentityHashMap<>();

    /**
     * Map from this trait's implementing class to the number of times {@link #tripEvery(int)} must be called before
     * {@link #tripped(int)} is called
     */
    Map<Class<?>, Integer> classToTimes = new IdentityHashMap<>();

    /**
     * The first time this method is called, sets an invocation threshold to set off the tripwire. For each call to this
     * method (including the first call), the invocation count is increased. When the threshold is reached, the tripwire
     * is {@link #tripped(int)}.
     *
     * @param every The invocation threshold for tripping this tripwire
     * @return True if the tripwire was tripped
     */
    default boolean tripEvery(int every)
    {
        var times = classToTimes.computeIfAbsent(getClass(), ignored -> every);
        var count = classToCount.computeIfAbsent(getClass(), ignored -> 0);
        try
        {
            if (count % times == 0)
            {
                tripped(count);
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
    default void tripped(int count)
    {
        System.out.println("tripped: " + count);
    }
}
