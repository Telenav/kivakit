package com.telenav.kivakit.configuration.settings.stores.memory;

import com.telenav.kivakit.configuration.settings.BaseSettingsStore;
import com.telenav.kivakit.configuration.settings.SettingsObject;
import com.telenav.kivakit.configuration.settings.SettingsStore;

import java.util.Set;

import static com.telenav.kivakit.configuration.settings.SettingsStore.AccessMode.ADD;
import static com.telenav.kivakit.configuration.settings.SettingsStore.AccessMode.CLEAR;
import static com.telenav.kivakit.configuration.settings.SettingsStore.AccessMode.REMOVE;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * A {@link SettingsStore} that stores {@link SettingsObject}s in memory
 *
 * @author jonathanl (shibo)
 * @see SettingsStore
 * @see SettingsObject
 */
public class MemorySettingsStore extends BaseSettingsStore
{
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<AccessMode> accessModes()
    {
        return Set.of(ADD, REMOVE, CLEAR);
    }

    @Override
    protected Set<SettingsObject> onLoad()
    {
        return unsupported();
    }

    @Override
    protected boolean onSave(SettingsObject object)
    {
        return unsupported();
    }
}
