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
