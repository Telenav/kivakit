////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.watcher;

import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramWatcher;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.messaging.repeaters.BaseRepeater;

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
    public void addListener(final CollectionChangeListener<T> listener)
    {
        listeners.add(listener);
    }

    @Override
    public void removeListener(final CollectionChangeListener<T> listener)
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
            catch (final InterruptedException ignored)
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

    protected void onAdded(final T value)
    {
        trace("An element was added to the collection");
        for (final var listener : listeners)
        {
            listener.onAdded(value);
        }
        changed();
    }

    protected void onModified(final T value)
    {
        trace("The collection was modified");
        for (final var listener : listeners)
        {
            listener.onModified(value);
        }
        changed();
    }

    protected void onRemoved(final T value)
    {
        trace("An element was removed from the collection");
        for (final var listener : listeners)
        {
            listener.onRemoved(value);
        }
        changed();
    }
}
