package com.telenav.kivakit.settings;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.registry.RegistryTrait;
import com.telenav.kivakit.interfaces.naming.Named;

import java.util.Set;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

/**
 * <b>Service Provider API</b>
 *
 * <p>
 * A store of {@link SettingsObject}s.
 * </p>
 *
 * <p>
 * <i>NOTE: This store object should not be accessed directly by user code. Instead, use
 * {@link SettingsRegistryTrait}.</i>
 * </p>
 *
 * <p><b>Loading and Saving</b></p>
 *
 * <p>
 * If the method {@link #supports(AccessMode)} returns true for {@link AccessMode#LOAD}, then settings can be loaded
 * with {@link #load()}. If the the store supports {@link AccessMode#SAVE}, then {@link #save(SettingsObject)} can be
 * used to save a settings object.
 * </p>
 *
 * <p><b>Hash/Equals Contract</b></p>
 *
 * <p>
 * Note that it is not necessary for settings {@link Object}s to implement the {@link Object#hashCode()} /
 * {@link Object#equals(Object)} contract. The {@link SettingsObject.SettingsObjectIdentifier} class implements this contract,
 * allowing {@link SettingsObject}s to be accessed in maps and stored in sets.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "UnusedReturnValue", "unused" })
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public interface SettingsStore extends
        RegistryTrait,
        Repeater,
        Named,
        Iterable<Object>
{
    /**
     * <p>
     * <b>Service Provider API</b>
     * </p>
     *
     * <p>
     * Settings store access capabilities
     * </p>
     */
    enum AccessMode
    {
        /** Can add objects to in-memory index */
        INDEX,

        /** Can remove objects from the in-memory index */
        DELETE,

        /** Can clear loaded and added objects from in-memory index */
        UNLOAD,

        /** Can load objects from persistent storage */
        LOAD,

        /** Can save objects to persistent storage */
        SAVE
    }

    /**
     * <b>Service Provider API</b>
     * <p>
     * Returns the access modes this store supports
     * </p>
     *
     * @return The access modes this store supports
     */
    Set<AccessMode> accessModes();

    /**
     * <b>Service Provider API</b>
     *
     * <p>
     * Deletes the given settings object from this store
     * </p>
     *
     * @return True if the given setting object was removed from the in-memory index of this store
     */
    boolean delete(SettingsObject object);

    /**
     * <b>Service Provider API</b>
     * <p>
     * Adds the given object to the in-memory index of this settings store. This will <i>not</i>> add the object to the
     * underlying persistent store. To do that, call {@link #save(SettingsObject)}.
     */
    boolean index(SettingsObject object);

    /**
     * <b>Service Provider API</b>
     * <p>
     * Adds all {@link SettingsObject}s to this store's in-memory index via {@link #index(SettingsObject)}.
     * </p>
     */
    default boolean indexAll(SettingsStore store)
    {
        store.indexed().forEach(this::index);
        return true;
    }

    /**
     * <b>Service Provider API</b>
     * <p>
     * Returns all indexed objects
     * </p>
     *
     * @return All objects in this store's in-memory index. If the store supports loading, and it has not yet been
     * loaded, {@link #load()} will be called first.
     */
    ObjectSet<SettingsObject> indexed();

    /**
     * <b>Service Provider API</b>
     *
     * <p>
     * Loads this settings store with {@link SettingsObject}s
     * </p>
     *
     * @return The set of {@link SettingsObject} instances in this store
     */
    Set<SettingsObject> load();

    /**
     * A settings store to propagate changes to
     *
     * @param store The store that should be updated when this store changes
     */
    void propagateChangesTo(SettingsStore store);

    /**
     * <b>Service Provider API</b>
     * <p>
     * Adds all {@link SettingsObject}s to the global registry.
     */
    default boolean registerAllIn(SettingsStore store)
    {
        store.indexed().forEach(at -> register(at.object(), at.identifier().instance()));
        return true;
    }

    /**
     * <b>Service Provider API</b>
     * <p>
     * Saves the given settings object to this store, and adds it to the in-memory index
     *
     * @param object The objects to save
     */
    boolean save(SettingsObject object);

    /**
     * Saves the objects from the given settings store into this one
     *
     * @param that The settings store to retrieve objects from
     */
    default void saveAllFrom(SettingsStore that)
    {
        that.indexed().forEach(this::save);
    }

    /**
     * <b>Service Provider API</b>
     * Returns true if this store supports the given access mode
     *
     * @return True if this store supports the given type of access
     */
    default boolean supports(AccessMode accessMode)
    {
        return accessModes().contains(accessMode);
    }

    /**
     * <b>Service Provider API</b>
     * <p>
     * Removes the given object from the in-memory index of this settings store. This will <i>not</i>> add the object to
     * the underlying persistent store. To do that, call {@link #save(SettingsObject)}.
     */
    @SuppressWarnings("SpellCheckingInspection")
    boolean unindex(SettingsObject object);

    /**
     * <b>Service Provider API</b>
     * <p>
     * Unloads all data in this store
     * </p>
     *
     * @return True if the in-memory index of this store was cleared
     */
    boolean unload();
}
