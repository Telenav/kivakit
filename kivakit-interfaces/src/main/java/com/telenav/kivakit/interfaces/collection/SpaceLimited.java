package com.telenav.kivakit.interfaces.collection;

import com.telenav.kivakit.annotations.code.ApiQuality;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

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
@ApiQuality(stability = STABLE,
            testing = TESTING_NOT_NEEDED,
            documentation = FULLY_DOCUMENTED,
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
        throw new IllegalStateException("Ignoring operation: Adding " + values + " would exceed maximum size of " + totalRoom());
    }

    /**
     * @return The amount of room remaining
     */
    default int roomLeft()
    {
        return Math.max(0, totalRoom() - size());
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
