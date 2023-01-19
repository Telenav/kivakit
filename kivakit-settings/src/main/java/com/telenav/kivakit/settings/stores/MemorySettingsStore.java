package com.telenav.kivakit.settings.stores;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.settings.BaseSettingsStore;
import com.telenav.kivakit.settings.SettingsObject;
import com.telenav.kivakit.settings.SettingsStore;

import java.util.Set;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.collections.set.ObjectSet.set;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.settings.SettingsStore.AccessMode.DELETE;
import static com.telenav.kivakit.settings.SettingsStore.AccessMode.INDEX;
import static com.telenav.kivakit.settings.SettingsStore.AccessMode.UNLOAD;

/**
 * A {@link SettingsStore} that stores {@link SettingsObject}s in memory
 *
 * @author jonathanl (shibo)
 * @see SettingsStore
 * @see SettingsObject
 */
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class MemorySettingsStore extends BaseSettingsStore
{
    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectSet<AccessMode> accessModes()
    {
        return set(INDEX, DELETE, UNLOAD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean onDelete(SettingsObject object)
    {
        return unsupported();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ObjectSet<SettingsObject> onLoad()
    {
        return unsupported();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean onSave(SettingsObject object)
    {
        return unsupported();
    }
}
