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
 * An interface to an object that watches changes to a collection
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramWatcher.class)
public interface CollectionChangeWatcher<T>
{
    void addListener(CollectionChangeListener<T> listener);

    void removeListener(CollectionChangeListener<T> listener);
}
