////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.watcher;

import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramWatcher;
import com.telenav.kivakit.core.kernel.interfaces.lifecycle.Startable;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.time.Frequency;
import com.telenav.kivakit.core.kernel.language.time.Time;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A {@link CollectionChangeWatcher} that checks for changes at a given {@link Frequency}. After being started with
 * {@link #start()} the object returned by {@link #objects()} is monitored for changes at the given frequency. If there
 * are changes, the change watchers are notified via {@link #onAdded(Object)}, {@link #onModified(Object)} and {@link
 * #onRemoved(Object)}. Modification of objects is determined by the timestamp returned by {@link
 * #lastModified(Object)}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramWatcher.class)
public abstract class PeriodicCollectionChangeWatcher<T> extends BaseCollectionChangeWatcher<T> implements Runnable, Startable
{
    private Collection<T> objects;

    private final Map<T, Time> previousLastModified = new ConcurrentHashMap<>();

    private final Frequency frequency;

    private boolean running;

    protected PeriodicCollectionChangeWatcher(final Frequency frequency)
    {
        this.frequency = frequency;
    }

    public void compare()
    {
        // Get new set of objects
        final var newObjects = objects();

        // For each old object,
        for (final var object : objects)
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
        for (final var object : newObjects)
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
                final var lastModified = lastModified(object);
                final var previousLastModified = this.previousLastModified.get(object);
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

    @Override
    public boolean isRunning()
    {
        return running;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                compare();
            }
            catch (final Exception ignored)
            {
            }
            frequency.duration().sleep();
        }
    }

    @Override
    public boolean start()
    {
        if (!running)
        {
            objects = objects();
            for (final var object : objects)
            {
                previousLastModified.put(object, lastModified(object));
            }
            new Thread(this).start();
            running = true;
        }
        return true;
    }

    /**
     * @return The last time the given object was modified
     */
    protected abstract Time lastModified(T object);

    /**
     * @return The collection to watch
     */
    protected abstract Collection<T> objects();
}
