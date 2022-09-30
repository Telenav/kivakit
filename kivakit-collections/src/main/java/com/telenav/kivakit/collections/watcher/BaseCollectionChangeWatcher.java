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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.collections.internal.lexakai.DiagramWatcher;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * A base implementation of {@link CollectionChangeWatcher}. Change listeners can be added and removed with
 * {@link #addListener(CollectionChangeListener)} and {@link #removeListener(CollectionChangeListener)}. A thread can
 * then wait for a change with {@link #waitForChange()}. Subclasses receive notification of changes with
 * {@link #onAdded(Object)}, {@link #onModified(Object)} and {@link #onRemoved(Object)}.
 *
 * @author jonathanl (shibo)
 * @see CollectionChangeWatcher
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramWatcher.class)
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public abstract class BaseCollectionChangeWatcher<T> extends BaseRepeater implements CollectionChangeWatcher<T>
{
    /** The list of listeners to notify if this collection changes */
    private final ObjectList<CollectionChangeListener<T>> listeners = new ObjectList<>();

    /** True if this collection has changed */
    private boolean changed;

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener(CollectionChangeListener<T> listener)
    {
        listeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListener(CollectionChangeListener<T> listener)
    {
        listeners.remove(listener);
    }

    /**
     * Waits for a change to occur
     */
    public synchronized void waitForChange()
    {
        trace("Waiting for a change");
        while (!changed)
        {
            try
            {
                wait();
            }
            catch (InterruptedException ignored)
            {
            }
        }

        trace("Waking up because something changed");
        changed = false;
    }

    /**
     * Called when a change has occurred to the watched collection
     */
    protected synchronized void changed()
    {
        trace("Notifying listeners of a change");
        changed = true;
        notifyAll();
    }

    protected void onAdded(T value)
    {
        trace("An element was added to the collection");
        for (var listener : listeners)
        {
            listener.onAdded(value);
        }
        changed();
    }

    protected void onModified(T value)
    {
        trace("The collection was modified");
        for (var listener : listeners)
        {
            listener.onModified(value);
        }
        changed();
    }

    protected void onRemoved(T value)
    {
        trace("An element was removed from the collection");
        for (var listener : listeners)
        {
            listener.onRemoved(value);
        }
        changed();
    }
}
