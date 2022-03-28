package com.telenav.kivakit.settings;

import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.registry.Registry;
import com.telenav.kivakit.core.registry.RegistryTrait;
import com.telenav.kivakit.core.thread.locks.ReadWriteLock;
import com.telenav.kivakit.core.vm.JavaTrait;
import com.telenav.kivakit.settings.stores.MemorySettingsStore;
import com.telenav.kivakit.settings.stores.ResourceFolderSettingsStore;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static com.telenav.kivakit.core.collections.set.ObjectSet.objectSet;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.settings.SettingsStore.AccessMode.DELETE;
import static com.telenav.kivakit.settings.SettingsStore.AccessMode.INDEX;
import static com.telenav.kivakit.settings.SettingsStore.AccessMode.LOAD;
import static com.telenav.kivakit.settings.SettingsStore.AccessMode.SAVE;
import static com.telenav.kivakit.settings.SettingsStore.AccessMode.UNLOAD;

/**
 * <b>Service Provider API</b>
 *
 * <p>
 * Base class for {@link SettingsStore} provider implementations.
 * </p>
 *
 * <p><b>Provided Stores</b></p>
 *
 * <ul>
 *     <li>{@link Deployment} - Loads settings for a particular application or server deployment</li>
 *     <li>{@link ResourceFolderSettingsStore} - Loads settings from <i>.properties</i> files</li>
 *     <li>{@link MemorySettingsStore} - A store of non-persistent settings objects in memory</li>
 * </ul>
 *
 * <p>
 * Deployments are the most commonly used settings store and typically they should be all that is required by most
 * applications. See {@link Deployment} for detailed usage examples.
 * </p>
 *
 * <p><b>Providers</b></p>
 *
 * <p>
 * {@link SettingsStore} providers inherit several useful methods and implementations from this base class:
 * <ul>
 *     <li>{@link #index(SettingsObject)} - Adds the given object to the store's in-memory index (but not to any persistent storage)</li>
 *     <li>{@link #indexed()} - The set of objects in this store. If the store is loadable, {@link #load()} is called before returning the set</li>
 *     <li>{@link #unload()} - Clears this store's in-memory index</li>
 *     <li>{@link #iterator()} - Iterates through each settings {@link Object} in this store</li>
 *     <li>{@link #load()} - Lazy-loads objects from persistent storage by calling {@link #onLoad()} and then adds them to the in-memory index</li>
 *     <li>{@link #lookup(SettingsObject.Identifier)} - Looks up the object for the given identifier in the store's index</li>
 *     <li>{@link #save(SettingsObject)} - Saves the given settings object to persistent storage by calling {@link #onSave(SettingsObject)}</li>
 * </ul>
 *
 * <p>
 * Providers must override these methods (although some may be unsupported methods):
 * </p>
 *
 * <ul>
 *     <li>{@link #accessModes()} - Specifies the kinds of access that the store supports</li>
 *     <li>{@link #onLoad()} - Loads settings objects from persistent storage</li>
 *     <li>{@link #onSave(SettingsObject)} - Saves the settings object to persistent storage</li>
 *     <li>{@link #onDelete(SettingsObject)} = Deleted the given settings object from persistent storage</li>
 *     <li>{@link #onUnload()} - Unloads all settings from this store (but does not remove them from persistent storage)</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see SettingsStore
 * @see Deployment
 * @see ResourceFolderSettingsStore
 */
