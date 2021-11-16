package com.telenav.kivakit.configuration.settings;

import com.telenav.kivakit.kernel.interfaces.naming.Named;
import com.telenav.kivakit.kernel.messaging.Repeater;

import java.util.Set;

/**
 * <b>Service Provider API</b>
 *
 * <p>
 * A store of {@link SettingsObject}s.
 * </p>
 *
 * <p><b>Loading and Saving</b></p>
 *
 * <p>
 * {@link SettingsObject}s can be loaded from a settings store with {@link #load()}. If the method {@link #access()}
 * returns {@link Access#SAVE}, then {@link #save(SettingsObject)} can be used to save a settings object.
 * </p>
 *
 * <p><b>Hash/Equals Contract</b></p>
 *
 * </p>
 * Note that it is not necessary for settings {@link Object}s to implement the {@link #hashCode()} / {@link
 * #equals(Object)} contract. The {@link SettingsObject.Identifier} class implements this contract, allowing {@link
 * SettingsObject}s to be accessed in maps and stored in sets.
 * </p>
 *
 * @author jonathanl (shibo)
 */
public interface SettingsStore extends Repeater, Named, Iterable<Object>
{
    enum Access
    {
        /** Can add objects to in-memory index */
        ADD,

        /** Can clear loaded and added objects from in-memory index */
        CLEAR,

        /** Can load objects from persistent storage */
        LOAD,

        /** Can save objects to persistent storage */
        SAVE
    }

    /**
     * @return The types of access this store supports
     */
    Set<Access> access();

    /**
     * Add the given object to the in-memory index of this settings store. This will not add the object to the
     * underlying persistent store. To do that, call {@link #save(SettingsObject)}.
     */
    boolean add(SettingsObject object);

    /**
     * Adds all {@link SettingsObject}s in the given store
     */
    default boolean addAll(SettingsStore store)
    {
        store.all().forEach(this::add);
        return true;
    }

    /**
     * @return All objects in this store's index
     */
    Set<SettingsObject> all();

    /**
     * @return True if this settings store was cleared
     */
    boolean clear();

    /**
     * @return True if this store is empty
     */
    default boolean isEmpty()
    {
        return all().isEmpty();
    }

    /**
     * @return A set of {@link SettingsObject} instances
     */
    Set<SettingsObject> load();

    /**
     * Saves the given settings object to this store
     *
     * @param object The objects to save
     */
    boolean save(SettingsObject object);

    /**
     * @return True if this store supports the given type of access
     */
    default boolean supports(Access access)
    {
        return access().contains(access);
    }
}
