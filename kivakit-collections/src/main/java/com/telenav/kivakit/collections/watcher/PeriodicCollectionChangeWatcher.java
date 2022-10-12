////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.collections.watcher;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.collections.internal.lexakai.DiagramWatcher;
import com.telenav.kivakit.core.thread.KivaKitThread;
import com.telenav.kivakit.core.time.Frequency;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.interfaces.lifecycle.Startable;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.thread.KivaKitThread.repeat;

/**
 * A {@link CollectionChangeWatcher} that checks for changes at a given {@link Frequency}. After being started with
 * {@link #start()} the object returned by {@link #objects()} is monitored for changes at the given frequency. If there
 * are changes, the change watchers are notified via {@link #onAdded(Object)}, {@link #onModified(Object)} and
 * {@link #onRemoved(Object)}. Modification of objects is determined by the timestamp returned by
 * {@link #lastModified(Object)}.
 *
 * <p><b>Lifecycle</b></p>
 *
 * <ul>
 *     <li>{@link #start()}</li>
 *     <li>{@link #isRunning()}</li>
 * </ul>
 *
 * <p><b>Collection Access</b></p>
 *
 * <ul>
 *     <li>{@link #objects()} - Returns a collection of objects to watch</li>
 *     <li>{@link #lastModified(Object)} - Determines the last time an object in the collection was modified</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramWatcher.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public abstract class PeriodicCollectionChangeWatcher<T> extends BaseCollectionChangeWatcher<T> implements Startable
{
    /** The collection of objects to watch */
    private Collection<T> objects;

    /** The last time each object was modified */
    private final Map<T, Time> previousLastModified = new ConcurrentHashMap<>();

    /** The frequency to check for changes */
    private final Frequency frequency;

    /** The background thread doing the watching */
    private KivaKitThread watcher;

    protected PeriodicCollectionChangeWatcher(Frequency frequency)
    {
        this.frequency = frequency;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRunning()
    {
        return watcher.isRunning();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean start()
    {
        if (watcher == null || !watcher.isRunning())
        {
            objects = objects();
            for (var object : objects)
            {
                previousLastModified.put(object, lastModified(object));
            }
            this.watcher = repeat(this, "CollectionWatcher", frequency, () ->
            {
                try
                {
                    compare();
                }
                catch (Exception ignored)
                {
                }
            });
        }
        return true;
    }

    /**
     * Sets the last time the given object was modified
     */
    protected abstract Time lastModified(T object);

    /**
     * Returns the collection to watch
     */
    protected abstract Collection<T> objects();

    private void compare()
    {
        // Get new set of objects
        var newObjects = objects();

        // For each old object,
        for (var object : objects)
        {
            // if the new objects don't contain it,
            if (!newObjects.contains(object))
            {
                // it has been removed
                onRemoved(object);
                previousLastModified.remove(object);
            }
        }

        // For each new object,
        for (var object : newObjects)
        {
            // if it's not in the current set
            if (!objects.contains(object))
            {
                // it was just added
                previousLastModified.put(object, lastModified(object));
                onAdded(object);
            }
            else
            {
                // If it wasn't just added and the last modified time is after the current last
                // modified time
                var lastModified = lastModified(object);
                var previousLastModified = this.previousLastModified.get(object);
                if (previousLastModified != null && lastModified.isAfter(previousLastModified))
                {
                    // it was modified
                    this.previousLastModified.put(object, lastModified);
                    onModified(object);
                }
            }
        }

        // Update current objects
        objects = newObjects;
    }
}
