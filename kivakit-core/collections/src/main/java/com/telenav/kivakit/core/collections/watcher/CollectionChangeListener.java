////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.watcher;

import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramWatcher;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Interface that listens to changes to a collection of objects.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramWatcher.class)
public interface CollectionChangeListener<T>
{
    void onAdded(T value);

    void onModified(T value);

    void onRemoved(T value);
}
