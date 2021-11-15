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
 * {@link SettingsObject}s can be loaded from a settings store with {@link #load()}. If the method {@link #isReadOnly()}
 * returns false, then {@link #save(Set)} can be used to save {@link SettingsObject}s.
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
public interface SettingsStore extends Repeater, Named
{
    /**
     * @return True if this settings store can only be read from
     */
    boolean isReadOnly();

    /**
     * @return A set of {@link SettingsObject} instances
     */
    Set<SettingsObject> load();

    /**
     * Saves the given settings objects to this store
     *
     * @param objects The objects to save
     */
    void save(Set<SettingsObject> objects);
}
