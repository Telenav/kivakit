package com.telenav.kivakit.settings.settings.stores;

import com.telenav.kivakit.settings.settings.BaseSettingsStore;
import com.telenav.kivakit.settings.settings.SettingsObject;
import com.telenav.kivakit.settings.settings.SettingsStore;

import java.util.Set;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.settings.settings.SettingsStore.AccessMode.DELETE;
import static com.telenav.kivakit.settings.settings.SettingsStore.AccessMode.INDEX;
import static com.telenav.kivakit.settings.settings.SettingsStore.AccessMode.UNLOAD;

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
        return Set.of(INDEX, DELETE, UNLOAD);
    }

    @Override
    protected boolean onDelete(SettingsObject object)
    {
        return unsupported();
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