public abstract class BaseSettingsStore extends BaseRepeater implements
        SettingsStore,
        RegistryTrait,
        JavaTrait
{
    /** True if settings have been loaded into this store */
    private boolean loaded;

    /** Lock for accessing settings entries */
    private final ReadWriteLock lock = new ReadWriteLock();

    /** Map to get settings entries by identifier */
    private final Map<SettingsObject.Identifier, SettingsObject> objects = new HashMap<>();

    /** Store to propagate changes to */
    private SettingsStore propagateChangesTo;

    /** True while this store is reloading */
    private boolean reloading;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(SettingsObject object)
    {
        ensure(supports(DELETE));

        lock.write(() ->
        {
            onDelete(object);
            unindex(object);
        });
        return true;
    }

    /**
     * Adds the given settings object to the in-memory index for this store. The object (from {@link
     * SettingsObject#object()}) is indexed under its class and all implemented interfaces. It is also indexed under all
     * superclasses and superinterfaces.
     */
    @Override
    @UmlExcludeMember
    public boolean index(SettingsObject settings)
    {
        ensure(supports(INDEX));
        ensureNotNull(settings);

        // Lock for writing,
        return lock.write(() ->
        {
            // then walk up the class hierarchy of the object,
            var instance = settings.identifier().instance();
            for (var at = (Class<?>) settings.object().getClass(); !at.equals(Object.class); at = at.getSuperclass())
            {
                // add the interfaces of the object,
                for (var in : at.getInterfaces())
                {
                    internalPut(new SettingsObject(settings.object(), in, instance));
                }

                // and the class itself.
                internalPut(new SettingsObject(settings.object(), at, instance));
            }
            return true;
        });
    }

    /**
     * Gets a <b>copy</b> of the {@link SettingsObject}s indexed in this store, loading them if need be
     */
    @Override
    public ObjectSet<SettingsObject> indexed()
    {
        maybeLoad();

        return lock.write(() -> objectSet(objects.values()));
    }

    @NotNull
    @Override
    public Iterator<Object> iterator()
    {
        maybeLoad();
        return indexed()
                .stream()
                .map(SettingsObject::object)
                .iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    public final synchronized Set<SettingsObject> load()
    {
        ensure(supports(LOAD));

        // If we are the first thread to call load(),
        if (!loaded)
        {
            // load settings by calling the subclass.
            trace("Loading settings from: $", name());
            lock.write(() -> onLoad().forEach(this::index));
            loaded = true;
        }

        return lock.read(() -> new HashSet<>(objects.values()));
    }

    /**
     * Looks up the settings object for the given identifier. First looks in the global object {@link Registry}, then
     * looks at the objects in this store's index.
     *
     * @return Any settings object for the given identifier
     */
    @SuppressWarnings("unchecked")
    public <T> T lookup(SettingsObject.Identifier identifier)
    {
        maybeLoad();

        return lock.read(() ->
        {
            // First try the global object registry,
            T object = (T) lookup(identifier.type(), identifier.instance());
            if (object == null)
            {
                // and try the index.
                var settings = objects.get(identifier);
                if (settings != null)
                {
                    object = settings.object();
                }
            }
            return object;
        });
    }

    public SettingsStore propagateChangesTo()
    {
        return propagateChangesTo;
    }

    @Override
    public void propagateChangesTo(SettingsStore store)
    {
        propagateChangesTo = store;
    }

    /**
     * <p><b>ServiceProvider API</b></p>
     * <p>
     * Forces this settings store to reload
     */
    public void reload()
    {
        if (!reloading)
        {
            reloading = true;
            unload();
            load();
            reloading = false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean save(SettingsObject object)
    {
        ensure(supports(SAVE));

        return lock.write(() ->
        {
            index(object);
            return onSave(object);
        });
    }

    @Override
    public String toString()
    {
        return name();
    }

    /**
     * Adds the given settings object to the in-memory index for this store. The object (from {@link
     * SettingsObject#object()}) is indexed under its class and all implemented interfaces. It is also indexed under all
     * superclasses and superinterfaces.
     */
    @Override
    @UmlExcludeMember
    public boolean unindex(SettingsObject settings)
    {
        ensure(supports(INDEX));
        ensureNotNull(settings);

        // Obtain a write-lock,
        return lock.write(() ->
        {
            // add the object to the global lookup registry
            unregister(settings.object(), settings.identifier().instance());

            // then walk up the class hierarchy of the object,
            var instance = settings.identifier().instance();
            for (var at = (Class<?>) settings.object().getClass(); !at.equals(Object.class); at = at.getSuperclass())
            {
                // add the interfaces of the object,
                for (var in : at.getInterfaces())
                {
                    internalRemove(new SettingsObject(settings.object(), in, instance));
                }

                // and the class itself.
                internalPut(new SettingsObject(settings.object(), at, instance));
            }
            return true;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean unload()
    {
        ensure(supports(UNLOAD));

        lock.write(() ->
        {
            objects.clear();
            onUnload();
            loaded = false;
        });

        return true;
    }

    /**
     * Called when a settings object is deleted
     *
     * @return True if the given object was removed from persistent storage
     */
    protected abstract boolean onDelete(SettingsObject object);

    /**
     * Called to load settings objects for this store, if this store is loadable
     *
     * @return All settings in this store
     */
    protected abstract Set<SettingsObject> onLoad();

    /**
     * Called to save a settings object, if this store supports saving
     *
     * @return True if the given object was saved to persistent storage
     */
    protected abstract boolean onSave(SettingsObject object);

    /**
     * Called when this store is unloaded
     */
    protected void onUnload()
    {
    }

    private void internalPut(SettingsObject settings)
    {
        lock.write(() -> objects.put(settings.identifier(), settings));
    }

    private void internalRemove(SettingsObject settings)
    {
        lock.write(() -> objects.remove(settings.identifier()));
    }

    private void maybeLoad()
    {
        // If we can load this store,
        if (supports(LOAD))
        {
            // load it.
            load();
        }
    }
}
