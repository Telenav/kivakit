package com.telenav.kivakit.interfaces.collection;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static java.lang.Math.max;

/**
 * An object that can store a limited number of values.
 *
 * <ul>
 *     <li>{@link #hasRoomFor(int)}</li>
 *     <li>{@link #onOutOfRoom(int)}</li>
 *     <li>{@link #roomLeft()}</li>
 *     <li>{@link #totalRoom()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@TypeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED,
             reviews = 1,
             reviewers = "shibo")
public interface SpaceLimited extends Sized
{
    /**
     * @param values The number of values desired to add
     * @return True if the given number of values can be added
     */
    default boolean hasRoomFor(int values)
    {
        if (values <= roomLeft())
        {
            return true;
        }
        else
        {
            onOutOfRoom(values);
            return false;
        }
    }

    /**
     * Called when some number of values cannot be added to this store
     *
     * @param values The number of values that could not be added
     */
    default void onOutOfRoom(int values)
    {
        System.out.println("Ignoring operation: Adding " + values + " would exceed maximum size of " + totalRoom());
    }

    /**
     * Returns the amount of room remaining
     */
    default int roomLeft()
    {
        return max(0, totalRoom() - size());
    }

    /**
     * Returns the number of values that can be held in this store
     *
     * @return The amount of room in this store
     */
    default int totalRoom()
    {
        return Integer.MAX_VALUE;
    }
}
