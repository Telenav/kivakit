package com.telenav.kivakit.settings.stores;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.settings.BaseSettingsStore;
import com.telenav.kivakit.settings.SettingsObject;
import com.telenav.kivakit.settings.SettingsStore;

import java.util.Set;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
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
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
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
    protected Set<SettingsObject> onLoad()
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
