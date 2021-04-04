////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.loading;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfacePersistence;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A lazy-loaded object is able to clear references to reloadable data, reducing its memory footprint. Unloading an
 * object shouldn't affect its data, just whether it is present in memory.
 *
 * @author jonathanl (shibo)
 * @see Lazy
 */
@UmlClassDiagram(diagram = DiagramInterfacePersistence.class)
public interface Loadable
{
    /**
     * Load data unloaded by {@link Unloadable#unload()}
     */
    void load();
}
