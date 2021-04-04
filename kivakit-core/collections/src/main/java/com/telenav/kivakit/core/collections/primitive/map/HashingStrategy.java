////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive.map;

import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveMap;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.language.values.count.Estimate;
import com.telenav.kivakit.core.kernel.language.values.level.Percent;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Pluggable hashing algorithm.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramPrimitiveMap.class)
public interface HashingStrategy
{
    /**
     * @return The occupancy level for rehashing
     */
    Percent maximumOccupancy();

    /**
     * @return The recommended capacity when initializing or resizing
     */
    Estimate recommendedSize();

    /**
     * @return The number of elements at which resizing should occur. This will typically be a prime number to improve
     * hashing performance
     */
    Count rehashThreshold();

    /**
     * @return With the given capacity
     */
    HashingStrategy withCapacity(final Estimate size);

    /**
     * @return With an acceptable increased capacity
     */
    HashingStrategy withIncreasedCapacity();
}
