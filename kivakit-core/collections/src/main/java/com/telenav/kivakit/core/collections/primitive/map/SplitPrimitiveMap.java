////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.map;

import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveMap;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.unsupported;

@UmlClassDiagram(diagram = DiagramPrimitiveMap.class)
public abstract class SplitPrimitiveMap extends PrimitiveMap
{
    protected SplitPrimitiveMap(final String name)
    {
        super(name);
    }

    protected SplitPrimitiveMap()
    {
    }

    @Override
    protected final void copyEntries(final PrimitiveMap that, final ProgressReporter reporter)
    {
        unsupported();
    }

    @Override
    protected final PrimitiveMap newMap()
    {
        return unsupported();
    }
}

