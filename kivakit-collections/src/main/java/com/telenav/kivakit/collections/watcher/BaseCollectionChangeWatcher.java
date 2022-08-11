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

import com.telenav.kivakit.collections.internal.lexakai.DiagramWatcher;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.List;

/**
 * A base implementation of {@link CollectionChangeWatcher}. Change listeners can be added and removed with {@link
 * #addListener(CollectionChangeListener)} and {@link #removeListener(CollectionChangeListener)}. A thread can then wait
 * for a change with {@link #waitForChange()}. Subclasses receive notification of changes with {@link #onAdded(Object)},
 * {@link #onModified(Object)} and {@link #onRemoved(Object)}.
 *
 * @author jonathanl (shibo)
 * @see CollectionChangeWatcher
 */
@UmlClassDiagram(diagram = DiagramWatcher.class)
public abstract class BaseCollectionChangeWatcher<T> extends BaseRepeater implements CollectionChangeWatcher<T>
{
    private final List<CollectionChangeListener<T>> listeners = new ArrayList<>();

    private boolean changed;

    @Override
    public void addListener(CollectionChangeListener<T> listener)
    {
        listeners.add(listener);
    }

    @Override
    public void removeListener(CollectionChangeListener<T> listener)
    {
        listeners.remove(listener);
    }

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
